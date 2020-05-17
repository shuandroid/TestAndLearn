package com.chendroid.learning.data.usecase

import com.chendroid.care.data.Result
import com.chendroid.learning.api.ApiServiceHelper
import com.chendroid.learning.bean.TodoData
import com.chendroid.learning.data.source.TodoDataSource

/**
 * @intro useCase 在这里构造出具体的请求类型，设置 pageNum 和 queryMap
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/5/15
 */
class TodoUseCase() {

    private val todoDataSource = TodoDataSource(ApiServiceHelper.newWanService)

    suspend fun getTodoList(): Result<TodoData> {

        val result = todoDataSource.getAllTodoType()

        return result
    }

}