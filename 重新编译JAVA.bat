@ECHO OFF
mode con lines=20 cols=90
color 0a
title %~n0
setlocal enabledelayedexpansion
cd /d "%~dp0"

javac -cp .;commons-io-2.8.0.jar -encoding UTF-8 -d . ���ɱ����ļ���¼�����ϣ.java
javac -cp .;commons-io-2.8.0.jar -encoding UTF-8 -d . ���ɱ����ļ���¼.java
echo ����ɱ���&pause