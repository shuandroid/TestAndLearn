package com.chendroid.learning.bean

/**
 * @intro
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/5/16
 */
class BaseDatas(
        var id: Int,
        var originId: Int,
        var title: String,
        var chapterId: Int,
        var chapterName: String?,
        var envelopePic: Any,
        var link: String,
        var author: String,
        var origin: Any,
        var publishTime: Long,
        var zan: Any,
        var desc: Any,
        var visible: Int,
        var niceDate: String,
        var courseId: Int,
        var collect: Boolean)