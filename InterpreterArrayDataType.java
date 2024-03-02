import java.util.HashMap;

//variable storage classes
public class InterpreterArrayDataType extends InterpreterDataType{
	
	HashMap<String, InterpreterDataType > iadtHashmap;
	
	public InterpreterArrayDataType(String strings) {
		super(strings);
	}
	
	public InterpreterArrayDataType(HashMap<String, InterpreterDataType > globalVariables) {
		this.iadtHashmap = new HashMap <String, InterpreterDataType>();
		this.iadtHashmap = globalVariables;
	}

	public HashMap<String, InterpreterDataType> getGlobalVariables() {
		return iadtHashmap;
	}

	public void setGlobalVariables(HashMap<String, InterpreterDataType> globalVariables) {
		globalVariables = new HashMap<>();
		this.iadtHashmap = globalVariables;
	}

	public String toString() {
		return iadtHashmap.toString();
	}

}
