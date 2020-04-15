package com.chendroid.learning.ui.holder

import android.animation.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.chendroid.learning.R
import com.chendroid.learning.bean.BaseDatas
import com.chendroid.learning.ui.activity.ContentDetailActivity
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarHolder
import kotlinx.android.synthetic.main.layout_article_list_item.view.*

/**
 * @intro 文章列表的 item holder
 * @author zhaochen @ Zhihu Inc.
 * @since  2019/5/20
 */
@Layout(R.layout.layout_article_list_item)
class HomeListItemHolder(view: View) : SugarHolder<BaseDatas>(view), View.OnClickListener {


    interface HomeItemListener {
        /**
         * articleId, 当前文章 id
         * isCollected, 是否收藏过
         * handleResult, 高阶函数，
         */
        fun onCollectViewClicked(articleId: Int, originId: Int, isCollected: Boolean, handleResult: (Boolean) -> Unit)
    }

    var homeItemListener: HomeItemListener? = null

    private val authorTextView by lazy {
        itemView.home_item_author
    }

    //    private val authorTextView : TextView by lazy { view.findViewById<TextView>(R.id.home_item_author) }
    private val articleTitleView by lazy {
        itemView.home_item_title
    }

    private val articleDateView by lazy {
        itemView.home_item_date
    }

    private val articleTypeView by lazy {
        itemView.home_item_type
    }

    private val articleLikeView by lazy {
        itemView.home_item_like
    }

    private var animatorAliveSet: AnimatorSet? = null

    init {
        itemView.setOnClickListener(this)
        articleLikeView.setOnClickListener(this)
    }

    override fun onBindData(data: BaseDatas) {

        Log.i("zc_test", "HomeListItemHolder onBindData()")
        if (data.author.isEmpty()) {
            authorTextView.text = data.shareUser
        } else {
            authorTextView.text = data.author
        }
        articleTitleView.text = data.title
        articleDateView.text = data.niceDate
        articleTypeView.text = data.chapterName

        if (data.collect) {
            //收藏了该文章 todo
            articleLikeView.setImageDrawable(getDrawable(R.drawable.ic_action_like))
        } else {
            articleLikeView.setImageDrawable(getDrawable(R.drawable.ic_action_no_like))
        }
    }

    override fun onClick(clickView: View) {

        if (clickView === articleLikeView) {
            handleCollectViewClicked()
        } else if (clickView === itemView) {
            // 进入具体的文章界面
            Intent(context, ContentDetailActivity::class.java).run {
                val bundle = Bundle()
                bundle.putString(Constant.CONTENT_TITLE_KEY, this@HomeListItemHolder.data.title)
                bundle.putString(Constant.CONTENT_URL_KEY, this@HomeListItemHolder.data.link)
                bundle.putInt(Constant.CONTENT_ID_KEY, this@HomeListItemHolder.data.id)
                putExtras(bundle)
                context.startActivity(this)
            }
        }
    }

    /**
     * 开启收藏喜欢动画， 难看的动画  「只有缩放」
     */
    private fun startLikeAnimator() {

        val scaleXAnim = ObjectAnimator.ofFloat(articleLikeView, "scaleX", 0.4f)

        val scaleYAnim = ObjectAnimator.ofFloat(articleLikeView, "scaleY", 0.4f)

        val animatorSet = AnimatorSet()
        val animatorNewSet = AnimatorSet()

        animatorSet.playTogether(scaleXAnim, scaleYAnim)
        animatorSet.duration = 300
        animatorSet.start()

        animatorAliveSet = animatorSet

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                Log.i("zc_test", "  111111 onAnimationEnd")
                if (data.collect) {
                    articleLikeView.setImageDrawable(getDrawable(R.drawable.ic_action_like))
                } else {
                    articleLikeView.setImageDrawable(getDrawable(R.drawable.ic_action_no_like))
                }
                animatorNewSet.start()
                animatorAliveSet = animatorNewSet
            }
        })

        val scaleXNewAnim = ObjectAnimator.ofFloat(articleLikeView, "scaleX", 0.4f, 1.2f, 1f)
        val scaleYNewAnim = ObjectAnimator.ofFloat(articleLikeView, "scaleY", 0.4f, 1.2f, 1f)

        animatorNewSet.playTogether(scaleXNewAnim, scaleYNewAnim)
        animatorNewSet.duration = 300

        animatorNewSet.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                Log.i("zc_test", " animatorNewSet onAnimationEnd")
            }
        })
    }

    override fun onViewDetachedFromWindow() {
        super.onViewDetachedFromWindow()
        animatorAliveSet?.run {
            if (isRunning) {
                cancel()
            }
        }
    }

    private fun collectArticle() {

//        ViewModelProvider().get()

    }

    /**
     * 处理收藏 view 的点击事件
     */
    private fun handleCollectViewClicked() {
        //
        homeItemListener?.run {
            if (data.collect) {
                onCollectViewClicked(data.id, data.originId,  data.collect) { isSuccess -> handleUnCollectArticleResult(isSuccess) }
            } else {
                onCollectViewClicked(data.id, data.originId, data.collect) { isSuccess -> handleCollectArticleResult(isSuccess) }
            }
        }
    }

    /**
     * 处理收藏文章的结果，成功则动画展示成功；失败，则可以弹窗显示「收藏失败」
     */
    private fun handleCollectArticleResult(isSuccess: Boolean) {
        if (isSuccess) {
            data.collect = true
            startLikeAnimator()
        }
    }

    private fun handleUnCollectArticleResult(isSuccess: Boolean) {
        if (isSuccess) {
            data.collect = false
            startLikeAnimator()
        }
    }

}