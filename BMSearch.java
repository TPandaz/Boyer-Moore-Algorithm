import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**Searches for a string pattern in a text file and outputs each line that the string occurs on to the standard output
 * @author Dolf ten Have
 * SID: 1617266
 * Date:
 */
public class BMSearch {
    private static String[][] skipArray; //The skip array.

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
        makeSkipArray(table);
        printSkipArray(skipArray);      
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

    /**
     * Creates a skip array from a file
     * @param skipFile the file containing the skip array
     * @return a skip array
     */
    private static String[][] makeSkipArray(File skipFile){
        List<String[]> skipArrayList = new ArrayList<String[]>();
        //Splits each line into an array and adds that to the array list
        try{
            Scanner sc = new Scanner(skipFile);
            while(sc.hasNext()){
                skipArrayList.add(sc.nextLine().split(","));
            }
        }catch(Exception e){
            System.err.println("Error: There was a problem creating the skip array");
            e.printStackTrace(System.err);
            System.exit(1);
        }
        //Copys the array list into a 2D array
        int rowLength = skipArrayList.get(0).length;
        int numRows = skipArrayList.size();
        skipArray = new String[rowLength][numRows];
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < rowLength; j++){
                skipArray[j][i] = skipArrayList.get(i)[j];
            }
        }
        return skipArray;
    }

    /**
     * Reads each line of the text into a string
     * @param text the text file
     */
    private static void readText(File text){
        String line;
        try{
            Scanner sc = new Scanner(text);
            while(sc.hasNext()){
                line = sc.nextLine();
                //Checks that the line is at least the length of the pattern
                if(line.length() > skipArray.length)
                    findPattern(line, skipArray.length);
            }
            sc.close();
        }catch(Exception e){
            System.err.println("Error: There was a problem reading the text file '" + text.toPath() + "'.");
            e.printStackTrace(System.err);
        }
    }

    /**
     * Tries to find a match for the pattern in the line
     * @param line the line of text 
     * @param index the current pointer index | should be 0 on initialisation
     */
    private static void findPattern(String line, int index){
       

    }

    /**
     * Prints the content of a 2d array to the standard output
     * @param sArray the 2d Array
     */
    private static void printSkipArray(String[][] sArray){
        for(int i = 0; i < sArray.length; i++){
            for(int j = 0; j < sArray[i].length; j++){
                System.out.print(sArray[j][i]);
            }
            System.out.println("");
        }
    }

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
