package gr.aueb.radio.broadcast.common;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super();
    }

}
