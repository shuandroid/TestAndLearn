package com.chendroid.learning

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import com.chendroid.annotation.PreInflater
import com.chendroid.learning.base.BaseActivity
import com.chendroid.learning.ui.activity.FirstMainActivity
import com.chendroid.learning.ui.activity.TryEveryThingActivity
import com.chendroid.learning.utils.DexUtil
import com.chendroid.preinflater.AsyncWrapperLayoutInflater
import com.permissionx.guolindev.PermissionX
import dp
import kotlinx.android.synthetic.main.activity_main.*

@PreInflater(layout = R2.layout.activity_main, scheduler = "io")
class MainActivity : BaseActivity() {

    private val secondButton: Button by lazy {
        second_learning_button
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "Core fundamental are based on these permissions",
                    "OK",
                    "Cancel"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "You need to allow necessary permissions in Settings manually",
                    "OK",
                    "Cancel"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        this,
                        "These permissions are denied: $deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

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
        Log.d("zc_test", "MainActivity, getTargetTest is ${TargetTest.getTestText()} ")
        text_view.text = TargetTest.getTestText()
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun cancelRequest() {
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(AsyncWrapperLayoutInflater.getInstance(newBase).wrapContext(newBase))

    }

}
