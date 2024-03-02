
public class Token {
	
	enum TokenType{WORD,NUMBER,SEPARATOR,WHILE,IF,
		          DO,FOR,BREAK,CONTINUE,ELSE,RETURN,BEGIN,END,
		          PRINT,PRINTF,NEXT,IN,DELETE,GETLINE,EXIT,
		          NEXTFILE,FUNCTION,STRINGLITERAL,HANDLEPATTERN,LCURLY
		          ,RCURLY,LSQUARE,RSQUARE,LBRACE,RBRACE,DOLLARSIGN,TILDE,
		          EQUAL,LESSTHAN,GREATERTHAN,EXCLAIMATION,PLUS,CARET,MINUS,
		          QUESTIONMARK,COLON,ASTERIX,SLASH,MODULUS,SEMICOLON,NEWLINE,
		          VERTICALBAR,COMMA,GREATEQUAL,LESSEQUAL,INCREMENT,DECREMENT,
		          EQUALITY,INEQUALITY,XOR,ASSIGNREMAINDER,ASSIGNMULTIPLICATION,
		          ASSIGNDIVISION,ASSIGNADDITION,ASSIGNSUBTRACT,DOESNOTMATCH,
		          AND,APPEND,OR}; //stores the multiple token types
		          
    private String value; //stores the value of the token
	private int lineNumber;
	private int charPosition;//stores the position we are pointing to in a string
	private TokenType tokenType;
	
	/**
	 * Constructor which sets token type,line number and character position
	 * @param tokenType
	 * @param lineNumber
	 * @param charPosition
	 */
	public Token(TokenType tokenType,int lineNumber,int charPosition) {
		this.tokenType = tokenType;
		this.lineNumber= lineNumber;
		this.charPosition =charPosition;
	}
	/**
	 * Constructor which sets token type,line number, character position and value
	 * @param tokenType
	 * @param lineNumber
	 * @param charPosition
	 * @param value
	 */
	public Token(TokenType tokenType,int lineNumber,int charPosition, String value) {
		this.tokenType = tokenType;
		this.lineNumber= lineNumber;
		this.charPosition =charPosition;
		this.value = value;
	}
	
	public String getValue() {
		return value.toString();
	}
	
	public String toString() {
		if(value == null) {
			return tokenType.toString();
		}else {return tokenType + "(" + value +")";}
	}
	
	public Token.TokenType getToken() {
		return tokenType;
	}
	

}
