package com.example.mvvm_jetpack.app.login

import androidx.databinding.ViewDataBinding
import com.example.mvvm_jetpack.R
import com.example.mvvm_jetpack_lib.base.view.BaseFragment
import com.example.mvvm_jetpack_lib.base.viewmodel.NoViewModel

/**
 * Description:
 * Date：2020/1/1-17:01
 * Author: cwh
 */
class NoViewDataBindingFragment : BaseFragment<NoViewModel, ViewDataBinding>() {
    override val mViewModel: NoViewModel by lazy {
        createViewModel(this,NoViewModel::class.java)
    }
    override val layoutId: Int= R.layout.fragment_no_view_data_binding
    override var mViewModelVariableId: Int=-1


    override fun initDataAndView() {
        super.initDataAndView()
    }

}