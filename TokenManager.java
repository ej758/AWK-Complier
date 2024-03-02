import java.util.LinkedList;
import java.util.Optional;

/**
 * Manages the token stream created by Lexer
 * @author Elissa Jagroop
 *
 */
public class TokenManager {
	
	private LinkedList<Token> tokens =new LinkedList();	
	private int index=0;
	
	public TokenManager(LinkedList <Token>tokenList) {
			tokens = tokenList;
	
	}
	
	/**
	 * peek “j” tokens ahead and return the token if we aren’t past the end of the token list.
	 * @param i
	 * @return
	 */
	public Optional<Token> Peek(int j){
		Token peekToken;
		if(index+j < tokens.size()) { 
			peekToken = tokens.get(j+index);
			return Optional.of(peekToken);
		}else return Optional.empty();
		
	}
	
	/**
	 * checks if there are more tokens present in the token stream
	 * @return
	 */
	public boolean MoreTokens() {
		if(tokens.isEmpty()) {
			return false;
		}else return true;
	}
	/**
	 * Looks at the head of the list. If the token type of the head is the same as what was passed in, remove that 
	 * token from the list and return it
	 * @param t
	 * @return
	 */
	public Optional<Token> MatchAndRemove(Token.TokenType t) {
		Token match;
		//if(tokens.getFirst().getToken().equals(t)) {
			if(MoreTokens() && tokens.getFirst().getToken().equals(t)) {
			match =tokens.getFirst();
			tokens.removeFirst();
			return Optional.of(match);
		}else 
	     return Optional.empty();		
	}
	
	public String toString() {
		return tokens.toString();
	}
	
	

	
}
