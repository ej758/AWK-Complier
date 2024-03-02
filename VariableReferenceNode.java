import java.util.Optional;
/**
 * Stores the name of the variable, and an optional Node that is the expression for the index. 
 * @author ejagg
 *
 */
public class VariableReferenceNode extends Node {
	
	private String variableName;
	Optional<Node>expression; // index
	
	public VariableReferenceNode(String variableName,Optional<Node>expression) {
		this.variableName=variableName;
		this.expression=expression;
	}
	public VariableReferenceNode(String variableName) {
		this.variableName=variableName;
		this.expression=Optional.empty();
		}
	
	public String getVariableName() {
		return variableName.toString();
	}

	public Optional<Node> getExpression() {
		return expression;
	}
	
	public String toString() {
		if(expression.isPresent()) {
			return variableName +", "+ expression.get().toString();
		}
		return variableName;
	}
	
	
	
}
