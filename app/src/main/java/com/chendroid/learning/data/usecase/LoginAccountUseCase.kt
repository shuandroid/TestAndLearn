package com.chendroid.learning.data.usecase

import com.chendroid.care.data.Result
import com.chendroid.care.util.safeApiCall
import com.chendroid.learning.api.ApiServiceHelper
import com.chendroid.learning.api.NewWanService
import com.chendroid.learning.bean.LoginResponse
import java.lang.Exception

/**
 * @intro 登陆账号的 UseCase
 * @author zhaochen@ZhiHu Inc.
 * @since 2020-01-16
 */
class LoginAccountUseCase {

    private var service: NewWanService = ApiServiceHelper.newWanService

    suspend fun loginAccount(username: String, password: String): Result<LoginResponse> {

        service.loginAccount(username, password)

        return safeApiCall(call = { loginAccountReally(username, password) },
                errorMessage = "登陆失败")

    }

    private suspend fun loginAccountReally(username: String, password: String): Result<LoginResponse> {

        val loginResult = service.loginAccount(username, password)

        if (loginResult.isSuccessful) {
            loginResult.body()?.data?.run {
                return Result.Success(loginResult.body()!!)
            }
        }

        return Result.Error(Exception("登陆账号失败 error code ${loginResult.code()} error body is ${loginResult.errorBody()}"))

    }
}