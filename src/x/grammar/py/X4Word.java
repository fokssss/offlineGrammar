package x.grammar.py;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.HashMap;

public class X4Word implements Comparable {
    private static final int LENGTH_ERROR = 99;
    public static final int MAX_CACHE_SIZE = 50000;
    public final String word;
    public final String pinyin1;
    public final String pinyin2;

    public X4Word(String word) throws PinyinException {
        this.word = word.trim();
        this.pinyin1 = PinyinHelper.convertToPinyinString(this.word, ",", PinyinFormat.WITH_TONE_NUMBER);
        this.pinyin2 = PinyinHelper.convertToPinyinString(this.word, ",", PinyinFormat.WITHOUT_TONE);
    }

    @Override
    public String toString() {
        return word;
    }


    private static HashMap<String, Integer> cache = new HashMap<>();

    @Override
    public int compareTo(Object o) {
        if (o instanceof X4Word) {
            X4Word o1 = (X4Word) o;
            if (o1.word.length() != this.word.length()) {
                return LENGTH_ERROR;
            }
            String key = o1.word + "_" + this.word;
            if (cache.containsKey(key)) {
                return cache.get(key);
            }
            int score2 = PinyinSearch.Companion.getEditDistance(this.pinyin2, o1.pinyin2);
            int score1 = PinyinSearch.Companion.getEditDistance(this.pinyin1, o1.pinyin1);
            int ss = score1 + score2;
            cache.put(key, ss);
            if(cache.size()> MAX_CACHE_SIZE) {
                cache = new HashMap<>();
            }
            return ss;
        }
        return 0;
    }
}
