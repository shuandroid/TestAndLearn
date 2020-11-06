package com.chendroid.learning.demo

import com.chendroid.learning.utils.ViewOutlineProviderUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * @intro 内联函数部分
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-10-23
 */

fun main(args: Array<String>) {
////    Log.i("zc_test", "main() start")
//    println("zc_test\", \"main() start")
//    val test = TestInline()
//    test.makeTest()
////    Log.i("zc_test", "main() end")
//    test.lock(TestInline.TestLock()) { test.foo({ return print(it + "enenen") }, {}) }
//    println("zc_test\", \"main() end")


    println("class is " + Test123::class.java)

    val test = Test123()

    println("class is 实例是：  " + test::class.java)

}

class Test123() {
}

class TestInline() {

    inline fun makeTest() {
        println("zc_test makeTest")
    }

    inline fun foo(testBody: (String) -> Boolean, noinline body: () -> Unit) {

        ordinaryFunction(body)
        testBody("haha")
        ordinaryFunction {
            println("zc_testlabama 表达式退出")
            return@ordinaryFunction
        }
        println("zc_test --->foo() end")
    }

    inline fun ordinaryFunction(block: () -> Unit) {
        println("hahha")
        block()
        println("hahha233333")
    }


    class TestLock

    inline fun <T> lock(lock: TestLock, body: () -> T) {

        body()
    }


    inline fun test1(body: () -> Unit) {

        test2(body, "从一到二")

    }

    inline fun test2(body: () -> Unit, testString: String) {

    }

    class TreeNode() {
        val parent: TreeNode = TreeNode()
    }

//    fun <T> TreeNode.findParentType(clazz: Class<T>): T? {
//        val p = parent
//
//    }

}
