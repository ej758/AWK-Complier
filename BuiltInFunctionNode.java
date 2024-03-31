import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

public class BuiltInFunctionNode extends FunctionDefinitionNode{
	
	private Boolean variadic;
	public Function<HashMap<String, InterpreterDataType>, String> execute;
	
	
        //BIFUNCTION make it accept hash map and linked list 
	//this linked list will store the number of params in the builtins

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
