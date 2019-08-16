package com.chendroid.learning.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseActivity
import com.just.agentweb.AgentWeb
import getAgentWeb
import kotlinx.android.synthetic.main.activity_content_detail_layout.*

/**
 * @intro 内容详情页
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-08-10
 */
class ContentDetailActivity : BaseActivity() {

    private lateinit var agentWeb: AgentWeb

    // 标题
    private lateinit var contentTitle: String

    private var contentId: Int = 0

    // 链接
    private lateinit var contentUrl: String


    private val toolbarView by lazy {
        toolbar
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.toolbar).init()
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_content_detail_layout
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbarView.run {
            title = "加载中"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }

        initImmersionBar()

        intent.extras?.run {
            contentId = getInt(Constant.CONTENT_ID_KEY, 0)
            contentTitle = getString(Constant.CONTENT_TITLE_KEY, "")

            toolbarView.title = contentTitle

            contentUrl = getString(Constant.CONTENT_URL_KEY, "")

            agentWeb = contentUrl.getAgentWeb(this@ContentDetailActivity, web_content, LinearLayout.LayoutParams(-1, -1))
        }
    }

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun cancelRequest() {

    }

    override fun onResume() {
        agentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onPause() {
        agentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        agentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (agentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else {
            finish()
            super.onKeyDown(keyCode, event)
        }
    }
}