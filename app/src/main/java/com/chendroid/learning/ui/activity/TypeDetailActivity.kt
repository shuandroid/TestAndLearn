package com.chendroid.learning.ui.activity

import Constant.EXTRA_ARTICLE_TAG_DATA
import Constant.EXTRA_TYPE_TITLE
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.chendroid.learning.R
import com.chendroid.learning.base.BaseActivity
import com.chendroid.learning.bean.ArticleTagData
import com.chendroid.learning.bean.BaseDatas
import com.chendroid.learning.bean.ChildrenTagData
import com.chendroid.learning.ui.holder.TagInnerHolder
import com.chendroid.learning.ui.holder.TypeDetailHolder
import com.chendroid.learning.ui.holder.data.ArticleBaseDataWrapper
import com.chendroid.learning.widget.view.StaggerItemDecoration
import com.google.android.flexbox.*
import com.zhihu.android.sugaradapter.SugarAdapter
import dp
import kotlinx.android.synthetic.main.activity_type_detail_layout.*

/**
 * @intro
 * @author zhaochen@ZhiHu Inc.
 * @since 2019-09-02
 */
class TypeDetailActivity : BaseActivity() {

    //类别数据类型
    private val typeList: MutableList<ChildrenTagData> = mutableListOf()
    private val typeHolderBuilder = SugarAdapter.Builder.with(typeList)
    private lateinit var typeSugarAdapter: SugarAdapter

    private var articleTypeTitle: String = ""


    // 具体文章列表
    private val detailList: MutableList<ArticleBaseDataWrapper> = mutableListOf()
    private val detailAdapterBuilder = SugarAdapter.Builder.with(detailList)
    private lateinit var detailSugarAdapter: SugarAdapter


    private val toobarView by lazy {
        toolbar
    }

    // 类型布局
    private val typeTagLayout: RecyclerView by lazy {
        type_tag_layout
    }

    private val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        type_swipe_refresh
    }

    private val detailRecyclerView: RecyclerView by lazy {
        type_recycler_view
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_type_detail_layout
    }

    override fun cancelRequest() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBundle()
        initImmersionBar()
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.toolbar).init()
    }

    override fun onStart() {
        super.onStart()

        initView()
    }


    /**
     * 解析 bundle 数据
     */
    private fun initBundle() {

//        intent.extras?.getParcelable(EXTRA_ARTICLE_TAG_DATA)

        intent.extras?.run {

            val extraData = getParcelable<ArticleTagData>(EXTRA_ARTICLE_TAG_DATA)

            articleTypeTitle = getString(EXTRA_TYPE_TITLE, "文章类型")

            extraData?.children?.run {
                typeList.addAll(this)
            }

        }

    }

    /**
     * 初始化 view
     */
    private fun initView() {

        toobarView.apply {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = articleTypeTitle
        }

        setupTagLayout()

        setupDetailRecyclerView()
    }

    /**
     * 设置文章类型的 layout
     */
    private fun setupTagLayout() {
        typeHolderBuilder.add(TagInnerHolder::class.java)

        typeSugarAdapter = typeHolderBuilder.build()

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.apply {
            // 正常换行
            flexWrap = FlexWrap.WRAP
            // 主轴方向，默认值，左端
            flexDirection = FlexDirection.ROW
            //交叉轴的起点对齐
            alignItems = AlignItems.FLEX_START
            //对齐方式，左对齐
            justifyContent = JustifyContent.FLEX_START
        }

        typeTagLayout.apply {
            isNestedScrollingEnabled = false
            this.layoutManager = layoutManager
            adapter = typeSugarAdapter
        }

        // 添加数据
        typeSugarAdapter.notifyDataSetChanged()
    }

    /**
     * 设置文章详情列表 RecyclerView
     */
    private fun setupDetailRecyclerView() {

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        detailRecyclerView.layoutManager = layoutManager

        detailAdapterBuilder.add(TypeDetailHolder::class.java)

        detailSugarAdapter = detailAdapterBuilder.build()

        detailRecyclerView.adapter = detailSugarAdapter
        detailRecyclerView.addItemDecoration(StaggerItemDecoration.with(this))

        val baseDatas = BaseDatas(0, 2, "hyahhaha", 12, "chaptername", 1, "wwww.zhihu.com"
                , "chendroid", 2, 1200, 3, 4, 4, "1234", 12, true)
        val articleBaseDataWrapper = ArticleBaseDataWrapper(baseDatas)

        // 构造假数据
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)
        detailList.add(articleBaseDataWrapper)

        detailSugarAdapter.notifyDataSetChanged()
    }

}