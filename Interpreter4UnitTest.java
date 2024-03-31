import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Test;

public class Interpreter4UnitTest {
  	
@Test
	  public void inutProcessing() throws Exception {
		  
	        String awkPath = new File("").getAbsolutePath() + "/officeTestFile.txt";
			Path myPath =Paths.get(awkPath);
			
			String doc= "{print $1}";
			Lexer lexer = new Lexer(doc);
			LinkedList<Token> tokens =lexer.Lex();
			Parser parser = new Parser(tokens);
			ProgramNode pg = parser.Parse();
			
			Interpreter print2 = new Interpreter(pg,myPath);
			 print2.InterpretProgram(pg);	
			 
			 
				String doc2= "{print $2}";
				Lexer lexer2 = new Lexer(doc2);
				LinkedList<Token> tokens2 =lexer2.Lex();
				Parser parser2 = new Parser(tokens2);
				ProgramNode pg2 = parser2.Parse();
				
				Interpreter print = new Interpreter(pg2,myPath);
				 print.InterpretProgram(pg2);	
			 
			 
			 
	   }
	
	 
	@Test
		 public void interpretBlockIF() throws Exception {

		      String awkPath = new File("").getAbsolutePath() + "/officeTestFile.txt";
				Path myPath =Paths.get(awkPath);
			
				String doc= " {\r\n"
						+ "		    print \"Processing line:\", $0\r\n"
						+ "\r\n"
						+ "		    # Check if it's the second line\r\n"
						+ "		    if (FNR == 2) {\r\n"
						+ "		        # Your action for the second line goes here\r\n"
						+ "		        print \"Second line information:\"\r\n"
						+ "		        print \"Amount:\", $1\r\n"
						+ "		        print \"Date:\", $2\r\n"
						+ "		        print \"User:\", $3\r\n"
						+ "		        print \"Department:\", $4\r\n"
						+ "		    }"
						+ "		}";
				Lexer lexer = new Lexer(doc);
				LinkedList<Token> tokens =lexer.Lex();
				Parser parser = new Parser(tokens);
				ProgramNode pg = parser.Parse();
				
				Interpreter print2 = new Interpreter(pg,myPath);
				print2.InterpretProgram(pg);
	
	}
/*
 * runs start and end blocks
 */
   @Test
	  public void InterpretBlocks() throws Exception {
		  
	        String awkPath = new File("").getAbsolutePath() + "/officeTestFile.txt";
			Path myPath =Paths.get(awkPath);
			
			String awkCode= "BEGIN {\n"
					+ "    print \"Processing input file...\" \n"
					+ "}"
					+ "{\r\n"
					+ "    amount = $1 \n"
					+ "    sum=0 \n"
					+ "    sum+= amount \n"
					+ "}"
					+ "END {\n"
					+ "    print \"Department-wise total amounts:\"\n"
					+ " amount $2"
					+ "}";
			
		
			Lexer lexer = new Lexer(awkCode);
			LinkedList<Token> tokens =lexer.Lex();
			Parser parser = new Parser(tokens);
			ProgramNode pg = parser.Parse();
			
			Interpreter print2 = new Interpreter(pg,myPath);
			print2.InterpretProgram(pg) ;
			
	   }
	  /**
	   * Tests condition, calls InterpretListOfStatements() if condition is true 
	   * @throws Exception
	   */
	@Test
	  public void InterpretBlockWhile() throws Exception {
		  
	    	String awkPath = new File("").getAbsolutePath() + "/officeTestFile.txt";
			Path myPath =Paths.get(awkPath);
			
			String doc="{\r\n"
					+ "    while ($2 != \"null\") {\r\n"
					+ "        print \"DATES ARE:\", $2\r\n"
					+ "        break\r\n"
					+ "    }"
					+ "}";
			Lexer lexer = new Lexer(doc);
			LinkedList<Token> tokens =lexer.Lex();
			Parser parser = new Parser(tokens);
			ProgramNode pg = parser.Parse();
			
			Interpreter print2 = new Interpreter(pg,myPath);
			print2.InterpretProgram(pg) ;
	   }
	  
	  /**
	   * Tests condition, calls InterpretListOfStatements() if condition is true 
	   * @throws Exception
	   */
	@Test
	  public void InterpretBlockForIn() throws Exception {
		  
		    String awkPath = new File("").getAbsolutePath() + "/officeTestFile.txt";
			Path myPath =Paths.get(awkPath);
			
			String doc= "BEGIN {"
					+ "    print \"DEPT  TOTAL AMOUNT\" \n"
					+ "}"
					+ "{\n"
					+ "    amount = $1 \n"
					+ "    dept = $4 \n"
					+ "    dept_sum[dept] += amount \n"
					+ "}\n"
					+ "END {\n"
					+ "    for (dept in dept_sum) {\n"
					+ "        print \"worked\""
					+ "    }\n"
					+ "}";
			Lexer lexer = new Lexer(doc);
			LinkedList<Token> tokens =lexer.Lex();
			Parser parser = new Parser(tokens);
			ProgramNode pg = parser.Parse();
			
			Interpreter print2 = new Interpreter(pg,myPath);
			print2.InterpretProgram(pg) ;
	   }
		@Test
		  public void RunFunctionCallNonVariadic() throws Exception {
		    	String awkPath = new File("").getAbsolutePath() + "/officeTestFile.txt";;
				Path myPath =Paths.get(awkPath);
				
			String doc= 
					"BEGIN {"
						+ "  print \"Testing non variadic run function calls.\" \r\n"	
						+ "}\r\n"
						+ "{\r\n"
						+ "    print \"original line:\"  $0 \n"
						+ "    print match($0, /AMOUNT DATE USER DEPT/)\n"
						+ "    print toupper($0) \n"
						+ "    print tolower($0)\n"
						+ "}";					
				Lexer lexer = new Lexer(doc);
				LinkedList<Token> tokens =lexer.Lex();
				Parser parser = new Parser(tokens);
				ProgramNode pg = parser.Parse();
				
				Interpreter print2 = new Interpreter(pg,myPath);
				print2.InterpretProgram(pg);
				
		   }
		@Test
		  public void RunFunctionCallVariadic() throws Exception {
			  
			String awkPath = new File("").getAbsolutePath() + "/officeTestFile.txt";
			Path myPath =Paths.get(awkPath);
			
			String doc= "BEGIN{"
					+ " print \"testing variadic funtion calls.\" \"Print column with amounts purchased\" \n"
					+ "}{print $1} \n";
			Lexer lexer = new Lexer(doc);
			LinkedList<Token> tokens =lexer.Lex();
			Parser parser = new Parser(tokens);
			ProgramNode pg = parser.Parse();
			
			Interpreter print2 = new Interpreter(pg,myPath);
			 print2.InterpretProgram(pg);	
		   }
	
		@Test
		  public void UserCreatedFuntions() throws Exception {
			  
			String awkPath = new File("").getAbsolutePath() + "/officeTestFile.txt";
				Path myPath =Paths.get(awkPath);
				
				String doc= "function hi(x,y) {\r\n"
						+ "  print x"
						+ "  print y  "
						+ "}\r\n";
				Lexer lexer = new Lexer(doc);
				LinkedList<Token> tokens =lexer.Lex();
				Parser parser = new Parser(tokens);
				ProgramNode pg = parser.Parse();
				
				Interpreter print2 = new Interpreter(pg,myPath);
				print2.InterpretProgram(pg);
				
		   }
		@Test
		  public void InputProcessing() throws Exception {
			  
			String awkPath = new File("").getAbsolutePath() + "/officeTestFile.txt";
				Path myPath =Paths.get(awkPath);
				
				String doc= "{print $1}";
				Lexer lexer = new Lexer(doc);
				LinkedList<Token> tokens =lexer.Lex();
				Parser parser = new Parser(tokens);
				ProgramNode pg = parser.Parse();
				
				Interpreter print2 = new Interpreter(pg,myPath);
				print2.InterpretProgram(pg);
				
		   }
		@Test
		  public void Math() throws Exception {
			  
			String awkPath = new File("").getAbsolutePath() + "/officeTestFile.txt";
				Path myPath =Paths.get(awkPath);
				
				String doc= "{"
						+ "    a=20 \n"
						+ "    b=5 \n"
						+ "    print a + b \n"
						+ "    print a - b \n"
						+ "    print a*b \n"
						+ "    print  a/b \n "
						+ "}";
				Lexer lexer = new Lexer(doc);
				LinkedList<Token> tokens =lexer.Lex();
				Parser parser = new Parser(tokens);
				ProgramNode pg = parser.Parse();
				
				Interpreter print2 = new Interpreter(pg,myPath);
				print2.InterpretProgram(pg);
				
		   }
		
		@Test
		  public void Conditionalblocks() throws Exception {
			  
			String awkPath = new File("").getAbsolutePath() + "/officeTestFile.txt";
				Path myPath =Paths.get(awkPath);
				String doc= "$1 > 10 { print $0 }";
				Lexer lexer = new Lexer(doc);
				LinkedList<Token> tokens =lexer.Lex();
				Parser parser = new Parser(tokens);
				ProgramNode pg = parser.Parse();
				
				Interpreter print2 = new Interpreter(pg,myPath);
				print2.InterpretProgram(pg);
				
		   }
		@Test
		  public void Comparisons() throws Exception {
			  
			   String awkPath = new File("").getAbsolutePath() + "/officeTestFile.txt";
				Path myPath =Paths.get(awkPath);
				
				String doc= "{\n"
						+"    a=6 \n"
						+ "   b=9 \n"
						+ "   c=6 \n"
						+ "   d = watermelon \n"
						+ "    if (b > a) {\n"
						+ "        print \"b greater than b\" \n"
						+ "    }\n"
						+ "     if (c == a) {\n"
						+ "        print \"c and a are equal\"\n"
						+ "    }\n"
						+ "    if (d ~ /me/) {\n"
						+ "        print \"word contains me\"\n"
						+ "    }\n"
						+ "    if (a != b) {\n"
						+ "        print \"a and b are not equal:\"\n"
						+ "    }\n"
						+ "    if (a <= 15 && a == c) {\n"
						+ "        print \"they all work yay\" \n"
						+ "    }";
				Lexer lexer = new Lexer(doc);
				LinkedList<Token> tokens =lexer.Lex();
				Parser parser = new Parser(tokens);
				ProgramNode pg = parser.Parse();
				
				Interpreter print2 = new Interpreter(pg,myPath);
				print2.InterpretProgram(pg);
				
		   }
		@Test
		  public void loops() throws Exception {
			  
			   String awkPath = new File("").getAbsolutePath() + "/officeTestFile.txt";
				Path myPath =Paths.get(awkPath);
				
				String doc = 
						"{\r\n"
								+ "    while ($2 != \"null\") {\r\n"
								+ "        print \"DATES ARE:\", $2\r\n"
								+ "        break\r\n"
								+ "    }"
								+ "}"
						+"{\r\n"
						+ "    for (i = 1; i <= 6; i++) { "
						+ "        print $1 "
						+ "    }"
						+ "}"
					    +"{\n"
					    + "count =0"
						+ "    do {\n"
						+ "        if (count<6) {\n"
						+ "            print \"Date for Amount\", $2\n"
						+ "        }\n"
						+"		  count++"
						+ "    } while (count<6)\n"
						+ "}";
				Lexer lexer = new Lexer(doc);
				LinkedList<Token> tokens =lexer.Lex();
				Parser parser = new Parser(tokens);
				ProgramNode pg = parser.Parse();
				
				Interpreter print2 = new Interpreter(pg,myPath);
				print2.InterpretProgram(pg);
				
		   }
}
