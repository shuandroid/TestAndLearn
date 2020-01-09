package com.chendroid.learning.demo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @intro 测试 suspend 关键字和 coroutines
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-12-10
 */
object TestSuspend {

    suspend fun mainTest() {
        println("mainTest() start start start " + Thread.currentThread())
        a()
        b()
        c()
        println("mainTest() end end end" + Thread.currentThread())
    }

    fun test2() {
        println("test2() doing doing doing " + Thread.currentThread())
    }

    fun a() {
        println("a() doing doing doing " + Thread.currentThread())
    }

    fun c() {
        println("c() doing doing doing " + Thread.currentThread())
    }

    suspend fun b() {
        val test = "hahaa"
        println("b() start start start" + Thread.currentThread())
        coroutineScope {
            println("11111 线程 是" + Thread.currentThread())
            launch(Dispatchers.IO) {
                println("22222 线程 是" + Thread.currentThread())
                delay(1000)
                println("22222 线程结束" + Thread.currentThread())
            }
            println("33333 线程 是" + Thread.currentThread())
        }

        println("b() end end end" + Thread.currentThread())
    }
}