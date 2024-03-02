import java.util.LinkedList;
import java.util.Optional;

/**
 * Stores the values within a block of awk code
 * @author ejagg
 *
 */
public class BlockNode extends Node {

	private LinkedList<StatementNode> statementNode = new LinkedList<>();
	private Optional<Node> Condition;

	public BlockNode(LinkedList<StatementNode> statementNode, Optional<Node> condition) {
		this.statementNode = statementNode;
		Condition = condition;
	}
	
	
	public LinkedList<StatementNode> getStatementNode() {
		return statementNode;
	}

	public Optional<Node> getCondition() {
		return Condition;
	}

	public void setStatementNode(LinkedList<StatementNode> statementNode) {
		this.statementNode = statementNode;
	}

	public void setCondition(Optional<Node> condition) {
		Condition = condition;
	}

	public String toString() {
		if(Condition.isPresent()) {
			return statementNode.toString()+" ," + Condition.get().toString();
		}
		return statementNode.toString();
	}
}
