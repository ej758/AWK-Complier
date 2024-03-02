import java.util.Optional;

public class InterpreterDataType {
	private String strings;
	
	public InterpreterDataType(){
		strings = "";
	}
	
	public InterpreterDataType(String strings){
		this.strings = strings;
	}
	
	public String getStrings() {
		return strings;
	}

	public void setStrings(String strings) {
		this.strings = strings;
	}
	
	public String toString() {
		return strings;
	}

	

}
