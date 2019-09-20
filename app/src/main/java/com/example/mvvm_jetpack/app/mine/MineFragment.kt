package com.example.mvvm_jetpack.app.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mvvm_jetpack.BR
import com.example.mvvm_jetpack.R
import com.example.mvvm_jetpack.databinding.MineDataBinding
import com.example.mvvm_jetpack_lib.base.view.BaseFragment
import com.example.mvvm_jetpack_lib.base.widget.StateLayout

/**
 * Description:
 * Date：2019/7/22-19:14
 * Author: cwh
 */
class MineFragment : BaseFragment<MineViewModel, MineDataBinding>() {
    override val mViewModel: MineViewModel by lazy {
        createViewModel(this, MineViewModel::class.java)
    }
    override val layoutId: Int
        get() = R.layout.fragment_mine
    override var mViewModelVariableId: Int = BR.viewModel

    private lateinit var mStatLayout: StateLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mStatLayout = StateLayout(activity!!).wrap(super.onCreateView(inflater, container, savedInstanceState))
            .showLoading()
        return mStatLayout
    }

    override fun initDataAndView() {
        mViewModel.getMineInfo()
    }

    override fun initViewObserver() {
        mViewModel.mState.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                handleState(it)
            }
        })

    }

    private fun handleState(status: Boolean) {
        if (status) {
            mStatLayout.showContentView()
        } else {
            mStatLayout.showEmptyView()
        }
    }


    companion object {

        fun newInstance(tag: String): MineFragment {
            val fragment = MineFragment()
            val bundle = Bundle()
            bundle.putString("tag", tag)
            fragment.arguments = bundle
            return fragment
        }

    }
}