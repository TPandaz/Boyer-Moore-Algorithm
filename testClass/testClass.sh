# Creates the tables
echo "Opening all files in '${PWD}/testClass/patterns/'"
for file in "${PWD}"/testClass/patterns/*.pattern
do
    java MakeBMTable "$(basename $file .pattern)" ${PWD}/testClass/tables/"$(basename $file .pattern)".table
done

# Does the word search
for file in "${PWD}"/testClass/tables/*.table
do  
    java BMSearch $file "${PWD}"/testClass/patterns/"$(basename $file .table)".pattern > "${PWD}"/testClass/results/"$(basename $file .table)".result
done