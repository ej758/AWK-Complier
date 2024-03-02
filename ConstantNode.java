/**
 * AST nodes that hold one string value.
 * @author ejagg
 *
 */
public class ConstantNode extends Node {
	
	private String constant;
	
	public ConstantNode(String constant) {
		this.constant = constant;		 
	}
	

	public String getConstant() {
		return constant;
	}

	public String toString() {
		return constant;
	}

}
