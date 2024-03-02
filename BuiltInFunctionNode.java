import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

public class BuiltInFunctionNode extends FunctionDefinitionNode{
	
	private Boolean variadic;
	public Function<HashMap<String, InterpreterDataType>, String> execute;
	
	//can hold an integer with how many parameters are needed
    //can make a over loaded constructor
	
	//get the lambdas to check
    //if the size of the hash map is more 
    //run function call collects what u get from the function call node and compare it to the params
		
	//in user define instead of matching to 0 , match every params to a string  

	//i need a way to store the numbers of parameter needed and it needs to be able to take an optional extra if the function can take one
	
    //BIFUNCTION make it accept hash map and linked list 
	//this linked list will store the number of params in the builtins
	//int minParams, int maxParams
	//public BuiltInFunctionNode(int max,int minString functionName,boolean variadic,Function <HashMap<String, InterpreterDataType >,String> execute) {
	public BuiltInFunctionNode(String functionName,boolean variadic,Function <HashMap<String, InterpreterDataType >,String> execute) {
     	super(functionName, null, null);
     	this.execute = execute;
	    this.variadic = variadic;
	    this.execute = execute;
}
	
	public String execute(HashMap<String, InterpreterDataType> params) {
		return execute.apply(params);
	}
	public void setExecute(Function<HashMap<String, InterpreterDataType>, String> execute) {
		this.execute = execute;
	}

	public Boolean getVariadic() {
		return variadic;
	}

}
