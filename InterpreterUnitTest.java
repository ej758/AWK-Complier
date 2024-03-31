import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

public class InterpreterUnitTest {
	
	  @Test
	    public void print() throws Exception {
		   ProgramNode print = new ProgramNode();
		   Interpreter print2 = new Interpreter(print,null);
		   
		   HashMap<String,InterpreterDataType> testHash= new HashMap<String,InterpreterDataType>();

		   testHash.put("0", new InterpreterDataType("this test"));
		   testHash.put("1", new InterpreterDataType("that exam"));
		   InterpreterArrayDataType param = new InterpreterArrayDataType(testHash);
		   HashMap<String, InterpreterDataType> mainHash = new HashMap<>();
		   mainHash.put("0", param);
		   
		   BuiltInFunctionNode bfd = (BuiltInFunctionNode)print2.getFunctionSource().get("print");
		    bfd.execute.apply(mainHash);
		  //  assertEquals(" this test that exam ",result);
	   }
	   
      
	  
	  @Test
	    public void splitTest() throws Exception{
	        String myString = "{}";
	        Lexer myLexer = new Lexer(myString);
	        LinkedList<Token> myTokens = myLexer.Lex();
	        Parser myParser = new Parser(myTokens);
	        ProgramNode myProgram = myParser.Parse();
	        Interpreter myInterpret = new Interpreter(myProgram, null);
	        HashMap<String, InterpreterDataType> splitParams = new HashMap<>();
	        splitParams.put("input", new InterpreterDataType("i-like-cheese-cake"));
	        splitParams.put("array", new InterpreterDataType("lineSplitArray"));
	        splitParams.put("fieldSeparator", new InterpreterDataType("-"));
	    }
	  
	
	  
	  @Test
	    public void printf() throws Exception {
		  
		   ProgramNode print = new ProgramNode();
		   Interpreter print2 = new Interpreter(print,null);
		   
		   HashMap<String,InterpreterDataType> testHash= new HashMap<String,InterpreterDataType>();
		   HashMap<String,InterpreterDataType> stringHash= new HashMap<String,InterpreterDataType>();
		   stringHash.put("0",  new InterpreterDataType("this"));
		   stringHash.put("1",  new InterpreterDataType("is"));
		   stringHash.put("2",  new InterpreterDataType("test"));

		   
		   InterpreterArrayDataType newA = new InterpreterArrayDataType(stringHash);
	
		   testHash.put("0", new InterpreterDataType("%s"));
		   testHash.put("1", newA);
		  String result =((BuiltInFunctionNode) print2.functionSource.get("printf")).execute.apply(testHash).toString();
		
	   }
	   
     @Test
	    public void getline() throws Exception {

	   	   ProgramNode getline = new ProgramNode();
		   Interpreter getline2 = new Interpreter(getline,null);
		   assertEquals(false,getline2.lManager.SplitAndAssign());
	   }  
	   
	   @Test
	    public void next() throws Exception {
		   
		   ProgramNode print = new ProgramNode();
		   Interpreter print2 = new Interpreter(print,null);
		   assertEquals(false,print2.lManager.SplitAndAssign());	
	   }
	   @Test
	    public void gsub() throws Exception {
	
		   ProgramNode print = new ProgramNode();
		   Interpreter print2 = new Interpreter(print,null);
		   
		   HashMap<String,InterpreterDataType> testHash= new HashMap<String,InterpreterDataType>();

		   testHash.put("pattern", new InterpreterDataType("is"));
		   testHash.put("replace", new InterpreterDataType("us"));
		   testHash.put("string", new InterpreterDataType("this"));
		   
		   BuiltInFunctionNode bfd = (BuiltInFunctionNode) print2.getFunctionSource().get("gsub");
		   //String result = bfd.execute.apply(testHash);
		//  assertEquals(result,"1" );
	   }
	   @Test
	    public void match() throws Exception {
		   
		   ProgramNode print = new ProgramNode();
		   Interpreter print2 = new Interpreter(print,null);
		   
		   HashMap<String,InterpreterDataType> testHash= new HashMap<String,InterpreterDataType>();

		   testHash.put("1", new InterpreterDataType("this"));
		   testHash.put("2", new InterpreterDataType("is"));
		   
		   

		   BuiltInFunctionNode bfd = (BuiltInFunctionNode) print2.getFunctionSource().get("MATCH");
		   String result = bfd.execute.apply(testHash);
		   assertEquals(result,"2" );
	   }
	   @Test
	    public void split() throws Exception {
		   
		   ProgramNode print = new ProgramNode();
		   Interpreter print2 = new Interpreter(print,null);
		   HashMap<String,InterpreterDataType> testHash= new HashMap<String,InterpreterDataType>();
		   InterpreterDataType test = new  InterpreterDataType("wonderwall hfyuh ug8iu");
		   InterpreterArrayDataType  test2 = new InterpreterArrayDataType(new HashMap <String, InterpreterDataType>());
		   
		   testHash.put("1",test);
		   testHash.put("2",test2);
		   var retVal = ((BuiltInFunctionNode)(print2.functionSource.get("split"))).execute(testHash);
		   assertEquals(retVal,"2");
		   var str = ((BuiltInFunctionNode)print2.functionSource.get("split")).execute.apply(testHash);
		   System.out.println(str);
		  
		 
	   }

	   
	   @Test
	    public void sub() throws Exception {
		
		   ProgramNode print = new ProgramNode();
		   Interpreter print2 = new Interpreter(print,null);
		   
		   HashMap<String,InterpreterDataType> testHash= new HashMap<String,InterpreterDataType>();
		   testHash.put("1", new InterpreterDataType("at"));
		   testHash.put("2", new InterpreterDataType("ith"));
		   testHash.put("3", new InterpreterDataType("water, water, everywhere"));

		   BuiltInFunctionNode bfd = (BuiltInFunctionNode) print2.getFunctionSource().get("SUB");
		   String result = bfd.execute.apply(testHash).toString();
		   
		   assertEquals("1",result);
	   }
	   @Test
	    public void index() throws Exception {

		   ProgramNode print = new ProgramNode();
		   Interpreter print2 = new Interpreter(print,null);
		   
		   HashMap<String,InterpreterDataType> testHash= new HashMap<String,InterpreterDataType>();

		   testHash.put("1", new InterpreterDataType("this"));
		   testHash.put("2", new InterpreterDataType("is"));
		   
		   InterpreterArrayDataType param = new InterpreterArrayDataType(testHash);
		   
		   BuiltInFunctionNode bfd = (BuiltInFunctionNode) print2.getFunctionSource().get("index");
		   String result = bfd.execute.apply(testHash);
		   
		   assertEquals(result,"2" );
	   }
   
	   @Test
	    public void length() throws Exception {
  
		   ProgramNode print = new ProgramNode();
		   Interpreter print2 = new Interpreter(print,null);
		   
		   HashMap<String,InterpreterDataType> testHash= new HashMap<String,InterpreterDataType>();

		   testHash.put("1", new InterpreterDataType("this"));
		   
		   BuiltInFunctionNode bfd = (BuiltInFunctionNode) print2.getFunctionSource().get("LENGTH");
		   String result = bfd.execute.apply(testHash);
		   
		   assertEquals(result,"4" );
	   }

	   @Test
	    public void substr() throws Exception {
		   
		   
		   ProgramNode print = new ProgramNode();
		   Interpreter print2 = new Interpreter(print,null);
		   
		   HashMap<String,InterpreterDataType> testHash= new HashMap<String,InterpreterDataType>();

		   testHash.put("1", new InterpreterDataType("washington"));
		   testHash.put("2", new InterpreterDataType("2"));
		   testHash.put("3", new InterpreterDataType("4"));
		  
		   String result = print2.getFunction(testHash,"SUBSTR");
		   assertEquals(result,"shin");
	   }
	   @Test
	    public void toLower() throws Exception {
	
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   
		   HashMap<String,InterpreterDataType> testHash= new HashMap<String,InterpreterDataType>();

		   testHash.put("1", new InterpreterDataType("THIS"));
  
		   String result = interpreter.getFunction(testHash,"TOLOWER");
		   assertEquals(result,"this" );
	   }
	   @Test
	    public void toUpper() throws Exception {

			   ProgramNode program = new ProgramNode();
			   Interpreter interpreter = new Interpreter(program,null);
			   
			   HashMap<String,InterpreterDataType> testHash= new HashMap<String,InterpreterDataType>();

			   testHash.put("1", new InterpreterDataType("this"));
	  
			   String result = interpreter.getFunction(testHash,"TOUPPER");
			   assertEquals(result,"THIS" );
			   
	   }
	   
	   @Test
	    public void assignmentNode() throws Exception {
		   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   
		   VariableReferenceNode v = new VariableReferenceNode("a");
		   Optional <Node> constant = Optional.of(new ConstantNode("2"));
		   AssignmentNode aNode  =new AssignmentNode(v,constant);
		   InterpreterDataType idt = new InterpreterDataType(aNode.toString());
		   params.put(v.toString(),idt);
		   
		   assertEquals("2",interpreter.getIDT(aNode, params).getStrings());
	   }
	   
	   @Test
	    public void ConstantNode() throws Exception {
		   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   
		   
		   ConstantNode constant =new ConstantNode("a");
		   
		   InterpreterDataType idt = new InterpreterDataType(constant.toString());
		 
		   assertEquals("a",interpreter.getIDT(constant, params).getStrings());
	   }
	   @Test
	    public void FunctionCallNode() throws Exception {
		   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   ConstantNode constant = new ConstantNode("printHelloWorld");
		   ConstantNode constant1 = new ConstantNode("hello");
		   ConstantNode constant2 = new ConstantNode("world");
		   
		   
		   LinkedList<Node> parameter = new LinkedList<>();
		   parameter.add(constant1);
		   parameter.add(constant2);
		   
		   FunctionCallNode func = new FunctionCallNode(constant.toString(),parameter);
		   InterpreterDataType idt = new InterpreterDataType(func.toString());
		   
		   assertEquals("",interpreter.getIDT(func, params).getStrings());
	   }
	   @Test
	    public void PatternNode() throws Exception {
		   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   
		   PatternNode pattern = new PatternNode("`.*`");
		  
		   try {
			   interpreter.getIDT(pattern, params).getStrings();
				fail("Throw exception");
			}catch(Exception error) {
				assertEquals("A pattern cannot be passed an assignment or a function",error.getMessage());
			}
	   }
	   @Test
	    public void TernaryNode() throws Exception {
		   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   
		   VariableReferenceNode v = new VariableReferenceNode("hours");
		   
		   ConstantNode constant = new ConstantNode("6");
		   Optional<Node> constant2 =Optional.of(new ConstantNode("8")); 
		   Optional<Node> op2 = Optional.of(new OperationNode(constant,OperationNode.Operation.GREATERT,constant2));
		   ConstantNode constantb =(new ConstantNode("enough sleep")); 
		   ConstantNode constantc =(new ConstantNode("need more sleep")); 

		   AssignmentNode aNode  =new AssignmentNode(v,op2);
		   
		   InterpreterDataType idt = new InterpreterDataType(aNode.toString());
		   params.put(v.toString(),idt);
		   
		   TernaryNode ter = new TernaryNode(aNode,constantb,constantc);
		   
		   HashMap<String, InterpreterDataType> params2 = new HashMap<String,InterpreterDataType>();
		   ProgramNode program2 = new ProgramNode();
		   Interpreter interpreter2 = new Interpreter(program2,null);
		   
		   VariableReferenceNode v2 = new VariableReferenceNode("hours");
		   
		   ConstantNode slept = new ConstantNode("9");
		   Optional<Node> scale =Optional.of(new ConstantNode("8")); 
		   Optional<Node> opt2 = Optional.of(new OperationNode(slept,OperationNode.Operation.GREATERT,scale));
		   ConstantNode truth =(new ConstantNode("enough sleep")); 
		   ConstantNode lie =(new ConstantNode("need more sleep")); 

		   AssignmentNode aN  =new AssignmentNode(v2,opt2);
		   
		   InterpreterDataType idt2 = new InterpreterDataType(aN.toString());
		   params.put(v2.toString(),idt2);
		   
		   TernaryNode ter2 = new TernaryNode(aN,truth,lie);
		   
		  assertEquals("need more sleep",interpreter.getIDT(ter, params).getStrings());
		  assertEquals("enough sleep",interpreter.getIDT(ter2, params).getStrings());
	   }
	   @Test
	   //Handles both arrays and scalars 
	    public void VariableReferenceNode() throws Exception {
		   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   VariableReferenceNode v = new VariableReferenceNode("a");
		   Optional <Node> cont = Optional.of(new ConstantNode("5"));
		   AssignmentNode assign =new AssignmentNode(v,cont);
		   InterpreterDataType VR = new InterpreterDataType(assign.toString());
		   params.put(v.toString(),VR);
		   
		   assertEquals("5",interpreter.getIDT(assign, params).getStrings());
		   
		   VariableReferenceNode v2 = new VariableReferenceNode("FS");
		   params.put(null,null);
		   assertEquals(" ",interpreter.getIDT(v2, params).getStrings());
		   
		   VariableReferenceNode vN = new VariableReferenceNode("arrayName");
		  InterpreterArrayData
		   AssignmentNode assign =new AssignmentNode(v,cont);
		   InterpreterDataType VR = new InterpreterDataType(assign.toString());
		   params.put(v.toString(),VR);
		   
		   assertEquals("5",interpreter.getIDT(assign, params).getStrings());
		   
		   VariableReferenceNode v2 = new VariableReferenceNode("FS");
		   params.put(null,null);
		   assertEquals(" ",interpreter.getIDT(v2, params).getStrings());
		   
	   }
	   @Test
	    public void OperationsMath() throws Exception {
		   
		   //Handles +,-,*,/,^,% 
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   ConstantNode constant = new ConstantNode("5");
		   Optional<Node> constant2 =Optional.of(new ConstantNode("8"));
		   
		   
		   VariableReferenceNode add = new VariableReferenceNode("a");
		   Optional<Node> op = Optional.of(new OperationNode(constant,OperationNode.Operation.ADD,constant2));
		   AssignmentNode assign =new AssignmentNode(add,op);
		   InterpreterDataType addidt = new InterpreterDataType(op.get().toString());
		   params.put(add.toString(),addidt);
		   
		   VariableReferenceNode subtract = new VariableReferenceNode("a"); 
		   Optional<Node> op2 = Optional.of(new OperationNode(constant,OperationNode.Operation.SUBTRACT,constant2));
		   AssignmentNode assign2 =new AssignmentNode(subtract,op2);
		   InterpreterDataType subidt = new InterpreterDataType(op2.get().toString());
		   params.put(subtract.toString(),subidt);
		   
		   VariableReferenceNode multiply = new VariableReferenceNode("a");
		   Optional<Node> op3 = Optional.of(new OperationNode(constant,OperationNode.Operation.MULTIPLY,constant2));
		   AssignmentNode assign3 =new AssignmentNode(multiply,op3);
		   InterpreterDataType mulidt = new InterpreterDataType(op3.get().toString());
		   params.put(multiply.toString(),mulidt);
		   
		   VariableReferenceNode divide = new VariableReferenceNode("a");
		   Optional<Node> op4 = Optional.of(new OperationNode(constant,OperationNode.Operation.DIVIDE,constant2));
		   AssignmentNode assign4 =new AssignmentNode(divide,op4);
		   InterpreterDataType dividt = new InterpreterDataType(op4.get().toString());
		   params.put(divide.toString(),dividt);
		   
		   VariableReferenceNode exponent = new VariableReferenceNode("a");
		   Optional<Node> op5 = Optional.of(new OperationNode(constant,OperationNode.Operation.EXPONENT,constant2));
		   AssignmentNode assign5 =new AssignmentNode(exponent,op5);
		   InterpreterDataType expidt = new InterpreterDataType(op5.get().toString());
		   params.put(exponent.toString(),expidt);
		   
		   VariableReferenceNode modulo = new VariableReferenceNode("a");
		   Optional<Node> op6 = Optional.of(new OperationNode(constant,OperationNode.Operation.MODULO,constant2));
		   AssignmentNode assign6 =new AssignmentNode(modulo,op6);
		   InterpreterDataType modidt = new InterpreterDataType(op6.get().toString());
		   params.put(modulo.toString(),modidt);
		   
		   
		   
		   assertEquals("13.0",interpreter.getIDT(assign, params).getStrings());
		   assertEquals("-3.0",interpreter.getIDT(assign2, params).getStrings());
		   assertEquals("40.0",interpreter.getIDT(assign3, params).getStrings());
		   assertEquals("0.625",interpreter.getIDT(assign4, params).getStrings());
		   assertEquals("390625.0",interpreter.getIDT(assign5, params).getStrings());
		   assertEquals("5.0",interpreter.getIDT(assign6, params).getStrings());
	   }
	   
	   @Test
	   //equal,notequal,GREATERT,LESSOREQ,GREATEROREQ
	    public void OperationsComparisons() throws Exception {
		   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   
		   ConstantNode constantNum = new ConstantNode("5");
		   Optional<Node> constantNum2 =Optional.of(new ConstantNode("8"));
		   VariableReferenceNode str = new VariableReferenceNode("a");
		   
		   Optional<Node> op = Optional.of(new OperationNode(constantNum,OperationNode.Operation.EQUAL,constantNum2));
		   AssignmentNode assign =new AssignmentNode(str,op);
		   InterpreterDataType eQidt = new InterpreterDataType(op.get().toString());
		   params.put(str.toString(),eQidt);
		   
		   assertEquals("0",interpreter.getIDT(assign, params).getStrings());
		   
		   Optional<Node> op2 = Optional.of(new OperationNode(constantNum,OperationNode.Operation.NOTEQ,constantNum2));
		   AssignmentNode assign2 =new AssignmentNode(str,op2);
		   InterpreterDataType noEQidt = new InterpreterDataType(op2.get().toString());
		   params.put(str.toString(),noEQidt);	   
		   
		   assertEquals("1",interpreter.getIDT(assign2, params).getStrings());
		   
		   Optional<Node> op3 = Optional.of(new OperationNode(constantNum,OperationNode.Operation.GREATERT,constantNum2));
		   AssignmentNode assign3 =new AssignmentNode(str,op3);
		   InterpreterDataType grIDT = new InterpreterDataType(op3.get().toString());
		   params.put(str.toString(),grIDT);	   
		   
		   assertEquals("0",interpreter.getIDT(assign3, params).getStrings());
		   
		   Optional<Node> op4 = Optional.of(new OperationNode(constantNum,OperationNode.Operation.LESST,constantNum2));
		   AssignmentNode assign4 =new AssignmentNode(str,op4);
		   InterpreterDataType lIDT = new InterpreterDataType(op4.get().toString());
		   params.put(str.toString(),lIDT);	   
		   
		   assertEquals("1",interpreter.getIDT(assign4, params).getStrings());
		   
		   ConstantNode constantNuma = new ConstantNode("8");
		   Optional<Node> constantNum2b =Optional.of(new ConstantNode("8"));
		   
		   Optional<Node> op5 = Optional.of(new OperationNode(constantNuma,OperationNode.Operation.GREATEROREQ,constantNum2b));
		   AssignmentNode assign5 =new AssignmentNode(str,op5);
		   InterpreterDataType gIDT = new InterpreterDataType(op5.get().toString());
		   params.put(str.toString(),gIDT);	   
		   
		   assertEquals("1",interpreter.getIDT(assign5, params).getStrings());
		   
		   Optional<Node> op6 = Optional.of(new OperationNode(constantNuma,OperationNode.Operation.LESSOREQ,constantNum2b));
		   AssignmentNode assign6 =new AssignmentNode(str,op6);
		   InterpreterDataType lqIDT = new InterpreterDataType(op6.get().toString());
		   params.put(str.toString(),lqIDT);	   
		   
		   assertEquals("1",interpreter.getIDT(assign6, params).getStrings());
	   }
	   
	   @Test
	   //equal,notequal,GREATERT,LESSOREQ,GREATEROREQ
	    public void OperationsComparisonsStrings() throws Exception {
		   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   
		   ConstantNode constantNum = new ConstantNode("word");
		   Optional<Node> constantNum2 =Optional.of(new ConstantNode("words"));
		   Optional<Node> op = Optional.of(new OperationNode(constantNum,OperationNode.Operation.EQUAL,constantNum2));
		   InterpreterDataType eQidt = new InterpreterDataType(op.get().toString());
		   params.put(op.toString(),eQidt);
		   
		   assertEquals("0",interpreter.getIDT(op.get(), params).getStrings());
		   
		   Optional<Node> op2 = Optional.of(new OperationNode(constantNum,OperationNode.Operation.NOTEQ,constantNum2));
		   InterpreterDataType noEQidt = new InterpreterDataType(op2.get().toString());
		   params.put(op2.toString(),noEQidt);	   
		   
		   assertEquals("1",interpreter.getIDT(op2.get(), params).getStrings());
		   
		   Optional<Node> op3 = Optional.of(new OperationNode(constantNum,OperationNode.Operation.GREATERT,constantNum2));
		   InterpreterDataType grIDT = new InterpreterDataType(op3.get().toString());
		   params.put(op3.toString(),grIDT);	   
		   
		   assertEquals("0",interpreter.getIDT(op3.get(), params).getStrings());
		   
		   Optional<Node> op4 = Optional.of(new OperationNode(constantNum,OperationNode.Operation.LESST,constantNum2));
		   InterpreterDataType lIDT = new InterpreterDataType(op4.get().toString());
		   params.put(op4.toString(),lIDT);	   
		   
		   assertEquals("1",interpreter.getIDT(op4.get(), params).getStrings());
		   
		   ConstantNode constantNuma = new ConstantNode("hello");
		   Optional<Node> constantNum2b =Optional.of(new ConstantNode("hello"));
		   
		   Optional<Node> op5 = Optional.of(new OperationNode(constantNuma,OperationNode.Operation.GREATEROREQ,constantNum2b));
		   InterpreterDataType gIDT = new InterpreterDataType(op5.get().toString());
		   params.put(op5.toString(),gIDT);	   
		   
		   assertEquals("1",interpreter.getIDT(op5.get(), params).getStrings());
		   
		   Optional<Node> op6 = Optional.of(new OperationNode(constantNuma,OperationNode.Operation.LESSOREQ,constantNum2b));
		   InterpreterDataType lqIDT = new InterpreterDataType(op6.get().toString());
		   params.put(op6.toString(),lqIDT);	   
		   
		   assertEquals("1",interpreter.getIDT(op6.get(), params).getStrings());
	   }
	   @Test
	    public void OperationsMatch() throws Exception {
		   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   
		   ConstantNode string = new ConstantNode("washington");
		   Optional<Node> regex =Optional.of(new PatternNode("shin"));
		   Optional<Node> op = Optional.of(new OperationNode(string,OperationNode.Operation.MATCH,regex));
		   InterpreterDataType IDT = new InterpreterDataType(op.get().toString());
		   
		   assertEquals("1",interpreter.getIDT(op.get(), params).getStrings());
	   }
	   @Test
	    public void OperationsNotMatch() throws Exception {
		   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   
		   ConstantNode string = new ConstantNode("washington");
		   Optional<Node> regex =Optional.of(new PatternNode("shin"));
		   Optional<Node> op = Optional.of(new OperationNode(string,OperationNode.Operation.NOTMATCH,regex));
		   InterpreterDataType IDT = new InterpreterDataType(op.get().toString());
		   
		   assertEquals("0",interpreter.getIDT(op.get(), params).getStrings());
	   }
	   @Test
	   //,AND,OR,NOT
	    public void OperationsBoolean() throws Exception {
		   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   
		   ConstantNode constantNum = new ConstantNode("5");
		   Optional<Node> constantNum2 =Optional.of(new ConstantNode("8"));
		   VariableReferenceNode str = new VariableReferenceNode("a");
		   VariableReferenceNode str2 = new VariableReferenceNode("a");
		   
		   Optional<Node> op = Optional.of(new OperationNode(constantNum,OperationNode.Operation.GREATERT,constantNum2));
		   AssignmentNode assign =new AssignmentNode(str,op);
		   InterpreterDataType IDT = new InterpreterDataType(op.get().toString());
		   params.put(str.toString(),IDT);
		   
		   Optional<Node> op2 = Optional.of(new OperationNode(constantNum2.get(),OperationNode.Operation.GREATERT,Optional.of(constantNum)));
		   AssignmentNode assign2 =new AssignmentNode(str2,op2);
		   InterpreterDataType IDT2 = new InterpreterDataType(op2.get().toString());
		   params.put(str2.toString(),IDT2);
		   
		   Optional<Node> operation = Optional.of(new OperationNode(op.get(),OperationNode.Operation.AND,op2));
		   assertEquals("0",interpreter.getIDT(operation.get(), params).getStrings());
		   
		   Optional<Node> operation2 = Optional.of(new OperationNode(op.get(),OperationNode.Operation.OR,op2));
		   assertEquals("1",interpreter.getIDT(operation2.get(), params).getStrings());
		   
		   Optional<Node> operation3 = Optional.of(new OperationNode(op.get(),OperationNode.Operation.NOT));
		   assertEquals("1",interpreter.getIDT(operation3.get(), params).getStrings());
	  
		   ConstantNode co = new ConstantNode("word");
		   Optional<Node> con =Optional.of(new ConstantNode("word"));
		   VariableReferenceNode str4 = new VariableReferenceNode("a");
		   
		   Optional<Node> op4 = Optional.of(new OperationNode(co,OperationNode.Operation.OR,con));
		   AssignmentNode assign4 =new AssignmentNode(str4,op4);
		   InterpreterDataType IDT4 = new InterpreterDataType(op4.get().toString());
		   params.put(str4.toString(),IDT4);
		  
		   assertEquals("1",interpreter.getIDT(assign4, params).getStrings());
		   
		   ConstantNode co1 = new ConstantNode("word");
		   Optional<Node> con1 =Optional.of(new ConstantNode("false"));
		   VariableReferenceNode stra = new VariableReferenceNode("a");
		   
		   Optional<Node> opa = Optional.of(new OperationNode(co1,OperationNode.Operation.OR,con1));
		   AssignmentNode assigna =new AssignmentNode(stra,opa);
		   InterpreterDataType IDTa = new InterpreterDataType(opa.get().toString());
		   params.put(stra.toString(),IDTa);
		  
		   assertEquals("1",interpreter.getIDT(assigna, params).getStrings());
		   
		   ConstantNode co3 = new ConstantNode("word");
		   Optional<Node> con3 =Optional.of(new ConstantNode("false"));
		   VariableReferenceNode str3 = new VariableReferenceNode("a");
		   
		   Optional<Node> op3 = Optional.of(new OperationNode(co3,OperationNode.Operation.AND,con3));
		   AssignmentNode assign3 =new AssignmentNode(str3,op3);
		   InterpreterDataType IDT3 = new InterpreterDataType(op3.get().toString());
		   params.put(str3.toString(),IDT3);
		  
		   assertEquals("0",interpreter.getIDT(assign3, params).getStrings());
	   
	   
	   
	   }
	   @Test
	    public void OperationsDollar() throws Exception {

		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   InterpreterDataType IDTa = new InterpreterDataType("the test works");
		   
		   interpreter.globalVariables.put("$7", IDTa);
		   
		   ConstantNode constantNum = new ConstantNode("7");
		   OperationNode op = new OperationNode(constantNum,OperationNode.Operation.DOLLAR);   
		   assertEquals("the test works",interpreter.getIDT(op, params).getStrings());
	   }
	   @Test
	    public void OperationsPrePostUnary() throws Exception {
		   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   ConstantNode constantNum = new ConstantNode("5");
		   Optional<Node> constantNum2 =Optional.of(new ConstantNode("8"));
		   VariableReferenceNode str = new VariableReferenceNode("a");
		   VariableReferenceNode str2 = new VariableReferenceNode("b");
		   
		
		   Optional<Node> op = Optional.of(new OperationNode(constantNum,OperationNode.Operation.PREINC));
		   AssignmentNode assign =new AssignmentNode(str,op);
		   InterpreterDataType IDT = new InterpreterDataType(assign.toString());
		   params.put(str.toString(),IDT);
		   
		   assertEquals("6.0",interpreter.getIDT(assign, params).getStrings());
		   
		   Optional<Node> op2 = Optional.of(new OperationNode(constantNum2.get(),OperationNode.Operation.PREDEC));
		   AssignmentNode assign2 =new AssignmentNode(str2,op2);
		   InterpreterDataType eQidt = new InterpreterDataType(assign2.toString());
		   params.put(str2.toString(),eQidt);
		   
		   assertEquals("7.0",interpreter.getIDT(assign2, params).getStrings());
		   
		
		   Optional<Node> op3 = Optional.of(new OperationNode(constantNum,OperationNode.Operation.POSTINC));
		   AssignmentNode assign3 =new AssignmentNode(str,op3);
		   InterpreterDataType IDT2 = new InterpreterDataType(op.get().toString());
		   params.put(str.toString(),IDT2);
		   
		   assertEquals("5.0",interpreter.getIDT(assign3, params).getStrings());
		   
		   Optional<Node> op4 = Optional.of(new OperationNode(constantNum,OperationNode.Operation.UNARYPOS));
		   AssignmentNode assign4 =new AssignmentNode(str,op4);
		   InterpreterDataType IDT4 = new InterpreterDataType(op4.get().toString());
		   params.put(str.toString(),IDT4);
		   
		   assertEquals("+5.0",interpreter.getIDT(assign4, params).getStrings());
		   
		   Optional<Node> op5 = Optional.of(new OperationNode(constantNum,OperationNode.Operation.UNARYNEG));
		   AssignmentNode assign5 =new AssignmentNode(str,op5);
		   InterpreterDataType IDT5 = new InterpreterDataType(op5.get().toString());
		   params.put(str.toString(),IDT5);
		   
		   assertEquals("-5.0",interpreter.getIDT(assign5, params).getStrings());

	   }
	   @Test
	    public void OperationsConcatenation() throws Exception {
	   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   ConstantNode constantNum = new ConstantNode("hello");
		   Optional<Node> constantNum2 =Optional.of(new ConstantNode("world"));
		   VariableReferenceNode str = new VariableReferenceNode("a");
		   
		   Optional<Node> op = Optional.of(new OperationNode(constantNum,OperationNode.Operation.CONCATENATION,constantNum2));
		   AssignmentNode assign =new AssignmentNode(str,op);
		   InterpreterDataType IDT = new InterpreterDataType(assign.toString());
		   params.put(str.toString(),IDT);
		   
		   assertEquals("hello world",interpreter.getIDT(assign, params).getStrings());
	   }
	
	   @Test
//	   Ensure the right-hand side is a variable reference and an array.
//	   Look up the left-hand side in the array (from globals or locals).
	    public void OperationsIn() throws Exception {
		  
		   ProgramNode program = new ProgramNode();
		   Interpreter interpreter = new Interpreter(program,null);
		   ConstantNode find = new ConstantNode("2");
		   Optional <Node> in = Optional.of(new VariableReferenceNode("arraytest"));
		   
		   HashMap<String, InterpreterDataType> params = new HashMap<String,InterpreterDataType>();
		   HashMap<String, InterpreterDataType> array = new HashMap<String,InterpreterDataType>();
		   int i=0;	
		   int adder = 5;
		   
	       while(i<6) {
			   array.put(String.valueOf(i), new InterpreterDataType(String.valueOf(adder)));
			   i++;
			   adder++;}
		 
		   
		   interpreter.globalVariables.put("arraytest", new InterpreterArrayDataType(array));
		   OperationNode op = new OperationNode(find,OperationNode.Operation.IN,in);
		   
		   params.put(null,null);
		   
		  assertEquals("1",interpreter.getIDT(op, params).getStrings());
	   }

	   
}
