@ECHO OFF
mode con lines=30 cols=100
color 0a
title ���ɹ�ϣ�ļ���¼
setlocal enabledelayedexpansion
if "%~1"=="" echo ���϶��ļ���ִ��&pause&exit
set oldFile=
cd /d "%~1"
if exist ��ϣ�ļ���¼.txt (
	echo ���ھɼ�¼,����,������
	move ��ϣ�ļ���¼.txt ..\��ϣ�ļ���¼[��].txt
	cd ..
	set oldFile=!cd!\��ϣ�ļ���¼[��].txt
)
cd /d "%~dp0"

rem ��������һ��б��
set filePath=%~1
if %filePath:~-1% == \ (
	set filePath=%filePath:~0,-1%
)

java -cp .;commons-io-2.8.0.jar ����.�����ļ���¼.���ɱ����ļ���¼�����ϣ "%filePath%"

set sublime_text=D:\Sublime Text Build 3176 x64\sublime_text.exe
start "" "%sublime_text%" "%filePath%\��ϣ�ļ���¼.txt" %oldFile%
CHOICE /T 10 /D N /C YN /N /M 10����˳�