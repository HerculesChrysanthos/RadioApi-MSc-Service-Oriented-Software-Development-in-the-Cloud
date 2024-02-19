package gr.aueb.radio.content.common;

@SuppressWarnings("serial")
public class RadioException extends RuntimeException {

    private final int statusCode;
    public RadioException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}