package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        Set<String> stopWords = new HashSet<>();
        Path path = Path.of("stopwords (1).txt");

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String currWord;
            while ((currWord = reader.readLine()) != null) {
                stopWords.add(currWord);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while reading stopwords.");
        }

        Map<String, SentimentScore> lexicon = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(Path.of("AFINN-111.txt"))) {
            String currSentence;

            while ((currSentence = reader.readLine()) != null) {
                String[] tokens = currSentence.split("\t");
                lexicon.put(tokens[0], SentimentScore.fromScore(Integer.parseInt(tokens[1])));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while reading lexicon.");
        }

        AnalyzerInput input1 = new AnalyzerInput("doc1", new StringReader("I love java"));

        AnalyzerInput input2 = new AnalyzerInput("doc2", new StringReader("I hate bugs"));

        ParallelSentimentAnalyzer analyzer = new ParallelSentimentAnalyzer(4, stopWords, lexicon);
        Map<String, SentimentScore> results = analyzer.analyze(input1, input2);

        System.out.println("here");
        for (var res : results.entrySet()) {
            System.out.printf("ID: %s, SCORE: %s%n", res.getKey(), res.getValue().getDescription());
        }

    }
}
