package io.nondev.calc;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Parser {
	private final Map<Character, Token> tokens = new HashMap<>();
	private final char decimalSeparator;
	
	public Parser() {
		DecimalFormat format= (DecimalFormat)DecimalFormat.getInstance();
		DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
		decimalSeparator =symbols.getDecimalSeparator();
	}
	
	public void addOperator(Token token) {
		tokens.put(token.getCharacter(), token);
	}
	
	public Number parse(String input) throws CalcException {
		String str = input.replaceAll("[\t\n ]", "").concat("=");
		char chars[] = str.toCharArray();
		StringBuilder number = new StringBuilder();
		Deque<Double> numbers = new ArrayDeque<>();
		Deque<Character> operators = new ArrayDeque<>();
		
		for (int i = 0; i < chars.length ; i++) {
			char cur = chars[i];

			if (Character.isDigit(cur) || cur == decimalSeparator) {
				number.append(cur);
				
				if (i == chars.length - 1) {
					numbers.push(parseDouble(input, i, number));
					number.setLength(0);
				}
			} else if (isToken(cur)) {
				if (i == 0) {
					throw new CalcException(input, Messages.ERR_OP_START, i);
				} else if ((i == chars.length - 2) && cur != '=') {
					throw new CalcException(input, Messages.ERR_OP_END, i);
				}
				
				numbers.push(parseDouble(input, i, number));
				operators.push(cur);
				number.setLength(0);
			} else {
				throw new CalcException(input, Messages.ERR_CHAR, i);
			}
			
			resolve(input, i, numbers, operators);
		}
		
		return numbers.pop();
	}
	
	private void resolve(String input, int position, Deque<Double> numbers, Deque<Character> operators) throws CalcException {
		while (operators.size() >= 2) {
			Token next = tokens.get(operators.pop());
			Token cur = tokens.get(operators.pop());
			
			if (next.getPriority() > cur.getPriority()) {
				operators.push(cur.getCharacter());
				operators.push(next.getCharacter());
				break;
			}
			
			double right = numbers.pop();
			double left = numbers.pop();
			double result = cur.process(left, right);
			
			if (Double.isInfinite(result)) {
				throw new CalcException(input, Messages.ERR_INFINITY, position - 1);
			}
			
			numbers.push(result);
			operators.push(next.getCharacter());
		}
	}
	
	private double parseDouble(String input, int position, StringBuilder number) throws CalcException {
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition parsePosition = new ParsePosition(0);
		String str = number.toString();
		Number result = formatter.parse(str, parsePosition);
		
		if (parsePosition.getIndex() != str.length()) {
			throw new CalcException(input, Messages.ERR_PARSE, position - 1);
		}
		
		return result.doubleValue();
	}
	
	private boolean isToken(char character) {
		return tokens.containsKey(character);
	}
}