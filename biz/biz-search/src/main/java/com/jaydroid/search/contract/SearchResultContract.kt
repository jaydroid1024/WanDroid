package com.jaydroid.search.contract

import com.jaydroid.base_component.base.mvp.IView
import com.jaydroid.base_component.network.bean.wan.search.SearchResultResponse

interface SearchResultContract {
    interface View : IView {
        fun onSearchResult(page: Int, response: SearchResultResponse?)
    }

    interface Presenter {
        fun getSearchResult(page: Int, keyword: String?)
    }
}