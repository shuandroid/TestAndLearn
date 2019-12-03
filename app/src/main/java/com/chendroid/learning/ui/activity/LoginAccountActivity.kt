package com.chendroid.learning.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chendroid.learning.R

/**
 * @intro 登录账号的 activity
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-11-20
 */
class LoginAccountActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login_layout)

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun onStart() {
        super.onStart()

    }






}