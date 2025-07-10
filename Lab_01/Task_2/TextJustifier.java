package HW_1.Task_2;

import java.util.Arrays;

public class TextJustifier {


     private static String createLine( String[] words, int ind, int spacesLeft, int wordsOnLine) {
       
        StringBuilder currLine = new StringBuilder();
        int evenSpaces = spacesLeft / (wordsOnLine - 1);
        int extraSpaces = spacesLeft % (wordsOnLine - 1);


        for (int i = ind - wordsOnLine; i < ind; i++) {
            
            currLine.append(words[i]);
 
            if(i < ind-1 ) {
             for (int j = 0; j < evenSpaces; j++) {
                 currLine.append(" ");
             }
             if(extraSpaces > 0){
                currLine.append(" ");
                extraSpaces--;
             }
            } 
         }

         return currLine.toString();
     }

     private static String createLastLine( String[] words, int ind, int maxLength, int wordsOnLine) {
    
        StringBuilder currLine = new StringBuilder();
        
        for (int i = ind - wordsOnLine; i < ind; i++) {
            
            currLine.append(words[i]);
            if(i < ind-1) {
                currLine.append(" ");
            }
         }
          
        for (int j = 0; j < maxLength - currLine.length() ; j++) {                 
            currLine.append(" ");
        }

        return currLine.toString();
     }


    public static String[] justifyText(String[] words, int maxLength){

     String result = "";

     for (int i = 0; i < words.length;) {
        
        String currLine ="";
        int countSymbolsOnLine = 0;
        int countWordsOnLine = 0;

        while (true) {
           
            if(i == words.length || !(countSymbolsOnLine + words[i].length() + countWordsOnLine - 1 <= maxLength)){
                break;
            }
            else{
                countSymbolsOnLine += words[i].length();
                countWordsOnLine++;
            } 
            i++;
        }

        int spacesLeft = maxLength - countSymbolsOnLine;

        if(i == words.length) {
            currLine = createLastLine(words, i, maxLength, countWordsOnLine);
        }
        else {
            currLine = createLine(words, i, spacesLeft, countWordsOnLine) ;
        }
        result+=currLine + '\n';
     }
     return result.split("\n");
    }

   
}
