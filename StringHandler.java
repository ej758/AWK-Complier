import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StringHandler {
	
	private int index=0;
	private String AWKfile;
	
	
	public StringHandler(String AWKfile){
		this.AWKfile=AWKfile;
	}
	
	/**
	 *Looks “i” characters ahead and returns that character 
	 * @param i
	 * @return
	 */
	public char Peek(int i) {
		char peeked;

		if(index+i < AWKfile.length()) { 
			 peeked = AWKfile.charAt(i+index);
		}else {
			peeked = '0';
		}
		return peeked;	
	}
	/**
	 * returns a string of the next “i” characters 
	 * @param i
	 * @return
	 */
	public String PeekString(int i) {
		String peeked;
		
		if(index+i <= AWKfile.length()) { 
			 peeked = AWKfile.substring(index,index+i);
		}else {
			peeked = "empty";
		}
		return peeked;
		
	}
	/**
	 *returns the next character in the file
	 * @return
	 */
	public char GetChar() {
		char next ;
		
		if(index < AWKfile.length()) { 
			next= AWKfile.charAt(index);
			index++;
		}else {
			next = ' ';
		}
		return next;
		
	}
	/**
	 * moves the index ahead “i” positions
	 * @param i
	 */
	public void Swallow(int i){
		index+=i;
	}
	
	/**
	 * returns true if we are at the end of the document
	 * @return
	 */
	public boolean IsDone() {
		if(index >= AWKfile.length()) {
			return true;
		}else return false;
	}
	/**
	 * returns the rest of the document as a string
	 * @return
	 */
	public String Remainder() {
		String remains = "empty";
		
		if(index<AWKfile.length()) {
			remains = AWKfile.substring(index);
		}
		return remains;
	}
	
	}


