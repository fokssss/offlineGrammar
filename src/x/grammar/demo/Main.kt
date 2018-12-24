package x.grammar.demo

import x.grammar.X4Callback
import x.grammar.X4CommandInvoker
import x.grammar.X4HotMatcher
import x.grammar.py.X4HotWord
import x.grammar.se.X4Lexer
import x.grammar.se.X4Program
import x.grammar.se.X4Result

import java.io.*

/**
 * Created by xyy on 2018/11/10.
 */
object Main {

    private val grammar_file = "/Users/xyy/src/projs/OfflineGrammarSimpleEdition/grammar/home.x4"
    private val hot_word_file = "/Users/xyy/src/projs/OfflineGrammarSimpleEdition/demo/names.txt"

    @Throws(IOException::class)
    @JvmStatic fun main(args: Array<String>) {
        val lexer = x4Lexer

        var prog = X4Program().addVariable("lookup", "查找", "看看")
                .addVariable("name")
                .addExpression("<lookup><name>")

        val ll = X4Lexer.build(prog)
        System.out.println(ll.find("你是我的小苹果"))
        System.out.println(ll.find("查找王小妹"))

//        val matcher = X4HotMatcher(lexer, hotWord)
//        val ssss = arrayOf("dd", "ss")

        val hotWord = getX4HotWord()
        for (i in 1..2000) {
            hotWord.addWord("姜振汤")
        }
        var cmd = X4CommandInvoker().addCommand(arrayOf("返回", "清空", "发送"), "Cancel")
        var start = System.currentTimeMillis()

        System.out.println(cmd.exec("上回"))
        println("done at " + (System.currentTimeMillis() - start))

        println("-----------2222----------->")

        start = System.currentTimeMillis()
        System.out.println(hotWord.match("姜振业"))
        println("done at " + (System.currentTimeMillis() - start))

        println("-----------3333----------->")

        start = System.currentTimeMillis()
        System.out.println(hotWord.match("姜振业"))
        println("done at " + (System.currentTimeMillis() - start))

//        var testString = "给杨林说明天开会"
//        var rs = lexer.find(testString)
//
//        rs?.let {
//            System.out.println(rs)
//        } ?: let {
//            System.out.println("no find")
//        }
//
//        lexer.findAsync(testString, object : X4Lexer.FindListener() {
//            override fun onFound(rs: X4Result) {
//                System.out.println("Async->$rs")
//            }
//
//            override fun onNoFind() {
//                System.out.println("Async->no find")
//            }
//
//        })
    }

    @Throws(IOException::class)
    private fun test() {
        val line = "让我们查看两根一的联系方式吧"

        //加载热词库
        val hotWord = getX4HotWord()

        //在语义中找到指定的关键词，再进行热词匹配
        var matcher = X4HotMatcher(x4Lexer)
        val rs = matcher.matchByKey(line, "name", hotWord)
        println(rs)

        //直接匹配热词，不进行语义分析
        matcher = X4HotMatcher()
        println(matcher.match("两根玉", hotWord))

        println(X4CommandInvoker.NUMBER.exec("第2个"))

    }

    @Throws(IOException::class)
    private fun getX4HotWord(): X4HotWord {
        val `is` = FileInputStream(hot_word_file)
        return X4HotWord.build(InputStreamReader(`is`))
    }

    private val x4Lexer: X4Lexer
        @Throws(IOException::class)
        get() {
            val `is` = FileInputStream(grammar_file)
            return X4Lexer.build(InputStreamReader(`is`))
        }
}
