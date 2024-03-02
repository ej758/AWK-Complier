import java.util.LinkedList;
import java.util.Optional;

public class doWhileNode extends StatementNode{
	
	private BlockNode block ;
	private Optional<Node> condition;
	
	public doWhileNode( BlockNode block,Optional<Node> condition) {
		this.block = block;
		this.condition = condition;
	}

	public BlockNode getBlock() {
		return block;
	}

	public void setBlock(BlockNode block) {
		this.block = block;
	}

	public Optional<Node> getCondition() {
		return condition;
	}

	public void setCondition(Optional<Node> condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "DO "  + block.toString() + " WHILE " + condition.get().toString();
	}

}
