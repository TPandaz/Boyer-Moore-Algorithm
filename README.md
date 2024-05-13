# Assignment 3: Boyer Moore
### File Descriptions

#### MakeBMTable.java
This program takes in a string and a filename from the console. Using the string, this program calculates the shift values for every character in the string following the Boyer Moore Algorithm. Writing the values to the filename specified in the form of a skip table.

Note: The character '*' represents any character that is not present in the pattern, and is being used for comparisons in the table. In the case where the pattern to be searched contains a '*', another placeholder character needs to be hardcoded into the program to ensure skip values for the table are accurate.

Example Usage: java MakeBMTable "kokako" table.txt

#### BMSearch.java
