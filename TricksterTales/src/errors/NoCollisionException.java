package errors;

public class NoCollisionException extends Exception {

    private static final long serialVersionUID = 1L;

    private String msg;

    public NoCollisionException() {
	this.msg = "Could not find collision";
    }

    public NoCollisionException(String msg) {
	this.msg = msg;
    }

    public String getMessage() {
	return msg;
    }

}
