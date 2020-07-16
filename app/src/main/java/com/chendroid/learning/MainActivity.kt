package com.chendroid.learning

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.chendroid.learning.base.BaseActivity
import com.chendroid.learning.ui.activity.FirstMainActivity
import com.chendroid.learning.ui.activity.TryEveryThingActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val secondButton: Button by lazy {
        second_learning_button
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<Button>(R.id.first_learning_button).setOnClickListener {

            // 跳转到第一个 activity
            startActivity(Intent(this, FirstMainActivity::class.java))
        }

        initImmersionBar()
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.toolbar).init()
    }

    override fun onResume() {
        super.onResume()

        secondButton.setOnClickListener {
            startActivity(Intent(this, TryEveryThingActivity::class.java))
        }
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun cancelRequest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
