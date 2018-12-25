package x.grammar.se;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xyy on 2018/11/9.
 */
public class X4Expression {


    private String name = "";

    public String getName() {
        return name;
    }

    private List<String> exprs = new ArrayList<>();

    private String expression = "";

    public String getExpression() {
        return expression;
    }

    public X4Expression(String name) {
        if (name == null || name.isEmpty()) {
            throw new Error("name is empty");
        }
        this.name = name;
    }

    public static X4Expression accept(String value, X4Lexer lexer) {
        String[] s = value.split("=");
        if (s.length < 2) {
            return null;
        }
        String left = s[0].trim();
        if (left.isEmpty()) {
            return null;
        }
        String right = s[1].trim();
        if (right.isEmpty()) {
            return null;
        }
        X4Expression rs = new X4Expression(left);
        rs.expression = right;
        rs.add(right.split("\\|"));
//
//        String pattern = "<\\w+>";
//        Pattern r = Pattern.compile(pattern);
//
//        for (int i = 0; i < rs.exprs.size(); i++) {
//            String line = rs.exprs.get(i);
//            Matcher m = r.matcher(line);
//            while (m.find()) {
//                String keyword = m.group(0);
//                X4Words w = lexer.getWords(keyword.substring(1, keyword.length() - 1));
//                if (w == null) {
//                    continue;
//                }
//                line = line.replaceAll(keyword, w.toLexer());
//            }
//        }

        return rs;
    }

    private void add(String[] values) {
        for (int i = 0; i < values.length; i++) {
            String s = values[i].trim();
            if (s.isEmpty()) {
                continue;
            }
            if (exprs.contains(s)) {
                continue;
            }
            exprs.add(s);
        }
    }

    public boolean containExpression(String key) {
        return expression.indexOf("<" + key + ">") >= 0;
    }

    public X4Result find(String key, String data, X4Lexer lexer) {
        for (int i = 0; i < exprs.size(); i++) {
            String line = exprs.get(i);
            if (line.indexOf("<" + key + ">") < 0) {
                continue;
            }
            X4Result rs = findExpr(data, exprs.get(i), lexer);
            if (rs != null) return rs;
        }
        return null;
    }

    private static Pattern findBrackets = Pattern.compile("<\\w+>");

    public X4Result find(String data, X4Lexer lexer) {
        for (int i = 0; i < exprs.size(); i++) {
            X4Result rs = findExpr(data, exprs.get(i), lexer);
            if (rs != null) return rs;
        }
        return null;
    }

    private X4Result findExpr(String data, String line, X4Lexer lexer) {
        Matcher m = findBrackets.matcher(line);
        ArrayList<String> index = new ArrayList<>();
        while (m.find()) {
            String keyword = m.group(0);
            index.add(keyword);
            line = line.replaceAll(keyword, lexer.getWords(keyword.substring(1, keyword.length() - 1)).toLexer());
        }
        Pattern pp = Pattern.compile(line);
        Matcher finder = pp.matcher(data);
        if (finder.find()) {
            X4Result rs = new X4Result(this.getName(),line, data);
            for (int x = 1; x <= index.size(); x++) {
                rs.putValue(index.get(x - 1), finder.group(x));
            }
            return rs;
        }
        return null;
    }
}
