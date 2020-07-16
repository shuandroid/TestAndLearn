package com.chendroid.learning.bean

/**
 * @intro 登陆的返回数据
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-12
 */
data class LoginResponse(
        var errorCode: Int,
        var errorMsg: String,
        var data: LoginData
) {
    /**
     * 登陆成功返回的数据
     */
    data class LoginData(
            var id: Int,
            var username: String,
            var password: String,
            var icon: String?,
            var type: Int,
            var collectIds: List<Int>?
    )
}