package com.deliver8R.uiddatasink;

public class IdAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3007777401246013180L;

	public IdAlreadyExistsException(String message)
	{
		super(message);
	}
	
	public IdAlreadyExistsException(String message, Throwable e)
	{
		super(message, e);
	}
}
