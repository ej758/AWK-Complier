import java.util.Optional;

//class which stores what was odne during the execution of a statement.
public class ReturnType {
	
	private ReturnTypes returnTypes;
	private Optional <String> valueToReturn;
	
	enum ReturnTypes{NORMAL,BREAK,CONTINUE,RETURN}
	

	public ReturnType(ReturnTypes returnTypes){	
		this.returnTypes = returnTypes;
	}
	public ReturnType(ReturnTypes returnTypes,Optional <String> returnedV){
		this.returnTypes = returnTypes;
		this.valueToReturn = returnedV;
	}
	
	public ReturnTypes getReturnTypes() {
		return returnTypes;
	}
	public void setReturnTypes(ReturnTypes returnTypes) {
		this.returnTypes = returnTypes;
	}
	public Optional<String> getReturnedV() {
		return valueToReturn;
	}
	public void setReturnedV(Optional<String> returnedV) {
		this.valueToReturn = returnedV;
	}

	public String toString() {
		if(valueToReturn.isPresent()) {
			return returnTypes.toString() + valueToReturn;
		}
		return returnTypes.toString();
	}
}
