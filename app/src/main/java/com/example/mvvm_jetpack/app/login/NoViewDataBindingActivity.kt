package com.example.mvvm_jetpack.app.login

import androidx.databinding.ViewDataBinding
import com.example.mvvm_jetpack.R
import com.example.mvvm_jetpack_lib.base.view.BaseActivity
import com.example.mvvm_jetpack_lib.base.viewmodel.NoViewModel

class NoViewDataBindingActivity : BaseActivity<NoViewModel, ViewDataBinding>() {
    override val mViewModel: NoViewModel by lazy {
        createViewModel(this, NoViewModel::class.java)
    }
    override var layoutId: Int = R.layout.activity_no_view_data_binding
    override var mViewModelVariableId: Int = -1

    override fun initDataAndView() {
        val fragment = NoViewDataBindingFragment()
        supportFragmentManager.beginTransaction().add(R.id.mFrame, fragment).commit()

    }

}
