package fr.dauphine.javaavance.phineloops.utils;

public class IncorrectArgumentException extends IllegalArgumentException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public IncorrectArgumentException(String errorMessage) {
        super(errorMessage);
    }
	public IncorrectArgumentException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
