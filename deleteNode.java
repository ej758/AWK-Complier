
import java.util.LinkedList;
import java.util.Optional;

public class deleteNode extends StatementNode{
	
	 Node arrayName;
	 Optional<LinkedList<String>> indices;
	
	//functionality: (delete the whole array) 
	//Example: delete a[1,2,3,4]or array reference with a comma separated list of indices.

	public deleteNode( Node arrayName) {
		this.arrayName = arrayName;
	}
	
	public deleteNode( Node arrayName,  LinkedList <String> indices ) {
		this.arrayName = arrayName;
		this.indices = Optional.of(indices);
	}

	@Override
	public String toString() {
		if(indices.isEmpty()) {
			return "delete " + arrayName.toString();
		}
		return "delete " + arrayName.toString() + indices.toString();
	}

}
