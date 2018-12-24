package x.grammar.se

import x.grammar.X4CommandInvoker
import java.util.*

/**
 * Created by xyy on 2018/12/24.
 */

class X4Program {

    private val keywords = HashMap<String, ArrayList<String>>()

    private val exprs = ArrayList<String>()

    fun addVariable(name: String, vararg values: String): X4Program {
        var v = keywords[name] ?: ArrayList<String>()
        if (values.size == 0 && v.size == 0) {
            v.add("*")
        } else {
            if (v.size == 1 && v[0] == "*") {
                v.clear()
            }
            for (s in values) {
                v.add(s)
            }
        }
        keywords.put(name, v)
        return this
    }

    fun addExpression(expr: String): X4Program {

        exprs.add(
                if (expr.indexOf("=") > 0)
                    expr else "#$expr")
        return this
    }

    fun getVariableCollection(): List<String> {
        val rs = ArrayList<String>()
        for (key in keywords.keys) {
            var item = "@$key="
            var data = keywords[key] ?: continue
            if (data.size < 1) {
                continue
            }
            for (value in data) {
                item += value + "|"
            }
            item = item.substring(0, item.length - 1)
            rs.add(item)
        }
        return rs
    }

    fun getExpressionCollection(): List<String> {
        return exprs
    }

}