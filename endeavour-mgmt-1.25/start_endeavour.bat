@echo off
setlocal
set JAVA_HOME=
set ENDEAVOUR_PATH=%~dp0
set JRE_HOME=%ENDEAVOUR_PATH%jre
set PATH=%JRE_HOME%\bin;%PATH%;
cd tomcat\bin
set CATALINA_HOME=%ENDEAVOUR_PATH%tomcat
set CATALINA_OPTS=-Xms128m -Xmx512m -XX:MaxPermSize=256m -Duser.language=en -Duser.country=EN -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000
call startup
endlocal
:quit