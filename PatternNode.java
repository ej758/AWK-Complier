import java.util.Optional;

/**
 * holds regular expression patterns
 * @author ejagg
 *
 */
public class PatternNode extends Node {
	
	private String pattern ;
	
	public PatternNode(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}

	public String toString() {
		return pattern;
	}

}
