import java.util.LinkedList;

/**
 * Holds the function name, the collection of parameter name and a LinkedList of StatementNode
 * @author ejagg
 *
 */
public class FunctionDefinitionNode extends Node{
	
	private String functionName;
	LinkedList <String> parameters;
	LinkedList<StatementNode> statementNode;
	
	public FunctionDefinitionNode(String functionName, LinkedList <String> parameters,LinkedList<StatementNode> statementNode) {
		this.functionName=functionName;
		this.parameters =parameters;
		this.statementNode=statementNode;
	}
	
	
	public String getFunctionName() {
		return functionName;
	}


	public LinkedList<String> getParameters() {
		return parameters;
	}


	public LinkedList<StatementNode> getStatementNode() {
		return statementNode;
	}


	public String toString() {
		return functionName.toString() +" ," + parameters.toString()+" ," + statementNode.toString();
	}

}
