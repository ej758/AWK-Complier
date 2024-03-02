
public class TernaryNode extends Node{
	Node condition;
	Node trueValue;
	Node falseValue;
	
	
	public TernaryNode(Node condition, Node trueValue, Node falseValue) {
		this.falseValue = falseValue;
		this.condition = condition;
		this.trueValue=trueValue;
	}
	
	public String toString() {
		return condition.toString() +" " + trueValue.toString() +" " + falseValue.toString();
	}
}
