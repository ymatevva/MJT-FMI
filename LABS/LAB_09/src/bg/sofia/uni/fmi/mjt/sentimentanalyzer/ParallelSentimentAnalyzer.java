package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerInput;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentAnalyzerAPI;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentScore;
import java.io.Reader;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;
import java.util.Map;
import java.util.Set;


public class ParallelSentimentAnalyzer implements SentimentAnalyzerAPI {



    /**
     * @param workersCount     number of consumer workers
     * @param stopWords        set containing stop words
     * @param sentimentLexicon map containing the sentiment lexicon, where the key is the word and the value is the sentiment score
     */
    public ParallelSentimentAnalyzer(int workersCount, Set<String> stopWords,
                                     Map<String, SentimentScore> sentimentLexicon) {
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
     *                                                                       {@code
     *                                                                       AnalyzerInput input1 = new AnalyzerInput("doc1", new StringReader("I love java"));
     *                                                                       AnalyzerInput input2 = new AnalyzerInput("doc2", new StringReader("I hate bugs"));
     *
     *                                                                       ParallelSentimentAnalyzer analyzer = new ParallelSentimentAnalyzer(4, stopWordsReader, lexiconReader);
     *                                                                       Map<String, SentimentScore> results = analyzer.analyze(input1, input2);
     *
     *                                                                       // Example output:
     *                                                                       // {
     *                                                                       //   "doc1": MODERATELY_POSITIVE,
     *                                                                       //   "doc2": SLIGHTLY_NEGATIVE
     *                                                                       // }
     *                                                                       }
     *                                                                       </pre>
     */

    @Override
    public Map<String, SentimentScore> analyze(AnalyzerInput... input) {
        return Map.of();
    }
}