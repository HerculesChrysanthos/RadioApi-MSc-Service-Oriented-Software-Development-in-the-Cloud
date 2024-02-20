package gr.aueb.radio.broadcast.common;

@SuppressWarnings("serial")
public class RadioException extends RuntimeException {

    private int statusCode;

    public RadioException(){}

    public RadioException(String message) {
        super(message);
    }
    public RadioException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}