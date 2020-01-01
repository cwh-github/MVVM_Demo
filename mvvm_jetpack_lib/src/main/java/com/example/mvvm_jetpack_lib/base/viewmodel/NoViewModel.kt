package com.example.mvvm_jetpack_lib.base.viewmodel

import android.app.Application
import com.example.mvvm_jetpack_lib.base.repository.IRepository

/**
 * Description:不做任何操作的其他的数据操作的ViewModel，只具有BaseViewModel
 * 的属性，用于不需要ViewModel的界面(但是还是会具有一些基本的ViewModel的操作，比如
 * 显示对话框，跳转界面)
 * Date：2020/1/1-16:37
 * Author: cwh
 */
class NoViewModel(application: Application) : BaseViewModel<IRepository>(application) {
    override val repo: IRepository
        get() = object :IRepository{
            override fun onClear() {
            }
        }

}