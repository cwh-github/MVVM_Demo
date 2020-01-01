package com.example.mvvm_jetpack.app.hot

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_jetpack.BR
import com.example.mvvm_jetpack.R
import com.example.mvvm_jetpack.databinding.HotDataBinding
import com.example.mvvm_jetpack_lib.base.view.BaseFragment
import com.example.mvvm_jetpack_lib.base.widget.StateLayout
import com.example.mvvm_jetpack_lib.utils.LogUtils
import com.example.mvvm_jetpack_lib.utils.ToastUtils
import com.m4coding.coolhub.api.datasource.bean.HotDataBean
import kotlinx.android.synthetic.main.fragment_hot.*
import java.util.ArrayList

/**
 * Description:
 * Date：2019/7/22-19:13
 * Author: cwh
 */
class HotFragment : BaseFragment<HotViewModel, HotDataBinding>() {
    override val mViewModel: HotViewModel by lazy {
        createViewModel(this, HotViewModel::class.java)
    }
    override val layoutId: Int
        get() = R.layout.fragment_hot
    override var mViewModelVariableId: Int = BR.viewModel



    private var mData: MutableList<HotDataBean> = ArrayList()
    private var mAdapter: HotAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initDataAndView() {
        mStatLayout.showLoading()
        mViewModel.getTrending()
    }

    override fun initViewObserver() {
        mViewModel.mHotList.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                if (mAdapter == null) {
                    mData.clear()
                    mData.addAll(it)
                    mRecyclerView.layoutManager = LinearLayoutManager(
                        mActivity,
                        RecyclerView.VERTICAL, false
                    )
                    mRecyclerView.adapter = mAdapter
                } else {
                    mAdapter!!.notifyDataSetChanged()
                }
                if (mData.isEmpty()) {
                    mStatLayout.showEmptyView()
                } else {
                    mStatLayout.showContentView()
                }
            }
        })

        mViewModel.finish.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                setStatLayout(it)
            }

            mRefreshLayout.isRefreshing = false
        })

    }

    private fun setStatLayout(result: Boolean) {
        if (result) {
            if (mData.isEmpty()) {
                mStatLayout.showEmptyView()
            } else {
                mStatLayout.showContentView()
            }

        } else {
            if (mData.isEmpty()) {
                ToastUtils.showToast(mActivity, "Show Error")
                LogUtils.d("Finish Inflate on Error")
                    mStatLayout.showErrorView()

            } else {
                mStatLayout.showContentView()
            }
        }
    }

    companion object {


        fun newInstance(tag: String): HotFragment {
            val fragment = HotFragment()
            val bundle = Bundle()
            bundle.putString("tag", tag)
            fragment.arguments = bundle
            return fragment
        }

    }
}