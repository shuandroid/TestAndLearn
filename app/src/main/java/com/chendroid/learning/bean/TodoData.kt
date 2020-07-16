package com.chendroid.learning.bean

/**
 * @intro to·do 数据
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/5/15
 */
data class TodoData(
        val data: TodoDataList,
        val errorCode: Int,
        val errorMsg: String
) {

    /**
     * 基础数据类 to`do
     */
    data class TodoBaseData(
            val completeDate: Long,
            val completeDateStr: String,
            val content: String?,
            val date: Long,
            val dateStr: String,
            val id: Int,
            val priority: Int,
            val status: Int,
            val title: String?,
            val type: Int,
            val userId: Int
    )

    /**
     * to·do 集合
     */
    data class TodoDataList(
            val curPage: Int,
            val datas: List<TodoBaseData>?,
            val offset: Int,
            val over: Boolean,
            val pageCount: Int,
            val size: Int,
            val total: Int
    )


}