package edu.ufl.cise.plpfa21.assignment1;

public class PLPToken implements IPLPToken {

	private Kind kind;
	private String text;
	private int line;
	private int charPositionInLine;
	private String stringValue;
	private int intValue;
		
	@Override
	public Kind getKind() {
		// TODO Auto-generated method stub
		return kind;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return text;
	}

	@Override
	public int getLine() {
		// TODO Auto-generated method stub
		return line;
	}

	@Override
	public int getCharPositionInLine() {
		// TODO Auto-generated method stub
		return charPositionInLine;
	}

	@Override
	public String getStringValue() {
		// TODO Auto-generated method stub
		return stringValue;
	}

	@Override
	public int getIntValue() {
		// TODO Auto-generated method stub
		return intValue;
	}

	@Override
	public void setKind( Kind kind ) {
		// TODO Auto-generated method stub
		this.kind = kind;
	}

	@Override
	public void setText( String text ) {
		// TODO Auto-generated method stub
		this.text = text;
	}

	@Override
	public void setLine( int line ) {
		// TODO Auto-generated method stub
		this.line = line;
	}

	@Override
	public void setCharPositionInLine( int charPositionInLine ) {
		// TODO Auto-generated method stub
	this.charPositionInLine = charPositionInLine;
	}

	@Override
	public void setStringValue( String stringValue ) {
		// TODO Auto-generated method stub
		this.stringValue = stringValue;
	}

	@Override
	public void setIntValue( int intValue ) {
		// TODO Auto-generated method stub
		this.intValue = intValue;
	}
	
	@Override
	public String toString() {
		return "PLPToken [kind=" + kind + ", text=" + text + ", line=" + line + ", charPositionInLine="
				+ charPositionInLine + ", stringValue=" + stringValue + ", intValue=" + intValue + "]";
	}

}
