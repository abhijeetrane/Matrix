echo off
rem This batch file sets up the environment for compiling and
rem running MIDlets. You need to change two variable for your
rem system: JAVA_HOME and MIDP_HOME. See below.
rem RC, 6 Sep 05

rem Change JAVA_HOME for your system. It's currently set for
rem 1.4.2 and commented-out for 1.5.0.

set JAVA_HOME=c:\j2sdk1.4.2_09
rem set JAVA_HOME=c:\Program Files\Java\jdk1.5.0_04

rem Change MIDP_HOME for your system.
set MIDP_HOME=c:\midp2.0fcs

rem ***********************************************************
rem DO NOT CHANGE ANYTHING BELOW THIS LINE

set PATH=%MIDP_HOME%\bin;%JAVA_HOME%\bin;%PATH%

set CLASSPATH=%MIDP_HOME%\classes;.