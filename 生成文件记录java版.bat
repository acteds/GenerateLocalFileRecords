@ECHO OFF
mode con lines=20 cols=60
color 0a
title 生成文件记录
setlocal enabledelayedexpansion
if "%~1"=="" echo 请拖动文件夹执行&pause&exit
set oldFile=
cd /d "%~1"
if exist 文件记录.txt (
	echo 存在旧记录,上移,并更名
	move 文件记录.txt ..\文件记录[老].txt
	cd ..
	set oldFile=!cd!\文件记录[老].txt
)
cd /d "%~dp0"

rem 处理掉最后一个斜杠
set filePath=%~1
if %filePath:~-1% == \ (
	set filePath=%filePath:~0,-1%
)

java -cp .;commons-io-2.8.0.jar 工具.生成文件记录.生成本地文件记录 "%filePath%"

set sublime_text=D:\Sublime Text Build 3176 x64\sublime_text.exe
start "" "%sublime_text%" "%filePath%\文件记录.txt" %oldFile%
CHOICE /T 10 /D N /C YN /N /M 10秒后退出