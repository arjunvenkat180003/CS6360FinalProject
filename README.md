This is our implementation of the BaselineComCQ, FastComCQ, and Index Update algorithms for top-k query processing

All of the source code files and data sets are provided in the zip file. Upon downloading and extracting the zip file, one can compile the file "CSVReadTest.java" with the command "javac CSVReadTest.java". Upon doing that, using "java CSVReadTest" will run the project. CSVReadTest.java is the main file that takes in the data sets and runs the top-k queries with a variety of parameters.

All of the tests are contained within the files BaselineCOMQTest.java and FastComCQTest.java. Each method inside the class runs one of the types of experiments outlined in the paper (varying k, varying keywords, etc.). Each test is done by creating a new object for running a query (either a BaselineComCQ or FastComCQ) and running the evalQuery function of that object with the required parameters. The result of that query is then printed out in the test function, and the time of execution for each test is printed out in the main runner file (CSVReadTest.java)

To test out a new query, one can write a new test in the test file based on algorithm (either Baseline or Fast), run the evalQuery function with the parameters, and then run that test in the main file. 
