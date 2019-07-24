package com.example.mvvm_jetpack_lib.base.view

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.example.mvvm_jetpack_lib.base.view.BaseFragment
import com.example.mvvm_jetpack_lib.base.viewmodel.BaseViewModel

/**
 * Description:懒加载Fragment
 * Date：2019/7/17-22:54
 * Author: cwh
 */
abstract class BaseLazyFragment<VM : BaseViewModel<*>, V : ViewDataBinding> :
    BaseFragment<VM, V>() {


    /**
     * fragment 是否是第一次可见的标志
     */
    private var isFirstVisible = false


    /**
     * fragment 是否已准备好
     */
    private var isPrepareView = false

    /**
     * 对用户来说是否可见
     */
    private var isVisibleToUser = false



    private fun initStatus() {
        isFirstVisible = true
        isPrepareView = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        initStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPrepareView = true
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false
                onFragmentFirstVisible()
            }
            onFragmentVisibleToUser(true)
        }
    }


    /**
     * Fragment 是否已准备好，且对用户可见
     *
     * @return
     */
    fun isFragmentVisible(): Boolean {
        return isVisibleToUser && isPrepareView
    }

    override fun onResume() {
        super.onResume()
        if (isVisibleToUser) {
            onFragmentVisibleToUser(true)
        }
    }


    /**
     * fragment 对用户是否可见发生改变时调用
     *
     * @param isVisible 是否可见
     */
    protected abstract fun onFragmentVisibleToUser(isVisible: Boolean)

    /**
     * fragment 第一次对用户可见
     */
    protected abstract fun onFragmentFirstVisible()

    //当对用户是否可见改变时，调用
    override fun setUserVisibleHint(isVisible: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isVisibleToUser = isVisible
        if (!isPrepareView) {
            return
        }
        if (isFirstVisible && isVisibleToUser) {
            isFirstVisible = false
            onFragmentFirstVisible()
        }
        onFragmentVisibleToUser(isVisibleToUser)
    }

}