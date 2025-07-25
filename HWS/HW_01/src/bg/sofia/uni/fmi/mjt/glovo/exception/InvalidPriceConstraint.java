package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidPriceConstraint extends RuntimeException {
    public InvalidPriceConstraint(String message) {
        super(message);
    }

    public InvalidPriceConstraint(String message, Throwable cause) {
        super(message, cause);
    }
}
