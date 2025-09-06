package bg.sofia.uni.fmi.mjt.sentimentanalyzer.input;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentScore;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.tokenizer.TextTokenizer;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class InputCalculator implements Runnable {

    private Queue<Input> inputs;
    private Map<String, SentimentScore> scores;
    private TextTokenizer textTokenizer;
    private Map<String, SentimentScore> sentimentLexicon;
    private AtomicBoolean areAllLoaded;

    public InputCalculator(Queue<Input> inputs, Map<String, SentimentScore> scores, TextTokenizer textTokenizer,
                           AtomicBoolean areAllLoaded, Map<String, SentimentScore> sentimentLexicon) {
        this.inputs = inputs;
        this.scores = scores;
        this.textTokenizer = textTokenizer;
        this.sentimentLexicon = sentimentLexicon;
        this.areAllLoaded = areAllLoaded;
    }

    public void run() {
        while (true) {
            Input currInput;

            synchronized (inputs) {
                while (inputs.isEmpty()) {
                    if (areAllLoaded.get()) {
                        return;
                    }
                    try {
                        inputs.wait();
                    } catch (InterruptedException e) {
                        throw new SentimentAnalysisException(
                            "Consumer thread was interrupted while waiting for a task.");
                    }
                }

                currInput = inputs.poll();
            }

            calculateAndStoreScore(currInput);
        }
    }

    private void calculateAndStoreScore(Input currInput) {
        List<String> currText = textTokenizer.tokenize(currInput.input());

        int scoreSum = currText.stream()
            .mapToInt(word -> sentimentLexicon.getOrDefault(word, SentimentScore.NEUTRAL).getScore())
            .sum();

        synchronized (scores) {
            scores.put(currInput.ID(), SentimentScore.fromScore(scoreSum));
        }
    }

}
