package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerInput;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentAnalyzerAPI;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentScore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class ParallelSentimentAnalyzer implements SentimentAnalyzerAPI {

    private int workersCount;
    private Set<String> stopWords;
    private Map<String, SentimentScore> sentimentLexicon;
    private final String PUNCTUAL_REGEX = "\\p{Punct}";
    private final String SPACE_REGEX = "\\s+";


    public ParallelSentimentAnalyzer(int workersCount, Set<String> stopWords,
                                     Map<String, SentimentScore> sentimentLexicon) {

        this.sentimentLexicon = sentimentLexicon;
        this.stopWords = stopWords;
        this.workersCount = workersCount;
    }


    /**
     * Analyzes the sentiment of multiple input streams concurrently using a producer-consumer pattern.
     *
     * <p>This method processes each {@link AnalyzerInput} by reading its content in parallel, calculating an
     * overall sentiment score for the text based on the sentiment lexicon and stop words. The results are aggregated
     * into a map where each key is the input's unique identifier (from {@code inputID}) and the value is the computed
     * {@link SentimentScore}.</p>
     *
     * @param input one or more {@link AnalyzerInput} instances, each containing an identifier and a {@link Reader}
     *              for the text to analyze.
     * @return a map where each key is the input's unique ID (as specified by {@code inputID}) and the value is the
     * computed {@link SentimentScore} for the associated text stream.
     * @throws SentimentAnalysisException if an error occurs during input processing or thread execution.
     *
     *                                    <p><b>Example:</b></p>
     *                                    <pre>
     *                                                                                                                                                                                {@code
     *                                                                                                                                                                                AnalyzerInput input1 = new AnalyzerInput("doc1", new StringReader("I love java"));
     *                                                                                                                                                                                AnalyzerInput input2 = new AnalyzerInput("doc2", new StringReader("I hate bugs"));
     *
     *                                                                                                                                                                                ParallelSentimentAnalyzer analyzer = new ParallelSentimentAnalyzer(4, stopWordsReader, lexiconReader);
     *                                                                                                                                                                                Map<String, SentimentScore> results = analyzer.analyze(input1, input2);
     *
     *                                                                                                                                                                                // Example output:
     *                                                                                                                                                                                // {
     *                                                                                                                                                                                //   "doc1": MODERATELY_POSITIVE,
     *                                                                                                                                                                                //   "doc2": SLIGHTLY_NEGATIVE
     *                                                                                                                                                                                // }
     *                                                                                                                                                                                }
     *                                                                                                                                                                                </pre>
     */

    @Override
    public Map<String, SentimentScore> analyze(AnalyzerInput... input) {

        Thread[] threads = new Thread[input.length];

        for (int i = 0; i < input.length; i++) {
            threads = new Thread[] {new Thread()};
        }


        Map<String, SentimentScore> currInput = analyzeInput(input[0]);

        return Map.of();
    }

    // SHOULDNT BE HERE AT ALL
    // TO BE UPDATED
    private Map<String, SentimentScore> analyzeInput(AnalyzerInput input) {
        StringBuilder textInput = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(input.inputReader())) {

            String currLine;
            while ((currLine = bufferedReader.readLine()) != null) {
                textInput.append(currLine + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while reading.");
        }


        String text = textInput.toString();
        text = text.toLowerCase();
        text = text.replaceAll(PUNCTUAL_REGEX, "");
        text = text.replaceAll(SPACE_REGEX, " ");
        List<String> words = Arrays.stream(text.split(" "))
            .filter(word -> !stopWords.contains(word))
            .toList();

        Map<String, SentimentScore> scores = words.stream()
            .collect(Collectors.toMap(
                word -> word,
                word -> sentimentLexicon.get(word),
                (o, n) -> o,
                HashMap::new
            ));
        return scores;
    }
}