package com.example.mvvm_jetpack.app.hot

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.mvvm_jetpack.app.bean.EventBean
import com.example.mvvm_jetpack_lib.base.Event
import com.example.mvvm_jetpack_lib.base.command.BindingAction
import com.example.mvvm_jetpack_lib.base.command.BindingCommand
import com.example.mvvm_jetpack_lib.base.viewmodel.BaseViewModel
import com.example.mvvm_jetpack_lib.base.viewmodel.RequestCallBackNoRestful
import com.m4coding.coolhub.api.datasource.bean.HotDataBean

/**
 * Description:
 * Date：2019/7/24-17:51
 * Author: cwh
 */
class HotViewModel(application: Application) : BaseViewModel<HotRepository>(application) {
    override val repo: HotRepository
        get() = HotRepository()


    val mHotList = MutableLiveData<Event<List<HotDataBean>>>()

    val finish = MutableLiveData<Event<Boolean>>()

    fun getTrending() {
        requestNoRestful(repo.getTrending("", "monthly"),
            object : RequestCallBackNoRestful<List<HotDataBean>> {
                override fun onRequsetSuccess(t: List<HotDataBean>) {
                    mHotList.value = Event(t)
                    finish.value = Event(true)
                }

                override fun onStart() {
                }

                override fun onError(throwable: Throwable) {
                    finish.value = Event(false)
                }

                override fun onFinally() {

                }

            })
    }

    val command=BindingCommand(object :BindingAction{
        override fun call() {
            getTrending()
        }

    })

}