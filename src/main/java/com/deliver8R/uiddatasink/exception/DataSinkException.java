package com.deliver8R.uiddatasink.exception;

public class DataSinkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6950568881578624837L;
	
	public DataSinkException(String message)
	{
		super(message);
	}
	
	public DataSinkException(String message, Throwable e)
	{
		super(message, e);
	}

}
