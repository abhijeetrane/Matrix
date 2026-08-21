Read me 

1)Installation instruction
    Unzip the file into a folder.
    Inorder to set the enviroment ,modify the env.bat to set the JAVA_HOME and MIDP_HOME 
    enviroment variable.

2)Compile instructions
    a)Modify the build.xml to set midp.home and wtk.home properties to appropriate directories
    on your system.
    b)First run env.bat
    c)To compile the command is 
     ant -buildfile build.xml

3)To run the program
   a)ant -buildfile build.xml run

4)To generate the javadoc 
   a)ant -buildfile build.xml docs

