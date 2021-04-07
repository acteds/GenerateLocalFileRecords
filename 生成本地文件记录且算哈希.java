package 工具.生成文件记录;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.Collator;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/*javac -cp .;commons-io-2.8.0.jar -encoding UTF-8 -d . 生成本地文件记录且算哈希.java*/
/*java -cp .;commons-io-2.8.0.jar 工具.生成文件记录.生成本地文件记录且算哈希 */

/**
 * 用以输出本地文件记录并计算哈希
 * @author aotmd
 * @version 1.0.5
 * @date 2021/3/9 00:14
 */
public class 生成本地文件记录且算哈希 {
    /** 待输出缓冲区*/
    private StringBuffer sb=new StringBuffer();
    /** 文件,文件夹计数*/
    private long n=0,m=0;
    public StringBuffer getSb() { return sb; }public void setSb(String s) { this.sb.append(s); }
    public long getN() { return n; }public long getM() { return m; }

    public static void main (String[]args)  {
        if (args.length==0){
            System.out.println("没有路径!");
            return;
        }
        System.out.println("当前给出的路径为:"+args[0]);


        生成本地文件记录且算哈希 notes=new 生成本地文件记录且算哈希();

        /*开始获取文件记录*/
        File index = new File(args[0]);
        notes.generateFileRecords(index);

        /*路径*/
        notes.setSb("路径:"+args[0]+"\n");
        /*文件数量,文件夹数量*/
        notes.setSb("文件数量:"+notes.getN()+",文件夹数量:"+notes.getM()+"\n");
        /*获取当前时间*/
        String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        notes.setSb("记录时间:"+date+"\n");

        /*输出文件记录到控制台*/
        System.out.println(notes.getSb());

        /*输出文件记录到文件*/
        BufferedWriter bw;
        try {
            bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(index.getPath()+File.separator+"哈希文件记录.txt"),"UTF-8"));
            bw.write(notes.getSb().toString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 生成文件记录且计算哈希
     */
    private void generateFileRecords(File file) {
        if(!file.exists()){
            System.out.println("文件路径不存在!");
            return;
        }
        char[] empty=new char[200];
        Arrays.fill(empty, '1');

        cycle(file,1,empty);
    }

    public void cycle(File file, int count, char[] empty) {
        File[] fileArray = file.listFiles();
        if (fileArray == null) {
            /*输出名称,该目录下的目录与文件数量和,目录大小*/
            sb.append(String.format("%s [%d] [%s]", file.getName(), 0, this.getPathSize(file))).append('\n');
            return;
        }
        /*输出名称,该目录下的目录与文件数量和,目录大小*/
        sb.append(String.format("%s [%d] [%s]", file.getName(), this.getTheNumberOfFiles(file), this.getPathSize(file))).append('\n');
        List<File> list = Arrays.asList(fileArray);
        /*排序*/
        Collections.sort(list, (f1, f2) -> {
            int a = f1.isDirectory() ? 1 : 0;
            int b = f2.isDirectory() ? 1 : 0;
            /*文件在文件夹前面*/
            int result = a - b;
            /*相等则比较文件名称,A-Z然后中文排序,升序*/
            Collator collator = Collator.getInstance(Locale.CHINA);
            if (a - b == 0) {
                result = collator.compare(f1.getName(), f2.getName());
            }
            return result;
        });
        for (int i = 0; i < list.size(); i++) {
            File temp = list.get(i);
            if (temp.isDirectory()) {
                m++;
                empty[count - 1] = '1';
                /*如果上一个是文件那么空一行*/
                if (i > 0 && !list.get(i - 1).isDirectory()) {
                    sb.append(printFilePrefix(empty, count).replaceAll("\\s+$","")).append('\n');
                }
                /*输出前缀*/
                sb.append(printFilePrefix(empty, count - 1));
                /*如果是最后一个文件夹*/
                if (i == list.size() - 1) {
                    sb.append("└─");
                    empty[count - 1] = '0';
                } else {
                    sb.append("├─");
                }
                /*迭代*/
                cycle(temp, count + 1, empty);
            } else {/*如果是文件*/
                n++;
                StringBuilder fileSb=new StringBuilder();
                fileSb.append(printFilePrefix(empty, count - 1));
                /*如果该目录下没有文件夹*/
                if (!list.get(list.size() - 1).isDirectory()) {
                    fileSb.append("    ");
                } else {
                    fileSb.append("│  ");
                }
                /*输出名称,大小*/
                fileSb.append(String.format("%s \t [%s]", temp.getName(), this.getPathSize(temp)));

                /*格式化输出哈希*/
                int addEmpty=1;
                if (100-fileSb.length()>1){
                    addEmpty=100-fileSb.length();
                }
                fileSb.append(String.format("%"+addEmpty+"s hash:%s","",this.getHash(temp)));

                sb.append(fileSb).append('\n');
            }
            /*如果是最后一个*/
            if (i == list.size() - 1) {
                sb.append(printFilePrefix(empty, count - 1).replaceAll("\\s+$","")).append('\n');
            }
        }
    }

    /**
     * 添加文件前缀
     * @param empty 空位控制符
     * @param n 减少输出数量
     */
    private String printFilePrefix(char[] empty,int n){
        StringBuilder s= new StringBuilder();
        for (int i = 0; i < n; i++) {
            char c = empty[i];
            if (c == '0') {
                s.append("    ");
            } else if (c == '1') {
                s.append("│  ");
            }
        }
        return s.toString();
    }

    /**
     * 计算文件哈希值
     * @param file 文件路径
     * @return 哈希字符串
     */
    private String getHash(File file){
        if (file.isDirectory()) {
            return null;
        }
        if (file.length()==0){
            return "文件大小为0,无哈希";
        }
        List<String> a=runCmd("certutil -hashfile \""+file.getPath()+"\"");
        if (a==null){
            return null;
        }
        return a.get(1);
    }
    /**
     * 运行cmd命令,已写好头:cmd /c
     * @param cmd cmd命令
     * @return List结果集
     */
    private List<String> runCmd(String cmd) {
        if (cmd.length()==0)return null;
        cmd="cmd /c "+cmd;
        List<String> list=new ArrayList<>();
        Runtime run = Runtime.getRuntime();
        try {
            Process p = run.exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "gb2312"));
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 获取路径下文件数量
     *
     * @param file 文件路径
     * @return 长整型
     */
    private  long getTheNumberOfFiles(File file) {
        long n=0;
        if (!file.isDirectory()) {
            return 0;
        }
        File[] array = file.listFiles();
        if (array != null) {
            for (File file1 : array) {
                if (file1.isDirectory()) {
                    n+=getTheNumberOfFiles(file1);
                } else {
                    n++;
                }
            }
        }
        return n;
    }
    /**
     * 获取路径大小
     * @param file 文件路径
     * @return 字符串,保留两位小数
     */
    private String getPathSize(File file){
        if (file.isDirectory()){
            return this.formatFileSize(FileUtils.sizeOfDirectory(file));
        } else {
            return this.formatFileSize(file.length());
        }
    }
    /**
     * 转换文件夹大小
     * @param size 文件大小
     * @return 字符串
     */
    private   String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        String wrongSize = "0B";
        if (size == 0) {
            return wrongSize;
        }
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "KB";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}