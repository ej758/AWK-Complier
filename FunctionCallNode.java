import java.util.LinkedList;
import java.util.Optional;

public class FunctionCallNode extends StatementNode{
	
	String name;
	private LinkedList<Node> parameter = new LinkedList<>();
	
	
	public FunctionCallNode(String name,LinkedList<Node> parameter) {
		this.name = name;
		this.parameter = parameter;
	}

	public LinkedList<Node> getParameter() {
		return parameter;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name.toString() +" " + parameter.toString();
	}

}
