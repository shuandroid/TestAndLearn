package com.chendroid.learning.data.usecase

import androidx.annotation.IntDef
import androidx.annotation.IntRange
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

    /**
     *  * 「status」, 状态， 1 表示完成；0 未完成; 默认全部展示；
     * 「type」大于0的整数, 创建时传入的类型, 默认全部展示; 目前有「工作」、「学习」、「生活」
     * 「priority」大于0的整数, 创建时传入的优先级；默认全部展示
     * 「orderby」 1:完成日期顺序；2.完成日期逆序；3.创建日期顺序；4.创建日期逆序(默认)；
     */

    private val todoDataSource = TodoDataSource(ApiServiceHelper.newWanService)


    companion object {
        // to·do 类型工作
        const val TYPE_TODO_JOB = 1
        // 学习
        const val TYPE_TODO_LEARN = 2
        // 生活
        const val TYPE_TODO_LIFE = 3
        // 默认
        const val TYPE_TODO_DEFAULT = 4
    }

    // to·do 列表的类型
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        TYPE_TODO_JOB,
        TYPE_TODO_LEARN,
        TYPE_TODO_LIFE,
        TYPE_TODO_DEFAULT)
    annotation class TodoType {
    }


    /**
     * 获取完成的 to'do 列表
     */
    suspend fun getTodoListFinished(pageNum: Int, @TodoType type: Int = 1, orderBy: Int = 4): Result<TodoData> {

        val queryMap = mutableMapOf<String, Int>()

        // 1 表示完成
        queryMap["status"] = 1
        queryMap["type"] = type
        queryMap["orderby"] = orderBy

        return getTodoListReally(pageNum, queryMap)
    }

    /**
     * 获取未完成的 to'do 列表
     */
    suspend fun getTodoListDoing(pageNum: Int, type: Int = 1, orderBy: Int = 4): Result<TodoData> {

        val queryMap = mutableMapOf<String, Int>()
        // 0 表示未完成
        queryMap["status"] = 0
        queryMap["type"] = type
        queryMap["orderby"] = orderBy

        return getTodoListReally(pageNum, queryMap)
    }

    // 设计成多中请求的情况
    private suspend fun getTodoListReally(@IntRange(from = 1) pageNum: Int = 1, queryMap: Map<String, Int>): Result<TodoData> {

        val result = todoDataSource.getAllTodoType(pageNum, queryMap)


        return result
    }


}