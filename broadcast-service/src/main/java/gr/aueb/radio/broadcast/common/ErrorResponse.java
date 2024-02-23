package gr.aueb.radio.broadcast.common;

public class ErrorResponse {

    private String message;
    private String[] acceptedValues;

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String message, String[] acceptedValues) {
        this.message = message;
        this.acceptedValues = acceptedValues;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getAcceptedValues() {
        return acceptedValues;
    }

    public void setAcceptedValues(String[] acceptedValues) {
        this.acceptedValues = acceptedValues;
    }
}
