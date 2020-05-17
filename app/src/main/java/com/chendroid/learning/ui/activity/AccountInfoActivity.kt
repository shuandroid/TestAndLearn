package com.chendroid.learning.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseActivity
import com.chendroid.learning.base.Preference
import com.chendroid.learning.bean.LoginResponse
import com.chendroid.learning.vm.AccountViewModel
import com.chendroid.learning.widget.view.TagView
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.activity_account_info.*
import kotlinx.android.synthetic.main.activity_login_layout.*
import kotlinx.android.synthetic.main.activity_todo.*
import toast
import java.lang.Exception

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

    private lateinit var toolbar: Toolbar

    private lateinit var accountViewModel: AccountViewModel

    /**
     * 是否登陆
     */
    private var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)

    /**
     * 用户名字
     */
    private var username: String by Preference(Constant.USERNAME_KEY, "")

    /**
     * 密码
     */
    private var password: String by Preference(Constant.PASSWORD_KEY, "")

    private lateinit var todoTagView: TagView

    override fun setLayoutId(): Int {
        return R.layout.activity_account_info
    }

    override fun cancelRequest() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
    }

    /**
     * 初始化 toolbar 相关信息
     */
    private fun initToolbar() {
        toolbar = account_toolbar
        toolbar.title = "账户"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setOnClickListener {
            finish()
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }
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

        // todo liveData 监听. 如果逻辑复杂的话，需要拆出来 Presenter  去处理这些逻辑

        val loginDataObserver = Observer<LoginResponse> {
            loginLayout.visibility = View.GONE
            userNameView.text = it.data.username
            it.data.icon?.run {
                userAvatarView.setImageURI(this)
            }
            saveLoginData(it.data)
        }

        val loginErrorObserver = Observer<Exception> {
            toast("登陆失败，因为：${it.toString()}")
        }

        accountViewModel.loginDataLD.observe(this, loginDataObserver)
        accountViewModel.loginErrorLD.observe(this, loginErrorObserver)
    }

    /**
     * 保存登陆后的数据
     */
    private fun saveLoginData(resultData: LoginResponse.LoginData) {
        isLogin = true
        username = resultData.username
        password = resultData.password
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

        initAccountView()
    }

    private fun initNotLoginLayout() {

        if (isLogin) {
            loginLayout.visibility = View.GONE
            userNameView.text = username
            return
        }

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

    private fun initAccountView() {
        todoTagView = account_todo

        todoTagView.setOnClickListener {
            // 跳转到 我的 to`do 界面
            startActivity(Intent(this, TodoActivity::class.java))
        }
    }


}