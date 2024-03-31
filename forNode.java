import java.util.Optional;

public class forNode extends StatementNode{
	
	//peek ahead to see if the next token
	private Optional<Node> initialize;
	private Optional<Node> conditiona1;
	private Optional<Node> increment;
	private BlockNode block ;
	
	
	public forNode(Optional<Node> initialize,Optional<Node> conditiona1,Optional<Node> increment, BlockNode block) {
		this.initialize=initialize;
		this.conditiona1=conditiona1;
		this.increment=increment;
		this.block = block;
	}


	public Optional<Node> getInitialize() {
		return initialize;
	}


	public void setInitialize(Optional<Node> initialize) {
		this.initialize = initialize;
	}


	public Optional<Node> getConditiona1() {
		return conditiona1;
	}


	public void setConditiona1(Optional<Node> conditiona1) {
		this.conditiona1 = conditiona1;
	}


	public Optional<Node> getIncrement() {
		return increment;
	}


	public void setIncrement(Optional<Node> increment) {
		this.increment = increment;
	}


	public BlockNode getBlock() {
		return block;
	}


	public void setBlock(BlockNode block) {
		this.block = block;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
					return "FOR " +initialize.get().toString() +" "+ conditiona1.get().toString() + increment.get().toString()+" "+block.toString();
		
	}

}
