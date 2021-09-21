set CSC=csc
set JAVA_HOME=%JAVA_HOME%
set JAVA=java
set JAVAC=javac
set JAR=jar

set SRC=src\\*.java
set CLASSES=out\classes
set CP=out\classes;lib\ImageUtil.jar

set MANIFEST=MANIFEST.MF
set BUILD_OPT=-cfvm
set BUILD_Files=-C out\classes .
set OUT_JAR=out\Sample.jar
set OUT_DIR=out

set DIST_DIR=dist
set MODULES=java.base,java.desktop
set JRE_DIR=dist\minjre

If not exist %DIST_DIR% mkdir %DIST_DIR%
If not exist %DIST_DIR%\lib mkdir %DIST_DIR%\lib
If not exist %DIST_DIR%\res mkdir %DIST_DIR%\res
If not exist %DIST_DIR%\res\icon mkdir %DIST_DIR%\res\icon

If not exist %OUT_DIR% mkdir %OUT_DIR%
If not exist %OUT_DIR%\lib mkdir %OUT_DIR%\lib
If not exist %OUT_DIR%\res mkdir %OUT_DIR%\res
If not exist %OUT_DIR%\res\icon mkdir %OUT_DIR%\res\icon
If not exist %OUT_DIR%\classes mkdir %OUT_DIR%\classes


set task=%1
call :%task%

exit /b

:compile
    %JAVAC% -cp %CP% -encoding UTF8 -d %CLASSES% %SRC%
    exit /b

:compile-run
	%JAVAC% -cp %CP% -encoding UTF8 -d %CLASSES% %SRC%
	%JAVA% -cp %CP% App
    exit /b

:copy-deps-files
	copy lib %OUT_DIR%\lib
	copy res\icon %OUT_DIR%\res\icon
    exit /b

:build
	%JAR% %BUILD_OPT% %OUT_JAR% %MANIFEST% %BUILD_Files%
    exit /b

:build-run
	%JAR% %BUILD_OPT% %OUT_JAR% %MANIFEST% %BUILD_Files%
	%JAVA% -jar %OUT_JAR%
    exit /b

:build-all-run
	call :compile
	call :copy-deps-files
	call :build-run
    exit /b

:mkdist
	call :compile
	call :copy-deps-files
	call :build
	%CSC% /nologo /target:winexe /win32icon:forWinC#\icon.ico /out:%DIST_DIR%\TakoImageEditor.exe forWinC#\TakoImageEditor.cs
	copy lib %DIST_DIR%\lib
	copy res\icon %DIST_DIR%\res\icon
	copy %OUT_JAR% %DIST_DIR%\TakoImageEditor.jar
	If not exist %JRE_DIR% jlink --compress=2 --module-path %JAVA_HOME%\jmods --add-modules %MODULES% --output %JRE_DIR%
    exit /b
