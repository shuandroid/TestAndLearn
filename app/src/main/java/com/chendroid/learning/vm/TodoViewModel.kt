package com.chendroid.learning.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chendroid.care.data.Result
import com.chendroid.learning.bean.TodoData
import com.chendroid.learning.data.usecase.TodoUseCase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @intro
 * @author zhaochen@ZhiHu Inc.
 * @since 2020/5/15
 */
class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val todoUseCase by lazy {
        TodoUseCase()
    }

    // 数据源
    val todoListLiveData = MutableLiveData<List<TodoData.TodoBaseData>>()

    fun getDoingTodoList(pageNum: Int = 1) {

        viewModelScope.launch(IO) {
            val result = todoUseCase.getTodoListDoing(pageNum)
            if (result is Result.Success) {
                // 这里成功了，必然是有数据的
                val resultData = result.data.data.datas
                resultData?.run {
                    withContext(Main) {
                        todoListLiveData.value = this@run
                    }
                }
            } else if (result is Result.Error) {
                // 失败了 todo 暂时处理
                Log.i("zc_test", result.toString())
            }
        }
    }

    fun getDoneTodoList(pageNum: Int = 1) {

        viewModelScope.launch(IO) {
            val result = todoUseCase.getTodoListFinished(pageNum)
            if (result is Result.Success) {
                // 这里成功了，必然是有数据的
                val resultData = result.data.data.datas
                resultData?.run {
                    withContext(Main) {
                        todoListLiveData.value = this@run
                    }
                }
            } else if (result is Result.Error) {
                // 失败了 todo 暂时处理
                Log.i("zc_test", result.toString())
            }
        }
    }


}