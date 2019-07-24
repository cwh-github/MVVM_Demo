package com.example.mvvm_jetpack.app.home.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_jetpack.BR
import com.example.mvvm_jetpack.R
import com.example.mvvm_jetpack.databinding.HomeDataBinding
import com.example.mvvm_jetpack_lib.base.view.BaseFragment
import com.example.mvvm_jetpack_lib.base.view.BaseLazyFragment
import com.example.mvvm_jetpack_lib.base.widget.StateLayout
import com.m4coding.coolhub.api.datasource.bean.RecommendBean
import kotlinx.android.synthetic.main.fragment_recommend.*

/**
 * Description:
 * Date：2019/7/22-18:59
 * Author: cwh
 */
class RecommendFragment : BaseLazyFragment<RecommendViewModel, HomeDataBinding>() {


    override val mViewModel: RecommendViewModel by lazy {
        createViewModel(this, RecommendViewModel::class.java)
    }
    override val layoutId: Int = R.layout.fragment_recommend
    override var mViewModelVariableId: Int = BR.viewModel

    lateinit var mStateLayout: StateLayout

    private var mData: ArrayList<RecommendBean> = ArrayList()
    private var mAdapter: RecommendAdapter? = null
    private var mPageNum = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mStateLayout =
            StateLayout(mActivity.applicationContext).wrap(super.onCreateView(inflater, container, savedInstanceState))
                .showLoading()
        return mStateLayout
    }


    override fun onFragmentVisibleToUser(isVisible: Boolean) {
    }

    override fun onFragmentFirstVisible() {
        mViewModel.onRefresh()
    }

    override fun initDataAndView() {

    }

    override fun initViewObserver() {
        //刷新列表返回的数据
        mViewModel.mRefreshList.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                //ToastUtils.showToast(mActivity, "Come in ${it.size}")
                mPageNum = 1
                mData.clear()
                mData.addAll(it)
                if (mAdapter == null) {
                    mAdapter = RecommendAdapter(mActivity, mData)
                    mAdapter!!.onLoadingListener = {
                        mViewModel.getRecommendList(mPageNum + 1)
                    }
                    mRecyclerView.layoutManager = LinearLayoutManager(
                        mActivity,
                        RecyclerView.VERTICAL, false
                    )
                    mRecyclerView.adapter = mAdapter
                } else {
                    mAdapter!!.notifyDataSetChanged()
                }
            }
        })

        //加载更多返回数据
        mViewModel.mLoadMoreList.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                mPageNum++
                mData.addAll(it)
                mAdapter?.notifyDataSetChanged()
                if(mData.isNullOrEmpty()){
                    mAdapter?.loadNoMoreData()
                }
            }
        })

        //加载成功或失败的状态
        mViewModel.mState.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                setLayoutStatus(it)
            }
        })
    }

    private fun setLayoutStatus(isSuccess: Boolean) {
        if (isSuccess) {
            if (mData.isEmpty()) {
                mStateLayout.showEmptyView()
            } else {
                mStateLayout.showContentView()
            }
            if (mRefreshLayout.isRefreshing) {
                mRefreshLayout.isRefreshing = false
            }
            if (mAdapter?.isLoading() == true) {
                mAdapter?.enableLoadMore()
            }
        } else {
            if (mData.isEmpty()) {
                mStateLayout.showErrorView()
            } else {
                mStateLayout.showContentView()
            }
            if (mRefreshLayout.isRefreshing) {
                mRefreshLayout.isRefreshing = false
            }
            if (mAdapter?.isLoading() == true) {
                mAdapter?.loadFail()
            }

        }
    }


    companion object {


        fun newInstance(tag: String): RecommendFragment {
            val fragment = RecommendFragment()
            val bundle = Bundle()
            bundle.putString("tag", tag)
            fragment.arguments = bundle
            return fragment
        }

    }
}