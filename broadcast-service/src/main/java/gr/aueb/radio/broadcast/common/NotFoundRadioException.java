package gr.aueb.radio.broadcast.common;

public class NotFoundRadioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotFoundRadioException(String message) {
        super(message);
    }

    public NotFoundRadioException() {
        super();
    }

}
