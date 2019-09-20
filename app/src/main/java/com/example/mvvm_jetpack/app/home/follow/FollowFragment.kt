package com.example.mvvm_jetpack.app.home.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_jetpack.BR
import com.example.mvvm_jetpack.R
import com.example.mvvm_jetpack.app.bean.EventBean
import com.example.mvvm_jetpack.databinding.FollowDataBinding
import com.example.mvvm_jetpack_lib.base.view.BaseLazyFragment
import com.example.mvvm_jetpack_lib.base.widget.StateLayout
import kotlinx.android.synthetic.main.fragment_follow.*
import java.util.*

/**
 * Description:
 * Date：2019/7/24-14:47
 * Author: cwh
 */
class FollowFragment : BaseLazyFragment<FollowViewModel, FollowDataBinding>() {


    override val mViewModel: FollowViewModel by lazy {
        createViewModel(this,FollowViewModel::class.java)
    }
    override val layoutId: Int
        get() = R.layout.fragment_follow
    override var mViewModelVariableId: Int = BR.viewModel


    private lateinit var mStateLayout: StateLayout

    private var mData:MutableList<EventBean> = ArrayList()

    private var mAdapter:FollowAdapter?=null

    private var mPageNum=1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mStateLayout = StateLayout(mActivity)
            .wrap(super.onCreateView(inflater, container, savedInstanceState)).showLoading()

        return mStateLayout
    }


    override fun onFragmentVisibleToUser(isVisible: Boolean) {

    }

    override fun onFragmentFirstVisible() {
        mViewModel.onRefresh()
    }

    override fun initViewObserver() {
        mViewModel.mRefreshList.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                mPageNum = 1
                mData.clear()
                mData.addAll(it)
                if (mAdapter == null) {
                    mAdapter = FollowAdapter(mActivity, mData)
                    mAdapter!!.onLoadingListener = {
                        mViewModel.getFollowEvent(mPageNum + 1)
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

        mViewModel.mLoadMoreList.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                mPageNum++
                mData.addAll(it)
                mAdapter?.notifyDataSetChanged()
                if(mData.isNullOrEmpty()){
                    mAdapter?.loadNoMoreData()
                }
            }
        })

        mViewModel.mState.observe(this, Observer {
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


        fun newInstance(tag: String): FollowFragment {
            val fragment = FollowFragment()
            val bundle = Bundle()
            bundle.putString("tag", tag)
            fragment.arguments = bundle
            return fragment
        }

    }

}