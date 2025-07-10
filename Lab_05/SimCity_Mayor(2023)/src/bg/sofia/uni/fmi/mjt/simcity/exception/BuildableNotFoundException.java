package bg.sofia.uni.fmi.mjt.simcity.exception;

public class BuildableNotFoundException extends RuntimeException {
    public BuildableNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuildableNotFoundException(String message) {
        super(message);
    }
}
