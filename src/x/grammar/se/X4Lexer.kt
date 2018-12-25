package x.grammar.se

import x.grammar.py.X4HotWord

import java.io.*
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by xyy on 2018/11/9.
 */
class X4Lexer private constructor() {

    private val variable = HashMap<String, X4Words>()
    private val expressions = ArrayList<X4Expression>()

    fun getWords(name: String): X4Words? {
        if (variable.containsKey(name)) {
            return variable[name]
        }
        return null
    }

    fun find(data: String): X4Result? {
        return find(data, null)
    }

//    fun find(data: String): X4Result? {
//        for (item in expressions) {
//            val rs = item.find(data, this)
//            if (rs != null) {
//                return rs
//            }
//        }
//        return null
//    }

    fun find(data: String, listener: FindListener? = null): X4Result? {
        for (item in expressions) {
            val rs = item.find(data, this)
            rs?.let {
                listener?.onFound(rs)
                return rs
            }
        }
        listener?.onNoFind()
        return null
    }

    fun findAsync(data: String, listener: FindListener) {
        Thread() {
            find(data, listener)
        }.start()
    }

    open abstract class FindListener {
        open fun onFound(rs: X4Result) {

        }

        open fun onNoFind() {

        }
    }

    companion object {

        private const val PREFIX = "noname"

        @Throws(IOException::class)
        fun build(stream: InputStream): X4Lexer {
            return build(InputStreamReader(stream))
        }

        @JvmStatic
        fun build(program: X4Program): X4Lexer {
            var seq = 0
            val rs = X4Lexer()
            for (item in program.getVariableCollection()) {
                val w = X4Words.accept(item.substring(1, item.length))
                rs.variable.put(w.name, w)
            }
            for (s in program.getExpressionCollection()) {
                seq++
                rs.expressions.add(
                        if (s.startsWith("#")) {
                            X4Expression.accept("$PREFIX$seq="+ s.substring(1, s.length), rs)
                        } else
                            X4Expression.accept(s, rs)
                )
            }
            return rs
        }

        @Throws(IOException::class)
        fun build(r: Reader): X4Lexer {
            var seq = 0
            val rs = X4Lexer()
            val reader = BufferedReader(r)
            reader.use {
                while (true) {
                    seq++
                    var s = reader.readLine() ?: break
                    s = s.trim { it <= ' ' }
                    if (s.isEmpty()) {
                        continue
                    }
                    if (s.startsWith("@")) {
                        val w = X4Words.accept(s.substring(1, s.length))
                        rs.variable.put(w.name, w)
                        continue
                    }
                    if (s.startsWith("#")) {
                        val ss = "$PREFIX$seq=" + s.substring(1, s.length)
                        val expression = X4Expression.accept(ss, rs)
                        rs.expressions.add(expression)
                        continue
                    }
                    val expression = X4Expression.accept(s, rs)
                    rs.expressions.add(expression)
                }
            }
            return rs
        }
    }
}
