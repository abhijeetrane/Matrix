IP Phone Home Instructions
To set up enviroment
a) j2sdk1.5 installed
b) ANT is installed


env.bat can be modified to suit your PC


Build command
-------------
In the DSProject directory 

ant


Run commnand
------------
java -jar IPPhoneHome.jar



Docs command
------------
ant docs

The testing requires to create  2 folders 3PO and R2.
Follow the instructions provided by Ron

If you have trouble listening to test sound 
provided by Ron,copy the audio folder from drivers
directory to 3PO and R2 folder.

It is neccesary to have sip.config in the same folder 
where you run the IPPhoneHome.jar

A sample sip.config is provided in the folder DSProject.
Copy it into build directory after you have build the jar.
and then run the jar file.

Regards
Lanikai Team




