package x.grammar.se;

import java.util.ArrayList;

/**
 * Created by xyy on 2018/11/9.
 */
public class X4Words extends ArrayList<String> {

    private String name = "";

    public String getName() {
        return name;
    }

    public X4Words(String name) {
        if (name == null || name.isEmpty()) {
            throw new Error("name is empty");
        }
        this.name = name;
    }

    public void add(String[] values) {
        for (int i = 0; i < values.length; i++) {
            String s = values[i].trim();
            if (s.isEmpty()) {
                continue;
            }
            if (this.contains(s)) {
                continue;
            }
            this.add(s);
        }
    }

    public static X4Words accept(String value) {
        String[] s = value.split("=");
        String left = s[0].trim();
        String right = s[1].trim();
        X4Words rs = new X4Words(left.trim());
        rs.add(right.trim().split("\\|"));
        return rs;
    }

    public String toLexer() {
        StringBuffer rs = new StringBuffer();
        for (String item : this) {
            if(item.equals("*")) {
                rs.append(".+|");
            } else {
                rs.append(item + "|");
            }
        }
        return "(" + rs.substring(0, rs.length() - 1) + ")";
    }
}
