import java.util.Optional;


public class forEachNode extends StatementNode{
		
		private Optional<Node> condition;
		private BlockNode block ;
		
		
		public forEachNode(Optional<Node> condition,BlockNode block) {
			this.condition=condition;
			this.block=block;
			
		}


		public Optional<Node> getCondition() {
			return condition;
		}


		public void setCondition(Optional<Node> condition) {
			this.condition = condition;
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
			return "for in " + condition.get().toString()+ " " + block.toString();
		}


}
