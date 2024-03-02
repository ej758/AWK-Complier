import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.Test;

public class ParserUnitTest {


@Test
public void parseExponent() throws Exception {

	String input1 ="a^4";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	String input2 ="(a^4^c)";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	Parser parser2 = new Parser(tokens2);
	
	String input3 =" a ^ b ^ (c^4)";
	Lexer lexer3 = new Lexer(input3);
	LinkedList<Token> tokens3 =lexer3.Lex();
	Parser parser3 = new Parser(tokens3);
	
	assertEquals("Optional[WORD(a)] ,EXPONENT ,Optional[NUMBER(4)]",parser.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] ,EXPONENT ,Optional[NUMBER(4)] ,EXPONENT ,Optional[WORD(c)]",parser2.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] ,EXPONENT ,Optional[WORD(b)] ,EXPONENT ,Optional[WORD(c)] ,EXPONENT ,Optional[NUMBER(4)]",parser3.ParseOperation().get().toString());
	
}

@Test
public void postIncrement() throws Exception {

	String input1 ="int++";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	String input2 ="int--";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	Parser parser2 = new Parser(tokens2);
	
	String input3 ="($int++)";
	Lexer lexer3 = new Lexer(input3);
	LinkedList<Token> tokens3 =lexer3.Lex();
	Parser parser3 = new Parser(tokens3);
	assertEquals("Optional[WORD(int)] ,POSTINC",parser.ParseOperation().get().toString());
	assertEquals("Optional[WORD(int)] ,POSTDEC",parser2.ParseOperation().get().toString());
	assertEquals("Optional[WORD(int)] ,DOLLAR ,POSTINC",parser3.ParseOperation().get().toString());
	 }

@Test
public void preDec() throws Exception {

	String input1 ="--9";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	String input2="(-int)";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	Parser parser2 = new Parser(tokens2);
	
	String input3 ="(+int--)";
	Lexer lexer3 = new Lexer(input3);
	LinkedList<Token> tokens3 =lexer3.Lex();
	Parser parser3 = new Parser(tokens3);
	
	
	assertEquals("Optional[NUMBER(9)] ,PREDEC",parser.ParseOperation().get().toString());
	assertEquals("Optional[WORD(int)] ,UNARYNEG",parser2.ParseOperation().get().toString());
	assertEquals("Optional[WORD(int)] ,POSTDEC ,UNARYPOS",parser3.ParseOperation().get().toString());
	  }

@Test
public void parseTerm() throws Exception {

	String input1 ="BEGIN{if(num>a) num *= int}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	String input2 ="BEGIN{while(num>a)num / int}";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	Parser parser2 = new Parser(tokens2);
	
	String input3 ="END{while(num>a)num % int}";
	Lexer lexer3 = new Lexer(input3);
	LinkedList<Token> tokens3 =lexer3.Lex();
	Parser parser3 = new Parser(tokens3);

	String input4 ="BEGIN{if(num>a) num = a / b * c}";
	Lexer lexer4 = new Lexer(input4);
	LinkedList<Token> tokens4 =lexer4.Lex();
	Parser parser4 = new Parser(tokens4);
	
	assertEquals(" BEGIN [[IF Optional[WORD(num)] ,GREATERT ,Optional[WORD(a)] [Optional[WORD(num)] = Optional[WORD(num)] ,MULTIPLY ,Optional[WORD(int)]]]]",parser.Parse().toString());
	assertEquals(" BEGIN [[WHILE Optional[WORD(num)] ,GREATERT ,Optional[WORD(a)] [Optional[WORD(num)] ,DIVIDE ,Optional[WORD(int)]]]]",parser2.Parse().toString());
	assertEquals(" END [[WHILE Optional[WORD(num)] ,GREATERT ,Optional[WORD(a)] [Optional[WORD(num)] ,MODULO ,Optional[WORD(int)]]]]",parser3.Parse().toString());
	assertEquals(" BEGIN [[IF Optional[WORD(num)] ,GREATERT ,Optional[WORD(a)] [Optional[WORD(num)] = Optional[WORD(a)] ,DIVIDE ,Optional[WORD(b)] ,MULTIPLY ,Optional[WORD(c)]]]]",parser4.Parse().toString());
	}

@Test
public void parseExpression() throws Exception {

	String input1 ="(num + int)";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	String input2 ="(a+ b - int)";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	Parser parser2 = new Parser(tokens2);
	
	String input3 ="(int + (num1-num2))";
	Lexer lexer3 = new Lexer(input3);
	LinkedList<Token> tokens3 =lexer3.Lex();
	Parser parser3 = new Parser(tokens3);
	

	String input4 ="(a+b+c-d)";
	Lexer lexer4 = new Lexer(input4);
	LinkedList<Token> tokens4 =lexer4.Lex();
	Parser parser4 = new Parser(tokens4);
	
	
	assertEquals("Optional[WORD(num)] ,ADD ,Optional[WORD(int)]",parser.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] ,ADD ,Optional[WORD(b)] ,SUBTRACT ,Optional[WORD(int)]",parser2.ParseOperation().get().toString());
	assertEquals("Optional[WORD(int)] ,ADD ,Optional[WORD(num1)] ,SUBTRACT ,Optional[WORD(num2)]",parser3.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] ,ADD ,Optional[WORD(b)] ,ADD ,Optional[WORD(c)] ,SUBTRACT ,Optional[WORD(d)]",parser4.ParseOperation().get().toString());
	  }

@Test
public void parseExpressionandTerm() throws Exception {

	String input1 ="(num + int * a)";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	String input2 ="(int / num - int2)";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	Parser parser2 = new Parser(tokens2);
	
	String input3 ="(a * b -c /d)";
	Lexer lexer3 = new Lexer(input3);
	LinkedList<Token> tokens3 =lexer3.Lex();
	Parser parser3 = new Parser(tokens3);
	

	
	assertEquals("Optional[WORD(num)] ,ADD ,Optional[WORD(int)] ,MULTIPLY ,Optional[WORD(a)]",parser.ParseOperation().get().toString());
	assertEquals("Optional[WORD(int)] ,DIVIDE ,Optional[WORD(num)] ,SUBTRACT ,Optional[WORD(int2)]",parser2.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] ,MULTIPLY ,Optional[WORD(b)] ,SUBTRACT ,Optional[WORD(c)] ,DIVIDE ,Optional[WORD(d)]",parser3.ParseOperation().get().toString());
	
	  }



@Test
public void StringConcatenation() throws Exception {

	String input1 ="(got the)";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	String input2 ="(a b c)";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	Parser parser2 = new Parser(tokens2);
	
	assertEquals("Optional[WORD(got)] ,CONCATENATION ,Optional[WORD(the)]",parser.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] ,CONCATENATION ,Optional[WORD(b)] ,CONCATENATION ,Optional[WORD(c)]",parser2.ParseOperation().get().toString());
	  }

@Test
public void comparisons() throws Exception {

	String input1 ="(a<b)";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	String input2 ="(a != b)";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	Parser parser2 = new Parser(tokens2);
	
	String input3 ="(a >= b)";
	Lexer lexer3 = new Lexer(input3);
	LinkedList<Token> tokens3 =lexer3.Lex();
	Parser parser3 = new Parser(tokens3);
	
	
	String input4 ="(a == b)";
	Lexer lexer4 = new Lexer(input4);
	LinkedList<Token> tokens4 =lexer4.Lex();
	Parser parser4 = new Parser(tokens4);
	
	assertEquals("Optional[WORD(a)] ,LESST ,Optional[WORD(b)]",parser.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] ,NOTEQ ,Optional[WORD(b)]",parser2.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] ,GREATEROREQ ,Optional[WORD(b)]",parser3.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] ,EQUAL ,Optional[WORD(b)]",parser4.ParseOperation().get().toString());

	  }

@Test
public void parseMatch() throws Exception {

	String input1 ="(num~int)";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	String input2 ="(a!~int)";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	Parser parser2 = new Parser(tokens2);
	
	assertEquals("Optional[WORD(num)] ,MATCH ,Optional[WORD(int)]",parser.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] ,NOTMATCH ,Optional[WORD(int)]",parser2.ParseOperation().get().toString());
}

@Test
public void parseInArray() throws Exception {
	String input1 ="name in arrayName";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	String input2 ="arrayName[index]";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	Parser parser2 = new Parser(tokens2);
	
	assertEquals("Optional[WORD(name)] ,IN ,Optional[WORD(arrayName)]",parser.ParseOperation().get().toString());
	assertEquals("Optional[WORD(arrayName)], Optional[WORD(index)]",parser2.ParseOperation().get().toString());
}

@Test
public void andOrTest() throws Exception {

	String input1 ="(num&&int)";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	String input2 ="(a||int)";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	Parser parser2 = new Parser(tokens2);
	
	String input3 ="(a || b && c)";
	Lexer lexer3 = new Lexer(input3);
	LinkedList<Token> tokens3 =lexer3.Lex();
	Parser parser3 = new Parser(tokens3);
	
	
	String input4 ="(a && b && c)";
	Lexer lexer4 = new Lexer(input4);
	LinkedList<Token> tokens4 =lexer4.Lex();
	Parser parser4 = new Parser(tokens4);
	
	String input5 ="{w && o && r && d || word}";
	Lexer lexer5 = new Lexer(input5);
	LinkedList<Token> tokens5 =lexer5.Lex();
	Parser parser5 = new Parser(tokens5);
	
	
	String input6 =" (w ||o ||r||d)";
	Lexer lexer6 = new Lexer(input6);
	LinkedList<Token> tokens6 =lexer6.Lex();
	Parser parser6 = new Parser(tokens6);
	
	assertEquals("Optional[WORD(num)] ,AND ,Optional[WORD(int)]",parser.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] ,OR ,Optional[WORD(int)]",parser2.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] ,OR ,Optional[WORD(b)] ,AND ,Optional[WORD(c)]",parser3.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] ,AND ,Optional[WORD(b)] ,AND ,Optional[WORD(c)]",parser4.ParseOperation().get().toString());
	assertEquals(" OTHER [[Optional[WORD(w)] ,AND ,Optional[WORD(o)] ,AND ,Optional[WORD(r)] ,AND ,Optional[WORD(d)] ,OR ,Optional[WORD(word)]]]",parser5.Parse().toString());
	assertEquals("Optional[WORD(w)] ,OR ,Optional[WORD(o)] ,OR ,Optional[WORD(r)] ,OR ,Optional[WORD(d)]",parser6.ParseOperation().get().toString());
	  }

/**
 * Test ternary expressions in awk
 * @throws Exception
 */
@Test
public void parseTernary() throws Exception {

	String input1 ="(num ?int:val)";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	assertEquals("Optional[WORD(num)] Optional[WORD(int)] Optional[WORD(val)]",parser.ParseOperation().get().toString());
	  }


/**
 * Tests assignment operations
 * @throws Exception
 */
		
@Test
public void parseAssignments() throws Exception {

	String input1 ="(num^=int)";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	String input2 ="(a%=int)";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	Parser parser2 = new Parser(tokens2);
	
	String input3 ="(num*=int)";
	Lexer lexer3 = new Lexer(input3);
	LinkedList<Token> tokens3 =lexer3.Lex();
	Parser parser3 = new Parser(tokens3);
	
	String input4 ="(num/= int)";
	Lexer lexer4 = new Lexer(input4);
	LinkedList<Token> tokens4 =lexer4.Lex();
	Parser parser4 = new Parser(tokens4);
	
	String input5 ="(num+=int)";
	Lexer lexer5 = new Lexer(input5);
	LinkedList<Token> tokens5 =lexer5.Lex();
	Parser parser5 = new Parser(tokens5);
	
	String input6 ="(num-= 8)";
	Lexer lexer6 = new Lexer(input6);
	LinkedList<Token> tokens6 =lexer6.Lex();
	Parser parser6 = new Parser(tokens6);
	
	String input7 ="(num=int)";
	Lexer lexer7 = new Lexer(input7);
	LinkedList<Token> tokens7 =lexer7.Lex();
	Parser parser7 = new Parser(tokens7);
	
	
	assertEquals("Optional[WORD(num)] = Optional[WORD(num)] ,EXPONENT ,Optional[WORD(int)]",parser.ParseOperation().get().toString());
	assertEquals("Optional[WORD(a)] = Optional[WORD(a)] ,MODULO ,Optional[WORD(int)]",parser2.ParseOperation().get().toString());
	assertEquals("Optional[WORD(num)] = Optional[WORD(num)] ,MULTIPLY ,Optional[WORD(int)]",parser3.ParseOperation().get().toString());
	assertEquals("Optional[WORD(num)] = Optional[WORD(num)] ,DIVIDE ,Optional[WORD(int)]",parser4.ParseOperation().get().toString());
	assertEquals("Optional[WORD(num)] = Optional[WORD(num)] ,ADD ,Optional[WORD(int)]",parser5.ParseOperation().get().toString());
	assertEquals("Optional[WORD(num)] = Optional[WORD(num)] ,SUBTRACT ,Optional[NUMBER(8)]",parser6.ParseOperation().get().toString());
	assertEquals("Optional[WORD(num)] = Optional[WORD(int)]",parser7.ParseOperation().get().toString());
	  }

@Test
public void testingExceptins() throws Exception{
	
	String input1 ="(condition ?correct)";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser = new Parser(tokens1);
	
	String input2 ="(number+ (value) ";
	Lexer lexer2 = new Lexer(input2);
	LinkedList<Token> tokens2 =lexer2.Lex();
	Parser parser2 = new Parser(tokens2);
	
	String input3 =" value <= ";
	Lexer lexer3 = new Lexer(input3);
	LinkedList<Token> tokens3 =lexer3.Lex();
	Parser parser3 = new Parser(tokens3);
	
	String input4 ="num =";
	Lexer lexer4 = new Lexer(input4);
	LinkedList<Token> tokens4 =lexer4.Lex();
	Parser parser4 = new Parser(tokens4);
	
	String input5 ="arrayName in ";
	Lexer lexer5 = new Lexer(input5);
	LinkedList<Token> tokens5 =lexer5.Lex();
	Parser parser5 = new Parser(tokens5);
	
	String input6 ="((concate these) ";
	Lexer lexer6 = new Lexer(input6);
	LinkedList<Token> tokens6 =lexer6.Lex();
	Parser parser6 = new Parser(tokens6);
	
	String input7 ="num ~";
	Lexer lexer7 = new Lexer(input7);
	LinkedList<Token> tokens7 =lexer7.Lex();
	Parser parser7 = new Parser(tokens7);
	
	
	try {
		parser.ParseOperation();
		fail("Throw exception");
	}catch(Exception error) {
		assertEquals("wrong ternary format",error.getMessage());
	}
	
	try {
		parser2.ParseOperation();
		fail("Throw exception");
	}catch(Exception error) {
		assertEquals("Missing a closing brace",error.getMessage());
	}
	
	
	try {
		parser3.ParseOperation();
		fail("Throw exception");
	}catch(Exception error) {
		assertEquals("Must have a value to compare to ",error.getMessage());
	}
	
	try {
		parser4.ParseOperation();
		fail("Throw exception");
	}catch(Exception error) {
		assertEquals("need a value after a equal sign",error.getMessage());
	}
	try {
		parser5.ParseOperation();
		fail("Throw exception");
	}catch(Exception error) {
		assertEquals("need a value after keywpord IN ",error.getMessage());
	}
	
	try {
		parser6.ParseOperation();
		fail("Throw exception");
	}catch(Exception error) {
		assertEquals("Missing a closing brace",error.getMessage());
	}
	
	try {
		parser7.ParseOperation();
		fail("Throw exception");
	}catch(Exception error) {
		assertEquals("you need a value to compare to",error.getMessage());
	}
	
}
//infinite loop

@Test
public void parseifElse() throws Exception {
	//not parsing the else
	String input1 ="BEGIN{if(num<b){num=a}else{num = num2}}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	
	//not printing else
	assertEquals(" BEGIN [[IF Optional[WORD(num)] ,LESST ,Optional[WORD(b)] [Optional[WORD(num)] = Optional[WORD(a)]]  ELSE [Optional[WORD(num)] = Optional[WORD(num2)]]]]",parser.Parse().toString());
	
 	
   }

@Test
public void parseIfElseNest() throws Exception {
	
	
	String input1 ="if(num<b){ num =34}else if(num>b){ num =66} else{num=5}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	
	//make it print else if
	assertEquals(" OTHER [[IF Optional[WORD(num)] ,LESST ,Optional[WORD(b)] [Optional[WORD(num)] = Optional[NUMBER(34)]] "
			+ "IF Optional[WORD(num)] ,GREATERT ,Optional[WORD(b)] [Optional[WORD(num)] = Optional[NUMBER(66)]]  "
			+ "ELSE [Optional[WORD(num)] = Optional[NUMBER(5)]]]]",parser.Parse().toString());
	
   }

@Test
public void parseContinue() throws Exception {
	
	String input1 ="{a=5 continue}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	
	assertEquals(" OTHER [[Optional[WORD(a)] = Optional[NUMBER(5)], Continue]]",parser.Parse().toString());
	
   }

@Test
public void parseBreak() throws Exception {
	
	String input1 ="BEGIN{while(num<b)break}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	
	assertEquals(" BEGIN [[WHILE Optional[WORD(num)] ,LESST ,Optional[WORD(b)] [Break]]]",parser.Parse().toString());
	
   }
@Test
public void parseFor() throws Exception {
	
	String input1 ="for(a=1;a<=5;a--){a=9}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);

	
	assertEquals(" OTHER [[FOR Optional[WORD(a)] = Optional[NUMBER(1)] Optional[WORD(a)] ,LESSOREQ ,Optional[NUMBER(5)]Optional[WORD(a)] "
			+ ",POSTDEC [Optional[WORD(a)] = Optional[NUMBER(9)]]]]",parser.Parse().toString());
	
   }
@Test
public void parseForIn() throws Exception {
	
	String input1 ="for(i in arrayName) {a=9}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	
	assertEquals(" OTHER [[for in Optional[WORD(i)] ,IN ,Optional[WORD(arrayName)] [Optional[WORD(a)] = Optional[NUMBER(9)]]]]",parser.Parse().toString());
	
   }
@Test
public void parseWHile() throws Exception {
	
	String input1 ="BEGIN{while(a<b){a=6}}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	
	assertEquals(" BEGIN [[WHILE Optional[WORD(a)] ,LESST ,Optional[WORD(b)] [Optional[WORD(a)] = Optional[NUMBER(6)]]]]",parser.Parse().toString());
	
   }

@Test
public void parseDoWhile() throws Exception {
	
	String input1 ="BEGIN{do{num=5}while(num<5)}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	
	
	assertEquals(" BEGIN [[DO [Optional[WORD(num)] = Optional[NUMBER(5)]] WHILE Optional[WORD(num)] ,LESST ,Optional[NUMBER(5)]]]",parser.Parse().toString());
	
   }
@Test
public void parseDeleteArray() throws Exception {
	
	String input1 ="{delete arrayName[i]}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	
	assertEquals(" OTHER [[delete Optional[WORD(arrayName)], Optional[WORD(i)]]]",parser.Parse().toString());
	
   }
@Test
public void parseDelete() throws Exception {
	
	String input1 ="delete arrayName";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	ProgramNode program = parser.Parse();
	
	assertEquals(" OTHER [[delete Optional[WORD(arrayName)]]]",program.toString());
	
   }
@Test
public void parseReturn() throws Exception {
	
	String input1 ="{return a}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	
	assertEquals(" OTHER [[Return Optional[WORD(a)]]]",parser.Parse().toString());
	
   }
@Test
public void parseFunctionCall() throws Exception {
	
	String input1 ="method(x,y,z)";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	assertEquals("Optional[WORD(method)] [Optional[WORD(x)], Optional[WORD(y)], Optional[WORD(z)]]",parser.ParseOperation().get().toString());
	
   }

@Test
public void everythingTest() throws Exception {
	
	String input1 ="BEGIN{if(a<b){num = b}else{num=a}}(num=a){num=b}END{while(num>a){num+=45}}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	assertEquals(" BEGIN [[IF Optional[WORD(a)] ,LESST ,Optional[WORD(b)] [Optional[WORD(num)] = Optional[WORD(b)]]  ELSE [Optional[WORD(num)] = Optional[WORD(a)]]]] "
			+ "OTHER [[Optional[WORD(num)] = Optional[WORD(b)]] ,Optional[WORD(num)] = Optional[WORD(a)]] "
			+ "END [[WHILE Optional[WORD(num)] ,GREATERT ,Optional[WORD(a)] [Optional[WORD(num)] = Optional[WORD(num)] ,ADD ,Optional[NUMBER(45)]]]]",parser.Parse().toString());
	
   }

@Test
public void print() throws Exception {
	
	String input1 ="BEGIN{printf  \"$f\" ,i+5, i, \"$f\";}" ;
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	assertEquals(" BEGIN [[PRINTF [Optional[STRINGLITERAL($f)], Optional[WORD(i)] ,ADD ,Optional[NUMBER(5)], Optional[WORD(i)], Optional[STRINGLITERAL($f)]]]]",parser.Parse().toString());
	
   }
@Test
public void print2i() throws Exception {
	
	String input1 = "BEGIN{print ($1 \":\",$2)}";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	assertEquals(" BEGIN [[PRINT [Optional[NUMBER(1)] ,DOLLAR ,CONCATENATION ,Optional[STRINGLITERAL(:)], Optional[NUMBER(2)] ,DOLLAR]]]",parser.Parse().toString());
	
   }
@Test

public void printf() throws Exception {
	//try with braces
	String input1 ="BEGIN{printf \"%-19s\", \"$f\" ,i+5}" ;
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	assertEquals(" BEGIN [[PRINTF [Optional[STRINGLITERAL(%-19s)], Optional[STRINGLITERAL($f)], Optional[WORD(i)] ,ADD ,Optional[NUMBER(5)]]]]",parser.Parse().toString());
	
   }
@Test

public void print2() throws Exception {
	//try with braces
	String input1 =" BEGIN {print (\"square of\", i, \"is\", i*i ) }" ;
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	assertEquals(" BEGIN [[PRINT [Optional[STRINGLITERAL(square of)], Optional[WORD(i)], Optional[STRINGLITERAL(is)], Optional[WORD(i)] ,MULTIPLY ,Optional[WORD(i)]]]]",parser.Parse().toString());
	
   }

@Test
public void getline() throws Exception {
	
	String input1 ="BEGIN{getline a[++c] < \"f\" > 0 } ";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	assertEquals(" BEGIN [[getline [Optional[WORD(a)], Optional[WORD(c)] ,PREINC ,LESST ,Optional[STRINGLITERAL(f)]] ,GREATERT ,Optional[NUMBER(0)]]]",parser.Parse().toString());
	
   }
@Test
public void exit() throws Exception {
	
	String input1 ="BEGIN{if(num=1) exit 1} ";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	assertEquals(" BEGIN [[IF Optional[WORD(num)] = Optional[NUMBER(1)] [EXIT [Optional[NUMBER(1)]]]]]",parser.Parse().toString());
	
   }
@Test
public void nextfile() throws Exception {
	
	String input1 ="BEGIN{filename nextfile} ";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	assertEquals(" BEGIN [[Optional[WORD(filename)] ,CONCATENATION ,NEXTFILE []]]",parser.Parse().toString());
	
   }
@Test
public void next() throws Exception {
	String input1 ="BEGIN{if(num=a) next} ";
	Lexer lexer1 = new Lexer(input1);
	LinkedList<Token> tokens1 =lexer1.Lex();
	Parser parser =new Parser(tokens1);
	assertEquals( " BEGIN [[IF Optional[WORD(num)] = Optional[WORD(a)] [NEXT []]]]",parser.Parse().toString());
	
   }
}
