package edu.ufl.cise.plpfa21.assignment2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.ufl.cise.plpfa21.assignment1.CompilerComponentFactory;


class ExampleParserTests {

	static boolean VERBOSE = true;

	void noErrorParse(String input)  {
		IPLPParser parser = CompilerComponentFactory.getParserForAssignment2(input);
		try {
			parser.parse();
		} catch (Throwable e) {
			throw new RuntimeException(e); 
		}
	}


	private void syntaxErrorParse(String input, int line, int column) {
		IPLPParser parser = CompilerComponentFactory.getParserForAssignment2(input);
		assertThrows(SyntaxException.class, () -> {
			try {
				parser.parse();
			}
			catch(SyntaxException e) {
				if (VERBOSE) System.out.println(input + '\n' + e.getMessage());
				Assertions.assertEquals(line, e.line);
				Assertions.assertEquals(column, e.column);
				throw e;
			}
		});

	}


	

	@Test public void test0() {
		String input = """

				""";
		noErrorParse(input);
	}


	@Test public void test1() {
		String input = """
				VAL a: INT = 0;
				""";
		noErrorParse(input);
	}


	@Test public void test2() {
		String input = """
				VAL a: STRING = "hello";
				""";
		noErrorParse(input);
	}


	@Test public void test3() {
		String input = """
				VAL b: BOOLEAN = TRUE;
				""";
		noErrorParse(input);
	}


	@Test public void test4() {
		String input = """
				VAR b: LIST[];
				""";
		noErrorParse(input);
	}

	//This input has a syntax error at line 2, position 19.
	@Test public void test5()  {
		String input = """
				FUN func() DO
				WHILE x>0 DO x=x-1 END
				END
				""";
		syntaxErrorParse(input,2,19);
	}
	
	

	@Test public void test6()  {
		String input = """
				VAR c = f();
				""";
		noErrorParse(input);
	}
	
	
	@Test public void test7()  {
		String input = """
				VAL d = ((a+b)/(c+f()));
				""";
		noErrorParse(input);
	}
	

	@Test public void test8()  {
		String input = """
				FUN g():INT DO RETURN 1; END
				FUN f()
				DO
				   RETURN g();
				END
				""";
		noErrorParse(input);
	}
	
	@Test public void test9()  {
		String input = """
				FUN f()
					DO RETURN a[1];
				END
				""";
		noErrorParse(input);
	}
	
	
	
	@Test public void test10()  {
		String input = """
				FUN func() DO
				SWITCH x
				CASE 0 : y=0;
				CASE 1 : y=1;
				CASE 2 : y=2;
				DEFAULT y=3;
				END
				END
				""";
		noErrorParse(input);
	}
	
	@Test public void test11()  {
		String input = """
				FUN func() DO
				IF x==0 DO x = 1; END
				END
				""";
		noErrorParse(input);
	}
	
	
	
	@Test public void test12()  {
		String input = """
				FUN f()
				DO
				   RETURN;
				END
				""";
		syntaxErrorParse(input, 3, 9);
	}
	
}
