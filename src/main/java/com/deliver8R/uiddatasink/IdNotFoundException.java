package com.deliver8R.uiddatasink;

public class IdNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8203076848679683089L;

	public IdNotFoundException(String message)
	{
		super(message);
	}
	
	public IdNotFoundException(String message, Throwable e)
	{
		super(message, e);
	}
}
