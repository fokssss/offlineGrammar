package x.grammar.py

import com.github.stuxuhai.jpinyin.PinyinException

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator

/**
 * 本文件用来演示如何通过文本相似度进行纠错
 *
 *
 *
 * String[] list = new String[]{"张三", "张衫", "张丹", "张成", "李四", "李奎"};
 * PinyinSearch d = new PinyinSearch(list);
 * System.out.println(d.search("张三", 10));
 * System.out.println(d.search("李四", 10));

 * 输出：
 * [{x4Word=张三, score=0}, {x4Word=张衫, score=1}, {x4Word=张丹, score=1}, {x4Word=张成, score=5}, {x4Word=李四, score=9}, {x4Word=李奎, score=10}]
 * [{x4Word=李四, score=0}, {x4Word=李奎, score=3}, {x4Word=张三, score=9}, {x4Word=张衫, score=10}, {x4Word=张丹, score=10}, {x4Word=张成, score=12}]
 *
 */
internal class PinyinSearch {

    var encoding = "UTF-8"

    val targets: MutableList<X4Word> = ArrayList()

    @Throws(PinyinException::class)
    fun addWord(list: Array<String>) {
        for (s in list) {
            val w = X4Word(s)
            targets.add(w)
        }
    }

    @Throws(IOException::class, PinyinException::class)
    fun addWord(`is`: InputStream) {
        val read = InputStreamReader(`is`, encoding)
        val bufferedReader = BufferedReader(read)
        var lineTxt: String = bufferedReader.readLine()
        while (lineTxt != null) {
            if (lineTxt == null || "" == lineTxt) {
                continue
            }
            try {
                val w = X4Word(lineTxt)
                targets.add(w)
                lineTxt = bufferedReader.readLine()
            } catch (e: Exception) {
                println(e.message)
            }

        }
        read.close()
    }

    @Throws(PinyinException::class, IOException::class)
    fun addWord(filename: String) {
        val file = File(filename)
        if (file.isFile && file.exists()) {
            addWord(FileInputStream(file))
        } else {
            throw FileNotFoundException("找不到指定的文件")
        }
    }

    //    public static void main(String[] args) throws PinyinException {
    //        String[] list = new String[]{"张三", "张散", "张丹", "张成", "李四", "李奎"};
    //        PinyinSearch d = new PinyinSearch(list);
    //        System.out.println(d.search("张山", 10));
    //        System.out.println(d.search("李四", 10));
    //    }


    //    public List<Score> search(String input, int limit) throws PinyinException {
    //        X4Word w = new X4Word(input);
    //        return targets.stream().map(x -> {
    //            Score s = new Score();
    //            s.x4Word = x;
    //            s.score = x.compareTo(w);
    //            return s;
    //        }).sorted().limit(limit).collect(Collectors.toList());
    //    }


    private inner class ScoreComputer(var rs: ArrayList<Score>, var start: Int, var end: Int, var word: X4Word) : Runnable {

        override fun run() {
            for (i in start..end) {
                var x = targets[i]
                val s = Score()
                s.x4Word = x
                s.score = x.compareTo(word)
                if (s.score > MINI_SCORE) {
                    continue
                }
                rs.add(s)

//                if (s.score < 1) {
//                    highscore.add(s)
//                    if (highscore.size >= limit) {
//                        return highscore
//                    }
//                } else {
//                }

            }
            pointer--
        }
    }


    var pointer = 0

    @Synchronized @Throws(PinyinException::class)
    private fun searchAsync(input: String, limit: Int): List<Score> {
        val w = X4Word(input)
        val highscore = ArrayList<Score>()
        val rs = ArrayList<Score>()
        val len = 100
        run {
            var i = 0
            while (i < targets.size) {
                val start = i
                var end = i + len
                if (end >= targets.size) {
                    end = targets.size - 1
                }
                pointer++
                Thread(ScoreComputer(rs, start, end, w)).start()
                i += len
            }
            while (pointer != 0) {
//                Thread.sleep(1)
            }
        }
        System.out.println(rs.size)

        Collections.sort(rs) { a, b -> Integer.compare(a.score, b.score) }
        //        rs.sort();
        if (rs.size > limit) {
            val rs2 = ArrayList<Score>()
            for (i in 0..limit - 1) {
                rs2.add(rs[i])
            }
            return rs2
        }
        return rs
    }

    @Synchronized @Throws(PinyinException::class)
    fun search(input: String, limit: Int): List<Score> {
        val w = X4Word(input)
        val highscore = ArrayList<Score>()
        val rs = ArrayList<Score>()
        var sss:Int = 0
        var start = System.currentTimeMillis()
        for (x in targets) {
            sss = x.compareTo(w)
            if (sss > MINI_SCORE) {
                continue
            }
            val s = Score()
            s.x4Word = x
            s.score = sss
            if (s.score < 1) {
                highscore.add(s)
                if (highscore.size >= limit) {
                    return highscore
                }
            } else {
                rs.add(s)
            }

        }
        rs.addAll(highscore)
        System.out.print("计算：")
        System.out.println(System.currentTimeMillis()-start)
        start = System.currentTimeMillis()
        Collections.sort(rs) { a, b -> Integer.compare(a.score, b.score) }
        //        rs.sort();
        System.out.print("排序：")
        System.out.println(System.currentTimeMillis()-start)
        if (rs.size > limit) {
            val rs2 = ArrayList<Score>()
            for (i in 0..limit - 1) {
                rs2.add(rs[i])
            }
            return rs2
        }
        return rs
    }

    @Throws(PinyinException::class)
    fun searchOne(input: String): Score {
        val rs = this.search(input, 1)
        return if (rs.size > 0) rs[0] else Score(ERR_SCORE)
    }


    companion object {

        val ERR_SCORE = 999999

        const val MINI_SCORE = 16


        fun getEditDistance(s: String, t: String): Int {
            val d: Array<IntArray> // matrix
            val n: Int // length of s
            val m: Int // length of t
            var i: Int // iterates through s
            var j: Int // iterates through t
            var s_i: Char // ith character of s
            var t_j: Char // jth character of t
            var cost: Int // cost
            // Step 1
            n = s.length
            m = t.length
            if (n == 0) {
                return m
            }
            if (m == 0) {
                return n
            }
            d = Array(n + 1) { IntArray(m + 1) }

            // Step 2
            i = 0
            while (i <= n) {
                d[i][0] = i
                i++
            }
            j = 0
            while (j <= m) {
                d[0][j] = j
                j++
            }

            // Step 3
            i = 1
            while (i <= n) {
                s_i = s[i - 1]
                // Step 4
                j = 1
                while (j <= m) {
                    t_j = t[j - 1]
                    // Step 5
                    cost = if (s_i == t_j) 0 else 1
                    // Step 6
                    d[i][j] = Minimum(d[i - 1][j] + 1, d[i][j - 1] + 1,
                            d[i - 1][j - 1] + cost)
                    j++
                }
                i++
            }
            // Step 7
            return d[n][m]
        }

        private fun Minimum(a: Int, b: Int, c: Int): Int {
            val im = if (a < b) a else b
            return if (im < c) im else c
        }
    }
}
