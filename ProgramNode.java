import java.util.LinkedList;

/**
 * The top level structure of the AWK program
 * @author Elissa Jagroop
 *
 */
public class ProgramNode extends Node{
	
	private LinkedList<BlockNode> begin =new LinkedList<>() ;
	private LinkedList<BlockNode> end =new LinkedList<>();
	private LinkedList<BlockNode> other =new LinkedList<>();// middle block of code
	
	//only return in user built functions
	private LinkedList<FunctionDefinitionNode> functionDef =new LinkedList<>(); //stores a function definition node which stores 
																				//the functionName, parameters,and statment
																				//in form of FUNCTION name(parameters,parameters)

	
	public LinkedList<BlockNode> getBegin() {
		return begin;
	}



	public void setBegin(BlockNode begin) {
		this.begin.add(begin);
	}



	public LinkedList<BlockNode> getEnd() {
		return end;
	}



	public void setEnd(BlockNode end) {
		this.end.add(end);
	}



	public LinkedList<BlockNode> getOther() {
		return other;
	}



	public void setOther(BlockNode other) {	
		this.other.add(other);
	}



	public LinkedList<FunctionDefinitionNode> getFunctionDef() {
		return functionDef;
	}



	public void setFunctionDef(FunctionDefinitionNode functionDef) {
		this.functionDef.add(functionDef);
	}



	public String toString() {
		String output= "";
		
		if(!begin.isEmpty()) {
			output += " BEGIN " + begin.toString();
		}
		if(!other.isEmpty()) {
			output += " OTHER " + other.toString();
		}
		if(!end.isEmpty()) {
			output += " END " + end.toString();
		}
		if(!functionDef.isEmpty()) {
			output += " FUNCTION " + functionDef.toString();
		}
		return output;
	}
}
