import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

	HashMap<String, InterpreterDataType> globalVariables = new HashMap<String, InterpreterDataType>();
	public HashMap<String, FunctionDefinitionNode> functionSource = new HashMap<String, FunctionDefinitionNode>();
	//HashMap<String, InterpreterDataType> LocalVariables= new HashMap<String, InterpreterDataType>();
	Path FILENAME;
	LineManager lManager = new LineManager(null);

	
	public Interpreter(ProgramNode program, Path filePath) {
		List<String> lines = null;

		this.FILENAME = filePath;
		globalVariables.put("FS", new InterpreterDataType(" ")); // field separator, which separates lines into fields
		globalVariables.put("OFMT", new InterpreterDataType("%.6g"));// output format for numbers
		globalVariables.put("OFS", new InterpreterDataType(" "));// Output Field Separator to determine how output will be printed
		globalVariables.put("ORS", new InterpreterDataType("'\n\'"));// output record separator determines how lines are separated in output
		
		
		if (filePath == null) {
			lManager = new LineManager(List.of());// send it an empty linked list
		} else {
			try {
				lines = Files.readAllLines(filePath);
				lManager = new LineManager(lines);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		// add functions from parse function to interpreter function hash map
		// program function had a linked list of function nodes which we need to add to the functionSource
		for (int i = 0; i < program.getFunctionDef().size(); i++) {
			functionSource.put(program.getFunctionDef().get(i).getFunctionName(), program.getFunctionDef().get(i));
		}
         //    the interger in build in 
		//compare in run function call 
		
		functionSource.put("PRINT", new BuiltInFunctionNode("PRINT", true, params -> {
			int count = 0;
			String string = " ";
           // System.out.println("in print");
			// get the global variables hash map of the InterpreterArrayDataType
			InterpreterArrayDataType value = (InterpreterArrayDataType) (params.get("0"));
			while (true) {// global variable could have a whole bunch so just make it loop till the number does not contain anything
				if (value.getGlobalVariables().containsKey(Integer.toString(count))) {
					string += value.getGlobalVariables().get(Integer.toString(count)).getStrings() + " ";
					//System.out.println("at word" + string);
					count++;
				} else {
					break;
				}
			}
			System.out.println(string.toString());
			return string;
		}));

		// loop twice once for the formatters then once for the strings
		functionSource.put("PRINTF", new BuiltInFunctionNode("PRINTF", true, params -> {
			//params has the format specifier  then a the things you're printing 
			StringBuilder printing = new StringBuilder();
			var stotre = (InterpreterArrayDataType)params.get("1");
			var formater = stotre.getGlobalVariables().get("0");//gets the formatter
		
		   //while there are more strings, keep going through the indexes
		    int sizeOfHash = stotre.getGlobalVariables().size();
		    
		    //append a string the gets and print that
		    //for loop with the size and make sure its not null then print it 
		    for(int i =0; i< sizeOfHash; i++) {
		    	if(stotre.iadtHashmap.containsKey(String.valueOf(i))) {
		    		var trial = stotre.iadtHashmap.get(String.valueOf(i)).toString();
		    		if(!trial.equals("null")) {
		    			 printing.append(trial);
		    		}
		    	}
		    }
		   
			return  printing.toString();
		}));

		// reads the next line of a text file and updates the global variables
		// reads and processes the line
		functionSource.put("GETLINE", new BuiltInFunctionNode("GETLINE", false, params -> {
			if (lManager.SplitAndAssign()) {
				return "1";
			} else {
				return "0";
			}
		}));

		// advances to the next line without processing the current line, can be used to skip lines
		functionSource.put("NEXT", new BuiltInFunctionNode("NEXT", false, params -> {
			if (lManager.SplitAndAssign()) {
				return "1";
			} else {
				return "0";
			}
		}));
		// replaces all occurrences of a specified pattern with a specific string
		// pattern, replacement, string to change
		// in the hash map we have the IDTs and were accessing the strings within the IDTs
		functionSource.put("GSUB", new BuiltInFunctionNode("GSUB", false, params -> {
		//	functionSource.put("gsub", new BuiltInFunctionNode("gsub", false, params, LL -> {
			String pattern = params.get("1").getStrings();// gets the string searching through
			String replacement = params.get("2").getStrings();// gets what were searching for
			String string = params.get("3").getStrings();// gets what we are replacing the pattern with

			int count = 0;

			Pattern patternA = Pattern.compile(pattern);
			Matcher match = patternA.matcher(string);

			while (match.find()) {
				count++;
			}
			string.replaceAll(replacement, pattern);
			return Integer.toString(count);
		}));

		// finds the position of a substring within a given string and returns the
		// index(not line)
		functionSource.put("INDEX", new BuiltInFunctionNode("INDEX", false, params -> {
			String string = params.get("1").getStrings();// gets the string searching through
			String find = params.get("2").getStrings();// gets what were searching for
			int index = string.indexOf(find);
			return Integer.toString(index);
		}));

		// finds the number of characters in a string
		functionSource.put("LENGTH", new BuiltInFunctionNode("LENGTH", false, params -> {
			String current = params.get("1").getStrings();// this gets the string we want to check the length of which is send after the function call
			int length = current.length();
			return Integer.toString(length);
		}));

		// searches for regular expressions in a given string and returns the position
		// of the first occurrence of the pattern
		functionSource.put("MATCH", new BuiltInFunctionNode("MATCH", false, params -> {
			String string = params.get("1").getStrings();// gets the string searching through
			String pattern = params.get("2").getStrings();// gets what were searching for

			Pattern searchPattern = Pattern.compile(pattern);
			Matcher find = searchPattern.matcher(string);

			if (find.find()) {
				return Integer.toString(find.start());
			} else {
				return "error";
			}
		}));

		// used to split a string into a array of substrings based on a delimiter and
		// returns the number of substrings made
		// there might be a forth parameter
		functionSource.put("SPLIT", new BuiltInFunctionNode("SPLIT", false, params -> {
			String string = params.get("1").getStrings();// gets the string to split
			InterpreterArrayDataType array = (InterpreterArrayDataType) (params.get("2")); // array tostore split values
			Optional<String> delimiter = Optional.ofNullable(params.get("3").getStrings()); // optional delimeter
			String[] splits;// array to store the split string

			if (delimiter.isEmpty()) {
				// get the FS value from the hash map
				splits = string.split(globalVariables.get("FS").getStrings());
				int count = 0;
				while (count < splits.length) {
					params.put(Integer.toString(count), new InterpreterDataType(splits[count]));
				//	array.globalVariables.put(Integer.toString(count), array);
				}
			} else {

				splits = string.split(delimiter.get());
				int count = 0;
				while (count < splits.length) {
					params.put(Integer.toString(count), new InterpreterDataType(splits[count]));
				}
			}

			return Integer.toString(splits.length);
		}));

		// substitute the first occurrence of a regular expression pattern in a string
		// with a replacement
		// returns number of substitutions made
		functionSource.put("SUB", new BuiltInFunctionNode("SUB", false, params -> {
			String pattern = params.get("1").getStrings();// gets what were searching for
			String replacewith = params.get("2").getStrings();// gets what we are replacing the pattern with
			String string = params.get("3").getStrings();// gets the string searching through

			String newString = string.replaceFirst(pattern, replacewith);
			params.put("$0", new InterpreterDataType(newString));

			Pattern searchPattern = Pattern.compile(pattern);
			Matcher find = searchPattern.matcher(string);

			int count = 0;
			if (find.find()) {
				count++;
			}
			return Integer.toString(count);
		}));

		// extracts a substring from a string by taking a starting point and a length
		// then returns the substring
		// if the length is not there is returns the whole substring
		// (input string,start pos,optional length of substring)
		functionSource.put("SUBSTR", new BuiltInFunctionNode("SUBSTR", false, params -> {
			String subString;
			String string = params.get("1").getStrings();
			int start = Integer.parseInt(params.get("2").getStrings()); // gets the starting index

			if (params.get("3") != null && params.get("3").getStrings() != null) {
				int length = Integer.parseInt(params.get("3").getStrings());

				if (start > string.length()) {
					return "-1";
				}
				if (length < 0) {
					length = 0;
				}

				if (length > string.length()) {
					length = string.length();
				}

				subString = string.substring(start, start + length);
			} else {
				subString = string.substring(start);
			}

			return subString;
		}));

		// converts all chars in a string to lower case
		functionSource.put("TOLOWER", new BuiltInFunctionNode("TOLOWER", false, params -> {
			String string = params.get("1").getStrings();// gets the string to change
			String newString = string.toLowerCase();

			return newString;
		}));

		// converts all chars in a string to lower case
		functionSource.put("TOUPPER", new BuiltInFunctionNode("TOUPPER", false, params -> {
			String string = params.get("1").getStrings();// gets the string to change
			String newString = string.toUpperCase();

			return newString;
		}));

	}

	public String getFunction(HashMap<String, InterpreterDataType> params, String keyword) {
		BuiltInFunctionNode function = (BuiltInFunctionNode) functionSource.get(keyword);
		return function.execute(params);
	}

	public HashMap<String, InterpreterDataType> getGlobalVariables() {
		return globalVariables;
	}

	public HashMap<String, FunctionDefinitionNode> getFunctionSource() {
		return functionSource;
	}

	// method which takes a node and a hash map,evaluates the node and returns an IDT.
	// The locals is the params
	public InterpreterDataType getIDT(Node node, HashMap<String, InterpreterDataType> LocalVariables) throws Exception {
		if (!node.equals(null)) {
			// check if the VRN is a array reference or not and ensure it has an IADT
			//gets the value of a variable, also looks in arrays if necessary
			if (node instanceof VariableReferenceNode) {
				String name = ((VariableReferenceNode) node).getVariableName().toString();
				var expression = ((VariableReferenceNode) node).getExpression();

				if (expression.isEmpty()) {
					if (LocalVariables.containsKey(name)) {
						var value = LocalVariables.get(name);
						return new InterpreterDataType(value.toString());
					} else if (globalVariables.containsKey(name)) {
						var value = globalVariables.get(name);
						return new InterpreterDataType(value.getStrings());
					}
					else {
						globalVariables.put(name, new InterpreterDataType(""));
						return new InterpreterDataType(globalVariables.get(name).toString());	
					}
				}
				//if there is an index to evaluate
				if (expression.isPresent()) {
					var expressionV = getIDT(expression.get(), globalVariables).getStrings();//resolve the index
					
					if(globalVariables.containsKey(name)) {//find the array in the hashmap
						var arrayN =globalVariables.get(name);//gets the array from the hashmap
						var valAtIndex = ((InterpreterArrayDataType)arrayN).getGlobalVariables().get(expressionV);//gets the value at the index 
						return valAtIndex;
					}
					 else {
						throw new Exception("array does not exist");
					}		
				}
			}

			//Evaluates a boolean condition and returns the true or false condition
			if (node instanceof TernaryNode) {
				var condition = ((TernaryNode) node).condition;
				var truth = ((TernaryNode) node).trueValue;
				var falseC = ((TernaryNode) node).falseValue;

				var evaluater = getIDT(condition, LocalVariables);

				if (!evaluater.getStrings().equals("0")) {
					return new InterpreterDataType(getIDT(truth, null).getStrings());
				} else {
					return new InterpreterDataType(getIDT(falseC, null).getStrings());
				}

			}
			//Throws exception as patterns are not allowed in functions or assignments.
			if (node instanceof PatternNode) {
				throw new Exception("A pattern cannot be passed an assignment or a function");
			}
			
			if (node instanceof FunctionCallNode) {
				var result = RunFunctionCall((FunctionCallNode) node,null);
				//set values
				return new InterpreterDataType(result);
			}
			//Creates an IDT with the constant's value
			if (node instanceof ConstantNode) {
				var value = (ConstantNode) node;
				//set 
				return new InterpreterDataType(value.getConstant());
			}
			
            //Ensures that the target is a variable and assigns the result of evaluating the right side to the target variable.
			if (node instanceof AssignmentNode) {
				var target = ((AssignmentNode) node).lValue;
				var expression = ((AssignmentNode) node).expr;
				if (target instanceof VariableReferenceNode || target instanceof OperationNode){//if the target is an instance of VariableReferenceNode or OperationNode
					//var targetV = getIDT(target, LocalVariables).getStrings();
					var expressionV = getIDT(expression.get(), LocalVariables);
					globalVariables.put(target.toString(), expressionV);
					return new InterpreterDataType(expressionV.toString());
				}
				if (target instanceof OperationNode && ((OperationNode) target).operationType.equals(OperationNode.Operation.DOLLAR)) {
					if (((OperationNode) target).operationType.toString().equals("DOLLAR")) {
						var targetV = getIDT(target, null).getStrings();
						var expressionV = getIDT(expression.get(), null);
						globalVariables.put(targetV, expressionV);
						return new InterpreterDataType(expressionV.getStrings());
					}
				}
			}
		}
		//Evaluates and performs operations on values based on the operation type, and returns an IDT with the result of the operation.
		if (node instanceof OperationNode) {
		
			var left = ((OperationNode) node).left;
			var operation = ((OperationNode) node).operationType;
			Optional<Node> right = ((OperationNode) node).right;

			if (operation.toString() == "EQUAL") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {

					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());

					if (Leftf == rightf) {
						return new InterpreterDataType("1");
					} else {
						return new InterpreterDataType("0");
					}

				} else {
					String Leftf = (getIDT(left, LocalVariables).getStrings());
					String rightf = (getIDT(right.get(), LocalVariables).getStrings());

					if (Leftf.equals(rightf)) {
						return new InterpreterDataType("1");
					} else {
						return new InterpreterDataType("0");
					}
				}
			}

			if (operation.toString() == "NOTEQ") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {

					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());

					if (Leftf != rightf) {
						return new InterpreterDataType("1");
					} else {
						return new InterpreterDataType("0");
					}

				} else {
					String Leftf = (getIDT(left, LocalVariables).getStrings());
					String rightf = (getIDT(right.get(), LocalVariables).getStrings());

					if (!Leftf.equals(rightf)) {
						return new InterpreterDataType("1");
					} else {
						return new InterpreterDataType("0");
					}
				}
			}

			if (operation.toString() == "GREATERT") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {

					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());

					if (Leftf > rightf) {
						return new InterpreterDataType("1");
					} else {
						return new InterpreterDataType("0");
					}

				} else {
					String Leftf = (getIDT(left, LocalVariables).getStrings());
					String rightf = (getIDT(right.get(), LocalVariables).getStrings());
					int compare = Leftf.compareTo(rightf);
					if (compare > 0) {
						return new InterpreterDataType("1");
					} else if (compare < 0) {
						return new InterpreterDataType("0");
					}
				}
			}

			if (operation.toString() == "LESSOREQ") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {

					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());

					if (Leftf <= rightf) {
						return new InterpreterDataType("1");
					} else {
						return new InterpreterDataType("0");
					}

				} else {
					String Leftf = (getIDT(left, LocalVariables).getStrings());
					String rightf = (getIDT(right.get(), LocalVariables).getStrings());
				
					int compare = Leftf.compareTo(rightf);
					if (compare > 0) {
						return new InterpreterDataType("0");
					} else if (compare < 0 || compare == 0) {
						return new InterpreterDataType("1");
					}
				}

			}
			if (operation.toString() == "LESST") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {

					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());

					if (Leftf < rightf) {
						return new InterpreterDataType("1");
					} else {
						return new InterpreterDataType("0");
					}

				} else {
					String Leftf = (getIDT(left, LocalVariables).getStrings());
					String rightf = (getIDT(right.get(), LocalVariables).getStrings());
					
					int compare = Leftf.compareTo(rightf);
					if (compare > 0) {
						return new InterpreterDataType("0");
					} else if (compare < 0) {
						return new InterpreterDataType("1");
					}
				}

			}
			if (operation.toString() == "GREATEROREQ") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {

					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());

					if (Leftf >= rightf) {
						return new InterpreterDataType("1");
					} else {
						return new InterpreterDataType("0");
					}

				} else {
					String Leftf = (getIDT(left, LocalVariables).getStrings());
					String rightf = (getIDT(right.get(), LocalVariables).getStrings());
				
					int compare = Leftf.compareTo(rightf);
					if (compare > 0 || compare == 0) {
						return new InterpreterDataType("1");
					} else if (compare < 0) {
						return new InterpreterDataType("0");
					}
				}

			}
			if (operation.toString() == "AND") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {

					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());

					if (Leftf != 0 && rightf != 0) {
						return new InterpreterDataType("1");
					} else {
						return new InterpreterDataType("0");
					}

				}
				else {
					String Leftf = (getIDT(left, LocalVariables).getStrings());
					String rightf = (getIDT(right.get(), LocalVariables).getStrings());
					
					var bool = (!Leftf.equals("0")&&!rightf.equalsIgnoreCase("false"));
					var bool2 = (!rightf.equals("0")&&!rightf.equalsIgnoreCase("false"));
					var returning = bool && bool2;
					
					if(returning) {
						return new InterpreterDataType("1");
					}
					else {
						return new InterpreterDataType("0");
					}
				}
			}
			if (operation.toString() == "OR") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {

					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());

					if (Leftf != 0 || rightf != 0) {
						return new InterpreterDataType("1");
					} else {
						return new InterpreterDataType("0");
					}

				} else {
					String Leftf = (getIDT(left, LocalVariables).getStrings());
					String rightf = (getIDT(right.get(), LocalVariables).getStrings());
					
					var bool = !(Leftf.equals("0"));
					var bool2 = !(rightf.equals("0"));
					var returning = bool || bool2;
					
					if(returning) {
						return new InterpreterDataType("1");
					}
					else {
						return new InterpreterDataType("0");
					}
				}
			}
			
			if (operation.toString() == "NOT") {
				var Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
				if (Leftf != 0) {
					// do we just return 1 for true and zero for false
					return new InterpreterDataType("0");
					// if its false its not will be true
				} else if (Leftf == 0) {
					return new InterpreterDataType("1");
				}
			}

			if (operation.toString() == "MATCH") {
				var Leftf = getIDT(left, LocalVariables).getStrings();
				PatternNode rightf = (PatternNode) (right.get()); // right must be a pattern node

				Pattern searchPattern = Pattern.compile(rightf.toString());
				Matcher find = searchPattern.matcher(Leftf);

				if (find.find()) {
					return new InterpreterDataType("1");
				} else {
					return new InterpreterDataType("0");
				}
			}
			
			if (operation.toString() == "NOTMATCH") {
				var Leftf = getIDT(left, LocalVariables).getStrings();
				PatternNode rightf = (PatternNode) (right.get()); // right must be a pattern node

				Pattern searchPattern = Pattern.compile(rightf.toString());
				Matcher find = searchPattern.matcher(Leftf);

				if (find.find()) {
					return new InterpreterDataType("0");
				} else {
					return new InterpreterDataType("1");
				}
			}
			if (operation.toString() == "DOLLAR") {
				InterpreterDataType value = null;
				InterpreterDataType Leftf = getIDT(left, LocalVariables);
				if (globalVariables.containsKey("$" + Leftf.getStrings())) {
					value = globalVariables.get("$" + Leftf.getStrings());
				} else {
					return new InterpreterDataType(" ");
				}
				return value;
			}
			//DO ALL OF THESE LIKE THAT CHANGE THE IDT BLAH BLAH BLAH
			if (operation.toString() == "PREINC") {
				var IDT = getIDT(left, LocalVariables);
				var lbool = floatConverter(IDT.getStrings());
				if (lbool) {
					float inc = Float.parseFloat(IDT.getStrings()) + 1;
					IDT.setStrings(Float.toString(inc));
					return new InterpreterDataType(Float.toString(inc));
				}
			}
			
			if (operation.toString() == "POSTINC") {
				var IDT = getIDT(left, LocalVariables);
				var lbool = floatConverter(IDT.getStrings());
				if (lbool) {
					float oldInc= Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float inc = oldInc + 1;
					IDT.setStrings(Float.toString(inc));
					return new InterpreterDataType(Float.toString(oldInc));
				}
			}
			if (operation.toString() == "PREDEC") {
				var IDT = getIDT(left, LocalVariables);
				var lbool = floatConverter(IDT.getStrings());
				if (lbool) {
					float inc = Float.parseFloat(getIDT(left, LocalVariables).getStrings()) - 1;
					IDT.setStrings(Float.toString(inc));
					return new InterpreterDataType(Float.toString(inc));
				}
				else {
					throw new Exception("you cannot do this operation on a string");
				}
			}
			if (operation.toString() == "POSTDEC") {
				var IDT = getIDT(left, LocalVariables);
				var lbool = floatConverter(IDT.getStrings());
				if (lbool) {
					float oldDec = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float dec = oldDec - 1;
					IDT.setStrings(Float.toString(dec));
					return new InterpreterDataType(Float.toString(oldDec));
				}
			}
			// add a negative or posItive i guess
			if (operation.toString() == "UNARYPOS") {
				var IDT = getIDT(left, LocalVariables);
				var lbool = floatConverter(IDT.getStrings());
				if (lbool) {
					float inc = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float newInc = Math.abs(inc);
					IDT.setStrings(Float.toString(newInc));
					return new InterpreterDataType("+" + Float.toString(inc));
				}
			}
			if (operation.toString() == "UNARYNEG") {
				var IDT = getIDT(left, LocalVariables);
				var lbool = floatConverter(IDT.getStrings());
				if (lbool) {
					float inc = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float newInc = Math.abs(inc)*-1;
					IDT.setStrings(Float.toString(newInc));
					return new InterpreterDataType(Float.toString(newInc));
				}
			}
			if (operation.toString() == "CONCATENATION") {
				String Leftf = getIDT(left, LocalVariables).getStrings();
				String rightV = getIDT(right.get(), LocalVariables).getStrings();
				String concat = Leftf + " " +rightV;
				return new InterpreterDataType(concat);

			}
			
//			   Ensure the right-hand side is a variable reference and an array.
//			   Look up the left-hand side in the array (from globals or locals).
			//returns true of falls
			if (operation.toString() == "IN") {
				
				if(right.get() instanceof VariableReferenceNode ) {
					if(globalVariables.containsKey(right.get().toString())) {
						var get = globalVariables.get(right.get().toString());
						if(get instanceof InterpreterArrayDataType) {
							return new InterpreterDataType("1");
						}
						return new InterpreterDataType("1");
					}
					if(LocalVariables.containsKey(right.get().toString())){
						return new InterpreterDataType("1");
					}
					else {
						return new InterpreterDataType("0");
					}
				}
				else {
					throw new Exception("need an array to use IN operation");
				}
			}
			if (operation.toString() == "EXPONENT") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {
					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());
					float result = (float) Math.pow(Leftf, rightf);
					return new InterpreterDataType(Float.toString(result));
				} else {
					throw new Exception("cannot exponentiate strings");
				}
			}

			if (operation.toString() == "ADD") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {
					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());
					float result = Leftf + rightf;

					return new InterpreterDataType(Float.toString(result));
				} else {
					throw new Exception("cannot add strings");
			
				}
			}
			if (operation.toString() == "SUBTRACT") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {
					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());
					float result = Leftf - rightf;

					return new InterpreterDataType(Float.toString(result));
				} else {
					throw new Exception("cannot subtract strings");
					
				}
			}
			if (operation.toString() == "MULTIPLY") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {
					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());
					float result = Leftf * rightf;

					return new InterpreterDataType(Float.toString(result));
				} else {
					throw new Exception("cannot multiply strings");
				}
			}
			if (operation.toString() == "DIVIDE") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {
					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());
					
					if(rightf == 0) {
						throw new Exception("arthmetic error:cannot divide by 0");
					}
					float result = Leftf / rightf;

					return new InterpreterDataType(Float.toString(result));
				} else {
					throw new Exception("cannot divide strings");
				}
			}
			if (operation.toString() == "MODULO") {
				var lbool = floatConverter(getIDT(left, LocalVariables).getStrings());
				var rbool = floatConverter(getIDT(right.get(), LocalVariables).getStrings());

				if (lbool && rbool) {
					float Leftf = Float.parseFloat(getIDT(left, LocalVariables).getStrings());
					float rightf = Float.parseFloat(getIDT(right.get(), LocalVariables).getStrings());
					
					if(rightf == 0) {
						throw new Exception("arthmetic error:cannot divide by 0");
					}
					float result = Leftf % rightf;

					return new InterpreterDataType(Float.toString(result));
				} else {
					throw new Exception("cannot use modulo with strings");
				}
			}
		}
		return null;
	}
	public boolean floatConverter(String idtResult) {
		float converted;
		try {
			converted = Float.parseFloat(idtResult);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	//processes individual statements in blocks
	public ReturnType ProcessStatement(HashMap<String, InterpreterDataType> locals, StatementNode statement) throws Exception {
		//System.out.println("in process statement");
		
		if (!statement.equals(null)) {
		
		if(statement instanceof  breakNode) {
			return new ReturnType(ReturnType.ReturnTypes.BREAK);
		}
		if(statement instanceof  continueNode) {
			return new ReturnType(ReturnType.ReturnTypes.CONTINUE);
		}
	 
		if(statement instanceof  deleteNode) {
			Node deleteNode = ((deleteNode)statement).arrayName; 
			VariableReferenceNode VariableNode = ((VariableReferenceNode)deleteNode);//has an array name pointing to an array
			String arrayName = VariableNode.getVariableName();//array name
			
			var searchIn = (getIDT(VariableNode.getExpression().get(), locals));//gets the index well be looking for in the hashmap
			if (locals.containsKey(arrayName)) {
				InterpreterArrayDataType actualArray = (InterpreterArrayDataType) locals.get(arrayName);//gets you the hashmap representing the array
				//if the index is not null look for it in the hashmap and remove the value at it
				if(!searchIn.equals(null)) {//if the indices is set, delete them from the array
					if(actualArray.iadtHashmap.containsKey(searchIn.getStrings())) {
						actualArray.iadtHashmap.remove(searchIn.getStrings());	//delete the index value from the array
					}
					else {
						throw new Exception("this index does not exist");
					}
				}
				else {
					actualArray.iadtHashmap.clear();//clear the entire array
				}
			}
			//if the array is in the global hash map
			if(globalVariables.containsKey(arrayName)) {
				InterpreterArrayDataType actualArray = (InterpreterArrayDataType) globalVariables.get(arrayName);//gets you the hashmap representing the array
				if(!searchIn.equals(null)) {
					if(actualArray.iadtHashmap.containsKey(searchIn.getStrings())) {
						actualArray.iadtHashmap.remove(searchIn.getStrings());	
					}
					else {
						throw new Exception("this index does not exist");
					}
				}
				else {
					actualArray.iadtHashmap.clear();//clear the entire array
				}
		     }
			return new ReturnType(ReturnType.ReturnTypes.NORMAL);
		}
		
		if(statement instanceof  doWhileNode) {
			var doWhileNode  = ((doWhileNode)statement);
			LinkedList<StatementNode> blockStatment= doWhileNode.getBlock().getStatementNode();//get the blocks of statments
			do {
				var returnedNode = InterpretListOfStatements(blockStatment,locals); //evaluate the block
				if(returnedNode.getReturnTypes().equals(ReturnType.ReturnTypes.BREAK)) {
					break;
				}
				if(returnedNode.getReturnTypes().equals(ReturnType.ReturnTypes.RETURN)) {
					return ProcessStatement(locals,statement);//call process statement again
				}
			}while(getIDT(doWhileNode.getCondition().get(),locals).getStrings().equals("1"));//while the getIDT on the condition is true keep looping through
		}
		
		if(statement instanceof  forNode) {
			var forNode = ((forNode)statement);
			LinkedList<StatementNode> statements = forNode.getBlock().getStatementNode();//get linked list with block
			
			if(!forNode.getInitialize().equals(Optional.empty())) {//while there is a initial value
				ProcessStatement(locals,statement);//pocess the initializer
				while(getIDT(forNode.getConditiona1().get(),locals).getStrings().equals("1")) {
					var returnline =InterpretListOfStatements(statements,locals);//go through block of statements
					if(returnline.getReturnTypes().equals(ReturnType.ReturnTypes.BREAK)) {
						break;
					}
					if(returnline.getReturnTypes().equals(ReturnType.ReturnTypes.RETURN)) {
						return ProcessStatement(locals,statement);//which one to call  it fir increment
					}
					ProcessStatement(locals,statement);//call process statement on increment
				}
			}
		}
		
		//Find the array, loop over every key in the array’s hashMap. 
		//Set the variable to the key, then call InterpretListOfStatements on the forEach’s statements.
		//do different operations based on what is returned from InterpretListOfStatements
		if(statement instanceof  forEachNode) {
			var forEachNode = ((forEachNode)statement);
			LinkedList<StatementNode> statements = forEachNode.getBlock().getStatementNode();
			var name = "";
			var condition = forEachNode.getCondition();
			if(!condition.equals(Optional.empty())) {
				var arrayname = ((OperationNode)condition.get()).getRight().get();//get the variable reference node with then name of the array from the operation node

				 if(arrayname instanceof VariableReferenceNode) {
					  name =((VariableReferenceNode) arrayname).getVariableName();//get the array name
				 }
				 if(locals.containsKey(name)) {//if the local hash map contains the array
					 InterpreterArrayDataType getArray = (InterpreterArrayDataType)locals.get(name);
					 for(String loop : getArray.iadtHashmap.keySet()) {
						 locals.put(name, new InterpreterDataType(loop));
						 var returnedNode = InterpretListOfStatements(statements,locals); 
							if(returnedNode.getReturnTypes().equals(ReturnType.ReturnTypes.BREAK)) {
								break;
							}
							if(returnedNode.getReturnTypes().equals(ReturnType.ReturnTypes.RETURN)) {
								return ProcessStatement(locals,statement);//call process statement again
							}
					 }
					 return new ReturnType(ReturnType.ReturnTypes.NORMAL);
				 }
				 if(globalVariables.containsKey(name)) {//if the globals hasmap contains the array
					 InterpreterArrayDataType getArray =(InterpreterArrayDataType) globalVariables.get(name);
					 for(String loop : getArray.iadtHashmap.keySet()) {
						 globalVariables.put(name, new InterpreterDataType(loop));
						 var returnedNode = InterpretListOfStatements(statements,locals); 
							if(returnedNode.getReturnTypes().equals(ReturnType.ReturnTypes.BREAK)) {
								break;
							}
							if(returnedNode.getReturnTypes().equals(ReturnType.ReturnTypes.RETURN)) {
								return ProcessStatement(locals,statement);//call process statement again
							}
					 }
				 }
				 return new ReturnType(ReturnType.ReturnTypes.NORMAL);//what do we return here 
			}else {
				throw new Exception("array does not exist");
			}
			
		}
		if(statement instanceof  ifNode) {
			var ifNode = ((ifNode)statement);
			var ifNodeBool = getIDT(ifNode.getCondition().get(),locals);
			ReturnType returnedNode = null;
			if(ifNodeBool.getStrings().equals("1")) {//if the condition for the first if node is true
				 returnedNode = InterpretListOfStatements(ifNode.getBlock().getStatementNode(),locals);//evaluate the block of code
				if(!returnedNode.getReturnTypes().equals(ReturnType.ReturnTypes.NORMAL)) {
					return returnedNode;//if it break or something send it to the parent
				}
				while(!ifNode.getNext().equals(Optional.empty())) {//if there is another if/else
					if(!ifNode.getNext().get().getCondition().equals(Optional.empty())) {//check if there is not another condtion
						if(getIDT(ifNode.getNext().get().getCondition().get(),locals).getStrings().equals("1")){
							var innerBlock = InterpretListOfStatements(ifNode.getNext().get().getBlock().getStatementNode(),locals);
							if(!innerBlock.getReturnTypes().equals(ReturnType.ReturnTypes.NORMAL)) {
								return innerBlock;
							}
						}
					}else {//if there is no condition,its  the else so just evaluate the block
						var innerBlock = InterpretListOfStatements(ifNode.getNext().get().getBlock().getStatementNode(),locals);
						if(!innerBlock.getReturnTypes().equals(ReturnType.ReturnTypes.NORMAL)) {
							return innerBlock;
						}
					}
				}
			}	else return returnedNode;
		}
		//if the node is an instance of return node, get the value that it is returning and return a return type along with the value if it is present
		if(statement instanceof  returnNode) {
			Optional<String> retVal =null;
			var optionRet = ((returnNode)statement).getReturning();
			if(optionRet.isPresent()) {
				 retVal = Optional.of(getIDT(optionRet.get(),locals).toString());
				 return new ReturnType(ReturnType.ReturnTypes.RETURN,retVal);	
			}
		}
      //if the node is an instance of while node, while its condition is true, evaluate the blockcode and based of of what is returned carry put different actions
		if(statement instanceof  whileNode) {	
			whileNode whileNode  = ((whileNode)statement); 
			LinkedList<StatementNode> blockStatment= whileNode.getBlock().getStatementNode();
			while(getIDT(whileNode.getCondition().get(),locals).getStrings().equals("1")){//while the condition is true 
				var returnedNode = InterpretListOfStatements(blockStatment,locals);
				if(returnedNode.getReturnTypes().equals(ReturnType.ReturnTypes.BREAK)) {
					break;
				}
				if(returnedNode.getReturnTypes().equals(ReturnType.ReturnTypes.RETURN)) {
					return ProcessStatement(locals,statement);//call process statement again
				}		
			}
			return new ReturnType(ReturnType.ReturnTypes.NORMAL);
		}
		else {
			Optional<String> returned = Optional.of(getIDT(statement,locals).getStrings());//call getidt and see if it returns a valid value,if not throw an exception,if it does 
			//System.out.println("in process statement for print ");
			if (returned.equals(null)) {
				throw new Exception("invalid assignment or function found");
			}else{
				//System.out.println("in process statement for print " + returned.get().toString());
				return new ReturnType (ReturnType.ReturnTypes.NORMAL,returned);
			}
		}
	}
return null;
}
// Maps parameters positionally from a function call to its definition, validates parameter counts, and either executes a built-in function or interprets the list of statements if it's not built-in.
	//Throws exceptions for errors.
public String RunFunctionCall(FunctionCallNode func,HashMap<String, InterpreterDataType> locals) throws Exception {
		if(functionSource.containsKey(func.name)) {
			var function = functionSource.get(func.name);
				
		    
			if(function instanceof BuiltInFunctionNode) {
				var BinFunc= (BuiltInFunctionNode)function;//get the bif and working w this
				
				if(!BinFunc.getVariadic()) {//if the function is not variadic it has a defined number of params 
					if (BinFunc.getFunctionName().equals("TOUPPER") ||BinFunc.getFunctionName().equals("TOLOWER")|| BinFunc.getFunctionName().equals("LENGTH")  ){	
						if(func.getParameter().size()== 1) {//were comparing what they sent into the function call node to the parameters each function should have in the built in
							locals = new HashMap<String, InterpreterDataType>();
							int size = function.getParameters().size();
							int i=0;
							while(i<size) {
							  locals.put(Integer.toString(i), getIDT(func.getParameter().get(i),new HashMap<String,InterpreterDataType>()));
							  i++;
							 }
						   }else {
							   throw new Exception("you have entered the wrong amount of parameters");
						   }
					}
					if (BinFunc.getFunctionName().equals("SPLIT") ||BinFunc.getFunctionName().equals("SUBSTR")){	
						if(func.getParameter().size()== 2 || func.getParameter().size()== 3) {//were comparing what they sent into the function call node to the parameters each function should have in the built in
							locals = new HashMap<String, InterpreterDataType>();
							int size = function.getParameters().size();
							int i=0;
							while(i<size) {
							  locals.put(Integer.toString(i), getIDT(func.getParameter().get(i),new HashMap<String,InterpreterDataType>()));
							  i++;
							 }
						   }else {
							   throw new Exception("you have entered the wrong amount of parameters");
						   }
					}
					if (BinFunc.getFunctionName().equals("SUB") ||BinFunc.getFunctionName().equals("GSUB")){	
						if(func.getParameter().size()== 3) {//were comparing what they sent into the function call node to the parameters each function should have in the built in
							locals = new HashMap<String, InterpreterDataType>();
							int size = function.getParameters().size();
							int i=0;
							while(i<size) {
							  locals.put(Integer.toString(i), getIDT(func.getParameter().get(i),new HashMap<String,InterpreterDataType>()));
							  i++;
							 }
						   }else {
							   throw new Exception("you have entered the wrong amount of parameters");
						   }
					}
					if (BinFunc.getFunctionName().equals("INDEX") ||BinFunc.getFunctionName().equals("MAX")){	
						if(func.getParameter().size()== 2) {//were comparing what they sent into the function call node to the parameters each function should have in the built in
							locals = new HashMap<String, InterpreterDataType>();
							int size = function.getParameters().size();
							int i=0;
							while(i<size) {
							  locals.put(Integer.toString(i), getIDT(func.getParameter().get(i),new HashMap<String,InterpreterDataType>()));
							  i++;
							 }
						   }else {
							   throw new Exception("you have entered the wrong amount of parameters");
						   }
					}
				  }
					else {
						if(BinFunc.getFunctionName().equals("PRINT")) {
							//need to do the whole array thing 
							//for all but the last variable
							//for the last variable we need to make an array and put the remaining values from the function call node in it 
							locals = new HashMap<String, InterpreterDataType>();
							 locals.put("0", new InterpreterArrayDataType(new HashMap()));
							 
							 InterpreterArrayDataType array = (InterpreterArrayDataType) locals.get("0");
							int size = func.getParameter().size();
							int i=0;
							while(i<size) {
								  array.iadtHashmap.put(Integer.toString(i), getIDT(func.getParameter().get(i),new HashMap<String,InterpreterDataType>()));
								  i++;
							  }
						}
					}
					BinFunc.execute.apply(locals);
				}
			//if it is not a built in function it is a user defined function
				else {
					for(int i=0;i< func.getParameter().size();i++) {
						function.getParameters().get(i);
						locals.put(function.getFunctionName(), getIDT(func.getParameter().get(i),locals));
					}
					InterpretListOfStatements(function.getStatementNode(),locals);
				}
			    //  function.execute.apply(locals);
			}
			return "";
		}
	
	//loops over a linked list of statements, calling processStatement() for each one.
	//check the return type from each processStatement, if it is not normal, return passing “up” the same ReturnType.
	public ReturnType InterpretListOfStatements(LinkedList<StatementNode> statements, HashMap<String, InterpreterDataType> locals) throws Exception {
		ReturnType retType = null;
		for(int i =0;i<statements.size();i++) {
			 retType= ProcessStatement(locals,statements.get(i));
			 if(!retType.getReturnTypes().equals(ReturnType.ReturnTypes.NORMAL)) {
				 return retType;
			 }
		}	
		return new ReturnType (ReturnType.ReturnTypes.NORMAL);
	}
	//Executes "BEGIN" blocks, processes non-BEGIN/END blocks using InterpretBlock for each record, and executes "END" blocks.
	public void InterpretProgram(ProgramNode proNode) throws Exception {
		var begin = proNode.getBegin();
		var end = proNode.getEnd();
		var other = proNode.getOther();
		int count=0;  
		//runs if it is a begin b;lock
		while(!begin.isEmpty()&& count<begin.size()){
			InterpretBlock(begin.get(count));
			count++;
		}
		count=0;
		
		//runs if it is a other b;lock
		while(lManager.SplitAndAssign() && !other.isEmpty()) {
					InterpretBlock(other.get(count));
		}
		count=0;
		//runs if it is a begin b;lock
		while(!end.isEmpty()&& count<end.size()){
			InterpretBlock(end.get(count));
			count++;
		}
	}	
	//Tests and evaluates the condition (if present), then processes each statement
	//using ProcessStatement if the condition is true or there is no condition.
    public void  InterpretBlock(BlockNode bn) throws Exception {
        HashMap<String, InterpreterDataType> newHash = new HashMap<String,InterpreterDataType>();
    	if(bn.getCondition().isPresent()) {    		
    		String bool= getIDT(bn.getCondition().get(), new HashMap<>()).getStrings();
    		if(bool.equals("1")||bool.equals("true")) { 			
    			for(int i=0;i < bn.getStatementNode().size()-1;i++ ) {
        			ProcessStatement(newHash,bn.getStatementNode().get(i));
        		}		
    		}
    	}else {
    		for(int i=0;i < bn.getStatementNode().size();i++ ) {
    			ProcessStatement(newHash,bn.getStatementNode().get(i));
    		}
    	}
	}
	

	public class LineManager {
		// should take a List<String> as a parameter to the constructor and store it in
		// a member.
		private List<String> readIN = new ArrayList();
		private int NF;
		private int NR;
		private int FNR;


		public LineManager(List<String> readIN) {

			this.readIN = readIN;// holds the lines of input text
			this.NF = 0;// number of fields in the current, incremented each time a line is split
			this.NR = 0;// current record number, stating the order of lines that have been processed
			this.FNR = 0;//// record number within the current file
		}

		// will get the next line and split it by looking at the global variables to
		// find “FS” – the field separator;
		//FNR =Integer.parseInt(globalVariables.get("FNR").getStrings());
		boolean SplitAndAssign() {
			
			// if we're at the end of the file
			if (readIN.isEmpty()) {
				return false;
			}
		  
			if(FNR>=readIN.size()) {
				return false;
			}
			
			
			String currentLine = readIN.get(FNR);// get the first line of the file
			//String currentLine = readIN.get(FNR);// get the first line of the file
			String separator = globalVariables.get("FS").toString();// get the value of the field separator to use split
			String[] fileFields = currentLine.split(separator);
			
			globalVariables.put("$" + 0, new InterpreterDataType(currentLine.toString()));
			NF=0;
			for(int count=1;count <= fileFields.length;count++){
			//while (count < fileFields.length) {
				InterpreterDataType currentVal = new InterpreterDataType(fileFields[NF]);
				globalVariables.put("$" + count, currentVal); // assigns each field with $name
				NF++;
			  }
			
			NR++;
			FNR++;
			globalVariables.put("FNR", new InterpreterDataType(Integer.toString(FNR)));
			return true;
		}

	}

}
