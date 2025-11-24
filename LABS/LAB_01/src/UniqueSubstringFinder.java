import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UniqueSubstringFinder {

    public final static int LETTERS_COUNT = 26;
    public final static int ONE_LETTER = 1;
    public final static int ZERO_LETTERS = 0;
    public final static String EMPTY_STRING = "";
    public final static char FIRST_LOWER_LETTER = 'a';
    public final static int BEGIN_INDEX = 0;

    public static String longestUniqueSubstring(String s) {

        if (s.length() == ZERO_LETTERS || s.length() == ONE_LETTER) {
            return s;
        }

        String result = EMPTY_STRING;
        int[] occurrences = new int[LETTERS_COUNT];
        int maxLen = Integer.MIN_VALUE;
        int begin = BEGIN_INDEX;

        for (int end = 0; end < s.length(); end++) {

            if (occurrences[s.charAt(end) - FIRST_LOWER_LETTER] == 0) {
                occurrences[s.charAt(end) - FIRST_LOWER_LETTER]++;
            }

            else {

                if(end - begin > maxLen) {
                    maxLen = end - begin;
                    result = s.substring(begin, end);
                }

                while(s.charAt(begin) != s.charAt(end)) {
                    occurrences[s.charAt(begin) - FIRST_LOWER_LETTER]--;
                    begin++;
                }

                begin++;
            }

        }
        result = s.length() - begin > maxLen ? s.substring(begin, s.length()) : result;
        return result;
    }

    static void main() {
        System.out.println(longestUniqueSubstring("abcdabcbb"));
        System.out.println(longestUniqueSubstring("bbbbb"));
        System.out.println(longestUniqueSubstring("wke"));
        System.out.println(longestUniqueSubstring("abcdefg"));
        System.out.println(longestUniqueSubstring("x"));
        System.out.println(longestUniqueSubstring(""));
        System.out.println(longestUniqueSubstring("absdaabsdf"));
        System.out.println(longestUniqueSubstring("hhhhjklo"));
    }
}
