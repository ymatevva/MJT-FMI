package bg.sofia.uni.fmi.mjt.sentimentanalyzer.input;

import java.io.Reader;

public record AnalyzerInput(String inputID, Reader inputReader) {
}