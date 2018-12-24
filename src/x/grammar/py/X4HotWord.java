package x.grammar.py;

import com.github.stuxuhai.jpinyin.PinyinException;
import x.grammar.X4Callback;

import java.io.*;

/**
 * Created by xyy on 2018/11/10.
 */
public class X4HotWord {

    private PinyinSearch search = new PinyinSearch();

    public X4HotWord() {
        ;
    }

    public static X4HotWord build(InputStream stream) throws IOException {
        return build(new InputStreamReader(stream));
    }

    public static X4HotWord build(Reader r) throws IOException {
        X4HotWord rs = new X4HotWord();
        BufferedReader reader = new BufferedReader(r);
        String lineTxt = null;
        while ((lineTxt = reader.readLine()) != null) {
            rs.addWord(lineTxt);
        }
        return rs;
    }

    public Score match(String value) throws PinyinException {
        return search.searchOne(value);
    }

    public void addWord(String s) {
        if (s == null || "".equals(s)) {
            return;
        }
        try {
            X4Word w = new X4Word(s);
            X4HotWord.this.search.getTargets().add(w);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addWord(String[] s) {
        for (String item : s) {
            addWord(item);
        }
    }
}
