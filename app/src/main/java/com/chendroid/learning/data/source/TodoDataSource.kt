package com.chendroid.learning.data.source

import android.util.Log
import androidx.annotation.IntRange
import com.chendroid.care.data.Result
import com.chendroid.care.util.safeApiCall
import com.chendroid.learning.api.NewWanService
import com.chendroid.learning.bean.TodoData
import com.chendroid.learning.bean.TodoResponseData
import java.lang.Exception

/**
 * @intro 获取 to`do 的数据类
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/5/15
 */
class TodoDataSource(private val wanService: NewWanService) {

    suspend fun getAllTodoType(@IntRange(from = 1) pageNum: Int = 1, queryMap: Map<String, Int>? = null): Result<TodoData> {

        return safeApiCall(
                call = {
                    getAllTodoTypeReal(pageNum, queryMap)
                },
                errorMessage = "todo 失败")
    }


    private suspend fun getAllTodoTypeReal(pageNum: Int, queryMap: Map<String, Int>? = null): Result<TodoData> {

        val response = wanService.getTodoList(pageNum, queryMap)

        Log.i("zc_test", response.message() + response.toString())

        if (response.isSuccessful) {
            val body = response.body()
            body?.run {
                return Result.Success(this)
            }
        }

        return Result.Error(Exception("获取 todo 失败 error code ${response.code()} error body is ${response.errorBody()} \""))

    }

    suspend fun addNewTodo(title: String, content: String, todoType: Int): Result<TodoResponseData> {
        return safeApiCall(
                call = {
                    addNewTodoReally(title, content, todoType)
                },
                errorMessage = "todo 失败")
    }

    private suspend fun addNewTodoReally(title: String, content: String, todoType: Int): Result<TodoResponseData> {

        val response = wanService.addNewTodo(title, content, todoType)
        Log.i("zc_test", "addNewTodo response is " + response.message() + response.toString())

        if (response.isSuccessful) {
            val body = response.body()
            body?.run {
                return Result.Success(this)
            }
        }

        return Result.Error(Exception("获取 todo 失败 error code ${response.code()} error body is ${response.errorBody()} \""))
    }

}