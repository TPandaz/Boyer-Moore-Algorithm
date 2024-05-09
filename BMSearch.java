import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**Searches for a string pattern in a text file and outputs each line that the string occurs on to the standard output
 * @author Dolf ten Have
 * SID: 1617266
 * Date:
 */
public class BMSearch {
    private static String[] pattern; //The skip array.
    private static String line;
    private static Map<String, int[]> skipArray;

    public static void main(String[] args){
        String usage = "java BMSearch table.txt [textfile].txt";
        File table; //The file containing the skip array.
        File text; //The text file the string will be searched for in.

        //Checks for a valid number of inputs
        if(args.length != 2){
            System.err.println("Error: The parameters did not match the propor class usage.\nusage: '" + usage + "'.");
            System.exit(1);
        }

        //Gets the table and text files
        table = trySetFile(args[0]);
        text = trySetFile(args[1]);  
        parseSkipTable(table); 
        searchFile(text); 
    }

    /**Attempts to load a file from the String path 
     * @param path the location of the file
     * @return a file
     */ 
    private static File trySetFile(String path){
        File file = null;
        try{
            file = new File(path);
        }catch(Exception e){
            System.err.println("Error: there was a problem with the file '" + path + "'.");
            e.printStackTrace(System.err);
            System.exit(1);
        }
        return file;
    }


    /**Reads parses the skip table file into the pattern and creates a skipArray hashMap
     * @param skipFile the file containing the skip array
     */
    private static void parseSkipTable(File skipFile){
        skipArray = new HashMap<String,int[]>();
        String[] line;
        int[] skipArrayLine;
        try{
            Scanner sc = new Scanner(skipFile);
            pattern = sc.nextLine().split(","); //Gets pattern minus the first character
            printPattern();
            //Creates a new line in the hashmap for each line in the skip array file
            while(sc.hasNext()){
                line = sc.nextLine().split(",");
                skipArrayLine = new int[line.length - 1];
                for(int i = 1; i < line.length; i++){
                    skipArrayLine[i - 1] = Integer.parseInt(line[i]);
                }
                skipArray.put(line[0], skipArrayLine);
            }
            sc.close();
        }catch(Exception e){
            System.err.println("Error: There was a problem creating the skip array");
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    /**Tries passes each line of the file to the findPattern method
     * @param file The file to search for the pattern in
     */
    private static void searchFile(File file){   
        try{
            Scanner sc = new Scanner(file);
            while(sc.hasNext()){
                line = sc.nextLine();
                findPattern(0);
            }
            sc.close();
        }catch(Exception e){
            System.err.println("Error: There was a problem finding the pattern");
            e.printStackTrace(System.err);
        }
    }

    /**
     * A recursive function that will try to find a pattern match in the line or reach the end of the line with no match
     * @param line the line of text 
     * @param index the current pointer index | should be the last index of the pattern on initialization
     */
    private static void findPattern(int index){
        //If the end of the line is reaches, return
        if(index >= (line.length() - 1) - (pattern.length - 2))
            return;
        for(int i = pattern.length - 2; i >= 0; i--){
            String s = line.substring(index + i, index + i + 1);
            //System.out.printf("Comparing %s and %s", s, pattern[i + 1]);
            if(pattern[i + 1].compareTo(s) != 0){
                //int skipN = skipArray.get(s) == null ? skipArray.get(pattern[0])[i - 1] : skipArray.get(s)[i - 1];
                findPattern(index += skipArray.get(s) == null ? skipArray.get(pattern[0])[i] : skipArray.get(s)[i]);
                return;
            }   
        }
        System.out.println(line);
    }

    /**
     * Checks if the strings match
     * @param line the line of text
     * @param patternIndex the current index of the pattern
     * @param index the index of the line
     * @return True if the strings match
     */
    private static boolean tryMatch(int patternIndex, int index){
        if(pattern[patternIndex].compareTo(line.substring(index, index)) == 0){
            return true;
        }
        return false;  
    }

    /**
     * Prints the pattern to look for to the standard output
     */
    private static void printPattern(){
        System.out.print("Searching for '");
        for(int i = 0; i < pattern.length; i++){
            System.out.print(pattern[i]);
        }
        System.out.println("'");
    }


    // /**
    //  * Creates a skip array from a file
    //  * @param skipFile the file containing the skip array
    //  * @return a skip array
    //  */
    // private static String[][] OldmakeSkipArray(File skipFile){
    //     List<String[]> skipArrayList = new ArrayList<String[]>();
    //     //Splits each line into an array and adds that to the array list
    //     try{
    //         Scanner sc = new Scanner(skipFile);
    //         while(sc.hasNext()){
    //             skipArrayList.add(sc.nextLine().split(","));
    //         }
    //     }catch(Exception e){
    //         System.err.println("Error: There was a problem creating the skip array");
    //         e.printStackTrace(System.err);
    //         System.exit(1);
    //     }
    //     //Copys the array list into a 2D array
    //     int rowLength = skipArrayList.get(0).length;
    //     int numRows = skipArrayList.size();
    //     skipArray = new String[rowLength][numRows];
    //     for(int i = 0; i < numRows; i++){
    //         for(int j = 0; j < rowLength; j++){
    //             skipArray[j][i] = skipArrayList.get(i)[j];
    //         }
    //     }
    //     return skipArray;
    // }

    // /**
    //  * Reads each line of the text into a string
    //  * @param text the text file
    //  */
    // private static void readText(File text){
    //     String line;
    //     try{
    //         Scanner sc = new Scanner(text);
    //         while(sc.hasNext()){
    //             line = sc.nextLine();
    //             //Checks that the line is at least the length of the pattern
    //             if(line.length() > skipArray.length)
    //                 findPattern(line, skipArray.length);
    //         }
    //         sc.close();
    //     }catch(Exception e){
    //         System.err.println("Error: There was a problem reading the text file '" + text.toPath() + "'.");
    //         e.printStackTrace(System.err);
    //     }
    // }

    // /**
    //  * Prints the content of a 2d array to the standard output
    //  * @param sArray the 2d Array
    //  */
    // private static void printSkipArray(String[][] sArray){
    //     for(int i = 0; i < sArray.length; i++){
    //         for(int j = 0; j < sArray[i].length; j++){
    //             System.out.print(sArray[j][i]);
    //         }
    //         System.out.println("");
    //     }
    // }

    // private static int getNumLines(File file){
    //     int lines = 0;
    //     try{      
    //         LineNumberReader lnr = new LineNumberReader(new FileReader(file));
    //         lnr.skip(Long.MAX_VALUE);
    //         lines = lnr.getLineNumber();
    //         lnr.close();
    //     }catch(Exception e){
    //         System.err.println("Error: There was a problem creating the skip array");
    //         e.printStackTrace(System.err);
    //         System.exit(1);
    //     }
    //     return lines;
    // }
}
