package x.grammar.se;

import x.grammar.X4CommandInvoker;

import java.io.*;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaTESTin {

    public static void test(String[] args) {
//        new Main().test2();
//        if(true)return;


        

        String line = "让我们查看郑文博的在联系方式吧";
        try {
            FileInputStream is = new FileInputStream("/Users/xyy/src/projs/OfflineGrammarSimpleEdition/grammar/grammar.x4");
            X4Lexer lexer = X4Lexer.Companion.build(new BufferedReader(new InputStreamReader(is)));

            X4Result rs = lexer.find( line);
            if (rs == null) {
                System.out.println("not found");
            }
            System.out.println(rs.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void test() {
        // 按指定模式在字符串查找
        String line = "This <order> waQK2<>9s pla<dd>44ced for QT1234! OK?";
//        String pattern = "(QT|QK|.{1}){1}\\d+";
        String pattern = "<\\w+>";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        while (m.find()) {
            System.out.println("Found value: " + m.group(0));
//            System.out.println("Found value: " + m.group(1) );
//            System.out.println("Found value: " + m.group(2) );
//            System.out.println("Found value: " + m.group(3) );
//            System.out.println("Found value: " + m.group(4) );
//            System.out.println("Found value: " + m.group(5) );
        }

    }

    public void test2() {
        String line = "看看郑文博的联系方式";
        String ss = "(查看|查找|看看)(.+)(的)(手机|电话|联系方式)";
//        String pattern = "看看(.+)的联系";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(ss);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        while (m.find()) {
            System.out.println("Found value: " + m.group(0));
//            System.out.println("Found value: " + m.group(1) );
//            System.out.println("Found value: " + m.group(2) );
//            System.out.println("Found value: " + m.group(3) );
//            System.out.println("Found value: " + m.group(4) );
//            System.out.println("Found value: " + m.group(5) );
        }
    }
}
