@echo off
cls


java -classpath "%~dp0lib\javacc.jar;%~dp0lib\javacc.jar;%~f0\..\lib\javacc.jar" javacc MiniJavaLexer.txt