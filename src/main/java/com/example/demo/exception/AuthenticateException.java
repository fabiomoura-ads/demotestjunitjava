package com.example.demo.exception;

public class AuthenticateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthenticateException(String mensagem) {
		super(mensagem);
	}
}
