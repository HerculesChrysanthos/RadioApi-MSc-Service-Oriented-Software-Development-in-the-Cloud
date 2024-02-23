package gr.aueb.radio.content.common;

public class ExternalServiceException extends RuntimeException{

    public final int statusCode;

    public ExternalServiceException(){
        super();
        this.statusCode = 424;
    }
    public ExternalServiceException(String message){
        super(message);
        this.statusCode = 424;
    }

    public ExternalServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
