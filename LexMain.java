import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Optional;

public class LexMain {
	
	public static void main(String[] args) throws Exception {
		if(args.length !=1) {
			return;
		}
		try {
			
			Path myPath =Paths.get(args[0]);	//creates an object from the first command line argument
			String doc= new String(Files.readAllBytes(myPath));//stores the entire doc into a string
			
			Lexer lexer = new Lexer(doc);
			LinkedList<Token> tokens =lexer.Lex();	
			Parser parser = new Parser(tokens);
			
			ProgramNode pg = parser.Parse();
			Interpreter print2 = new Interpreter(pg,myPath);
			print2.InterpretProgram(pg);
				
		
			}catch (Exception except) {
				System.out.println(except.getMessage());
				System.out.println(except);
			}
	}
	private static String readAWKFILE(String filename) throws IOException {
        Path myPath = Paths.get(filename);
        return new String(Files.readAllBytes(myPath));
    }

}
