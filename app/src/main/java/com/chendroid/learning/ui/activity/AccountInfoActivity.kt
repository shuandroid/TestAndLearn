package com.chendroid.learning.ui.activity

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseActivity
import com.chendroid.learning.bean.LoginResponse
import com.chendroid.learning.vm.AccountViewModel
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.activity_account_info.*
import kotlinx.android.synthetic.main.activity_login_layout.*
import toast

/**
 * @intro 账号信息
 * @author zhaochen@ZhiHu Inc.
 * @since 2020-01-13
 */
class AccountInfoActivity : BaseActivity() {


    private lateinit var loginLayout: View
    private lateinit var userNameView: TextView
    private lateinit var userAvatarView: SimpleDraweeView

    private lateinit var loginNameView: AppCompatEditText
    private lateinit var loginPasswordView: AppCompatEditText
    private lateinit var loginConfirmButton: Button

    private lateinit var accountViewModel: AccountViewModel

    override fun setLayoutId(): Int {
        return R.layout.activity_account_info
    }

    override fun cancelRequest() {
    }

    override fun onResume() {
        super.onResume()

        bindViewModel()
        initView()
    }

    override fun initImmersionBar() {
        super.initImmersionBar()

        immersionBar.titleBar(R.id.account_toolbar).init()
    }

    /**
     * 绑定 viewModel
     */
    private fun bindViewModel() {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)

        // todo liveData 监听

        val loginDataObserver = Observer<LoginResponse> {
            loginLayout.visibility = View.GONE
            userNameView.text = it.data.username
            it.data.icon?.run {
                userAvatarView.setImageURI(this)
            }
        }

        accountViewModel.loginDataLD.observe(this, loginDataObserver)
    }

    /**
     *
     */
    private fun initView() {

        initImmersionBar()

        loginLayout = account_login_layout_root
        userNameView = account_user_name
        userAvatarView = account_user_avatar

        loginNameView = login_account_text
        loginPasswordView = login_password_text
        loginConfirmButton = login_confirm_button

        initNotLoginLayout()
    }

    private fun initNotLoginLayout() {

        loginConfirmButton.setOnClickListener {
            // 登陆
            val username = loginNameView.text.toString()
            val password = loginPasswordView.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                toast("账号和密码不能为空")
                return@setOnClickListener
            }
            accountViewModel.loginAccount(username, password)
        }
    }


}