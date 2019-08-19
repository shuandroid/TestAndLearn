package com.chendroid.learning.bean

/**
 * @intro
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-12
 */
data class LoginResponse(
        var errorCode: Int,
        var errorMsg: String,
        var data: LoginData
) {
    data class LoginData(
            var id: Int,
            var username: String,
            var password: String,
            var icon: String?,
            var type: Int,
            var collectIds: List<Int>?
    )
}