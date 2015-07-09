package io.nondev.calc;

import static java.lang.System.in;
import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Calc {
	private final Parser parser = new Parser();
	
	public Calc() {
		parser.addOperator(new Token('^', 5) {
			public double process(double left, double right) {
				return Math.pow(left, right);
			}
		});
		
		parser.addOperator(new Token('%', 4) {
			public double process(double left, double right) {
				return left % right;
			}
		});
		
		parser.addOperator(new Token('*', 4) {
			public double process(double left, double right) {
				return left * right;
			}
		});
		
		parser.addOperator(new Token('/', 4) {
			public double process(double left, double right) {
				return left / right;
			}
		});
		
		parser.addOperator(new Token('+', 3) {
			public double process(double left, double right) {
				return left + right;
			}
		});
		
		parser.addOperator(new Token('-', 3) {
			public double process(double left, double right) {
				return left - right;
			}
		});
		
		parser.addOperator(new Token('&', 2) {
			public double process(double left, double right) {
				return (long)left & (long)right;
			}
		});
		
		parser.addOperator(new Token('o', 1) {
			public double process(double left, double right) {
				return (long)left ^ (long)right;
			}
		});
		
		parser.addOperator(new Token('|', 0) {
			public double process(double left, double right) {
				return (long)left | (long)right;
			}
		});
		
		parser.addOperator(new Token('=', Integer.MIN_VALUE) {
			public double process(double left, double right) {
				return 0;
			}
		});
	}
	
	public Number evaluate(String input) throws CalcException {
		return parser.parse(input);
	}

	public static void main(String... args) throws IOException, CalcException {
		Reader reader = new InputStreamReader(in);
		BufferedReader breader = new BufferedReader(reader);
		Calc calc = new Calc();
		String line;
		
		out.println(Messages.WELCOME);
		
		while (true) {
			out.print("> ");
			line = breader.readLine();
			if (line.trim().equals("exit")) break;
			out.println(calc.evaluate(line));
		}
	}
}