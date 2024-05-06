import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.stream.Collectors;
import java.lang.StringBuilder;

// 1st case when no suffix and char does not match
// 2nd case when char doesnt match but there is a matched char in pattern
// check if either no suffix then value is position of char
// or suffix matches a pttern in string then move to position
// 3rd case when char matches
public class MakeBMTable {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: java MakeBMTable [pattern] outputFile");
      System.exit(1);
    }

    String pattern = args[0];
    String outputFile = args[1];
    try {
      File file = new File(outputFile);
      PrintWriter writer = new PrintWriter(file, "UTF-8");
      // write the first row
      writer.print('*');
      for (char c : pattern.toCharArray()) {
        writer.print(',');
        writer.print(c);
      }

      // get 1st column of characters in distinct and sorted order using java streams
      ArrayList<Character> uniqueChars = pattern.chars().distinct().sorted().mapToObj(c -> (char) c)
          .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
      uniqueChars.add('*');

      Deque<Integer> row = new ArrayDeque<>();
      StringBuilder suffixValues = new StringBuilder();
      // for every row in column
      for (int i = 0; i < uniqueChars.size(); i++) {
        for (int j = pattern.length() - 1; j >= 0; j--) {
          // check if char in row matches column
          if (uniqueChars.get(i) != pattern.charAt(j)) {
            // if there is no suffix present (1st char in row being compared)
            if (j == pattern.length() - 1) {
              // then check if there is another instance of char in pattern that matches the
              // char in row
              if (pattern.contains(String.valueOf(uniqueChars.get(i)))) {
                // insert into row the skip value
                int skipValue = j - (pattern.lastIndexOf(uniqueChars.get(i)));
                row.push(skipValue);
              } else {
                // this means there is no suffix and no char in row matches, so input length of
                // pattern
                row.push(pattern.length());
              }
            } else {
              // there is a suffix, so check if there is a pattern in suffix that matches
              // pattern in row
              // if not, then output length of pattern

              StringBuilder patternToBeMatched = new StringBuilder(suffixValues);
              patternToBeMatched.insert(0, uniqueChars.get(i));
              
              for (int k = pattern.length(); k > 0; k--) {
                // check if suffix matches the pattern at index
                if (pattern.substring(k - patternToBeMatched.length(), k).equals(patternToBeMatched.toString())) {
                  //if so, shift value is difference between pattern length and k(the count of right shifts made)
                  int shiftValue = pattern.length() - k;
                  row.push(shiftValue);
                  break;
                }
                // remove first char from patternToBeMatched if shift exceeds the 1st index of
                // pattern
                if ((k - patternToBeMatched.length()) == 0) {
                  patternToBeMatched.deleteCharAt(0);
                }
              }

              // check if matchedPrefix is empty, if so, no suffix matched
              if (patternToBeMatched.length() == 0) {
                row.push(pattern.length());
                System.out.println("inserted: " + pattern.length());
              }

            }

          } else {
            // pattern matches so input 0
            row.push(0);
          }

          suffixValues.insert(0, pattern.charAt(j));
          System.out.println(uniqueChars.get(i) + ": " + suffixValues);

        }
        // row is Int values
        writer.println();
        writer.print(uniqueChars.get(i) + ",");
        writer.print(row.stream().map(Object::toString).collect(Collectors.joining(",")));
        row.clear();
        suffixValues.setLength(0);

      }
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
