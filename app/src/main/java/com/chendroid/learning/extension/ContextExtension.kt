package com.chendroid.learning.extension

import android.content.Context
import android.content.Intent
import com.chendroid.learning.ui.activity.AccountInfoActivity

/**
 * @intro Context 的扩展
 * @author zhaochen@ZhiHu Inc.
 * @since 2020-01-16
 */

/**
 * 打开 账号页面 「AccountInfoActivity」
 */
fun Context.openAccount() {
    val intent = Intent(this, AccountInfoActivity::class.java)
    this.startActivity(intent)
}


