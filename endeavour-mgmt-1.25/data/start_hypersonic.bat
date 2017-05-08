@Echo Off

setlocal
set JAVA_HOME=
set JRE_HOME=..\jre
set PATH=%JRE_HOME%\bin;%PATH%;

REM ---------------------------------------------
REM - Create the classpath for this application -
REM ---------------------------------------------
SET tempclasspath=
SET libdir=.\lib

FOR /f "delims=" %%a IN ('dir %libdir%\hsqldb*.jar /b /a-d') DO call :addToClasspath %%a
GOTO :startApp

:addToClasspath
IF "%tempclasspath%"=="" SET tempclasspath=%libdir%\%1& GOTO :end
SET tempclasspath=%tempclasspath%;%libdir%\%1
GOTO :end

REM -----------------------
REM - Run the application -
REM -----------------------
:startApp
SET command=java -cp %tempclasspath% org.hsqldb.Server -database.0 hsqldb\endeavour -dbname.0 endeavour
echo %command%
%command%
exit

:end
