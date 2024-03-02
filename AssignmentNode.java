import java.util.Optional;

public class AssignmentNode extends StatementNode{
	Node lValue;
	Optional<Node> expr;

	
	public AssignmentNode(Node lValue, Optional<Node> expr ) {
		this.lValue= lValue;
		this.expr = expr;
	}

	public Node getlValue() {
		return lValue;
	}

	public Optional<Node> getExpr() {
		return expr;
	}

	public String toString() {	
		return lValue.toString()+" = "+expr.get().toString();
	}
	
}