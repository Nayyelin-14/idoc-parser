package com.nayyelin.idocparser.core;

/**
 * Custom exception for your service.
 * <p>
 * This exception provides structured error reporting with error codes
 * for better error handling and diagnostics.
 * </p>
 * <p>
 * Usage Example:
 * </p>
 * <pre>{@code
 * throw new ParserServiceException("INVALID_INPUT", "Input cannot be empty");
 * }</pre>
 *
 * @see ParserService
 */
public class ParserServiceException extends Exception {

    /** The error code identifying the type of error. */
    private final String errorCode;

    /**
     * Constructs a new ParserServiceException with the specified error code and message.
     *
     * @param errorCode a unique error code identifying the error type
     * @param message   the detail message explaining the error
     */
    public ParserServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new ParserServiceException with the specified error code, message, and cause.
     *
     * @param errorCode a unique error code identifying the error type
     * @param message   the detail message explaining the error
     * @param cause     the cause of this exception
     */
    public ParserServiceException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Gets the error code associated with this exception.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
