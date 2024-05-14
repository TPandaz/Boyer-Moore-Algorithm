import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**Searches for a string pattern in a text file and outputs each line that the string occurs on to the standard output
 * @author Dolf ten Have
 * SID: 1617266, 1617175
 * Date: 09/05/2024
 */
public class BMSearch {
    private static String[] pattern; //The pattern to find in the text.
    private static String line; //The current line from the text file
    private static Map<String, int[]> skipArray; //A hashmap of the skip table. String = the String key | int[] the skip array for a given letter

    public static void main(String[] args){
        String usage = "java BMSearch table.txt [textfile].txt";
        File table; //The file containing the skip table.
        File text; //The text file that will be searched in.

        //Checks for a valid number of inputs
        if(args.length != 2){
            System.err.println("Error: The parameters did not match the propor class usage.\nusage: '" + usage + "'.");
            System.exit(1);
        }

        //Gets the table and text files
        table = trySetFile(args[0]);
        text = trySetFile(args[1]);  
        //Parses the skip table
        parseSkipTable(table); 
        //Searches each line of the text file for the pattern and prints each line with a match to the standard output
        searchFile(text); 
    }

    /**Attempts to load a file from the path 
     * @param path the path to the file
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

    /**Parses the file of skip table into a usable format for the pattern search funtions
     * @param skipFile the file containing the skip table
     */
    private static void parseSkipTable(File skipFile){
        skipArray = new HashMap<String,int[]>();
        String[] line; //The current line of the file
        int[] skipArrayLine; //The skip array behind the string key
        try{
            Scanner sc = new Scanner(skipFile);
            pattern = sc.nextLine().split(","); //Gets pattern minus the first character
            printPattern();
            //Creates a new line in the hashmap for each line in the skip array file. The letter becomes the key and the the numbers an int array
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

    /**Passes each line of the file to the search function
     * @param file The file to search in for the pattern
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
     * A recursive function that will try to find a pattern match in a String or reach the end of the String with no match
     * @param line the line of text (String)
     * @param index the current pointer index | should be initialised at 0
     */
    private static void findPattern(int index){
        //If the end of the line is reaches, return
        if(index >= (line.length() - 1) - (pattern.length - 2))
            return;
        //compares each letter in the pattern to the letters in the line from the current index
        for(int i = pattern.length - 2; i >= 0; i--){
            String s = line.substring(index + i, index + i + 1);
            //System.out.printf("Comparing '%s' to '%s'", s, pattern[i + 1]);
            //If there is a missmatch then the the next function is called
            if(pattern[i + 1].compareTo(s) != 0){
                //int skipN = skipArray.get(s) == null ? skipArray.get(pattern[0])[i - 1] : skipArray.get(s)[i - 1];
                findPattern(index += skipArray.get(s) == null ? skipArray.get(pattern[0])[i] : skipArray.get(s)[i]);
                return;
            }   
        }
        //A match is found so the line is printed and the method returns
        System.out.println(line);
    }

    /**************************Remove before release *************************************/

    /**
     * A debug function that prints the current pattern to look for to the standard output
     */
    private static void printPattern(){
        System.out.print("Searching for '");
        for(int i = 0; i < pattern.length; i++){
            System.out.print(pattern[i]);
        }
        System.out.println("'");
    }
}
