package edu.ufl.cise.plpfa21.assignment1;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;

public class PLPLexer implements IPLPLexer {

	private String input = null;
	private PLPUtils pLPUtils;
	private int inputLine = 1;
	private int charPositionInLine = 0;
	private Boolean firstTime = true;

	@Override
	public void setInput(String input) {
		this.input = input;
	}

	@Override
	public String getInput() {
		return this.input;
	}

	@Override
	public PLPUtils getpLPUtils() {
		return pLPUtils;
	}

	@Override
	public void setpLPUtils(PLPUtils pLPUtils) {
		this.pLPUtils = pLPUtils;
	}

	@Override
	public IPLPToken nextToken() throws LexicalException { 

		char[] inputCharacters = input.toCharArray();
		int inputTracker = 0;
		boolean reachedEndOfFile = false;
		IPLPToken tokenInformation = new PLPToken();
		
		/*
		 * Checking for comments and surpassing it if found 
		 */
		
		if(firstTime)
		{
			firstTime = false;
			pLPUtils.handleCommentsChanged(this);
		}

		while(true)
		{
			if(inputTracker >= input.length())
			{
				reachedEndOfFile = true;
				break;
			}
			if( pLPUtils.skipTheCharacter( inputCharacters[inputTracker] ) )
			{
				if( pLPUtils.checkIfLine( inputCharacters[inputTracker] ) || ( pLPUtils.checkIfReturnCarriage(inputCharacters[inputTracker]) && pLPUtils.checkIfLine( inputCharacters[inputTracker + 1]) ) )
				{
					charPositionInLine = 0;
					inputLine++;
				}
				else charPositionInLine++;
				inputTracker++;
				continue;
			}
			else break; 
		}

		if(reachedEndOfFile)
		{
			tokenInformation.setKind(Kind.EOF);
			return tokenInformation;
		}

		/*
		
		Map<String, String> commentsHandled = pLPUtils.handleComments(input);
		for(Map.Entry<String, String> singleEntry : commentsHandled.entrySet())
		{
			if(singleEntry.getKey()=="InputString")
			{
				input = singleEntry.getValue();
				inputCharacters = input.toCharArray();
				inputTracker = 0;
			}
			else if(singleEntry.getKey()=="InputLine")
			{
				inputLine = Integer.parseInt(singleEntry.getValue());
			}
			else if(singleEntry.getKey()=="CharPositionInLine")
			{
				charPositionInLine = Integer.parseInt(singleEntry.getValue());
			}
		}
		
		*/
		
		/*
		 * Start :: Handling String literal
		 */
		
		if(inputCharacters[inputTracker]=='"' )
		{
			tokenInformation.setCharPositionInLine(charPositionInLine);
			int startToken = inputTracker;
			while(inputCharacters.length>inputTracker && (inputCharacters[inputTracker+1]!='"' || ( inputCharacters[inputTracker+1]=='"' && inputCharacters[inputTracker]=='\\' )))
			{
				if(inputCharacters[inputTracker]=='\n' || ( inputTracker > 0 && inputCharacters[inputTracker-1]=='\r' && inputCharacters[inputTracker]=='\n'))
				{
					inputLine++;
					charPositionInLine=0;
				}
				else charPositionInLine++;
				inputTracker++;
			}
			
			String stringLiteralToken =  input.substring( startToken, inputTracker + 2 );
			input = input.substring(inputTracker + 2);
			charPositionInLine+=2;
			
			tokenInformation.setKind(Kind.STRING_LITERAL);
			//tokenInformation.setStringValue(tokenToBeChecked);
			tokenInformation.setText(stringLiteralToken);
			for(Map.Entry<String, String> singleEntry : Constants.escapeSequence.entrySet())
			{
				if(stringLiteralToken.contains(singleEntry.getKey()))
				{
					stringLiteralToken.replace(singleEntry.getKey(), singleEntry.getValue());
				}
			}
			tokenInformation.setStringValue(stringLiteralToken.substring(1, stringLiteralToken.length()-1));
			tokenInformation.setLine(inputLine);
			return tokenInformation;
			
		}
		else if(inputCharacters[inputTracker]=='\'')
		{
			tokenInformation.setCharPositionInLine(charPositionInLine);
			int startToken = inputTracker;
			while(inputCharacters.length>inputTracker && (inputCharacters[inputTracker+1]!='\'' || ( inputCharacters[inputTracker+1]=='\'' && inputCharacters[inputTracker]=='\\' )))
			{
				if(inputCharacters[inputTracker]=='\n' || (inputTracker > 0 && inputCharacters[inputTracker-1]=='\r' && inputCharacters[inputTracker]=='\n'))
				{
					inputLine++;
					charPositionInLine=0;
				}
				else charPositionInLine++;
				inputTracker++;
			}
			String stringLiteralToken =  input.substring( startToken, inputTracker + 2 );
			input = input.substring(inputTracker + 2);
			charPositionInLine+=2;
			tokenInformation.setKind(Kind.STRING_LITERAL);
			//tokenInformation.setStringValue(tokenToBeChecked);
			tokenInformation.setText(stringLiteralToken);
			for(Map.Entry<String, String> singleEntry : Constants.escapeSequence.entrySet())
			{
				if(stringLiteralToken.contains(singleEntry.getKey()))
				{
					stringLiteralToken.replace(singleEntry.getKey(), singleEntry.getValue());
				}
			}
			tokenInformation.setStringValue(stringLiteralToken.substring(1, stringLiteralToken.length()-1));
			tokenInformation.setLine(inputLine);
			return tokenInformation;
		}
		
		/*
		 * End :: Handling String literal
		 */
		
		int startToken = inputTracker;
		
		if(inputTracker >= input.length())
		{
			reachedEndOfFile = true;
		}
		
		if(reachedEndOfFile)
		{
			tokenInformation.setKind(Kind.EOF);
			return tokenInformation;
		}
		
		while(true)
		{
			if( !pLPUtils.skipTheCharacter( inputCharacters[inputTracker] ) )
			{
				inputTracker++;
				continue;
			}
			else break;
		}

		int endToken = inputTracker;

		String tokenToBeChecked = input.substring( startToken, endToken );
		
		char[] tokenToBeCheckedCharArray = tokenToBeChecked.toCharArray();
		
		/*
		 * Check if the token starts with keyword
		 */
		for(String singleKey : PLPUtils.keywordsList.keySet())
		{
			if(tokenToBeChecked.startsWith(singleKey))
			{
				Kind kindFound = PLPUtils.keywordsList.get(singleKey);
				tokenInformation.setKind(kindFound);
				tokenInformation.setStringValue(singleKey);
				tokenInformation.setText(singleKey);
				tokenInformation.setLine(inputLine);
				tokenInformation.setCharPositionInLine(charPositionInLine);
				charPositionInLine+=singleKey.length();
				input = input.substring(startToken + singleKey.length());
				return tokenInformation;
			}
		}
		
		/*
		 * Checking for symbols of length 1 and then of length 2
		 */
		if(tokenToBeCheckedCharArray.length > 1 && pLPUtils.checkIfSymbol(tokenToBeChecked.substring(0, 2)))
		{
			tokenInformation.setKind(pLPUtils.getSymbolType(tokenToBeChecked.substring(0, 2)));
			tokenInformation.setStringValue(tokenToBeChecked.substring(0, 2));
			tokenInformation.setText(tokenToBeChecked.substring(0, 2));
			tokenInformation.setLine(inputLine);
			tokenInformation.setCharPositionInLine(charPositionInLine);
			input = input.substring(startToken+2);
			charPositionInLine+=2;
		}
		else if(pLPUtils.checkIfSymbol(tokenToBeChecked.substring(0, 1)))
		{
			tokenInformation.setKind(pLPUtils.getSymbolType(tokenToBeChecked.substring(0, 1)));
			tokenInformation.setStringValue(tokenToBeChecked.substring(0, 1));
			tokenInformation.setText(tokenToBeChecked.substring(0, 1));
			tokenInformation.setLine(inputLine);
			tokenInformation.setCharPositionInLine(charPositionInLine);
			input = input.substring(startToken+1);
			charPositionInLine++;
		}
		else if(pLPUtils.checkIfIntLiteral(tokenToBeChecked.substring(0, 1)))
		{	
			Pattern p = Pattern.compile(Constants.intLiteralRegex);
	        Matcher m = p.matcher(tokenToBeChecked);
	        String realTokenToBeChecked = null;
	        if(m.find()) {
	            realTokenToBeChecked = m.group();
	        }
			tokenInformation.setKind(Kind.INT_LITERAL);
			tokenInformation.setStringValue(realTokenToBeChecked);
			tokenInformation.setText(realTokenToBeChecked);
			tokenInformation.setLine(inputLine);
			try
			{
				int intValue = Integer.parseInt(realTokenToBeChecked);
				tokenInformation.setIntValue(intValue);
			}
			catch(NumberFormatException numberFormatException)
			{
				throw new LexicalException("The number in out of integer range", inputLine, charPositionInLine);
			}
			
			tokenInformation.setCharPositionInLine(charPositionInLine);
			charPositionInLine+=realTokenToBeChecked.length();
			input = input.substring(startToken+realTokenToBeChecked.length());
		}
		else if(pLPUtils.checkIfIdentifier(tokenToBeChecked.substring(0, 1)))
		{
			Pattern p = Pattern.compile(Constants.identifierRegex);
	        Matcher m = p.matcher(tokenToBeChecked);
	        String realTokenToBeChecked = null;
	        if(m.find()) {
	            realTokenToBeChecked = m.group();
	        }
			tokenInformation.setKind(Kind.IDENTIFIER);
			tokenInformation.setStringValue(realTokenToBeChecked);
			tokenInformation.setText(realTokenToBeChecked);
			tokenInformation.setLine(inputLine);
			tokenInformation.setCharPositionInLine(charPositionInLine);
			charPositionInLine+=realTokenToBeChecked.length();
			input = input.substring(startToken+realTokenToBeChecked.length());
		}
		else
		{
			tokenInformation.setKind(Kind.ERROR);
			throw new LexicalException("Cannot be indentified in any of the forms", inputLine, charPositionInLine);
		}
	
		return tokenInformation;

	}


}
