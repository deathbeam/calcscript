package io.nondev.calc;

public abstract class Token {
	private final char character;
	private final int priority;
	
	public Token(char character, int priority) {
		this.character = character;
		this.priority = priority;
	}
	
	public char getCharacter() {
		return character;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public abstract double process(double left, double right);
}