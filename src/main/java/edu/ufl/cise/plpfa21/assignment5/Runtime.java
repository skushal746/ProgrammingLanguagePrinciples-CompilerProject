package edu.ufl.cise.plpfa21.assignment5;

public class Runtime {

	/*
	 * Operation for binary expression integer type 
	 */
	public static int add(int a, int b)
	{
		return a + b;
	}
	
	public static int minus(int a, int b)
	{
		return a - b;
	}
	
	public static boolean equals(int a, int b)
	{
		return a==b;
	}
	
	public static boolean notEquals(int a, int b)
	{
		return a!=b;
	}
	
	public static int times(int a, int b)
	{
		return a*b;
	}
	
	public static int divide(int a, int b)
	{
		return a/b;
	}
	
	public static boolean lessThan(int a, int b)
	{
		return a<b;
	}
	
	public static boolean greaterThan(int a, int b)
	{
		return a>b;
	}
	
	
	/*
	 * Operation for binary expression String type 
	 */
	public static String concat(String a, String b)
	{
		return a + b;
	}
	
	public static boolean equals(String a, String b)
	{
		return a.equals(b);
	}
	
	public static boolean notEquals(String a, String b)
	{
		return !a.equals(b);
	}
	
	public static boolean lessThan(String a, String b)
	{
		if( a.compareTo(b) < 0)
			return true;
		else return false;
	}
	
	public static boolean greaterThan(String a, String b)
	{
		if( a.compareTo(b) > 0)
			return true;
		else return false;
	}
	
	/*
	 * Operation for binary expression Boolean type 
	 */
	
	public static boolean equals(boolean a, boolean b)
	{
		return a==b;
	}
	
	public static boolean notEquals(boolean a, boolean b)
	{
		return a != b;
	}
	
	public static boolean and(boolean a, boolean b)
	{
		return a && b;
	}
	
	public static boolean or(boolean a, boolean b)
	{
		return a || b;
	}
	
	
	/*
	 * Following methods are for unary
	 */
	public static boolean not(boolean arg) {
		return !arg;
	}
	
	public static int minus(int arg) {
		return -arg;
	}
}
