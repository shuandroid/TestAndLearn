package com.chendroid.learning.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chendroid.care.data.Result
import com.chendroid.learning.bean.LoginResponse
import com.chendroid.learning.data.usecase.LoginAccountUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * @intro 有关账号的 viewModel 类
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-11-29
 */
class AccountViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "AccountViewModel"
    }

    private val loginAccountUseCase by lazy {
        LoginAccountUseCase()
    }


    /**
     * login 登陆的 LiveData
     */
    private val vmLoginDataLD = MutableLiveData<LoginResponse>()

    val loginDataLD: LiveData<LoginResponse> = vmLoginDataLD

    /**
     * 登陆失败
     */
    private val vmLoginErrorLD = MutableLiveData<Exception>()

    /**
     * 对外部暴露的 liveData
     */
    val loginErrorLD: LiveData<Exception> = vmLoginErrorLD


    /**
     * 登录，需要账号和密码
     */
    fun loginAccount(username: String, password: String) {

        loginAccountReally(username, password)
    }

    private fun loginAccountReally(username: String, password: String) {
        Log.i("zc_test", "loginAccountReally")
        viewModelScope.launch(IO) {

            val loginResult = loginAccountUseCase.loginAccount(username, password)

            if (loginResult is Result.Success) {
                Log.i("zc_test", "$TAG loginAccountReally() 成功")

                withContext(Main) {
                    vmLoginDataLD.value = loginResult.data
                }

            } else if (loginResult is Result.Error) {
                Log.i("zc_test", "$TAG loginAccountReally() 失败")
                withContext(Main) {
                    vmLoginErrorLD.value = loginResult.exception
                }
            }

        }

    }

}