package com.deliver8R.uiddatasink;

public class AuthorizationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6128214638605317117L;

	public AuthorizationException(String message)
	{
		super(message);
	}
	
	public AuthorizationException(String message, Throwable e)
	{
		super(message, e);
	}

}
