## 生成本地文件记录
### 实现动机
 - 需要一个文件记录管理文件夹下的文件和目录变动
 - 通过对比工具对比前后记录即可显示目录文件的增删改情况.
 - batch脚本有缺陷,无法愉快的简单实现需求.
### 之前的版本
 - bat版本代码,可以实现输出相对地址,但因编码为GBK,且bat限制,特殊字符不会显示.
```batch
for /R %1 %%f in (*) do (
	set f1=%%f
	set f1=!f1:%~1\=!
	echo !f1!^|%%~zf>>%1\文件记录.txt
)
```
 - 还可通过tree 生成文件树,但不会显示文件大小和目录大小.
 ```batch
 tree "%~1" /f >>%1\文件记录.txt
 ```
### 实现原理
 - 通过递归,以及控制输出前缀实现生成目录树.
 - 计算哈希通过CMD的`certutil -hashfile`命令实现.


### 使用的库
[Apache Commons](http://commons.apache.org/)：Java 工具库集,用以获取文件夹大小

### 使用方法
- 目录下所有文件请保存文件结构
- 拖动文件夹到两个批处理文件上即可生成对应的文件记录
- 生成的文件记录在被生成文件记录的文件夹下
- 若待生成文件记录的文件夹下已有文件记录则修改名称后再生成
- 记录生成完成后打开新老两个文件记录,通过对比软件即可对比差异
- 默认在生成文件记录后通过sublime打开文件,请自行修改`sublime_text`变量(25行)为sublime的根路径
- 通过sublime的FileDiffs插件可以较好的完成对比.
- 若无法运行,请双击`重新编译JAVA.bat`
- 可自行为两个bat文件生成快捷方式,改变快捷方式位置,图标等.
### License
```java
                Copyright 2021 by acteds

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
