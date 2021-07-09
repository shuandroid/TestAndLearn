package com.chendroid.learning.utils

/**
 * 组合类， 三种不同种类的 class 组合, 不可为空
 * 可以参考：#Tuples.Triple
 */
class Triple<out A, out B , out C>(val first: A, val second:B, val third: C) {
    override fun toString(): String = "($first, $second, $third)"
}