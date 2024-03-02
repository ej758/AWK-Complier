import java.util.LinkedList;
import java.util.Optional;

public class ifNode extends StatementNode{
	
	private BlockNode block ;
	private Optional<Node> Condition;//operation node
	private Optional<ifNode> next;
	
	
	public ifNode(Optional<Node> Condition,BlockNode block ) {
		this.block = block;
		this.Condition = Condition;
		this.next= Optional.empty();
		}
	
	public ifNode(Optional<Node> Condition,BlockNode block,Optional<ifNode> next  ) {
		this.block = block;
		this.Condition = Condition;
		this.next=next;
		}
	
	
	public BlockNode getBlock() {
		return block;
	}
	
	public void setBlock(BlockNode block) {
		this.block = block;
	}
	
	
	public Optional<Node> getCondition() {
		return Condition;
	}
	public void setCondition(Optional<Node> condition) {
		Condition = condition;
	}
	
	public Optional<ifNode> getNext() {
		return next;
	}

	public void setNext(Optional<ifNode> next) {
		this.next = next;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(next == null) 
		{
			return "IF " + Condition.get().toString() + " " + block.toString();
		}
		if(Condition.isEmpty()) {
			return  " ELSE " + block.toString();
		}
		else return "IF " + Condition.get().toString() + " " + block.toString()+ " "  + next.get().toString();
	}

}
