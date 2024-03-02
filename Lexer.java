import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class Lexer {

	private int lineNum;
	private int characterPos;
	private StringHandler accessor;//used to reference the text which we get from the String Handler class
	LinkedList<Token> tokens = new LinkedList<>(); 
	HashMap<String, Token.TokenType> keyWords = new HashMap<String, Token.TokenType>();//stores keyword type tokens
	HashMap<String, Token.TokenType> oneChar = new HashMap<String, Token.TokenType>();//stores one character symbols
	HashMap<String, Token.TokenType> twoChar = new HashMap<String, Token.TokenType>();//stores two character symbols

	public void keywordMap() {//places the keyword values into the hashmap
		//stores the key which is the keyword, and the value which is the token 
		keyWords.put("while", Token.TokenType.WHILE);
		keyWords.put("if", Token.TokenType.IF);
		keyWords.put("do", Token.TokenType.DO);
		keyWords.put("for", Token.TokenType.FOR);
		keyWords.put("break", Token.TokenType.BREAK);
		keyWords.put("continue", Token.TokenType.CONTINUE);
		keyWords.put("else", Token.TokenType.ELSE);
		keyWords.put("return", Token.TokenType.RETURN);
		keyWords.put("BEGIN", Token.TokenType.BEGIN);
		keyWords.put("END", Token.TokenType.END);
		keyWords.put("print", Token.TokenType.PRINT);
		keyWords.put("printf", Token.TokenType.PRINTF);
		keyWords.put("next", Token.TokenType.NEXT);
		keyWords.put("in", Token.TokenType.IN);
		keyWords.put("delete", Token.TokenType.DELETE);
		keyWords.put("getline", Token.TokenType.GETLINE);
		keyWords.put("exit", Token.TokenType.EXIT);
		keyWords.put("nextfile", Token.TokenType.NEXTFILE);
		keyWords.put("function", Token.TokenType.FUNCTION);

		//places the one character symbols into the HashMap
	    //stores the key which is the single symbol, and the value which is the token
		oneChar.put("{", Token.TokenType.LCURLY);
		oneChar.put("}", Token.TokenType.RCURLY);
		oneChar.put("[", Token.TokenType.LSQUARE);
		oneChar.put("]", Token.TokenType.RSQUARE);
		oneChar.put("(", Token.TokenType.LBRACE);
		oneChar.put(")", Token.TokenType.RBRACE);
		oneChar.put("$", Token.TokenType.DOLLARSIGN);
		oneChar.put("~", Token.TokenType.TILDE);
		oneChar.put("=", Token.TokenType.EQUAL);
		oneChar.put("<", Token.TokenType.LESSTHAN);
		oneChar.put(">", Token.TokenType.GREATERTHAN);
		oneChar.put("!", Token.TokenType.EXCLAIMATION);
		oneChar.put("+", Token.TokenType.PLUS);
		oneChar.put("^", Token.TokenType.CARET);
		oneChar.put("-", Token.TokenType.MINUS);
		oneChar.put("?", Token.TokenType.QUESTIONMARK);
		oneChar.put(":", Token.TokenType.COLON);
		oneChar.put("*", Token.TokenType.ASTERIX);
		oneChar.put("/", Token.TokenType.SLASH);
		oneChar.put("%", Token.TokenType.MODULUS);
		oneChar.put(";", Token.TokenType.SEMICOLON);
		oneChar.put("\n", Token.TokenType.NEWLINE);
		oneChar.put("|", Token.TokenType.VERTICALBAR);
		oneChar.put(",", Token.TokenType.COMMA);

		//places the two character symbols into the HashMap
	    //stores the key which are the 2 symbols, and the value which is the token
		twoChar.put(">=", Token.TokenType.GREATEQUAL);
		twoChar.put("<=", Token.TokenType.LESSEQUAL);
		twoChar.put("++", Token.TokenType.INCREMENT);
		twoChar.put("--", Token.TokenType.DECREMENT);
		twoChar.put("==", Token.TokenType.EQUALITY);
		twoChar.put("!=", Token.TokenType.INEQUALITY);
		twoChar.put("^=", Token.TokenType.XOR);
		twoChar.put("%=", Token.TokenType.ASSIGNREMAINDER);
		twoChar.put("*=", Token.TokenType.ASSIGNMULTIPLICATION);
		twoChar.put("/=", Token.TokenType.ASSIGNDIVISION);
		twoChar.put("+=", Token.TokenType.ASSIGNADDITION);
		twoChar.put("-=", Token.TokenType.ASSIGNSUBTRACT);
		twoChar.put("!~", Token.TokenType.DOESNOTMATCH);
		twoChar.put("&&", Token.TokenType.AND);
		twoChar.put(">>", Token.TokenType.APPEND);
		twoChar.put("||", Token.TokenType.OR);
	}

	//Lexer constructor which gets the text value from the StringValue class
	
	public Lexer(String doc) {
		accessor = new StringHandler(doc);
		keywordMap();//initializes the hashmap so the values and keys can be populated
		lineNum = 1;
		characterPos = 0;
	}

	//method which finds which tokens are in the string of text from the AWK file
	public LinkedList<Token> Lex() throws Exception {
		while (!accessor.IsDone()) {//while the text from the file is not done

			char nextChar = accessor.Peek(0);//find the next character in the text by peeking

			if (nextChar == ' ' || nextChar == '\t') {//if the next character is a space or a new tab, ignore it by using swallow
				accessor.Swallow(1);

			} else if (nextChar == '\n' || nextChar == ';') {//if the next character is a new line or a semicolon
				tokens.add(new Token(Token.TokenType.SEPARATOR, lineNum, characterPos));//add a new token of tokenType separator to the linked list of tokens
				accessor.Swallow(1);//swallow the separator
				lineNum++;//increment the line number cuz you have ,oved to a new line
				characterPos = 0;//since we are on a new line we will be starting at the position zero once again

			} else if (nextChar == '\r') {
				accessor.Swallow(1);

			} else if (nextChar == '#') {//if the next character is a hash tag, it is the beginning of a comment
				loop();//call the loop method to go through the line till the end of the comment

			} else if (nextChar == '"') {//if the next char is a quote
				tokens.add(StringLiteral());//add the token returned by String literal

			} else if (Character.isLetter(nextChar) || nextChar == '_') {//if the next character is a letter or underscore
				tokens.add(ProcessWord());//add the token returned by Process Word

			} else if (Character.isDigit(nextChar) || nextChar == '.') {//if the next character is a number or decimal
				tokens.add(ProcessNumber());//add the token returned by process number

			} else if (nextChar == '`') {//if the next character is a right tick
				tokens.add(HandlePattern());//add the token returned by handle pattern
				
			} else if (nextChar == '(' || nextChar == ')' || nextChar == '$' || nextChar == '{' || nextChar == '}'
					|| nextChar == '[' || nextChar == ']' || nextChar == '~' || nextChar == '=' || nextChar == '<'
					|| nextChar == '>' || nextChar == '!' || nextChar == '+' || nextChar == '^' || nextChar == '-'
					|| nextChar == '?' || nextChar == '-' || nextChar == '?' || nextChar == ':' || nextChar == '*'
					|| nextChar == '/' || nextChar == '%' || nextChar == ';' || nextChar == '|' || nextChar == ','
					|| nextChar == '&') {

				
				Token holder = ProcessSymbol();//store the token retrieved from process symbol
				if (holder == null) {
					throw new Exception("no symbol found");
				} else {
					tokens.add(holder);
				}

			} else {//if the character cannot be identified throw this error
				accessor.GetChar();
				throw new Exception("Unrecognized Character");
			}
		}
		return tokens;

	}

	public Token ProcessWord() throws Exception {

		char temp;
		StringBuilder wordBuilder = new StringBuilder();//used to add letters and underscores together to make your word
		temp = accessor.Peek(0);

		//while the text is not done and the character is a letter or is a number or is an underscore
		while ((!(accessor.IsDone())) && (Character.isLetter(temp) || (Character.isDigit(temp)) || (temp == '_'))) {

			temp = accessor.GetChar();//get the character
			wordBuilder.append(temp);//add it to the word
			characterPos++;
			temp = accessor.Peek(0);
		}

		String finalword = wordBuilder.toString();

		//go through the keyword hash map  to see if it is one of the keywords
		//if it does, return that token
		if (keyWords.containsKey(finalword)) {
			return (new Token(keyWords.get(finalword), lineNum, characterPos));
		} else//return the word token with the word value 
			return (new Token(Token.TokenType.WORD, lineNum, characterPos, wordBuilder.toString()));

	}

	public Token ProcessNumber() throws Exception {

		char temp;
		boolean decimal = false;//checks if a decimal was already added to the number
		StringBuilder number = new StringBuilder();
		temp = accessor.Peek(0);

		//while the text is not done and the character is a number or is a decimal
		while ((!accessor.IsDone()) && Character.isDigit(temp) || temp == '.') {

			temp = accessor.GetChar();//get the character

			if (Character.isDigit(temp)) {//if it is a number, add it to the number and check the next value
				number.append(temp);
				characterPos++;
				temp = accessor.Peek(0);
			} else if (temp == '.' && !decimal) {//if it is a decimal and it there is not already a decimal add it to the number and check the next value
				number.append(temp);
				characterPos++;
				decimal = true;
				temp = accessor.Peek(0);
			} else if (temp == '.' && decimal) {//if it is a decimal and there is already a decimal, throw an exception
				throw new Exception("Not a valid number");
			} else {//if it is any other character break to see what value it is
				break;
			}

		}
		return (new Token(Token.TokenType.NUMBER, lineNum, characterPos, number.toString()));

	}

/**
 * Loops to the end of a line
 */
	public void loop() {
		char skip = accessor.Peek(0); //peek to see what are the characters of the comment that we are skipping

		while (!accessor.IsDone() && skip != '\n') {//while the text is not done and the next character in the comment is not a newline
			accessor.Swallow(1);
			skip = accessor.Peek(0);
		}
	}
/**
 * Creates string literal tokens
 * @return STRINGLITERAL token type
 * @throws Exception
 */
	public Token StringLiteral() throws Exception {

		StringBuilder sentence = new StringBuilder(); //used to add the values together to create the string literal
		accessor.Swallow(1); // swallows the first quotation mark

		char temp = accessor.Peek(0);//peeks to see the next letter in the text

		while (!accessor.IsDone() && temp != '"') {//while the text is not done and the next character is not another quote to end the string literal,

			if (temp == '\\') {//if the next char is two backslashes, the quotes need ot be included in the string literal
				accessor.Swallow(1);//swallow the back slashes 
				characterPos++;
				temp = accessor.Peek(0);//peek to see the next value

				if (temp != '"') {// in order for the backslash to be valid there must be a quote after it
					throw new Exception("Error in string literal");//or else throw an exception
				}

				while (accessor.Peek(0) != '\\') {//while the closing backspaces are not next,
					sentence.append(accessor.GetChar());//add the characters to the string literal until we get the closing backspaces
					characterPos++;
				}

				accessor.Swallow(1);//when we find the closing backslashes,swallow them
				characterPos++;

				temp = accessor.Peek(0);//peek to see the next value
				if (temp != '"') {// in order for the backslash to be valid there must be a quote after it
					throw new Exception("Error in string literal");
				}

				sentence.append(accessor.GetChar());//if it is valid add the closing quote to the string lit
				characterPos++;
			} else {// if the characters are not escaped, just add it to the string literal
				sentence.append(accessor.GetChar());
				characterPos++;
			}

			temp = accessor.Peek(0);// check to see if the loop continues or ends
		}

		accessor.Swallow(1);// swallow and increment the last quote
		characterPos++;
		return (new Token(Token.TokenType.STRINGLITERAL, lineNum, characterPos, sentence.toString()));
	}
/**
 * Stores pattern tokens for regular expressions
 * @return HANDLEPATTERN token types
 * @throws Exception
 */
	public Token HandlePattern() throws Exception {
		StringBuilder pattern = new StringBuilder();
		accessor.Swallow(1);//swallow the first right tick
		char temp;

		while (!accessor.IsDone()) {
			temp = accessor.Peek(0);

			if (temp == '`') {//if the ending back tick is next, swallow is and return the empty pattern
				accessor.Swallow(1);
				characterPos++;
				return (new Token(Token.TokenType.HANDLEPATTERN, lineNum, characterPos, pattern.toString()));
			} else {//else add the values to the pattern 
				temp = accessor.GetChar();
				characterPos++;
				pattern.append(temp);

			}
		}//if there is not another right tick, throw this error
		throw new Exception("Error in pattern"); 

	}
/**
 * Processes one and two character symbols and creates tokens 
 * Based on the symbols
 * @return
 */
	
//check if there are 2 symbols next available 
//if they do have 2 symbols peek two then peek one
	public Token ProcessSymbol() {
		String temp;

		temp = accessor.PeekString(2);//peek to see the next two characters 
		//what if there are not 2 characters left, it would just go to the one char check right??
		if (twoChar.containsKey(temp)) {//if the two symbols are in the two char hashmap, 
			characterPos++;//increment by two
			characterPos++;
			accessor.Swallow(2);//swallow the two character
			return (new Token(twoChar.get(temp), lineNum, characterPos));//get the tokentype
		} else {//else check for the one character
			temp = accessor.PeekString(1);
			if (oneChar.containsKey(temp)) {//check if that value is a key in the one symbol hash map
				accessor.Swallow(1);
				characterPos++;
				return (new Token(oneChar.get(temp), lineNum, characterPos));//get thr value from the hash map
			}
		}

		return null;//else if the symbol is not found return null
	}
	


}
