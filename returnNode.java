import java.util.LinkedList;
import java.util.Optional;

public class returnNode extends StatementNode{
	
	private Optional<Node> returning;
	
	public returnNode(Optional<Node> returning) {
		this.returning = returning;	
	}

	public Optional<Node> getReturning() {
		return returning;
	}

	public void setReturning(Optional<Node> returning) {
		this.returning = returning;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Return " + returning.get().toString();
	}

}
