package bg.sofia.uni.fmi.mjt.sentimentanalyzer.text;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentScore;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.tokenizer.TextTokenizer;

import java.util.List;
import java.util.Map;
import java.util.Queue;

public class InputCalculator implements  Runnable {

    private Queue<Input> inputs;
    private Map<String, SentimentScore> scores;
    private TextTokenizer textTokenizer;
    private Map<String, SentimentScore> sentimentLexicon;

    private InputCalculator (Queue<Input> inputs, Map<String, SentimentScore> scores, TextTokenizer textTokenizer,  Map<String, SentimentScore> sentimentLexicon) {
        this.inputs = inputs;
        this.scores = scores;
        this.textTokenizer = textTokenizer;
        this.sentimentLexicon = sentimentLexicon;
    }

    public void run() {
        Input currInput = inputs.poll();
        String currID = currInput.ID();
        List<String> currText = textTokenizer.tokenize(currInput.input());

        int score = currText.stream()
                .mapToInt( word -> sentimentLexicon.get(word).getScore())
                    .sum();

        scores.put(currID, SentimentScore.fromScore(score));
    }
}
