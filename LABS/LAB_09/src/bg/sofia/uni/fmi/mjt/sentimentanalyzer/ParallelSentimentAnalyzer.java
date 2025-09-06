package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.input.Input;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.input.InputCalculator;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.input.InputLoader;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.tokenizer.TextTokenizer;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelSentimentAnalyzer implements SentimentAnalyzerAPI {

    private final int workersCount;
    private final Set<String> stopWords;
    private final Map<String, SentimentScore> sentimentLexicon;
    private final TextTokenizer textTokenizer;

    public ParallelSentimentAnalyzer(int workersCount, Set<String> stopWords,
                                     Map<String, SentimentScore> sentimentLexicon) {
        this.workersCount = workersCount;
        this.sentimentLexicon = sentimentLexicon;
        this.stopWords = stopWords;
        textTokenizer = new TextTokenizer(stopWords);
    }

    @Override
    public Map<String, SentimentScore> analyze(AnalyzerInput... input) {

        Queue<Input> inputs = new ArrayDeque<>();
        Map<String, SentimentScore> scores = new HashMap<>();

        AtomicInteger inputLength = new AtomicInteger(input.length);
        AtomicBoolean areAllLoaded = new AtomicBoolean(false);

        Thread[] loaders = new Thread[input.length];
        for (int i = 0; i < input.length; i++) {
            loaders[i] = ( new Thread(new InputLoader(inputs, input[i], areAllLoaded, inputLength)));
            loaders[i].start();
        }

        Thread[] calculators = new Thread[workersCount];
        for (int i = 0; i < workersCount; i++) {
            calculators[i] = (new Thread(new InputCalculator(inputs, scores, textTokenizer, areAllLoaded, sentimentLexicon)));
            calculators[i].start();
        }

        for (var calculator : calculators) {
            try {
                calculator.join();
            } catch (InterruptedException e) {
                throw new SentimentAnalysisException("Interrupted exception thrown while joining consumer threads.");
            }
        }

        return scores;
    }
}