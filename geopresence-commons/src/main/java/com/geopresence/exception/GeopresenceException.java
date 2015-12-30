package com.geopresence.exception;

public class GeopresenceException extends Exception {

	private static final long serialVersionUID = 3912381209187154314L;	

	public GeopresenceException(String msg) {
		super(msg);
	}
	
	public GeopresenceException(String msg, Exception cause){
		super(msg, cause);
	}

}
