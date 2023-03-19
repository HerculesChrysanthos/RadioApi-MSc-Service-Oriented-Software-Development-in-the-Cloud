package gr.aueb.radio.exceptions;

@SuppressWarnings("serial")
public class RadioException extends RuntimeException {
    public RadioException(String message) {
        super(message);
    }
}