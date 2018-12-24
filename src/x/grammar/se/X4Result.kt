package x.grammar.se

import java.util.*

/**
 * Created by xyy on 2018/11/9.
 */
class X4Result constructor(var expr: String = "", var data: String = "") {

    private var innerValue = HashMap<String, String>()

    fun getValue(key: String): String {
        return innerValue[key] ?: ""
    }

    fun putValue(key: String, value: String) {
        var s = key
        if (s.startsWith("<")) {
            s = s.substring(1, s.length)
        }
        if (s.endsWith(">")) {
            s = s.substring(0, s.length - 1)
        }
        innerValue[s] = value
    }

    override fun toString(): String {
        return data + "->" + innerValue.toString()
    }
}
