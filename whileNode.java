import java.util.LinkedList;
import java.util.Optional;

public class whileNode extends StatementNode{
	
	private BlockNode block ;
	private Optional<Node> condition;
	
	public whileNode(Optional<Node> condition, BlockNode block) {
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
		return "WHILE " +  condition.get().toString() + " " + block.toString();
	}


}





