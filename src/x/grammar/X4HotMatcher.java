package x.grammar;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.sun.tools.classfile.Code_attribute;
import org.jetbrains.annotations.NotNull;
import x.grammar.py.Score;
import x.grammar.py.X4HotWord;
import x.grammar.se.X4Lexer;
import x.grammar.se.X4Result;

/**
 * Created by xyy on 2018/11/10.
 */
public class X4HotMatcher {

    private int score = 10;

    private X4Lexer lexer = null;
    private X4HotWord hotWord = null;

    public X4HotMatcher(X4Lexer lexer, X4HotWord hotWord) {
        this.lexer = lexer;
        this.hotWord = hotWord;
    }

    public X4HotMatcher(X4Lexer lexer) {
        this.lexer = lexer;
    }

    public X4HotMatcher() {

    }

    public X4Lexer getLexer() {
        return lexer;
    }

    public void setLexer(X4Lexer lexer) {
        this.lexer = lexer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public X4HotWord getHotWord() {
        return hotWord;
    }

    public void setHotWord(X4HotWord hotWord) {
        this.hotWord = hotWord;
    }

    public void addHotWord(String s) {
        if (this.hotWord == null) {
            this.hotWord = new X4HotWord();
        }
        this.hotWord.addWord(s);
    }

    public void addHotWord(String[] s) {
        if (this.hotWord == null) {
            this.hotWord = new X4HotWord();
        }
        this.hotWord.addWord(s);
    }

    public String match(String value) {
        return match(value, null);
    }

    public String match(String value, X4HotWord words) {
        X4HotWord w = words;
        if (w == null) {
            w = this.hotWord;
        }
        if (w == null) {
            return value;
        }
        try {
            Score ss = w.match(value);
            if (ss != null) {
                if (ss.getScore() < this.score) {
                    return ss.getX4Word().word;
                }
            }
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        return value;
    }

    public String matchByKey(String value, String keyword) {
        return matchByKey(value, keyword, null);
    }

    public String matchByKey(String value, String keyword, X4HotWord words) {
        X4HotWord w = words;
        if (w == null) {
            w = this.hotWord;
        }
        if (w == null) {
            return value;
        }
        X4Result rs = lexer.find(value);
        if (rs == null) {
            return value;
        }
        String vv = rs.getValue(keyword);
        try {
            Score ss = w.match(vv);
            if (ss != null) {
                if (ss.getScore() < this.score) {
                    return value.replaceAll(vv, ss.getX4Word().word);
                }
            }
        } catch (PinyinException e) {
            e.printStackTrace();
        }

        return value;
    }

}
