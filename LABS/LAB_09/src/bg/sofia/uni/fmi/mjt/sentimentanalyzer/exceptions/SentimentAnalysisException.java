package bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions;

public class SentimentAnalysisException extends RuntimeException {
    public SentimentAnalysisException(String message) {
        super(message);
    }

    public SentimentAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
