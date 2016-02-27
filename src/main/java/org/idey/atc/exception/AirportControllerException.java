package org.idey.atc.exception;

/**
 * @author indranildey
 * Custom Checked Exception class extends {@link Exception}
 */
public class AirportControllerException extends Exception{
    public AirportControllerException(Throwable cause) {
        super(cause);
    }
    public AirportControllerException(String message) {
        super(message);
    }
}
