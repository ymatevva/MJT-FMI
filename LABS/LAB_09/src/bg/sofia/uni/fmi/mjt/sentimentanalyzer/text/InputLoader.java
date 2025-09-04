package bg.sofia.uni.fmi.mjt.sentimentanalyzer.text;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerInput;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class InputLoader implements Runnable{

    private Queue<Input> inputs;
    private AnalyzerInput currInput;

    public InputLoader(Queue<Input> inputs, AnalyzerInput currInput) {
        this.inputs = inputs;
    }

    public void run ()  {

        String ID = currInput.inputID();
        StringBuffer text = new StringBuffer();

        try(BufferedReader bufferedReader = new BufferedReader(currInput.inputReader())) {
            String currLine;
            while((currLine = bufferedReader.readLine()) != null) {
                text.append(currLine + System.lineSeparator());
            }
        }  catch (IOException e) {
            throw new SentimentAnalysisException ("Error while reading.");
        }

        Input loadedInput = new Input(ID, text.toString());
        inputs.add(loadedInput);
    }




}
