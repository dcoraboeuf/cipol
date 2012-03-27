package net.cipol.rule.impl;

public class MessageLength {
	
	public static final String MINLENGTH = "minlength";

	private final int minlength;

	public MessageLength(int minlength) {
		this.minlength = minlength;
	}

	public int getMinlength() {
		return minlength;
	}

}
