package x.grammar

import java.util.ArrayList
import java.util.HashMap
import java.util.regex.Matcher
import java.util.regex.Pattern

open class X4CommandInvoker {

    class X4CNumberInvoker : X4CommandInvoker() {

        init {
            val time = System.currentTimeMillis()
            //            Log.e("yyy-time", "CommnadNumberInvoker- :start: " + time);
            for (i in 0..999) {
                val s = ArrayList<String>()
                val value = i + 1
                val vv = toChinese(value)
                this.addCommand(arrayOf(vv), "" + value)
            }
            //            Log.e("yyy-time", "CommnadNumberInvoker- :end: " + (System.currentTimeMillis() - time));
        }

        override fun exec(cmd: String): String {
            if (!Pattern.matches(".*[第]{1}[\\d零一二三四五六七八九十百千万]+[个条]{1}.*", cmd)) {
                return ""
            }
            val num = n.matcher(cmd)
            if (num.find()) {
                return num.group(0)
            }
            val m = r.matcher(cmd)
            if (!m.find()) {
                return ""
            }
            val value = m.group(0)
            if (value.length == 2) {
                var r = if (value.startsWith("十")) "1" else getNumber(value.substring(0, 1))
                r += getNumber(value.substring(1, 2))
                return r
            }
            return super.exec(value)
        }

        private fun getNumber(value: String): String {
            when (value) {
                "一" -> return "1"
                "二" -> return "2"
                "三" -> return "3"
                "四" -> return "4"
                "五" -> return "5"
                "六" -> return "6"
                "七" -> return "7"
                "八" -> return "8"
                "九" -> return "9"
                "十" -> return "0"
                "百" -> return "00"
                "千" -> return "000"
                "万" -> return "0000"
            }
            throw Error("不能识别的数字" + value)
        }

        private fun toChinese(v: Int): String {

            when (v) {
                10 -> return "十"
            }
            val string = "" + v

            val s1 = arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
            val s2 = arrayOf("十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千")

            var result = ""

            val n = string.length
            for (i in 0..n - 1) {

                val num = string[i] - '0'

                if (i != n - 1 && num != 0) {
                    result += s1[num] + s2[n - 2 - i]
                } else {
                    result += s1[num]
                }
            }
            while (result.endsWith("零")) {
                result = result.substring(0, result.length - 1)
            }
            return result

        }

        companion object {

            private val r = Pattern.compile("[零一二三四五六七八九十百千万]+")
            private val n = Pattern.compile("\\d+")
        }
    }

    private val matcher = X4HotMatcher()

    private val cmds = HashMap<String, String>()

    private val keywords = HashMap<String, ArrayList<String>>()

    fun setScore(score: Int): X4CommandInvoker {
        matcher.score = score
        return this
    }

    fun addCommand(values: Array<String>, cmd: String): X4CommandInvoker {
        for (s in values) {
            if (isExpr(s)) {
                for (witem in parser(s)) {
                    matcher.addHotWord(witem)
                    cmds.put(witem, cmd)
                }
            } else {
                matcher.addHotWord(s)
                cmds.put(s, cmd)
            }
        }
        return this
    }

    private fun isExpr(s: String): Boolean {
        return s.indexOf("@@{") >= 0
    }

    private fun parser(s: String): List<String> {
        val rs = ArrayList<String>()
        var w = s
        var index = w.indexOf("@@{")
        while (index >= 0) {
            val endIndex = w.indexOf("}")
            val key = w.substring(index + 3, endIndex)
            w = w.substring(0, index) + w.substring(endIndex + 1, w.length)
            val values = keywords[key] ?: continue
            for (item in values) {
                rs.add(s.replace("@@{$key}", item))
            }
            index = w.indexOf("@@{")
        }
        return rs
    }

    open fun exec(cmd: String): String {
        return execResult(cmd).command
    }

    class X4CommandResult constructor(var command: String = "", var content: String = "") {

        fun isEmpty(): Boolean {
            return "" == command;
        }
    }

    open fun execResult(cmd: String): X4CommandResult {
        var s = cmd.trim { it <= ' ' }
        if (s.endsWith("。")) {
            s = s.substring(0, s.length - 1)
        }
        val w = matcher.match(s)
        if (cmds.containsKey(w)) {
            return X4CommandResult(cmds[w] ?: "", w);
        }
        return X4CommandResult()
    }

    fun addVariable(key: String, vararg values: String): X4CommandInvoker {
        var v = keywords[key] ?: ArrayList<String>()
        for (s in values) {
            v.add(s)
        }
        keywords.put(key, v)
        return this
    }

    companion object {

        @JvmField
        var RETURN = X4CommandInvoker().addCommand(arrayOf("返回上一页", "返回"), "返回")

        @JvmField
        var NUMBER = X4CNumberInvoker()


    }
}
