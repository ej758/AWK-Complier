import java.util.LinkedList;
import java.util.Optional;

public class Parser {

	LinkedList<Token> tokens = new LinkedList<>();
	private TokenManager tManager;

	public Parser(LinkedList<Token> tokens) {
		this.tokens = tokens;
		tManager = new TokenManager(tokens);
	}

	/**
	 * Accepts any number of separators and returns true if it finds at least one
	 * @return
	 */
	public boolean AcceptSeperators() {
		boolean separator = false;
		Optional<Token> temp;

		while (tManager.MoreTokens()) {
			temp = tManager.Peek(0);
			if ((temp.get().getToken().equals(Token.TokenType.SEPARATOR)
					|| (temp.get().getToken().equals(Token.TokenType.NEWLINE)))) {
				tManager.MatchAndRemove(temp.get().getToken());
				separator = true;
			} else {
				break;
			}
		}
		return separator;
	}

	public ProgramNode Parse() throws Exception {
		ProgramNode node = new ProgramNode();

		while (tManager.MoreTokens()) {

			//calls parse function ,if its false it calls parse action
			if (ParseFunction(node) || ParseAction(node)) {
			} else {
				throw new Exception("Unknown item found");
			}
		}
		return node;

	}

	/**
	 * Ensures that a valid function call is made and creates a
	 * FunctionDefinitionNode The function node is added to the programs nodes
	 * function list
	 * 
	 * @param programnode
	 * @return
	 */
	public boolean ParseFunction(ProgramNode programnode) {

		String funcName;
		Token.TokenType next;
		LinkedList<String> parameter = new LinkedList<>();
		LinkedList<StatementNode> statement = new LinkedList<>();

		while (tManager.MoreTokens()) {

			if (tManager.MatchAndRemove(Token.TokenType.FUNCTION).isPresent()) { //looks for function keyword
				if (tManager.Peek(0).get().getToken().equals(Token.TokenType.WORD)) {
					funcName = tManager.MatchAndRemove(Token.TokenType.WORD).toString();//stores the function name 
				} else
					return false;

				//begins to look for parameters  by looking for a left brace, the values in between and then a right brace 
				if (tManager.MatchAndRemove(Token.TokenType.LBRACE).isPresent()) { 

					while (tManager.MoreTokens()) {
						if (tManager.Peek(0).get().getToken().equals(Token.TokenType.WORD)) {
							parameter.add(tManager.MatchAndRemove(Token.TokenType.WORD).toString());

							if (!tManager.Peek(0).get().getToken().equals(Token.TokenType.RBRACE)) {
								next = tManager.Peek(0).get().getToken();
								if (next.equals(Token.TokenType.SEPARATOR)) {//allows separators and commas between parameters
									AcceptSeperators();
								}
								if (tManager.Peek(0).get().getToken().equals(Token.TokenType.COMMA)) {
									tManager.MatchAndRemove(Token.TokenType.COMMA);
									if (tManager.Peek(0).get().getToken().equals(Token.TokenType.SEPARATOR)) {
										AcceptSeperators();
									}
								} else {
									return false;
								}
							}
						} else if (tManager.MatchAndRemove(Token.TokenType.RBRACE).isPresent()) {
							break;
						} else {
							return false;
						}
					}

				} else {
					return false;
				}

				FunctionDefinitionNode func = new FunctionDefinitionNode(funcName, parameter, statement);
				programnode.setFunctionDef(func); // adds the created function node to the ProgramNodes function list
				return true;
			}else return false;
		}
		return false;
	}

	/**
	 * Identifies the different action calls being made in a block of code
	 * @param programNode
	 * @return
	 * @throws Exception
	 */
	public boolean ParseAction(ProgramNode programNode) throws Exception {
		Boolean action = false;

		while (tManager.MoreTokens()) {
			if (tManager.MatchAndRemove(Token.TokenType.BEGIN).isPresent()) { 
				programNode.setBegin(ParseBlock());
				action = true;
			} else if (tManager.MatchAndRemove(Token.TokenType.END).isPresent()) {
				programNode.setEnd(ParseBlock());
				action = true;
			} else {
				
				Optional<Node> condition = ParseOperation();//to get the condition
				BlockNode block =  ParseBlock();
			
				block.setCondition(condition);
				//make a block node and set the condition with the condition
				
			//	programNode.setOther(ParseBlock());//get the block
				programNode.setOther(block);
				action = true;
			}
			break;
		}
		return action;
	}
	
	
	
	private ifNode parseIf() throws Exception {

		 BlockNode blockCode ;
		 Optional<BlockNode> block2;
		 Optional<Node> condition;
		
			
			var token = tManager.MatchAndRemove(Token.TokenType.LBRACE);
			if(token.isPresent()) {
				 condition = ParseOperation();//get the condition in between the braces
			}else throw new Exception("need a condtion after an if call");
			
			token = tManager.MatchAndRemove(Token.TokenType.RBRACE);
			if(token.isEmpty()) {
				throw new Exception("need a right closing brace for your if condition");
			}		
				       blockCode = ParseBlock();
		if(tManager.Peek(0).get().getToken().equals(Token.TokenType.ELSE)) {
			tManager.MatchAndRemove(Token.TokenType.ELSE);
			//check if theres an if after and if there is 
			if(tManager.Peek(0).get().getToken().equals(Token.TokenType.IF)){
				tManager.MatchAndRemove(Token.TokenType.IF);
				
				Optional<ifNode> nestedIf = Optional.of(parseIf());
		        
				return new ifNode(condition,blockCode,nestedIf);	
			}

			else {
				//do i call parse block to get block after the else or something else 
				 block2 = Optional.of(ParseBlock());
				 //send it a new oif node with the else conditionwith empty condition and empty ifnode art the end
				 return new ifNode(condition,blockCode,Optional.of(new ifNode(Optional.empty(),block2.get(),Optional.empty())));
			}
		}
	  return new ifNode(condition,blockCode);
	}
	
	private continueNode parseContinue() {
		
		return new continueNode() ;
	}
	
	private breakNode parseBreak() {
		
		return new breakNode();
	}
	
	private StatementNode parseFor() throws Exception {
		
		BlockNode blockCode= null;
	    Optional<Node> initialize;
	    Optional<Node> conditional;
	    Optional<Node> increment;

			var token = tManager.MatchAndRemove(Token.TokenType.LBRACE);
			
			if(token.isPresent()) {
			
				if(tManager.Peek(1).get().getToken().equals(Token.TokenType.IN)){
					 initialize = ParseOperation(); 
					 tManager.MatchAndRemove(Token.TokenType.RBRACE);
					 
					 blockCode = ParseBlock(); 
					 
					 return new forEachNode(initialize,blockCode);
				}
				
			
				initialize = ParseOperation();
				 AcceptSeperators();
				 
				conditional =ParseOperation();
				 AcceptSeperators();
				
				increment =ParseOperation();
				 AcceptSeperators();
				 
				 token = tManager.MatchAndRemove(Token.TokenType.RBRACE);
					if(token.isEmpty()) {
						throw new Exception("need a right closing brace for your condition");
					}
					
					 token= tManager.MatchAndRemove(Token.TokenType.LCURLY);
						if(token.isPresent()){
							//looping through to get the block
							while(!tManager.Peek(0).get().getToken().equals(Token.TokenType.RCURLY)) {
							       blockCode = ParseBlock();
							}
							tManager.MatchAndRemove(Token.TokenType.RCURLY);
							
						}
				
			}else throw new Exception("need a condtion after an for call");
			
		return new forNode(initialize,conditional,increment,blockCode);
	
	}
	
	//might be moving braces too many times
	private whileNode parseWhile() throws Exception {
		//match and remove the braces then store condition using parse op
		//use parse block for block statements
		
		Optional<Node>condition;
		BlockNode blockCode;
		
		var token = tManager.MatchAndRemove(Token.TokenType.LBRACE);
		if(token.isPresent()) {
			condition = ParseOperation();
		} else throw new Exception("need a condtion after a while call");
		token = tManager.MatchAndRemove(Token.TokenType.RBRACE);
		if(token.isEmpty()) {
			throw new Exception("need a right closing brace fir your if condition");
		}						
		blockCode = ParseBlock();
		return new whileNode(condition,blockCode);
	}
	private doWhileNode parseDoWhile() throws Exception {

		Optional<Node>condition;
		BlockNode blockCode;
		
		blockCode = ParseBlock();//get the block code after the do 
	
		var token = tManager.MatchAndRemove(Token.TokenType.WHILE);//there must be while and a condition after a do
		if(token.isPresent()) {
			token = tManager.MatchAndRemove(Token.TokenType.LBRACE);
			if(token.isPresent()) {
				
			condition = ParseOperation();
			}else throw new Exception("need a condition after a while ");
			token = tManager.MatchAndRemove(Token.TokenType.RBRACE);
			if(token.isEmpty()) {
				throw new Exception("need a right closing brace fir your do condition");
			}	
		}else throw new Exception("need a while after a do ");
	
		return new doWhileNode(blockCode,condition);
	}
	
	private deleteNode parseDelete() throws Exception {	
		
		//delete arrayName then condition(array index)
		//delete entire array by just using name
		LinkedList<String> parameter = new LinkedList<>();
		Optional<Node> delete = ParseLValue();	
		
		if(delete.isPresent()) {
			return new deleteNode(delete.get());//gets the index
		}	
		//check peek zero for square braces, and if present loop and add to a linked list of strings and it to a delete node and parse the delete node
		if(tManager.Peek(0).get().getToken().equals(Token.TokenType.LSQUARE)){	
			tManager.MatchAndRemove(Token.TokenType.LSQUARE);
			while(!tManager.Peek(0).get().getToken().equals(Token.TokenType.RSQUARE)) {
				
		       if (tManager.Peek(0).get().getToken().equals(Token.TokenType.WORD)) {
		        	parameter.add(tManager.MatchAndRemove(Token.TokenType.WORD).toString());	

				var next = tManager.Peek(0).get().getToken();
				
			  	if (next.equals(Token.TokenType.SEPARATOR)) {//allows separators and commas between parameters
					AcceptSeperators();
				}
				if (tManager.Peek(0).get().getToken().equals(Token.TokenType.COMMA)) {
					tManager.MatchAndRemove(Token.TokenType.COMMA);
				}
		    }
		
			}
			tManager.MatchAndRemove(Token.TokenType.RSQUARE);
		}
		return new deleteNode(delete.get(),parameter);//gets the index;
	}
	
	private returnNode parseReturn() throws Exception {
	
		Optional <Node> returning = ParseOperation();
		return new returnNode(returning);
	}
	

	private StatementNode ParseStatement() throws Exception {
		
		
		//StatementNode state;
		Optional<Node> returnedNode = null;
		
		if(tManager.MoreTokens()) {
		//going to match each condition and match and remove it and call its method 
		//match and remove call here so you just parse in the functions	
			var token = tManager.MatchAndRemove(Token.TokenType.IF);
			
			if (token.isPresent()) {
				return parseIf();
			}
			
			token =tManager.MatchAndRemove(Token.TokenType.CONTINUE);
			if (token.isPresent()) {
				return parseContinue();
			}
			
			token =tManager.MatchAndRemove(Token.TokenType.BREAK);
			if (token.isPresent()) {
				return parseBreak();
			}
			
			token =tManager.MatchAndRemove(Token.TokenType.FOR);
			if (token.isPresent()) {
				return parseFor();
			}
			token =tManager.MatchAndRemove(Token.TokenType.WHILE);
			if (token.isPresent()) {
				return parseWhile();
			}
			token =tManager.MatchAndRemove(Token.TokenType.DO);
			if (token.isPresent()) {
				return parseDoWhile();
			}
			token =tManager.MatchAndRemove(Token.TokenType.DELETE);
			if (token.isPresent()) {
				return parseDelete();
			}
			token =tManager.MatchAndRemove(Token.TokenType.RETURN);
			if (token.isPresent()) {
				return parseReturn();
			} 
			else {
				returnedNode = ParseOperation();
			
				if(returnedNode.isPresent()) {
				//check what type of node is being returned from parseOperation and cast it as a statement node aand return it 
				if(returnedNode.get() instanceof FunctionCallNode)
				{
					return (StatementNode)returnedNode.get();
				}
				else if(returnedNode.get()instanceof OperationNode)
				{
					return (StatementNode)returnedNode.get();
				}
				else if(returnedNode.get()instanceof AssignmentNode)
				{
					return (StatementNode)returnedNode.get();
				}
				}
			}
			
		}

		 return null;
	}

	private BlockNode ParseBlock() throws Exception {
		
		LinkedList<StatementNode> statement = new LinkedList<>();

		AcceptSeperators(); 
		
		
		//throw exception if the curly brace is not there
		if (tManager.MatchAndRemove(Token.TokenType.LCURLY).isPresent()) {//looks for the beginning of parse block 
			//call parse statement until end brace is found
			//while(tManager.MatchAndRemove(Token.TokenType.RCURLY).isEmpty()) {
				while(tManager.MatchAndRemove(Token.TokenType.RCURLY).isEmpty()) {
				
				AcceptSeperators(); 
				//get the statement node 
				StatementNode state =  ParseStatement();
				//add it to your linked list of statement nodes 
				statement.add(state);			 
		    	}
			}
		    //for single line methods 
			else { 
				AcceptSeperators(); 
				statement.add(ParseStatement());
			}
	
		BlockNode blockNode = new BlockNode(statement, Optional.empty());
		return blockNode;
	}
	
	/**
	 * Calls parseAssignment 
	 * 
	 * @return
	 * @throws Exception
	 */
	public Optional<Node> ParseOperation() throws Exception {
		return ParseAssignment();
	}

	/**
	 * Looks for tokens which Assign different operations to values 
	 * @return Assignment nodes with the values and the operation being done 
	 * @throws Exception
	 */
	private Optional<Node> ParseAssignment() throws Exception {

		Optional<Node> lvalue = ParseTeranaryConditionals();//get conditional value
		
		do {
			if (tManager.MoreTokens()) {
				
				////find the token for the next operation being completed
				//if the token is found, call ParseAssignment to get the right side of the expression
				//return  an assignment  node with the values being operated on 

				var token = tManager.MatchAndRemove(Token.TokenType.EQUAL);
				if (token.isPresent()) {
					
					Optional<Node> expr = ParseAssignment();
					if(expr.isPresent()) {
						return Optional.of(new AssignmentNode(lvalue.get(), expr));
					}else throw new Exception ("need a value after a equal sign");
				}

				token = tManager.MatchAndRemove(Token.TokenType.ASSIGNSUBTRACT);
				if (token.isPresent()) {

					Optional<Node> expr = ParseAssignment();
					if(expr.isPresent()) {
						//returns an assignment node with the operation being done and the values 
						return Optional.of(new AssignmentNode(lvalue.get(),Optional.of(new OperationNode(lvalue.get(), OperationNode.Operation.SUBTRACT, expr))));
					}else throw new Exception ("need a value after a equal sign");
				}

				token = tManager.MatchAndRemove(Token.TokenType.ASSIGNADDITION);
				if (token.isPresent()) {
					Optional<Node> expr = ParseAssignment();
					if(expr.isPresent()) {
						//returns an assignment node with the operation being done and the values 
						return Optional.of(new AssignmentNode(lvalue.get(),Optional.of(new OperationNode(lvalue.get(), OperationNode.Operation.ADD, expr))));
					}else throw new Exception ("need a value after a equal sign");
				}

				token = tManager.MatchAndRemove(Token.TokenType.ASSIGNDIVISION);
				if (token.isPresent()) {

					Optional<Node> expr = ParseAssignment();
					if(expr.isPresent()) {
						//returns an assignment node with the operation being done and the values 
						return Optional.of(new AssignmentNode(lvalue.get(),Optional.of(new OperationNode(lvalue.get(), OperationNode.Operation.DIVIDE, expr))));
					}else throw new Exception ("need a value after a equal sign");
				}

				token = tManager.MatchAndRemove(Token.TokenType.ASSIGNMULTIPLICATION);
				if (token.isPresent()) {
					Optional<Node> expr = ParseAssignment();
					
					if(expr.isPresent()) {
						//returns an assignment node with the operation being done and the values 
						return Optional.of(new AssignmentNode(lvalue.get(),Optional.of(new OperationNode(lvalue.get(), OperationNode.Operation.MULTIPLY, expr))));
					}else throw new Exception ("need a value after a equal sign");
				}

				token = tManager.MatchAndRemove(Token.TokenType.ASSIGNREMAINDER);
				if (token.isPresent()) {
					Optional<Node> expr = ParseAssignment();
					
					if(expr.isPresent()) {
					//returns an assignment node with the operation being done and the values 
					return Optional.of(new AssignmentNode(lvalue.get(),Optional.of(new OperationNode(lvalue.get(), OperationNode.Operation.MODULO, expr))));
					}else throw new Exception ("need a value after a equal sign");
				}

				token = tManager.MatchAndRemove(Token.TokenType.XOR);
				if (token.isPresent()) {

					Optional<Node> expr = ParseAssignment();
					
					if(expr.isPresent()) {
						//returns an assignment node with the operation being done and the values 
						return Optional.of(new AssignmentNode(lvalue.get(),Optional.of(new OperationNode(lvalue.get(), OperationNode.Operation.EXPONENT, expr))));
					}else throw new Exception ("need a value after a equal sign");
				}
				return lvalue;
			}
			return lvalue;
		} while (true);

	}
/**
 * Looks for expressions in the form of a ternary condition
 * @return Ternary node with the condition and the 2 other values 
 * @throws Exception
 */
	private  Optional<Node> ParseTeranaryConditionals() throws Exception {
		Optional<Node> fvalue = ParseOr();//gets  condition
		Optional<Node> lValue;
		
		//if there are more tokens, the question mark token is matched and removed
		//if it is found, ParseTeranaryConditionals is called to get the true value
		//after the true value, a colon must be present 
		//if it is found  ParseTeranaryConditionals is called again to get the false value
		//a ternary node is returned 
		if (tManager.MoreTokens()) {
			var token = tManager.MatchAndRemove(Token.TokenType.QUESTIONMARK); 
			if (token.isPresent()) {
				Optional<Node> midValue = ParseTeranaryConditionals();
				token = tManager.MatchAndRemove(Token.TokenType.COLON);//a colon must be found in order to make the condition valid 
				if (token.isPresent()) {//Ensures that there is a value after the operation, or throws an exception
					lValue = ParseTeranaryConditionals();
				} else {
					throw new Exception("wrong ternary format");
				}

				return Optional.of(new TernaryNode(fvalue.get(), midValue.get(), lValue.get()));
			}
		}
		return fvalue;
	}

	/**
	 * Looks for expressions which Perform OR operations on values 
	 * @return operation node with OR operator and the values 
	 * @throws Exception
	 */
	private Optional<Node> ParseOr() throws Exception {

		Optional<Node> expr1 = ParseAnd();

		do {
			var token = tManager.MatchAndRemove(Token.TokenType.OR);
			if (token.isPresent()) {
				Optional<Node> expr2 = ParseAnd();
				expr1 = Optional.of(new OperationNode(expr1.get(), OperationNode.Operation.OR, expr2));
			} else {
				return expr1;
			}

		} while (true);
	}

	/**
	 * Looks for expressions which Perform AND operations on values 
	 * @return operation node with AND operator and the values 
	 * @throws Exception
	 */
	private Optional<Node> ParseAnd() throws Exception {
		Optional<Node> expr1 = parseInArray();
		if (tManager.MoreTokens()) {
			do {
				var token = tManager.MatchAndRemove(Token.TokenType.AND);
				if (token.isPresent()) {
					Optional<Node> expr2 = Expression();
					if(expr2.isPresent()) {
						expr1 = Optional.of(new OperationNode(expr1.get(), OperationNode.Operation.AND, expr2));
					}else throw new Exception("need a value after &&");
				} else {
					return expr1;
				}

			} while (true);
		} else
			return expr1;
	}
/**
 * Looks for expressions which uses the IN keyword which is Used to check if  a specific element is within an array 
 * @return an operation node with the In operation, the array name and the element being searched for 
 * @throws Exception
 */
	private Optional<Node> parseInArray() throws Exception {

		Optional<Node> expr1 = parseMatch();//gets the value returned from the level below
		
		if (tManager.MoreTokens()) {
			do {
				var token = tManager.MatchAndRemove(Token.TokenType.IN);
			
				if (token.isPresent()) {
					
					Optional<Node> expr2 = ParseLValue();
					if(expr2.isPresent()) {
						expr1 = Optional.of(new OperationNode(expr1.get(), OperationNode.Operation.IN, expr2));
					}else throw new Exception ("need a value after keywpord IN ");
				} else {return expr1;}
			} while (true);
		} else
			return expr1;
	}

	/**
	 * Looks for the tokens which Perform a matching operation to see if two values are matching or not matching one another
	 * @return operation node with the operation being done and the two values being compared
	 * @throws Exception
	 */
			
	private Optional<Node> parseMatch() throws Exception {
		

		Optional<Node> value = comparisons();

		if (tManager.MoreTokens()) {
			var token = tManager.MatchAndRemove(Token.TokenType.TILDE);
			if (token.isPresent()) {
				
				Optional<Node> value2 = Expression();
				
				if(value2.isPresent()) {//Ensures that there is a value after the operation, or throws an exception
					return Optional.of(new OperationNode(value.get(), OperationNode.Operation.MATCH, value2));
				}else throw new Exception("you need a value to compare to");
			}

			token = tManager.MatchAndRemove(Token.TokenType.DOESNOTMATCH);
			if (token.isPresent()) {
				Optional<Node> value2 = Expression();
				
				if ( value2.isPresent()) {//Ensures that there is a value after the operation, or throws an exception
					return Optional.of(new OperationNode(value.get(), OperationNode.Operation.NOTMATCH, value2));
			     }else throw new Exception("you need a value to compare to");
			}
		}
		return value;
	}
/**
 * Looks for the tokens which perform comparisons on two values 
 * @return an operation node with the comparison being done
 * @throws Exception
 */
	private Optional<Node> comparisons() throws Exception {

		Optional<Node> value = StringConcatenation();//get left side of expression

		
		////find the token for  the next operation being completed
		//if the token is found, call StringConcatenation to get the right side of the expression
		//return  an operation node with the values being compared  and the operator		 
		if (tManager.MoreTokens()) {
			var token = tManager.MatchAndRemove(Token.TokenType.LESSTHAN);
			if (token.isPresent()) {
				
				Optional<Node> value2 = StringConcatenation();
				 
				if(value2.isPresent()) {
					return Optional.of(new OperationNode(value.get(), OperationNode.Operation.LESST, value2));
				}else throw new Exception("Must have a value to compare to ");
			}
			token = tManager.MatchAndRemove(Token.TokenType.GREATERTHAN);
			if (token.isPresent()) {
				Optional<Node> value2 = StringConcatenation();
				 
				if(value2.isPresent()) {
					return Optional.of(new OperationNode(value.get(), OperationNode.Operation.GREATERT, value2));
				}else throw new Exception("Must have a value to compare to ");
			}
			token = tManager.MatchAndRemove(Token.TokenType.LESSEQUAL);
			if (token.isPresent()) {
				Optional<Node> value2 = StringConcatenation();
				 
				if(value2.isPresent()) {
					return Optional.of(new OperationNode(value.get(), OperationNode.Operation.LESSOREQ, value2));
				}else throw new Exception("Must have a value to compare to ");
			}
			token = tManager.MatchAndRemove(Token.TokenType.GREATEQUAL);
			if (token.isPresent()) {
				Optional<Node> value2 = StringConcatenation();
				 
				if(value2.isPresent()) {
					return Optional.of(new OperationNode(value.get(), OperationNode.Operation.GREATEROREQ, value2));
				}else throw new Exception("Must have a value to compare to ");
			}
			token = tManager.MatchAndRemove(Token.TokenType.INEQUALITY);
			if (token.isPresent()) {
				Optional<Node> value2 = StringConcatenation();
				 
				if(value2.isPresent()) {
					return Optional.of(new OperationNode(value.get(), OperationNode.Operation.NOTEQ, value2));
				}else throw new Exception("Must have a value to compare to ");
			}
			token = tManager.MatchAndRemove(Token.TokenType.EQUALITY);
			if (token.isPresent()) {
				Optional<Node> value2 = StringConcatenation();
				 
				if(value2.isPresent()) {
					return Optional.of(new OperationNode(value.get(), OperationNode.Operation.EQUAL, value2));
				}else throw new Exception("Must have a value to compare to ");
			}
		}
		return value;
	}
/**
 * Performs string concatenation on values in the correct format
 * @return an operation node with the concatenation operation and the values which were concatenated
 * @throws Exception
 */
	private Optional<Node> StringConcatenation() throws Exception {
		Optional<Node> value = Expression();//get first value from expression method
		//if a value is present,  expression is called to get your second value
		//the first value is assigned to a operation node with the values already found
		if (value.isPresent()) {
			do {
				Optional<Node> value2 = Expression();
				if (value2.isPresent()) {
					value = Optional.of(new OperationNode(value.get(), OperationNode.Operation.CONCATENATION, value2));
				} else return value;

			} while (true);
		} else return value;
	}
	
	
	
	/**
	 * Performs addition and subtraction on values
	 * @return operation node with the add or subtract token and the values which were used 
	 * @throws Exception
	 */
		private Optional<Node> Factor() throws Exception {	
		 return ParseExponentiation();	
		}	
	
	
/**
 * Performs addition and subtraction on values
 * @return operation node with the add or subtract token and the values which were used 
 * @throws Exception
 */
	private Optional<Node> Expression() throws Exception {
		
		//find the token for the next operation being completed
		//if the token is found,  ParseOperation is called to get the right side of the expression
		// the first expression is assigned to  an operation node with the values found and the operator
		//values are continuously added  until no more tokens are found , the node is then returned 

		Optional<Node> value = Term();

		do {
			if (tManager.MoreTokens()) {

				var token = tManager.MatchAndRemove(Token.TokenType.PLUS);
				if (token.isPresent()) {
					Optional<Node> value2 = ParseOperation();
					if (value2.isPresent()) {

						value = Optional.of(new OperationNode(value.get(), OperationNode.Operation.ADD, value2));

					} else throw new Exception("need a value after a plus sign");
				}

				token = tManager.MatchAndRemove(Token.TokenType.MINUS);
				if (token.isPresent()) {
					Optional<Node> value2 = ParseOperation();

					if (value2.isPresent()) {
						value = Optional.of(new OperationNode(value.get(), OperationNode.Operation.SUBTRACT, value2));
					} else
						throw new Exception("need a value after a minus sign");
				} else
					return value;
			} else
				return value;
		} while (true);

	}

	/**
	 * Performs multiplication, division and modulus on values
	 * @return an operation node with t he operation done and the values which were used 
	 * @throws Exception
	 */
	private Optional<Node> Term() throws Exception {

		Optional<Node> expr = ParseExponentiation(); 

		do {
			if (tManager.MoreTokens()) {

				//find the token for the next operation being completed
				//if the token is found,  ParseBottomLevel is called  to get the right side of the expression
				// the first expression is assigned  to  an operation node with the values found and the operator
				//values are continuously added  until no more tokens are found , the node is then returned 
				
				var token = tManager.MatchAndRemove(Token.TokenType.ASTERIX);
				if (token.isPresent()) {
					Optional<Node> expr2 = ParseBottomLevel();
					if (expr2.isPresent()) {
						expr = Optional.of(new OperationNode(expr.get(), OperationNode.Operation.MULTIPLY, expr2)); 
					} else
						throw new Exception("need a value to multiply by");

				}

				else if (tManager.Peek(0).get().getToken().equals(Token.TokenType.SLASH)) {
					token = tManager.MatchAndRemove(Token.TokenType.SLASH);
					Optional<Node> expr2 = ParseBottomLevel();

					if (expr2.isPresent()) {
						expr = Optional.of(new OperationNode(expr.get(), OperationNode.Operation.DIVIDE, expr2));
					} else
						throw new Exception("need a value to divide by ");
				}

				else if (tManager.Peek(0).get().getToken().equals(Token.TokenType.MODULUS)) {
					token = tManager.MatchAndRemove(Token.TokenType.MODULUS);

					Optional<Node> expr2 = ParseBottomLevel();

					if (expr2.isPresent()) {
						expr = Optional.of(new OperationNode(expr.get(), OperationNode.Operation.MODULO, expr2));
					} else
						throw new Exception("need a value to perform modulus with");
				} else
					return expr;//if none of the conditions are true return expr 
			} else
				return expr;
		} while (true);

	}

/**
 * Looks for caret tokens in order to perform exponentiation
 * @return an operation node with the values being exponentiated 
 * @throws Exception
 */
	private Optional<Node> ParseExponentiation() throws Exception {
		Optional<Node> expr = PostLevels();
		
		if (tManager.MoreTokens()) {
			do {
				var token = tManager.MatchAndRemove(Token.TokenType.CARET);
				if (token.isPresent()) {
					
					Optional<Node> expr2 = ParseExponentiation(); //Recursively calls parse exponentiation as this is a right associative method
					if (expr2.isPresent()) {
						
						expr = Optional.of(new OperationNode(expr.get(), OperationNode.Operation.EXPONENT, expr2));
					
					} else throw new Exception("value needed after caret");
				} else return expr;
			} while (true);
		} else {return expr;}
	}

/**
 *Stores values which have post increment and post decrement 
 * @return an operation node with the values being incremented or decremented  
 * @throws Exception
 */
	private Optional<Node> PostLevels() throws Exception {

		Optional<Node> lvalue = ParseBottomLevel();

		//if an increment or decrement token is found after a value , it is assigned to an operation node with post increment or decrement 
		
		var token = tManager.MatchAndRemove(Token.TokenType.INCREMENT);

		if (token.isPresent()) {
			return Optional.of(new OperationNode(lvalue.get(), OperationNode.Operation.POSTINC));
		}

		token = tManager.MatchAndRemove(Token.TokenType.DECREMENT);
		if (token.isPresent()) {
			return Optional.of(new OperationNode(lvalue.get(), OperationNode.Operation.POSTDEC));
		}
		return lvalue;
	}

	/**
	 * Finds and stores bottom level constants
	 * @return constant nodes with the values 
	 * @throws Exception
	 */
	private Optional<Node> ParseBottomLevel() throws Exception {

		var token = tManager.MatchAndRemove(Token.TokenType.STRINGLITERAL);
		// if a string literal token is found, return a Constant node with the token
		// value
		if (token.isPresent()) {
			return Optional.of(new ConstantNode(token.get().getValue()));
		}

		// if a Number token is found, return a Constant node with the token value
		token = tManager.MatchAndRemove(Token.TokenType.NUMBER);
		if (token.isPresent()) {

			return Optional.of(new ConstantNode(token.get().getValue()));
		}

		// if a Pattern token is found, return a Pattern node with the token value
		token = tManager.MatchAndRemove(Token.TokenType.HANDLEPATTERN);
		if (token.isPresent()) {
			return Optional.of(new PatternNode(token.get().getValue()));
		}
		// If a Left brace token is found, call Parse operation to process the values
		// within the bracket.
		// Return the result if the closing brace is found.
		token = tManager.MatchAndRemove(Token.TokenType.LBRACE);
		if (token.isPresent()) {
			Optional<Node> result = ParseOperation();
			if (tManager.MatchAndRemove(Token.TokenType.RBRACE).isPresent()) {
				return result;
			} else {
				throw new Exception("Missing a closing brace");
			}

		}

		// If an Exclamation token is found, call Parse operation to process the values
		// after.
		// Return an operation node with the parse operation and the Operation enum NOT.
		token = tManager.MatchAndRemove(Token.TokenType.EXCLAIMATION);
		if (token.isPresent()) {
			Node result = ParseOperation().get();
			return Optional.of(new OperationNode(result, OperationNode.Operation.NOT));
		}
		// If a minus token is found, call Parse operation to process the values after.
		// Return an operation node with the parse operation and the Operation enum
		// UNARYNEG.
		token = tManager.MatchAndRemove(Token.TokenType.MINUS);
		if (token.isPresent()) {
			Node result = ParseOperation().get();
			return Optional.of(new OperationNode(result, OperationNode.Operation.UNARYNEG));
		}

		// If a Plus token is found, call Parse operation to process the values after.
		// Return an operation node with the parse operation and the Operation enum
		// UNARYPOS.
		token = tManager.MatchAndRemove(Token.TokenType.PLUS);
		if (token.isPresent()) {
			Node result = ParseOperation().get();
			return Optional.of(new OperationNode(result, OperationNode.Operation.UNARYPOS));
		}

		// If an INCREMENT token is found, call Parse operation to process the values
		// after.
		// Return an operation node with the parse operation and the Operation enum
		// PREINC.
		token = tManager.MatchAndRemove(Token.TokenType.INCREMENT);
		if (token.isPresent()) {
			Optional<Node> optionalResult = ParseOperation();
			Node result = optionalResult.get();
			return Optional.of(new OperationNode(result, OperationNode.Operation.PREINC));
		}

		// If an DECREMENT token is found, call Parse operation to process the values
		// after.
		// Return an operation node with the parse operation and the Operation enum
		// PREDEC.
		token = tManager.MatchAndRemove(Token.TokenType.DECREMENT);
		if (token.isPresent()) {
			Node result = ParseOperation().get();
			return Optional.of(new OperationNode(result, OperationNode.Operation.PREDEC));
		} 	
		
		
		//looks for the word and peek to see if theirs a brace after making it a function call 
	    if(tManager.MoreTokens() && tManager.Peek(0).get().getToken().equals(Token.TokenType.WORD)) {
	    		if(tManager.Peek(1).get().getToken().equals(Token.TokenType.LBRACE)){
		//	if parse function call returns a value return parseFunctionCall
			 Optional<Node> func =ParseFunctionCall();
		//	 System.out.println(func + " in bL");
			 if(func.isPresent()){
				 //System.out.println("here");
				 return func;
			 }
	       }
		}
	   
		 if(tManager.MoreTokens()&&tManager.Peek(0).get().getToken().equals(Token.TokenType.GETLINE)){
			
			 Optional<Node> func =ParseFunctionCall();
			 
			 
			 if(func.isPresent()){
				 return func;
			 }
			
		}
		 
		 //prints simple output
		 if(tManager.MoreTokens()&&tManager.Peek(0).get().getToken().equals(Token.TokenType.PRINT)){
				
			
				 Optional<Node> func =ParseFunctionCall();
				 
				 if(func.isPresent()){
					 return func;
				 }
		 }
		 //prints formatted output using format specifiers
		 if(tManager.MoreTokens()&&tManager.Peek(0).get().getToken().equals(Token.TokenType.PRINTF)){
			 
			
				 Optional<Node> func =ParseFunctionCall();
				 
				 if(func.isPresent()){
					 return func;
				 }
		 }
		 if(tManager.MoreTokens()&&tManager.Peek(0).get().getToken().equals(Token.TokenType.EXIT)){
				//call parsefunctioncall
				 Optional<Node> func =ParseFunctionCall();
				 
				 if(func.isPresent()){
					 //System.out.println("here");
					 return func;
				 }
		 }
		 if(tManager.MoreTokens()&&tManager.Peek(0).get().getToken().equals(Token.TokenType.NEXTFILE)){
				 Optional<Node> func =ParseFunctionCall();
				 
				 if(func.isPresent()){
					 return func;}
		 }
		 if(tManager.MoreTokens()&&tManager.Peek(0).get().getToken().equals(Token.TokenType.NEXT)){
				 Optional<Node> func =ParseFunctionCall();
				 if(func.isPresent()){
					 return func;}
		}	
			   return ParseLValue();
}

	private Optional<Node> ParseLValue() throws Exception {

		String name;// STORES THE NAME WHICH WILL BE USED IN THE VARIABLE REF NODE

		// If an DOLLARSIGN token is found, call Parse Bottom level.
		// Return an operation node with the parse bottom level value and the Operation
		// Enumeration DOLLAR.
		var token = tManager.MatchAndRemove(Token.TokenType.DOLLARSIGN);
		if (token.isPresent()) {
			Node left = ParseBottomLevel().get();
			return Optional.of(new OperationNode(left, OperationNode.Operation.DOLLAR));
		}

		// If a WORD token is found, store it.If there is a left Square brace after the
		// word, call parse operation.
		// Return an Variable reference node with the Word value and the value received
		// from parse operation.
		// If there is no left token,Return an Variable reference node with the Word
		// value.
		if (tManager.MoreTokens() && tManager.Peek(0).get().getToken().equals(Token.TokenType.WORD)) {

			name = tManager.MatchAndRemove(Token.TokenType.WORD).toString();

			if (tManager.MoreTokens() && tManager.MatchAndRemove(Token.TokenType.LSQUARE).isPresent()) {
				Optional<Node> expression = ParseOperation();
				if (tManager.MoreTokens() && tManager.MatchAndRemove(Token.TokenType.RSQUARE).isPresent()) {
					return Optional.of(new VariableReferenceNode(name, expression));
				} else {
					throw new Exception("Missing a closing square bracket");
				}
			} else {
				return Optional.of(new VariableReferenceNode(name));
			}
		} else {
			return Optional.empty();
		}
	}
	
	//checked for print,and all the others then if not check for word and brace
	private Optional<Node> ParseFunctionCall() throws Exception {
		String funcName;
		LinkedList<Node> parameter = new LinkedList<>();
		Token.TokenType next;
		FunctionCallNode func = null;
	
		
		if(tManager.Peek(0).isPresent()) {

		//next is next token and linked list thats empty 
		//print next token to string or next 
			//do i need to remove braces for this???
		if(tManager.MatchAndRemove(Token.TokenType.NEXT).isPresent()) {		
			return Optional.of(new FunctionCallNode(Token.TokenType.NEXT.toString() , parameter));//might have to make this null
		}
		
		// causes AWK to immediately stop executing the current rule and to stop processing input
		if(tManager.MatchAndRemove(Token.TokenType.EXIT).isPresent()) {
				 tManager.MatchAndRemove(Token.TokenType.LBRACE);
			
			if (tManager.Peek(0).get().getToken().equals(Token.TokenType.NUMBER)) {	
				parameter.add(ParseOperation().get());
			}
				 tManager.MatchAndRemove(Token.TokenType.RBRACE);
			return Optional.of(new FunctionCallNode(Token.TokenType.EXIT.toString(), parameter));
		}
		
		
		if(tManager.MatchAndRemove(Token.TokenType.NEXTFILE).isPresent()) {		
			return Optional.of(new FunctionCallNode(Token.TokenType.NEXTFILE.toString(), parameter));
		}
		
		
		if(tManager.MatchAndRemove(Token.TokenType.PRINTF).isPresent()) {
				 tManager.MatchAndRemove(Token.TokenType.LBRACE);

			do {
				Node param = ParseOperation().get();
				if(param != null) {
					parameter.add(param);
					AcceptSeperators();
				}
			}while(tManager.MatchAndRemove(Token.TokenType.COMMA).isPresent());
				 tManager.MatchAndRemove(Token.TokenType.RBRACE);
		  return Optional.of(new FunctionCallNode(Token.TokenType.PRINTF.toString(), parameter));
	    }
		
		
		if(tManager.MatchAndRemove(Token.TokenType.PRINT).isPresent()) {
			 tManager.MatchAndRemove(Token.TokenType.LBRACE);
			do {
				Node param = ParseOperation().get();
				if(param != null) {
					parameter.add(param);
					AcceptSeperators();
				}
			}while(tManager.MatchAndRemove(Token.TokenType.COMMA).isPresent());  
			tManager.MatchAndRemove(Token.TokenType.RBRACE);	
		return Optional.of(new FunctionCallNode(Token.TokenType.PRINT.toString(), parameter));
		}
		
		if(tManager.MatchAndRemove(Token.TokenType.GETLINE).isPresent()) {
			 tManager.MatchAndRemove(Token.TokenType.LBRACE);
			do {
				Node param = ParseOperation().get();
				if(param != null) {
					parameter.add(param);
					AcceptSeperators();
					}	
			}while(tManager.MatchAndRemove(Token.TokenType.COMMA).isPresent());
			
			tManager.MatchAndRemove(Token.TokenType.RBRACE);
			return Optional.of(new FunctionCallNode("getline", parameter));
		}
		
		
		if(tManager.Peek(0).get().getToken().equals(Token.TokenType.WORD)&& tManager.Peek(1).get().getToken().equals(Token.TokenType.LBRACE)) {
			
		funcName = tManager.MatchAndRemove(Token.TokenType.WORD).toString();
	 
			if (tManager.MatchAndRemove(Token.TokenType.LBRACE).isPresent()) { 

				while (tManager.MoreTokens()) {
					if (tManager.Peek(0).get().getToken().equals(Token.TokenType.WORD)) {
						parameter.add(ParseOperation().get());

						if(!tManager.Peek(0).get().getToken().equals(Token.TokenType.RBRACE)) {
							next = tManager.Peek(0).get().getToken();
							
							if (next.equals(Token.TokenType.SEPARATOR)) {//allows separators and commas between parameters
								AcceptSeperators();
							}
							if (tManager.Peek(0).get().getToken().equals(Token.TokenType.COMMA)) {
								tManager.MatchAndRemove(Token.TokenType.COMMA);
								

							} else {
								throw new Exception ("function call error");
							}
						}
					} else if (tManager.MatchAndRemove(Token.TokenType.RBRACE).isPresent()) {
						return Optional.ofNullable(new FunctionCallNode(funcName,parameter));
					} else {
						throw new Exception ("function call error");
					}
				}

			} else {
				throw new Exception ("function call error");
			}

			func = new FunctionCallNode(funcName, parameter);
			return Optional.of(func);
	    }else throw new Exception("not a function call");
	}
		return Optional.empty();
	}
}
