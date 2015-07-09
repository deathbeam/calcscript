package io.nondev.calc;

public class CalcException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private static String formatMessage(String input, String message, int position) {
		StringBuilder pos = new StringBuilder();
		
		for(int i = 0; i < position; i++) {
			pos.append(" ");
		}
		
		pos.append("^");
		
		return String.format("%n%s%n%s%n%s", input.replace(" ", ""), pos.toString(), message);
	}

	public CalcException(String input, String message, int position) {
		super(formatMessage(input, message, position));
	}
}