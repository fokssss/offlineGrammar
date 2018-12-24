package x.grammar.demo

/**
 * Created by xyy on 2018/12/1.
 */

class Test {

    fun test() {
        var s:String? = null
        s = "dddd"

        var x = s?:""

        System.out.println(x)
    }
}