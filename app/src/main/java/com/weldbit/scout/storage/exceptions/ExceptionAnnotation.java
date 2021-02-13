package com.weldbit.scout.storage.exceptions;

public class ExceptionAnnotation extends Exception {

    private String errorCode;

    /**
     *
     */
    private static final long serialVersionUID = 1943940425208340691L;
    public ExceptionAnnotation() {
        super();
    }

    public ExceptionAnnotation(String _errorCode, String message) {
        super(message);
        this.errorCode = _errorCode;
    }

    
}
