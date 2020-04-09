package com.jaydroid.favorite

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jaydroid.base_component.arouter.ARHelper
import com.jaydroid.base_component.base.mvp.BaseMVPActivity
import com.jaydroid.base_component.network.bean.wan.Article
import com.jaydroid.base_component.network.bean.wan.ArticleResponse
import com.jaydroid.base_component.widget.LinearItemDecoration
import com.jaydroid.favorite.adapter.FavoriteAdapter
import com.jaydroid.favorite.contract.FavoriteContract
import com.jaydroid.favorite.presenter.FavoritePresenter
import com.scwang.smartrefresh.layout.SmartRefreshLayout

@Route(path = ARHelper.PathFavorite.FAVORITE_ACTIVITY_PATH)
class FavoriteActivity : BaseMVPActivity<FavoriteContract.View, FavoritePresenter>(),
    FavoriteContract.View {

    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter
    private var curPage = 0
    private lateinit var toolbar: Toolbar
    private var dataList: ArrayList<Article> = ArrayList()
    private var mCurPage: Int = 0

    override fun getLayoutResId(): Int {
        return R.layout.activity_favorite
    }

    override fun createPresenter(): FavoritePresenter {
        return FavoritePresenter()
    }

    override fun initView() {
        refreshLayout = findViewById(R.id.srl_favorite)
        refreshLayout.setEnableRefresh(false)
        toolbar = findViewById(R.id.tb_favorite)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "收藏"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        recyclerView = findViewById(R.id.rv_favorite)
        recyclerView.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        val itemDecoration = LinearItemDecoration(mContext).color(
            ContextCompat.getColor(
                this,
                R.color.cornsilk
            )
        )
            .height(1f)
            .margin(15f, 15f)
        recyclerView.addItemDecoration(itemDecoration)
        favoriteAdapter = FavoriteAdapter(R.layout.item_home_recycler)
        favoriteAdapter.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                val url = dataList[position].link
                val bundle = Bundle()
                //                bundle.putString(WebViewActivity.URL, url)
                //                gotoActivity(mContext as Activity, WebViewActivity().javaClass, bundle)
            }
        recyclerView.adapter = favoriteAdapter
    }

    override fun initData() {
        super.initData()
        presenter.getArticleFavorites(curPage)
        refreshLayout.setOnLoadMoreListener { presenter.getArticleFavorites(mCurPage) }
    }

    override fun onArticleFavorite(page: Int, response: ArticleResponse?) {
        refreshLayout.finishLoadMore()
        if (response?.datas != null) {
            dataList.addAll(response.datas!!)
        }
        mCurPage = page + 1
        favoriteAdapter.setNewData(dataList)
    }


    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

}