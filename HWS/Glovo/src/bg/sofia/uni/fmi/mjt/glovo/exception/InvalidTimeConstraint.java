package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidTimeConstraint extends  RuntimeException {
    public InvalidTimeConstraint(String message) {
        super(message);
    }

    public InvalidTimeConstraint(String message, Throwable cause) {
        super(message, cause);
    }
}
