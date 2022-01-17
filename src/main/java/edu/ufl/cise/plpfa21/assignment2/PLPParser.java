package edu.ufl.cise.plpfa21.assignment2;

import java.util.ArrayList;
import java.util.List;

import edu.ufl.cise.plpfa21.assignment1.CompilerComponentFactory;
import edu.ufl.cise.plpfa21.assignment1.IPLPLexer;
import edu.ufl.cise.plpfa21.assignment1.IPLPToken;
import edu.ufl.cise.plpfa21.assignment1.LexicalException;
import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;
import edu.ufl.cise.plpfa21.assignment3.ast.IASTNode;
import edu.ufl.cise.plpfa21.assignment3.ast.IAssignmentStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IBinaryExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IBlock;
import edu.ufl.cise.plpfa21.assignment3.ast.IBooleanLiteralExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IDeclaration;
import edu.ufl.cise.plpfa21.assignment3.ast.IExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IFunctionCallExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IFunctionDeclaration;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IIdentifier;
import edu.ufl.cise.plpfa21.assignment3.ast.IIfStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IImmutableGlobal;
import edu.ufl.cise.plpfa21.assignment3.ast.IIntLiteralExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.ILetStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IListSelectorExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IMutableGlobal;
import edu.ufl.cise.plpfa21.assignment3.ast.INameDef;
import edu.ufl.cise.plpfa21.assignment3.ast.INilConstantExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IProgram;
import edu.ufl.cise.plpfa21.assignment3.ast.IReturnStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IStringLiteralExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.ISwitchStatement;
import edu.ufl.cise.plpfa21.assignment3.ast.IType;
import edu.ufl.cise.plpfa21.assignment3.ast.IType.TypeKind;
import edu.ufl.cise.plpfa21.assignment3.ast.IUnaryExpression;
import edu.ufl.cise.plpfa21.assignment3.ast.IWhileStatement;
import edu.ufl.cise.plpfa21.assignment3.astimpl.AssignmentStatement__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.BinaryExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.Block__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.BooleanLiteralExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.FunctionCallExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.FunctionDeclaration___;
import edu.ufl.cise.plpfa21.assignment3.astimpl.IdentExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.Identifier__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.IfStatement__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.ImmutableGlobal__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.IntLiteralExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.LetStatement__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.ListSelectorExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.ListType__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.MutableGlobal__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.NameDef__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.NilConstantExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.PrimitiveType__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.Program__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.ReturnStatement__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.StringLiteralExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.SwitchStatement__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.UnaryExpression__;
import edu.ufl.cise.plpfa21.assignment3.astimpl.WhileStatement__;

public class PLPParser implements IPLPParser {

	String code;
	Integer tokenTracker = 0;
	List<IPLPToken> programTokens = new ArrayList<IPLPToken>();

	public PLPParser(String code){
		this.code = code;
	}

	@Override
	public IASTNode parse() throws Exception {

		IPLPToken tokenFetched = null;
		IProgram program = null;
		try
		{
			IPLPLexer pLPLexer = CompilerComponentFactory.getLexer(code);
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
			program = parseProgram();
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}

		return program;
	}

	private IProgram parseProgram() throws Exception
	{
		IProgram program = null;
		int valTokenTracker = 0;
		List<IDeclaration> declarationList = new ArrayList<IDeclaration>();
		while(programTokens.get(tokenTracker).getKind()!=Kind.EOF)
		{
			valTokenTracker = tokenTracker;
			IDeclaration declaration = parseDeclaration();
			declarationList.add(declaration);
		}
		program = new Program__(programTokens.get(valTokenTracker).getLine(), programTokens.get(valTokenTracker).getCharPositionInLine(), programTokens.get(valTokenTracker).getText(), declarationList);
		return program;
	}

	private IDeclaration parseDeclaration() throws Exception
	{
		IDeclaration declaration = null;
		try
		{
			if(programTokens.get(tokenTracker).getKind()==Kind.KW_FUN)
			{
				declaration = parseFunction();
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_VAR)
			{
				/*
				 * Mutable Global
				 */
				declaration = parseVar();
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_VAL)
			{
				/*
				 * ImmutableGlobal
				 */
				declaration = parseVal();
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
		return declaration;
	}

	private IMutableGlobal parseVar() throws Exception
	{
		IMutableGlobal mutableGlobal = null;
		int valTokenTracker;
		try
		{
			if(programTokens.get(tokenTracker).getKind()!=Kind.KW_VAR)
				throw new SyntaxException("Var not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else
			{
				valTokenTracker = tokenTracker;
				tokenTracker++;
			}

			INameDef namedef = parseNameDef();
			IExpression expression = null;
			if(programTokens.get(tokenTracker).getKind()==Kind.ASSIGN)
			{
				tokenTracker++;
				expression = parseExpression();
			}

			if(programTokens.get(tokenTracker).getKind()!=Kind.SEMI)
				throw new SyntaxException("VAL not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;

			mutableGlobal = new MutableGlobal__(programTokens.get(valTokenTracker).getLine(), programTokens.get(valTokenTracker).getCharPositionInLine(), programTokens.get(valTokenTracker).getText(), namedef, expression);
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		return mutableGlobal;
	}

	private IImmutableGlobal parseVal() throws Exception
	{
		IImmutableGlobal immutableGlobal = null;
		int valTokenTracker;
		try
		{
			if(programTokens.get(tokenTracker).getKind()!=Kind.KW_VAL)
				throw new SyntaxException("VAL not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else
			{
				valTokenTracker = tokenTracker;
				tokenTracker++;
			}

			INameDef nameDef = parseNameDef();

			if(programTokens.get(tokenTracker).getKind()!=Kind.ASSIGN)
				throw new SyntaxException("VAL not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;

			IExpression expression = parseExpression();

			if(programTokens.get(tokenTracker).getKind()!=Kind.SEMI)
				throw new SyntaxException("VAL not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;
			immutableGlobal = new ImmutableGlobal__(programTokens.get(valTokenTracker).getLine(), programTokens.get(valTokenTracker).getCharPositionInLine(), programTokens.get(valTokenTracker).getText(), nameDef, expression);
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		return immutableGlobal;
	}

	private IFunctionDeclaration parseFunction() throws Exception
	{
		/*
		 * Function grammar is Function::= FUN Identifier  ( ( NameDef (, NameDef)* )? ) 
		 * (: Type)? DO Block END
		 */
		IFunctionDeclaration functionDeclaration = null;
		try
		{
			int valTokenTracker = tokenTracker;
			IIdentifier name = null;
			if(programTokens.get(tokenTracker).getKind()!=Kind.KW_FUN)
				throw new SyntaxException("Function not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;

			if(programTokens.get(tokenTracker).getKind()!=Kind.IDENTIFIER)
				throw new SyntaxException("Function not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else
			{
				name = parseIdentifier();
			}

			List<INameDef> arguments = new ArrayList<INameDef>();

			if(programTokens.get(tokenTracker).getKind()!=Kind.LPAREN)
				throw new SyntaxException("Function not defined properly, parenthesis problem", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;

			if(programTokens.get(tokenTracker).getKind()==Kind.IDENTIFIER)
			{
				arguments.add( parseNameDef() );
				while(programTokens.get(tokenTracker).getKind()==Kind.COMMA)
				{
					tokenTracker++;
					arguments.add( parseNameDef() );
				}
			}

			if(programTokens.get(tokenTracker).getKind()!=Kind.RPAREN)
				throw new SyntaxException("Function not defined properly, parenthesis problem", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;

			IType resultType = null;

			if(programTokens.get(tokenTracker).getKind()==Kind.COLON)
			{
				tokenTracker++;
				resultType = parseType();
			}

			if(programTokens.get(tokenTracker).getKind()!=Kind.KW_DO)
				throw new SyntaxException("Function not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else tokenTracker++;
			//parseBlock

			int blockTracker = tokenTracker;
			List<IStatement> statementList = new ArrayList<IStatement>();

			while(programTokens.get(tokenTracker).getKind()!=Kind.KW_END)
			{
				statementList.add(parseStatement());
			}

			IBlock block = new Block__(programTokens.get(blockTracker).getLine(), programTokens.get(blockTracker).getCharPositionInLine(), programTokens.get(blockTracker).getText(), statementList);
			/*
			 * End exists for sure
			 */
			tokenTracker++;

			functionDeclaration = new FunctionDeclaration___(programTokens.get(valTokenTracker).getLine(), programTokens.get(valTokenTracker).getCharPositionInLine(), programTokens.get(valTokenTracker).getText(), name, arguments, resultType, block);

		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		return functionDeclaration;
	}

	private INameDef parseNameDef() throws Exception {

		/*
		 * Function to parse NameDef, signature is  Identifier (: Type)?
		 */

		IIdentifier identifier = null;
		IType type = null;
		try
		{
			if(programTokens.get(tokenTracker).getKind()!=Kind.IDENTIFIER)
				throw new SyntaxException("Identifier expected as the start of Name Def", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
			else
			{
				identifier = parseIdentifier();
			}

			if(programTokens.get(tokenTracker).getKind()==Kind.COLON)
			{
				tokenTracker++;
				type = parseType();
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

		INameDef nameDef = new NameDef__(identifier.getLine(), identifier.getPosInLine(), identifier.getText(), identifier, type);
		return nameDef;
	}



	private IStatement parseStatement() throws Exception {

		/*
		 * Function to parse Single Statement.
		 */
		IStatement statement = null;

		try
		{
			if(programTokens.get(tokenTracker).getKind()==Kind.KW_LET)
			{
				ILetStatement letStatement = null;
				int valTokenTracker = tokenTracker;
				tokenTracker++;
				INameDef localDef = parseNameDef();
				IExpression expression = null;
				if(programTokens.get(tokenTracker).getKind()==Kind.ASSIGN)
				{
					tokenTracker++;
					expression = parseExpression();
				}

				/*
				if(programTokens.get(tokenTracker).getKind()!=Kind.SEMI)
					throw new SyntaxException("LET is not constructed properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;
				 */

				if(programTokens.get(tokenTracker).getKind()!=Kind.KW_DO)
					throw new SyntaxException("Function not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;
				//parseBlock

				int blockTracker = tokenTracker;
				List<IStatement> statementList = new ArrayList<IStatement>();

				while(programTokens.get(tokenTracker).getKind()!=Kind.KW_END)
				{
					statementList.add(parseStatement());
				}
				tokenTracker++;

				IBlock block = new Block__(programTokens.get(blockTracker).getLine(), programTokens.get(blockTracker).getCharPositionInLine(), programTokens.get(blockTracker).getText(), statementList);

				letStatement = new LetStatement__(programTokens.get(valTokenTracker).getLine(), programTokens.get(valTokenTracker).getCharPositionInLine(), programTokens.get(valTokenTracker).getText(), block, expression, localDef);
				statement = letStatement;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_SWITCH)
			{
				ISwitchStatement switchStatement = null;
				IExpression switchExpression = null;
				List<IExpression> branchExpressions = new ArrayList<IExpression>();
				List<IBlock> blocks = new ArrayList<IBlock>();
				IBlock defaultBlock = null;

				int valTokenTracker = tokenTracker;
				tokenTracker++;

				switchExpression = parseExpression();

				while(programTokens.get(tokenTracker).getKind()==Kind.KW_CASE)
				{
					tokenTracker++;
					branchExpressions.add( parseExpression() );

					if(programTokens.get(tokenTracker).getKind()!=Kind.COLON)
						throw new SyntaxException("SWITCH is not constructed properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
					else tokenTracker++;

					List<IStatement> statementList = new ArrayList<IStatement>();
					while(programTokens.get(tokenTracker).getKind()!=Kind.KW_CASE
							&& programTokens.get(tokenTracker).getKind()!=Kind.KW_DEFAULT)
					{
						statementList.add(parseStatement());
					}

					IBlock blockCorrespondingToCase = new Block__(programTokens.get(valTokenTracker).getLine(), programTokens.get(valTokenTracker).getCharPositionInLine(), programTokens.get(valTokenTracker).getText(), statementList);
					blocks.add(blockCorrespondingToCase);
				}

				if(programTokens.get(tokenTracker).getKind()!=Kind.KW_DEFAULT)
					throw new SyntaxException("SWITCH is not constructed properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;

				List<IStatement> statementList = new ArrayList<IStatement>();
				while(programTokens.get(tokenTracker).getKind()!=Kind.KW_END)
				{
					statementList.add(parseStatement());
				}
				tokenTracker++;

				defaultBlock = new Block__(programTokens.get(valTokenTracker).getLine(), programTokens.get(valTokenTracker).getCharPositionInLine(), programTokens.get(valTokenTracker).getText(), statementList);

				switchStatement = new SwitchStatement__(programTokens.get(valTokenTracker).getLine(), programTokens.get(valTokenTracker).getCharPositionInLine(), programTokens.get(valTokenTracker).getText(), switchExpression, branchExpressions, blocks, defaultBlock);
				statement = switchStatement;

			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_IF)
			{
				int ifTokenTracker = tokenTracker;
				tokenTracker++;

				IExpression guardExpression = parseExpression();

				if(programTokens.get(tokenTracker).getKind()!=Kind.KW_DO)
					throw new SyntaxException("if statement is not constructed properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;

				int ifBlockTokenTracker = tokenTracker;
				List<IStatement> statementList = new ArrayList<IStatement>();
				while(programTokens.get(tokenTracker).getKind()!=Kind.KW_END)
				{
					IStatement statementInternal = parseStatement();
					statementList.add(statementInternal);
				}
				IBlock ifBlock = new Block__(programTokens.get(ifBlockTokenTracker).getLine(), programTokens.get(ifBlockTokenTracker).getCharPositionInLine(), programTokens.get(ifBlockTokenTracker).getText(), statementList);

				/*
				 * the last token is for sure end, incrementing without checking
				 */
				tokenTracker++;

				IIfStatement ifStatement = new IfStatement__(programTokens.get(ifTokenTracker).getLine(), programTokens.get(ifTokenTracker).getCharPositionInLine(), programTokens.get(ifTokenTracker).getText(), guardExpression, ifBlock);
				statement = ifStatement;

			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_WHILE)
			{
				/*
				 * same as if statement
				 */
				int whileTokenTracker = tokenTracker;
				tokenTracker++;

				IExpression guardExpression = parseExpression();

				if(programTokens.get(tokenTracker).getKind()!=Kind.KW_DO)
					throw new SyntaxException("whle statement is not constructed properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;

				int whileBlockTokenTracker = tokenTracker;
				List<IStatement> statementList = new ArrayList<IStatement>();
				while(programTokens.get(tokenTracker).getKind()!=Kind.KW_END)
				{
					statementList.add(parseStatement());
				}
				IBlock whileBlock = new Block__(programTokens.get(whileBlockTokenTracker).getLine(), programTokens.get(whileBlockTokenTracker).getCharPositionInLine(), programTokens.get(whileBlockTokenTracker).getText(), statementList);

				/*
				 * the last token is for sure end, incrementing without checking
				 */
				IWhileStatement whileStatement = new WhileStatement__(programTokens.get(whileTokenTracker).getLine(), programTokens.get(whileTokenTracker).getCharPositionInLine(), programTokens.get(whileTokenTracker).getText(), guardExpression, whileBlock);
				statement = whileStatement;
				tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_RETURN)
			{
				int returnTokenTracker = tokenTracker;
				tokenTracker++;
				IExpression returnExpression = parseExpression();

				if(programTokens.get(tokenTracker).getKind()!=Kind.SEMI)
					throw new SyntaxException("Semi colon missing from return statement", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++; 

				IReturnStatement returnStatement = new ReturnStatement__(programTokens.get(returnTokenTracker).getLine(), programTokens.get(returnTokenTracker).getCharPositionInLine(), programTokens.get(returnTokenTracker).getText(), returnExpression);
				statement = returnStatement;
			}
			else
			{
				int expressionTokenTracker = tokenTracker;
				IExpression leftExpression = parseExpression();
				IExpression rightExpression = null;
				if(programTokens.get(tokenTracker).getKind()==Kind.ASSIGN)
				{
					tokenTracker++;
					rightExpression = parseExpression();
				}

				if(programTokens.get(tokenTracker).getKind()!=Kind.SEMI)
					throw new SyntaxException("Semi colon missing from the statement", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;

				IAssignmentStatement assignmentStatement = new AssignmentStatement__(programTokens.get(expressionTokenTracker).getLine(), programTokens.get(expressionTokenTracker).getCharPositionInLine(), programTokens.get(expressionTokenTracker).getText(), leftExpression, rightExpression);
				statement = assignmentStatement;
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
		return statement;
	}

	private IExpression parseExpression() throws Exception {
		/*
		 * Function to parse Expression,
		 * grammar is Expression::= LogicalExpression
		 */
		IExpression expression = parseLogicalExpression();
		return expression;
	}

	private IExpression parseLogicalExpression() throws Exception {
		/*
		 * Function to parse LogicalExpression, 
		 * whose grammar is ComparisonExpression ( ( && | || ) ComparisonExpression)*
		 */

		int logicalExpressionTokenTracker = tokenTracker;
		IBinaryExpression binaryExpression = null;

		try
		{
			IExpression leftComparisonExpression = parseComparisonExpression();
			Kind op = null;
			IExpression rightComparisonExpression = null;

			while(programTokens.get(tokenTracker).getKind()==Kind.AND
					|| programTokens.get(tokenTracker).getKind()==Kind.OR)
			{
				op = programTokens.get(tokenTracker).getKind();
				tokenTracker++;
				rightComparisonExpression = parseComparisonExpression();
			}
			
			if(op==null)
				return leftComparisonExpression;
			
			binaryExpression = new BinaryExpression__(programTokens.get(logicalExpressionTokenTracker).getLine(), programTokens.get(logicalExpressionTokenTracker).getCharPositionInLine(), programTokens.get(logicalExpressionTokenTracker).getText(), leftComparisonExpression, rightComparisonExpression, op);
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}

		return binaryExpression;
	}

	private IExpression parseComparisonExpression() throws Exception {
		/*
		 * Function to parse ComparisonExpression, 
		 * whose grammar is  AdditiveExpression ( ( < | > | == | != ) AdditiveExpression)*
		 */
		int comparisonExpressionTokenTracker = tokenTracker;
		IBinaryExpression binaryExpression = null;
		try
		{
			IExpression leftComparisonExpression = parseAdditiveExpression();
			Kind op = null;
			IExpression rightComparisonExpression = null;

			while(programTokens.get(tokenTracker).getKind()==Kind.LT
					|| programTokens.get(tokenTracker).getKind()==Kind.GT
					|| programTokens.get(tokenTracker).getKind()==Kind.EQUALS
					|| programTokens.get(tokenTracker).getKind()==Kind.NOT_EQUALS)
			{
				op = programTokens.get(tokenTracker).getKind();
				tokenTracker++;
				rightComparisonExpression = parseAdditiveExpression();
			}
			
			if(op==null)
				return leftComparisonExpression;
			
			binaryExpression = new BinaryExpression__(programTokens.get(comparisonExpressionTokenTracker).getLine(), programTokens.get(comparisonExpressionTokenTracker).getCharPositionInLine(), programTokens.get(comparisonExpressionTokenTracker).getText(), leftComparisonExpression, rightComparisonExpression, op);
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		return binaryExpression;
	}

	private IExpression parseAdditiveExpression() throws Exception  {
		/*
		 * Function to parse AdditiveExpression, 
		 * grammar is MultiplicativeExpression ( ( +  |- ) MultiplicativeExpression )*
		 */

		int additiveExpressionTokenTracker = tokenTracker;
		IBinaryExpression binaryExpression = null;
		try
		{
			IExpression leftComparisonExpression = parseMultiplicativeExpression();
			Kind op = null;
			IExpression rightComparisonExpression = null;


			while(programTokens.get(tokenTracker).getKind()==Kind.PLUS
					|| programTokens.get(tokenTracker).getKind()==Kind.MINUS)
			{
				op = programTokens.get(tokenTracker).getKind();
				tokenTracker++;
				rightComparisonExpression = parseMultiplicativeExpression();
			}
			
			if(op==null)
				return leftComparisonExpression;
			
			binaryExpression = new BinaryExpression__(programTokens.get(additiveExpressionTokenTracker).getLine(), programTokens.get(additiveExpressionTokenTracker).getCharPositionInLine(), programTokens.get(additiveExpressionTokenTracker).getText(), leftComparisonExpression, rightComparisonExpression, op);
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}

		return binaryExpression;
	}


	private IExpression parseMultiplicativeExpression() throws Exception {
		/*
		 * Function to parse MultiplicativeExpression,
		 * grammar is  UnaryExpression ( ( * | / ) UnaryExpression)*
		 */

		int binaryExpressionTokenTracker = tokenTracker;
		IBinaryExpression binaryExpression = null;
		try
		{
			IExpression leftComparisonExpression = parseUnaryExpression();
			Kind op = null;
			IExpression rightComparisonExpression = null;

			while(programTokens.get(tokenTracker).getKind()==Kind.DIV
					|| programTokens.get(tokenTracker).getKind()==Kind.TIMES)
			{
				op = programTokens.get(tokenTracker).getKind();
				tokenTracker++;
				rightComparisonExpression = parseUnaryExpression();
			}
			
			if(op==null)
				return leftComparisonExpression;
			binaryExpression = new BinaryExpression__(programTokens.get(binaryExpressionTokenTracker).getLine(), programTokens.get(binaryExpressionTokenTracker).getCharPositionInLine(), programTokens.get(binaryExpressionTokenTracker).getText(), leftComparisonExpression, rightComparisonExpression, op);
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}

		return binaryExpression;

	}

	private IExpression parseUnaryExpression() throws Exception {
		/*
		 * Function to parse UnaryExpression ,
		 * grammar is  (! | - )? PrimaryExpression
		 */
		int unaryExpressionTokenTracker = tokenTracker;
		IExpression expression = null;
		IUnaryExpression unaryExpression = null;
		try
		{
			Kind op = null;
			if(programTokens.get(tokenTracker).getKind()==Kind.BANG
					|| programTokens.get(tokenTracker).getKind()==Kind.MINUS)
			{
				op = programTokens.get(tokenTracker).getKind();
				tokenTracker++;
				expression =  parsePrimaryExpression();
			}
			else
			{
				expression = parsePrimaryExpression();
			}
			
			if(op==null)
				return expression;
			
			unaryExpression = new UnaryExpression__(programTokens.get(unaryExpressionTokenTracker).getLine(), programTokens.get(unaryExpressionTokenTracker).getCharPositionInLine(), programTokens.get(unaryExpressionTokenTracker).getText(), expression, op);
		}
		catch(SyntaxException syntaxException)
		{
			throw syntaxException;
		}
		catch(Exception exception)
		{
			throw new Exception("Abrupt exception");
		}
		return unaryExpression;
	}

	private IExpression parsePrimaryExpression() throws Exception  {
		/*
		 * Function to parse PrimaryExpression
		 * grammar is NIL | TRUE | FALSE |  IntLiteral | StringLiteral   |  ( Expression ) |
		 * Identifier  ( (Expression ( , Expression)* )? )  |
		 * Identifier |  Identifier [ Expression ]    
		 */

		IExpression expression = null;

		try
		{
			if(programTokens.get(tokenTracker).getKind()==Kind.KW_NIL)
			{
				INilConstantExpression nilConstantExpression = new NilConstantExpression__(programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine(), programTokens.get(tokenTracker).getText());
				expression = nilConstantExpression;
				tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_TRUE
					|| programTokens.get(tokenTracker).getKind()==Kind.KW_FALSE)
			{
				Boolean kindToBooleanValue = true;
				if(programTokens.get(tokenTracker).getKind()==Kind.KW_FALSE)
					kindToBooleanValue = false;
				IBooleanLiteralExpression booleanLiteralExpression = new BooleanLiteralExpression__(programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine(), programTokens.get(tokenTracker).getText(), kindToBooleanValue);
				expression = booleanLiteralExpression;
				tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.INT_LITERAL)
			{
				IIntLiteralExpression intLiteralExpression = new IntLiteralExpression__(programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine(), programTokens.get(tokenTracker).getText(), programTokens.get(tokenTracker).getIntValue());
				expression = intLiteralExpression;
				tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.STRING_LITERAL)
			{
				IStringLiteralExpression stringLiteralExpression = new StringLiteralExpression__(programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine(), programTokens.get(tokenTracker).getText(), programTokens.get(tokenTracker).getStringValue());
				expression = stringLiteralExpression;
				tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.IDENTIFIER
					&& programTokens.size()>tokenTracker+1 
					&& programTokens.get(tokenTracker+1).getKind()==Kind.LPAREN)
			{
				int functionCallExpressionTokenTracker = tokenTracker;
				IIdentifier name = parseIdentifier();
				tokenTracker++;

				List<IExpression> arguments = new ArrayList<IExpression>();
				if(programTokens.get(tokenTracker).getKind()!=Kind.RPAREN)
				{
					arguments.add(parseExpression());
					while(programTokens.get(tokenTracker).getKind()==Kind.COMMA)
					{
						tokenTracker++;
						arguments.add(parseExpression());
					}
				}

				IFunctionCallExpression functionCallExpression = new FunctionCallExpression__(programTokens.get(functionCallExpressionTokenTracker).getLine(), programTokens.get(functionCallExpressionTokenTracker).getCharPositionInLine(), programTokens.get(functionCallExpressionTokenTracker).getText(), name, arguments);

				if(programTokens.get(tokenTracker).getKind()!=Kind.RPAREN)
					throw new SyntaxException("Parenthesis Mismatch issue", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;
				expression = functionCallExpression;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.IDENTIFIER
					&& programTokens.size()>tokenTracker+1 
					&& programTokens.get(tokenTracker+1).getKind()==Kind.LSQUARE )
			{

				int listSelectorExpressionTokenTracker = tokenTracker;
				IIdentifier name = parseIdentifier();
				tokenTracker++;
				IExpression index = parseExpression();

				if(programTokens.get(tokenTracker).getKind()!=Kind.RSQUARE)
					throw new SyntaxException("Parenthesis mismatch issue", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;

				IListSelectorExpression listSelectorExpression = new ListSelectorExpression__(programTokens.get(listSelectorExpressionTokenTracker).getLine(), programTokens.get(listSelectorExpressionTokenTracker).getCharPositionInLine(), programTokens.get(listSelectorExpressionTokenTracker).getText(), name, index);
				expression = listSelectorExpression;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.IDENTIFIER)
			{
				int identExpressionTokenTracker = tokenTracker;
				IIdentifier name = parseIdentifier();
				IIdentExpression identExpression = new IdentExpression__(programTokens.get(identExpressionTokenTracker).getLine(), programTokens.get(identExpressionTokenTracker).getCharPositionInLine(), programTokens.get(identExpressionTokenTracker).getText(), name);
				expression = identExpression;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.LPAREN)
			{
				tokenTracker++;
				expression = parseExpression();

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

		return expression;

	}

	private IIdentifier parseIdentifier() throws Exception{
		/*
		 * Function to parse Identifier 
		 */
		IIdentifier identifier = null;
		if(programTokens.get(tokenTracker).getKind()==Kind.IDENTIFIER)
			identifier = new Identifier__(programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine(), programTokens.get(tokenTracker).getText(), programTokens.get(tokenTracker).getStringValue());
		tokenTracker++;
		return identifier;
	}

	private IType parseType() throws Exception {
		/*
		 * Function to parse Type, signature is INT | STRING | BOOLEAN | LIST [ Type ? ]
		 */
		IType type = null;
		try
		{			
			if(programTokens.get(tokenTracker).getKind()==Kind.KW_INT)
			{
				type = new PrimitiveType__(programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine(), programTokens.get(tokenTracker).getText(), TypeKind.INT);
				tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_STRING)
			{
				type = new PrimitiveType__(programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine(), programTokens.get(tokenTracker).getText(), TypeKind.STRING);
				tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_BOOLEAN)
			{
				type = new PrimitiveType__(programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine(), programTokens.get(tokenTracker).getText(), TypeKind.BOOLEAN);
				tokenTracker++;
			}
			else if(programTokens.get(tokenTracker).getKind()==Kind.KW_LIST)
			{
				int valTokenTracker = tokenTracker;
				tokenTracker++;
				if(programTokens.get(tokenTracker).getKind()!=Kind.LSQUARE)
					throw new SyntaxException("Problem with defining List, could not find closing bracket", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;

				if(programTokens.get(tokenTracker).getKind()!=Kind.RSQUARE)
				{
					IType listInternalType = parseType();
					type = new ListType__(programTokens.get(valTokenTracker).getLine(), programTokens.get(valTokenTracker).getCharPositionInLine(), programTokens.get(valTokenTracker).getText(), listInternalType);
				}
				else
				{
					type = new ListType__(programTokens.get(valTokenTracker).getLine(), programTokens.get(valTokenTracker).getCharPositionInLine(), programTokens.get(valTokenTracker).getText(), null);
				}

				if(programTokens.get(tokenTracker).getKind()!=Kind.RSQUARE)
					throw new SyntaxException("Problem with defining List, could not find closing bracket", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());
				else tokenTracker++;
			}
			else throw new SyntaxException("Type is not defined properly", programTokens.get(tokenTracker).getLine(), programTokens.get(tokenTracker).getCharPositionInLine());

			return type;
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
