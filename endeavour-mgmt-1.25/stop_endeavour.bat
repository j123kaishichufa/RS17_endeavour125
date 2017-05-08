@echo on
setlocal
set JAVA_HOME=
set ENDEAVOUR_PATH=%~dp0
set JRE_HOME=%ENDEAVOUR_PATH%jre
cd tomcat\bin
set CATALINA_HOME=%ENDEAVOUR_PATH%tomcat
shutdown.bat
endlocal
exit
