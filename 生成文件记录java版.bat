@ECHO OFF
mode con lines=20 cols=60
color 0a
title �����ļ���¼
setlocal enabledelayedexpansion
if "%~1"=="" echo ���϶��ļ���ִ��&pause&exit
set oldFile=
cd /d "%~1"
if exist �ļ���¼.txt (
	echo ���ھɼ�¼,����,������
	move �ļ���¼.txt ..\�ļ���¼[��].txt
	cd ..
	set oldFile=!cd!\�ļ���¼[��].txt
)
cd /d "%~dp0"

rem ��������һ��б��
set filePath=%~1
if %filePath:~-1% == \ (
	set filePath=%filePath:~0,-1%
)

java -cp .;commons-io-2.8.0.jar ����.�����ļ���¼.���ɱ����ļ���¼ "%filePath%"

set sublime_text=D:\Sublime Text Build 3176 x64\sublime_text.exe
start "" "%sublime_text%" "%filePath%\�ļ���¼.txt" %oldFile%
CHOICE /T 10 /D N /C YN /N /M 10����˳�