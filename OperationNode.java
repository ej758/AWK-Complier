import java.util.Optional;

/**
 * has a Node left, an Optional<Node> right and a list of possible operations 
 * @author ejagg
 */
public class OperationNode extends StatementNode{
	
	Node left;
	Optional<Node>right;
	Operation operationType;
	Node trial;
	
	//stores different operations that could be done
	enum Operation{EQUAL,NOTEQ,LESST,GREATERT,LESSOREQ,GREATEROREQ,AND,OR,NOT,MATCH,
		NOTMATCH,DOLLAR,PREINC,POSTINC,PREDEC,POSTDEC,UNARYPOS,UNARYNEG,IN,EXPONENT,
		ADD,SUBTRACT,MULTIPLY,DIVIDE,MODULO,CONCATENATION}
	
	
	public OperationNode(Node left,Operation operation) {
		this.left=left;
		this.operationType =operation;
		this.right = Optional.empty();
	}
	
	public OperationNode(Node left,Operation operation,Optional<Node>right) {
		this.left=left;
		this.operationType =operation;
		this.right=right;
	}
	
	
	
	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Optional<Node> getRight() {
		return right;
	}

	public void setRight(Optional<Node> right) {
		this.right = right;
	}

	public Operation getOperationType() {
		return operationType;
	}

	public void setOperationType(Operation operationType) {
		this.operationType = operationType;
	}

	public Node getTrial() {
		return trial;
	}

	public void setTrial(Node trial) {
		this.trial = trial;
	}

	public String toString() {
		if(right.isPresent()) {
			return left.toString()+ " ," + operationType.toString()+  " ," + right.get().toString();
		}
		return left.toString()+ " ," + operationType.toString();
		}

}
