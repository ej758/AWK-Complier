import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.Test;

public class UnitTest {
	

@Test 
public void lexerTest() throws Exception {
	String input="testing 123 ok_ay";
	Lexer lexer = new Lexer(input);
	LinkedList<Token> tokens =lexer.Lex();	
	
	assertEquals("[WORD(testing), NUMBER(123), WORD(ok_ay)]",tokens.toString());
	
}



@Test 
public void lexer2Test() throws Exception {
	String input="$0 = tolower($0)";
	Lexer lexer = new Lexer(input);
	LinkedList<Token> tokens =lexer.Lex();	
	
	assertEquals(8,tokens.size());
	assertEquals("[DOLLARSIGN, NUMBER(0), EQUAL, WORD(tolower), LBRACE, DOLLARSIGN, NUMBER(0), RBRACE]",tokens.toString());
	
}

	@Test
	public void randomNum() throws Exception {
		String input=".678";
		Lexer lexer = new Lexer(input);
		LinkedList<Token> tokens =lexer.Lex();	
		assertEquals("[NUMBER(.678)]",tokens.toString());
	}


@Test 
public void proccessWord() throws Exception {
	String input="345 test w8ing _works";
	Lexer lexer = new Lexer(input);
	LinkedList<Token> tokens =lexer.Lex();
	
	assertEquals("[NUMBER(345), WORD(test), WORD(w8ing), WORD(_works)]",tokens.toString());

}

@Test 
public void processNum() throws Exception {
	String input="345 3.14 758";
	Lexer lexer = new Lexer(input);
	LinkedList<Token> tokens =lexer.Lex();
	
	assertEquals("[NUMBER(345), NUMBER(3.14), NUMBER(758)]",tokens.toString());
	
}


@Test
public void throwsExcp() throws Exception {
	String input="7.5.8 45";
	Lexer lexer = new Lexer(input);
	//Token token =lexer.ProcessNumber();
	
	//test for throwing exceptions
	try {
		lexer.Lex();
		fail("Throw exception");
	}catch(Exception error) {
		assertEquals("Not a valid number",error.getMessage());
	}
   
}

@Test
public void throwsExcp2() throws Exception {
	String input="t%sti!ng";
	Lexer lexer = new Lexer(input);
	LinkedList<Token> tokens =lexer.Lex();
	assertEquals("[WORD(t), MODULUS, WORD(sti), EXCLAIMATION, WORD(ng)]",tokens.toString());
   
}

@Test
public void separators() throws Exception {
	String input="hi test \n yh";
	Lexer lexer = new Lexer(input);
	LinkedList<Token> tokens =lexer.Lex();
	
	assertEquals(4,tokens.size());
	assertEquals("[WORD(hi), WORD(test), SEPARATOR, WORD(yh)]",tokens.toString());
	
}

/**
 * tests for random figures in words
 * @throws Exception
 */
@Test
public void randomChar() throws Exception {
	String input="h! wha^t is th@t test";
	Lexer lexer = new Lexer(input);
	
	try {
		lexer.Lex();
		fail("Throw exception");
	}catch(Exception error) {
		assertEquals("Unrecognized Character",error.getMessage());
	}
	
}

/**
 * tests for random figures in numbers
 * @throws Exception
 */

@Test 
public void randomNum2() throws Exception {
	String input="678 56&3 42";
	Lexer lexer = new Lexer(input);

	try {
		lexer.Lex();
		fail("Throw exception");
	}catch(Exception error) {
		assertEquals("no symbol found",error.getMessage());
	}
}


@Test
public void emptyLine() throws Exception {
	String input="\n";
	Lexer lexer = new Lexer(input);
	LinkedList<Token> tokens =lexer.Lex();
	
	assertEquals("[SEPARATOR]",tokens.toString());	
}
@Test
public void numAndWord() throws Exception {
	String input="5 goodbye \n";
	Lexer lexer = new Lexer(input);
	LinkedList<Token> tokens =lexer.Lex();
	
	assertEquals("[NUMBER(5), WORD(goodbye), SEPARATOR]",tokens.toString());
	
}

@Test 
public void decimals() throws Exception {
	String input="5.23  8.5  3 \n";
	Lexer lexer = new Lexer(input);
	LinkedList<Token> tokens =lexer.Lex();
	
	assertEquals("[NUMBER(5.23), NUMBER(8.5), NUMBER(3), SEPARATOR]",tokens.toString());
	
}

@Test 
public void keywords() throws Exception {
		String input="for while hello do";
		Lexer lexer = new Lexer(input);
		LinkedList<Token> tokens =lexer.Lex();	
assertEquals("[FOR, WHILE, WORD(hello), DO]",tokens.toString());  
	}

@Test 
public void keywords2() throws Exception {
		String input="fo BEGIN end hi printf";
		Lexer lexer = new Lexer(input);
		LinkedList<Token> tokens =lexer.Lex();	
assertEquals("[WORD(fo), BEGIN, WORD(end), WORD(hi), PRINTF]",tokens.toString());  
		
	}

@Test 
public void comment() throws Exception {
		String input="#this should not show\n"
				+ "this should show";
		Lexer lexer = new Lexer(input);
		LinkedList<Token> tokens =lexer.Lex();
		
assertEquals("[SEPARATOR, WORD(this), WORD(should), WORD(show)]",tokens.toString());
		
	}

@Test 
public void comment2() throws Exception {
		String input="Im gonna try this #this should not show\n"
				+ "okay it works BEGIN";
		Lexer lexer = new Lexer(input);
		LinkedList<Token> tokens =lexer.Lex();
		
assertEquals("[WORD(Im), WORD(gonna), WORD(try), WORD(this), SEPARATOR, WORD(okay), WORD(it), WORD(works), BEGIN]",tokens.toString());
		
	}
@Test 
public void comment3() throws Exception {
		String input="Im gonna try this #this should not show";
		Lexer lexer = new Lexer(input);
		LinkedList<Token> tokens =lexer.Lex();
		
assertEquals("[WORD(Im), WORD(gonna), WORD(try), WORD(this)]",tokens.toString());
		
	}

@Test 
public void Stringliterals() throws Exception {
		String input= "\"hello world\"";
		String trial ="these should be words \"this is a literal\"";
		String quote = "She said, \"hello world\"";
		Lexer lexer = new Lexer(input);
		Lexer lexer1 = new Lexer(trial);
		Lexer lexer2 =new Lexer(quote);
		LinkedList<Token> tokens =lexer.Lex();
		LinkedList<Token> tokens1 =lexer1.Lex();
		LinkedList<Token> tokens2 =lexer2.Lex();
		
assertEquals("[STRINGLITERAL(hello world)]",tokens.toString());
assertEquals("[WORD(these), WORD(should), WORD(be), WORD(words), STRINGLITERAL(this is a literal)]",tokens1.toString());
assertEquals("[WORD(She), WORD(said), COMMA, STRINGLITERAL(hello world)]",tokens2.toString());		
	}
@Test 
public void Empty() throws Exception {
		String input= "";
		Lexer lexer = new Lexer(input);
		LinkedList<Token> tokens =lexer.Lex();
		
assertEquals("[]",tokens.toString());		
	}

@Test 
public void pattern() throws Exception {
		String input="`.*`";
		String input1 ="`%4df#@`";
		
		Lexer lexer = new Lexer(input);
		Lexer lexer1 = new Lexer(input1);
		
		LinkedList<Token> tokens =lexer.Lex();	
		LinkedList<Token> tokens1 =lexer1.Lex();	
		
assertEquals("[HANDLEPATTERN(.*)]",tokens.toString());
assertEquals("[HANDLEPATTERN(%4df#@)]",tokens1.toString());
	}

@Test 
public void PatternError() throws Exception {
	String input="`465%";
	Lexer lexer = new Lexer(input);

	try {
		lexer.Lex();
		fail("Throw exception");
	}catch(Exception error) {
		assertEquals("Error in pattern",error.getMessage());
	}
}

@Test 
	public void ProcessSymbol()throws Exception{
			String input= "-=";
			String input1= "%";
			String input2= "||";
			String input3= "|";
			String input4= "^=";
			
			Lexer lexer = new Lexer(input);
			Lexer lexer1 = new Lexer(input1);
			Lexer lexer2= new Lexer(input2);
			Lexer lexer3 = new Lexer(input3);
			Lexer lexer4 = new Lexer(input4);
			
			LinkedList<Token> tokens =lexer.Lex();
			LinkedList<Token> tokens1 =lexer1.Lex();
			LinkedList<Token> tokens2 =lexer2.Lex();
			LinkedList<Token> tokens3=lexer3.Lex();
			LinkedList<Token> tokens4 =lexer4.Lex();
			
	assertEquals("[ASSIGNSUBTRACT]",tokens.toString());
	assertEquals("[MODULUS]",tokens1.toString());
	assertEquals("[OR]",tokens2.toString());
	assertEquals("[VERTICALBAR]",tokens3.toString());
	assertEquals("[XOR]",tokens4.toString());
	}
	
@Test 
public void SymbolError() throws Exception {
	String input="&_";
	Lexer lexer = new Lexer(input);

	try {
		lexer.Lex();
		fail("Throw exception");
	}catch(Exception error) {
		assertEquals("no symbol found",error.getMessage());
	}
}

/**
 * Tests the different methods in  the token manager class
 * Compares the peeked value to the match and remove value
 * Tests the moreTokens method
 * Ensures the token is removed when match and remove is called
 * @throws Exception
 */

@Test
public void tokenManager() throws Exception {

	String input1 ="hello my 67 yth";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();	
	
	TokenManager manage = new TokenManager(tokens1);

	assertEquals("[WORD(hello), WORD(my), NUMBER(67), WORD(yth)]",tokens1.toString());
	Optional<Token> peeked = manage.Peek(0);
	Optional<Token> matchedAndRemoved = manage.MatchAndRemove(peeked.get().getToken());

	assertTrue(manage.MoreTokens());
    assertEquals(matchedAndRemoved,peeked);
    assertEquals("[WORD(my), NUMBER(67), WORD(yth)]",tokens1.toString());
}



/**
 * Tests the accept Separators method
 * Ensures that the separators are removed using match and remove 
 * @throws Exception
 */

@Test
public void acceptSeperator() throws Exception {
	
	boolean separator;
	String input1 ="\n ; hello \n ; my \n 67 ; yth";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	
	assertEquals(10,parser.tokens.size());
	assertEquals("[SEPARATOR, SEPARATOR, WORD(hello), SEPARATOR, SEPARATOR, WORD(my), SEPARATOR, NUMBER(67), SEPARATOR, WORD(yth)]",parser.tokens.toString());
	
	separator = parser.AcceptSeperators();
    assertEquals(8,parser.tokens.size());
    assertEquals("[WORD(hello), SEPARATOR, SEPARATOR, WORD(my), SEPARATOR, NUMBER(67), SEPARATOR, WORD(yth)]",parser.tokens.toString());
    assertTrue(separator);
   }



/**
 * Tests the parse Function method
 * Ensures that a function has the valid format 
 * @throws Exception
 */
//empty parameters, one param, multiple params with new line , new line before and after commas 

@Test
public void parseFunc() throws Exception {
	

	boolean functionPresent;
	String input1 ="function test ( Long \n , \n num ){}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	
	Parser parser =new Parser(tokens1);
	ProgramNode program = new ProgramNode();
	functionPresent = parser.ParseFunction(program);
	
	 assertTrue(functionPresent);
	 
	
     String input2 ="function test ( Long,\n String){}";
	 Lexer lexer2 = new Lexer(input2);
	 LinkedList<Token> tokens2 =lexer2.Lex();
		
	 Parser parser2 =new Parser(tokens2);
	 ProgramNode program2 = new ProgramNode();
	 functionPresent = parser2.ParseFunction(program2);
		
	 assertTrue(functionPresent);
	 
	
	
   }
/**
 * tests function calls with the wrong format
 * @throws Exception
 */
@Test
public void errorParseFunc1() throws Exception {
	

	boolean functionPresent;
	String input1 ="function ( on ,tw ){ ";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	
	Parser parser =new Parser(tokens1);
	ProgramNode program = new ProgramNode();
	functionPresent = parser.ParseFunction(program);
	
	 assertFalse(functionPresent);
	
	String input2 ="function (test ,on ,tw ){ ";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	
	Parser parser2 =new Parser(tokens2);
	ProgramNode program2 = new ProgramNode();
	functionPresent = parser2.ParseFunction(program2);
	
	assertFalse(functionPresent);
		
   }
/**
 * Tests the parse action method to ensure it is called correctly
 * @throws Exception
 */
@Test
public void parseAction() throws Exception {
	
	boolean actionPresent;
	
	String input1 ="begin that end";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	
	Parser parser =new Parser(tokens1);
	ProgramNode program = new ProgramNode();
	actionPresent = parser.ParseAction(program);
	
	 assertTrue(actionPresent);
	
   }

		
@Test
public void parseOperationInc() throws Exception {

	String input1 ="++a";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
		
	assertEquals("Optional[WORD(a)] ,PREINC",parser.ParseOperation().get().toString());
	
   }

@Test
public void parseOperationDollar() throws Exception {
	
	String input1 ="++$b";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	
	Parser parser = new Parser(tokens1);
	assertEquals("Optional[WORD(b)] ,DOLLAR ,PREINC",parser.ParseOperation().get().toString());
	
   }
@Test
public void parseOperationIncrement() throws Exception {
	
	String input1 ="(++d)";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	
	Parser parser = new Parser(tokens1);
	assertEquals("Optional[WORD(d)] ,PREINC",parser.ParseOperation().get().toString());
	
   }
@Test
public void array() throws Exception {
	

	String input1 ="a[9]";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	
	Parser parser = new Parser(tokens1);
	assertEquals("Optional[WORD(a)], Optional[NUMBER(9)]",parser.ParseOperation().get().toString());


   }

@Test
public void parseOperationNegate() throws Exception {
	
	String input1 ="-5";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	
	Parser parser = new Parser(tokens1);
	assertEquals("Optional[NUMBER(5)] ,UNARYNEG",parser.ParseOperation().get().toString());


   }
@Test
public void parseOperationLiteral() throws Exception {

	String input1 ="`[abc]`";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	
	Parser parser = new Parser(tokens1);
	assertEquals("Optional[HANDLEPATTERN([abc])]",parser.ParseOperation().get().toString());

   }
@Test
public void parseOperationIndexWithName() throws Exception {

	String input1 ="e[++b]";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	
	Parser parser = new Parser(tokens1);
	assertEquals("Optional[WORD(e)], Optional[WORD(b)] ,PREINC",parser.ParseOperation().get().toString());

   }
@Test
public void parseOperationDollarSign() throws Exception {
	

	String input1 ="$7";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	
	
	Parser parser = new Parser(tokens1);
	assertEquals("Optional[NUMBER(7)] ,DOLLAR",parser.ParseOperation().get().toString());
   }




}
