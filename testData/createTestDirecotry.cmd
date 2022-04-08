@echo off

:: IMPORTANT! START THIS SCRIPT IN AN ELEVATED command prompt

mkdir c:\ads
net share ads=c:\ads /grant:everyone,FULL

copy ads\*.* c:\ads\*.*