package errors;

public class LevelLoadFailed extends Exception {

    private static final long serialVersionUID = 1L;

    private String err;

    public LevelLoadFailed() {
	this.err = "Level Load Failed";
    }

    public LevelLoadFailed(String err) {
	this.err = err;
    }

    public String getMessage() {
	return err;
    }

}
