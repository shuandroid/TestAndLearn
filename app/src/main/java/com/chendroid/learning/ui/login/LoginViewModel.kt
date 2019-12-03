package com.chendroid.learning.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @intro
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-11-04
 */
class LoginViewModel : ViewModel() {


    /**
     * 登录，需要账号和密码
     */
    fun login(username: String, password: String) {

        username.isNotBlank()

    }

    private fun launchLogin(username: String, password: String) {

        viewModelScope.launch {

            // 在主线程里面做
            withContext(Main) {
                //
                println("刷新 UI 之类的操作")
            }

            // 进行网络请求



        }

        GlobalScope
    }
}