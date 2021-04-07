@ECHO OFF
mode con lines=20 cols=90
color 0a
title %~n0
setlocal enabledelayedexpansion
cd /d "%~dp0"

javac -cp .;commons-io-2.8.0.jar -encoding UTF-8 -d . 生成本地文件记录且算哈希.java
javac -cp .;commons-io-2.8.0.jar -encoding UTF-8 -d . 生成本地文件记录.java
echo 已完成编译&pause