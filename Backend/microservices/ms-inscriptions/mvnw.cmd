@echo off
setlocal
for %%i in ("%~dp0.") do set "MAVEN_PROJECTBASEDIR=%%~fi"
set "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
if not exist "%WRAPPER_JAR%" (
	where curl.exe >nul 2>nul
	if errorlevel 1 (
		echo curl.exe is required to download the Maven Wrapper bootstrap.
		exit /b 1
	)
	curl.exe -fSL --connect-timeout 15 --max-time 60 "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar" -o "%WRAPPER_JAR%"
	if errorlevel 1 exit /b 1
)
if defined JAVA_HOME (
	set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
	set "JAVA_EXE=java"
)
"%JAVA_EXE%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
exit /b %ERRORLEVEL%
