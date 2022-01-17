package edu.ufl.cise.plpfa21.assignment2;

import java.util.ArrayList;
import java.util.List;

import edu.ufl.cise.plpfa21.assignment1.CompilerComponentFactory;
import edu.ufl.cise.plpfa21.assignment1.IPLPLexer;
import edu.ufl.cise.plpfa21.assignment1.IPLPToken;
import edu.ufl.cise.plpfa21.assignment1.LexicalException;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;

public class PLPParser implements IPLPParser {
	
	String program;
	Integer tokenTracker = 0;
	List<IPLPToken> programTokens = new ArrayList<IPLPToken>();
	
	public PLPParser(String program){
		this.program = program;
	}

	@Override
	public void parse() throws Exception {
		
		IPLPToken tokenFetched = null;
		
		try
		{
			IPLPLexer pLPLexer = CompilerComponentFactory.getLexer(program);
			do
			{
				tokenFetched = pLPLexer.nextToken();
				programTokens.add(tokenFetched);
			}while(tokenFetched.getKind()!=Kind.EOF);
		}
		catch(LexicalException lexicalException)
		{
			throw lexicalException;
		}
		
		try
		{
			while(programTokens.get(tokenTracker).getKind()!=Kind.EOF)
			{
				parseDeclaration();
			}
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
	}
	
	private void parseDeclaration() throws Exception
	{
		try
		{
			if(programTokens.get(tokenTracker).getKind()==Kind.KW_FUN)
			{
				parseFunction();
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_VAR)
			{
				parseVar();
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_VAL)
			{
				parseVal();
			}
			else
				throw new SyntaxException("Start of the program not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		
	}

	private void parseVar() throws Exception
	{
		try
		{
			if(programTokens.get(tokenTracker).getKind()!=Kind.KW_VAR)
				throw new SyntaxException("Var not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			tokenTracker++;
			
			parseNameDef();
			
			if(programTokens.get(tokenTracker).getKind()==Kind.ASSIGN)
			{
				tokenTracker++;
				parseExpression();
			}
			
			if(programTokens.get(tokenTracker).getKind()!=Kind.SEMI)
				throw new SyntaxException("VAL not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		
	}
	
	private void parseVal() throws Exception
	{
		try
		{
			if(programTokens.get(tokenTracker).getKind()!=Kind.KW_VAL)
				throw new SyntaxException("VAL not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;
			
			parseNameDef();
			
			if(programTokens.get(tokenTracker).getKind()!=Kind.ASSIGN)
				throw new SyntaxException("VAL not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;
			
			parseExpression();
			
			if(programTokens.get(tokenTracker).getKind()!=Kind.SEMI)
				throw new SyntaxException("VAL not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
	}
	
	private void parseFunction() throws Exception
	{
		/*
		 * Function grammar is Function::= FUN Identifier  ( ( NameDef (, NameDef)* )? ) 
		 * (: Type)? DO Block END
		 */
		
		try
		{
			if(programTokens.get(tokenTracker).getKind()!=Kind.KW_FUN)
				throw new SyntaxException("Function not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;
			
			if(programTokens.get(tokenTracker).getKind()!=Kind.IDENTIFIER)
				throw new SyntaxException("Function not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;
			
			if(programTokens.get(tokenTracker).getKind()!=Kind.LPAREN)
				throw new SyntaxException("Function not defined properly, parenthesis problem", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;
			
			if(programTokens.get(tokenTracker).getKind()==Kind.IDENTIFIER)
			{
				parseNameDef();
				while(programTokens.get(tokenTracker).getKind()==Kind.COMMA)
				{
					tokenTracker++;
					parseNameDef();
				}
			}
			
			if(programTokens.get(tokenTracker).getKind()!=Kind.RPAREN)
				throw new SyntaxException("Function not defined properly, parenthesis problem", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;
			
			if(programTokens.get(tokenTracker).getKind()==Kind.COLON)
			{
				tokenTracker++;
				parseType();
			}
			
			if(programTokens.get(tokenTracker).getKind()!=Kind.KW_DO)
				throw new SyntaxException("Function not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;
			//parseBlock
			
			while(programTokens.get(tokenTracker).getKind()!=Kind.KW_END)
			{
				parseStatement();
			}
			
			/*
			 * End exists for sure
			 */
			tokenTracker++;
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		
	}
	
	private void parseNameDef() throws Exception {
		
		/*
		 * Function to parse NameDef, signature is  Identifier (: Type)?
		 */
		
		try
		{
			if(programTokens.get(tokenTracker).getKind()!=Kind.IDENTIFIER)
				throw new SyntaxException("Identifier expected as the start of Name Def", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;
			
			if(programTokens.get(tokenTracker).getKind()==Kind.COLON)
			{
				tokenTracker++;
				parseType();
			}
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		
	}
	
	
	
	private void parseStatement() throws Exception {
		
		/*
		 * Function to parse Single Statement.
		 */
		
		try
		{
			if(programTokens.get(tokenTracker).getKind()==Kind.KW_LET)
			{
				tokenTracker++;
				parseNameDef();
				
				if(programTokens.get(tokenTracker).getKind()==Kind.ASSIGN)
				{
					tokenTracker++;
					parseExpression();
				}
				if(programTokens.get(tokenTracker).getKind()!=Kind.SEMI)
					throw new SyntaxException("LET is not constructed properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_SWITCH)
			{
				tokenTracker++;
				parseExpression();
				while(programTokens.get(tokenTracker).getKind()==Kind.KW_CASE)
				{
					tokenTracker++;
					parseExpression();
					if(programTokens.get(tokenTracker).getKind()!=Kind.COLON)
						throw new SyntaxException("SWITCH is not constructed properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
					else tokenTracker++;
					
					while(programTokens.get(tokenTracker).getKind()!=Kind.KW_CASE
							&& programTokens.get(tokenTracker).getKind()!=Kind.KW_DEFAULT)
					{
						parseStatement();
					}
				}
				
				if(programTokens.get(tokenTracker).getKind()!=Kind.KW_DEFAULT)
					throw new SyntaxException("SWITCH is not constructed properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;
				
				while(programTokens.get(tokenTracker).getKind()!=Kind.KW_END)
				{
					parseStatement();
				}
				tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_IF)
			{
				tokenTracker++;
				
				parseExpression();
				
				if(programTokens.get(tokenTracker).getKind()!=Kind.KW_DO)
					throw new SyntaxException("if statement is not constructed properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;
				
				while(programTokens.get(tokenTracker).getKind()!=Kind.KW_END)
				{
					parseStatement();
				}
				
				/*
				 * the last token is for sure end, incrementing without checking
				 */
				tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_WHILE)
			{
				/*
				 * same as if statement
				 */
				tokenTracker++;
				
				parseExpression();
				
				if(programTokens.get(tokenTracker).getKind()!=Kind.KW_DO)
					throw new SyntaxException("whle statement is not constructed properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;
				
				while(programTokens.get(tokenTracker).getKind()!=Kind.KW_END)
				{
					parseStatement();
				}
				
				/*
				 * the last token is for sure end, incrementing without checking
				 */
				tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_RETURN)
			{
				tokenTracker++;
				parseExpression();
				
				if(programTokens.get(tokenTracker).getKind()!=Kind.SEMI)
					throw new SyntaxException("Semi colon missing from return statement", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++; 
			}
			else
			{
				parseExpression();
				
				if(programTokens.get(tokenTracker).getKind()==Kind.ASSIGN)
				{
					tokenTracker++;
					parseExpression();
				}
				
				if(programTokens.get(tokenTracker).getKind()!=Kind.SEMI)
					throw new SyntaxException("Semi colon missing from the statement", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;
			}
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		
	}
	
	private void parseExpression() throws Exception {
		/*
		 * Function to parse Expression,
		 * grammar is Expression::= LogicalExpression
		 */
		
		parseLogicalExpression();
	}
	
	private void parseLogicalExpression() throws Exception {
		/*
		 * Function to parse LogicalExpression, 
		 * whose grammar is ComparisonExpression ( ( && | || ) ComparisonExpression)*
		 */
		
		try
		{
			parseComparisonExpression();
			
			while(programTokens.get(tokenTracker).getKind()==Kind.AND
					|| programTokens.get(tokenTracker).getKind()==Kind.OR)
			{
				tokenTracker++;
				parseComparisonExpression();
			}
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		
	}
	
	private void parseComparisonExpression() throws Exception {
		/*
		 * Function to parse ComparisonExpression, 
		 * whose grammar is  AdditiveExpression ( ( < | > | == | != ) AdditiveExpression)*
		 */
		
		try
		{
			parseAdditiveExpression();
			
			while(programTokens.get(tokenTracker).getKind()==Kind.LT
					|| programTokens.get(tokenTracker).getKind()==Kind.GT
					|| programTokens.get(tokenTracker).getKind()==Kind.EQUALS
					|| programTokens.get(tokenTracker).getKind()==Kind.NOT_EQUALS)
			{
				tokenTracker++;
				parseAdditiveExpression();
			}
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		
	}
	
	private void parseAdditiveExpression() throws Exception  {
		/*
		 * Function to parse AdditiveExpression, 
		 * grammar is MultiplicativeExpression ( ( +  |- ) MultiplicativeExpression )*
		 */
		try
		{
			parseMultiplicativeExpression();
			
			while(programTokens.get(tokenTracker).getKind()==Kind.PLUS
					|| programTokens.get(tokenTracker).getKind()==Kind.MINUS)
			{
				tokenTracker++;
				parseMultiplicativeExpression();
			}
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		
	}
	
	
	private void parseMultiplicativeExpression() throws Exception {
		/*
		 * Function to parse MultiplicativeExpression,
		 * grammar is  UnaryExpression ( ( * | / ) UnaryExpression)*
		 */
		
		try
		{
			parseUnaryExpression();
			
			while(programTokens.get(tokenTracker).getKind()==Kind.DIV
					|| programTokens.get(tokenTracker).getKind()==Kind.TIMES)
			{
				tokenTracker++;
				parseUnaryExpression();
			}
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		
	}
	
	private void parseUnaryExpression() throws Exception {
		/*
		 * Function to parse UnaryExpression ,
		 * grammar is  (! | - )? PrimaryExpression
		 */
		
		try
		{
			if(programTokens.get(tokenTracker).getKind()==Kind.BANG
					|| programTokens.get(tokenTracker).getKind()==Kind.MINUS)
			{
				tokenTracker++;
				parsePrimaryExpression();
			}
			else
			{
				parsePrimaryExpression();
			}
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		
	}
	
	private void parsePrimaryExpression() throws Exception  {
		/*
		 * Function to parse PrimaryExpression
		 * grammar is NIL | TRUE | FALSE |  IntLiteral | StringLiteral   |  ( Expression ) |
		 * Identifier  ( (Expression ( , Expression)* )? )  |
		 * Identifier |  Identifier [ Expression ]    
		 */
		
		try
		{
			if(programTokens.get(tokenTracker).getKind()==Kind.KW_NIL
					|| programTokens.get(tokenTracker).getKind()==Kind.KW_TRUE
					|| programTokens.get(tokenTracker).getKind()==Kind.KW_FALSE
					|| programTokens.get(tokenTracker).getKind()==Kind.INT_LITERAL
					|| programTokens.get(tokenTracker).getKind()==Kind.STRING_LITERAL)
			{
				tokenTracker++;
				return;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.IDENTIFIER
					&& programTokens.size()>tokenTracker+1 
					&& programTokens.get(tokenTracker+1).getKind()==Kind.LPAREN )
			{
				tokenTracker+=2;
				
				if(programTokens.get(tokenTracker).getKind()!=Kind.RPAREN)
				{
					parseExpression();
					while(programTokens.get(tokenTracker).getKind()==Kind.COMMA)
					{
						tokenTracker++;
						parseExpression();
					}
				}
				
				if(programTokens.get(tokenTracker).getKind()!=Kind.RPAREN)
					throw new SyntaxException("Parenthesis Mismatch issue", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.IDENTIFIER
					&& programTokens.size()>tokenTracker+1 
					&& programTokens.get(tokenTracker+1).getKind()==Kind.LSQUARE )
			{
				tokenTracker+=2;
				parseExpression();
				
				if(programTokens.get(tokenTracker).getKind()!=Kind.RSQUARE)
					throw new SyntaxException("Parenthesis mismatch issue", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.IDENTIFIER)
			{
				tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.LPAREN)
			{
				tokenTracker++;
				parseExpression();
				
				if(programTokens.get(tokenTracker).getKind()!=Kind.RPAREN)
					throw new SyntaxException("Parenthesis mismatch issue", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;
			}
			else
				throw new SyntaxException("Could not find correct primary expression", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
		
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		
	}
	
	private void parseType() throws Exception {
		/*
		 * Function to parse Type, signature is INT | STRING | BOOLEAN | LIST [ Type ? ]
		 */
		
		try
		{
			if(programTokens.get(tokenTracker).getKind()==Kind.KW_INT 
					|| programTokens.get(tokenTracker).getKind()==Kind.KW_STRING
					|| programTokens.get(tokenTracker).getKind()==Kind.KW_BOOLEAN
					|| programTokens.get(tokenTracker).getKind()==Kind.KW_LIST)
			{
				tokenTracker++;
				if(programTokens.get(tokenTracker-1).getKind()==Kind.KW_LIST)
				{
					if(programTokens.get(tokenTracker).getKind()!=Kind.LSQUARE)
						throw new SyntaxException("Problem with defining List, could not find closing bracket", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
					else tokenTracker++;
					
					if(programTokens.get(tokenTracker).getKind()!=Kind.RSQUARE)
					{
						parseType();
					}
					
					if(programTokens.get(tokenTracker).getKind()!=Kind.RSQUARE)
						throw new SyntaxException("Problem with defining List, could not find closing bracket", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
					else tokenTracker++;
				}
			}
			else throw new SyntaxException("Type is not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		
	}
	
}
