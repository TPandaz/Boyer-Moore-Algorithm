import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.stream.Collectors;

/**
 * This program computes the boyer moore skip array by calculating the shift
 * values for every character in the String. It evaluates the following using
 * these scenarios:
 * 1. when character does not match, check if there is a similar character in pattern
 * 1.a. similar pattern is found, output number of shifts to position of pattern
 * 1.b. if no similar pattern, then output character length
 * 2. when character matches, output 0
 * 
 * Code used for java streams referenced from:
 * https://stackoverflow.com/questions/54671799/how-to-sort-and-eliminate-the-duplicates-from-arraylist/54671928
 * 
 * @author: Samuel Tan
 * Student ID: 1617175, 1617266
 * Date: 
 */
public class MakeBMTable {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java MakeBMTable <pattern> <outputFile>");
            System.exit(1);
        }

        // place arguments into variables
        String pattern = args[0];
        String outputFile = args[1];
        try {
            File file = new File(outputFile);
            PrintWriter writer = new PrintWriter(file, "UTF-8");

            // write the first row to outputFile
            writer.print('*');
            for (char c : pattern.toCharArray()) {
                writer.print(',');
                writer.print(c);
            }

            // get 1st column of characters in distinct and sorted order using java stream
            ArrayList<Character> uniqueChars = (ArrayList<Character>) pattern.chars().distinct().sorted()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.toList());
            uniqueChars.add('*');

            // deque for row used as we are inserting to the front of the queue
            Deque<Integer> row = new ArrayDeque<>();
            StringBuilder suffixValues = new StringBuilder();

            // for every row in column
            for (int i = 0; i < uniqueChars.size(); i++) {
                for (int j = pattern.length() - 1; j >= 0; j--) {
                    // check if character in row does not matches
                    if (uniqueChars.get(i) != pattern.charAt(j)) {
                        // check if there is a pattern/character in row that matches the character(and the suffix if it has one)
                        StringBuilder patternToBeMatched = new StringBuilder(suffixValues);
                        patternToBeMatched.insert(0, uniqueChars.get(i)); // add the char

                        // for every iteration, shift suffix to the left, comparing it with the pattern
                        for (int k = pattern.length(); k > 0; k--) {
                            // check if suffix matches the pattern at index k
                            if (pattern.substring(k - patternToBeMatched.length(), k)
                                    .equals(patternToBeMatched.toString())) {
                                // if so, the shiftValue is the difference between pattern length and k(count of left shifts made)
                                row.push(pattern.length() - k);
                                break;
                            }
                            // check if patternToBeMatched is at index 0 of pattern(number of leftshifts)
                            if ((k - patternToBeMatched.length()) == 0) {
                                // remove the 1st character for next iteration
                                patternToBeMatched.deleteCharAt(0);
                            }
                        }

                        // check if there is no pattern matching, input pattern length if so
                        if (patternToBeMatched.length() == 0) {
                            row.push(pattern.length());
                        }

                    } else {
                        // character matches so input 0
                        row.push(0);
                    }

                    // insert the character into suffix
                    suffixValues.insert(0, pattern.charAt(j));
                }
                writer.println();
                writer.print(uniqueChars.get(i) + ",");
                // using java streams, print formatted row
                writer.print(row.stream().map(Object::toString).collect(Collectors.joining(",")));
                row.clear();
                // clear suffix values
                suffixValues.setLength(0);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
