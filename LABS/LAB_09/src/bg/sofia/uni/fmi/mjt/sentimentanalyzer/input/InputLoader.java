package bg.sofia.uni.fmi.mjt.sentimentanalyzer.input;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class InputLoader implements Runnable {

    private final Queue<Input> inputs;
    private final AnalyzerInput currInput;
    private final AtomicInteger remainingLoaders;
    private final AtomicBoolean areAllLoaded;

    public InputLoader(Queue<Input> inputs,
                       AnalyzerInput currInput,
                       AtomicBoolean areAllLoaded,
                       AtomicInteger remainingLoaders) {

        this.inputs = inputs;
        this.currInput = currInput;
        this.areAllLoaded = areAllLoaded;
        this.remainingLoaders = remainingLoaders;
    }

    @Override
    public void run() {
        String id = currInput.inputID();
        StringBuilder text = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(currInput.inputReader())) {
            String currLine;
            while ((currLine = bufferedReader.readLine()) != null) {
                text.append(currLine).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new SentimentAnalysisException("IOException thrown while reading the content of the document.");
        }

        Input loadedInput = new Input(id, text.toString());

        synchronized (inputs) {
            inputs.offer(loadedInput);
            inputs.notifyAll();
        }

        if (remainingLoaders.decrementAndGet() == 0) {
            areAllLoaded.set(true);
            synchronized (inputs) {
                inputs.notifyAll();
            }
        }
    }
}
