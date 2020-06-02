package com.chendroid.learning.bean

/**
 * @intro 新增 to`do 成功后，返回的数据
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/5/27
 */
data class TodoResponseData(
        val data: TodoData.TodoBaseData,
        val errorCode: Int,
        val errorMsg: String
)