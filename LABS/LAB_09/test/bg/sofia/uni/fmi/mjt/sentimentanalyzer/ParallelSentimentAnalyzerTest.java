import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ParallelSentimentAnalyzerTest {

    private Set<String> stopwords = Set.of("a", "of", "and", "or", "if");
    private int workersCount = 4;
    private Map<String, SentimentScore> scores = Map.of(
        "love", SentimentScore.MODERATELY_POSITIVE,
        "sad", SentimentScore.MODERATELY_NEGATIVE,
        "cat", SentimentScore.NEUTRAL,
        "super", SentimentScore.POSITIVE,
        "tired", SentimentScore.SLIGHTLY_NEGATIVE,
        "awful", SentimentScore.NEGATIVE,
        "perfect", SentimentScore.VERY_POSITIVE);



    @Test
    void testAnalyzeSentiment() {
        ParallelSentimentAnalyzer analyzer = new ParallelSentimentAnalyzer(workersCount, stopwords, scores);
        AnalyzerInput input1 = Mockito.mock(AnalyzerInput.class);
        AnalyzerInput input2 = Mockito.mock(AnalyzerInput.class);

        when(input1.inputID()).thenReturn("doc1");
        when(input2.inputID()).thenReturn("doc2");

        when(input1.inputReader()).thenReturn(new StringReader("If she is tired the day is awful and sad."));
        when(input2.inputReader()).thenReturn(new StringReader("The cats are super tired and love to sleep."));

        Map<String, SentimentScore> analyzedDocs = analyzer.analyze(input1, input2);

        assertEquals(2, analyzedDocs.size(), "Method should return the same count of pairs as the input size");
        assertEquals(SentimentScore.VERY_NEGATIVE, analyzedDocs.get(input1.inputID()),
            "Method should correctly calculate the sentiment score.");
        assertEquals(SentimentScore.VERY_POSITIVE, analyzedDocs.get(input2.inputID()),
            "Method should correctly calculate the sentiment score.");
    }
}
