package com.example.mvvm_jetpack.app.home.follow

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.mvvm_jetpack.app.bean.EventBean
import com.example.mvvm_jetpack_lib.base.Event
import com.example.mvvm_jetpack_lib.base.command.BindingAction
import com.example.mvvm_jetpack_lib.base.command.BindingCommand
import com.example.mvvm_jetpack_lib.base.viewmodel.BaseViewModel
import com.example.mvvm_jetpack_lib.base.viewmodel.RequestCallBackNoRestful

/**
 * Description:
 * Date：2019/7/24-14:46
 * Author: cwh
 */
class FollowViewModel(application: Application) : BaseViewModel<FollowRepository>(application) {
    override val repo: FollowRepository
        get() = FollowRepository()

    val mRefreshList=MutableLiveData<Event<List<EventBean>>>()

    val mLoadMoreList=MutableLiveData<Event<List<EventBean>>>()

    val mState=MutableLiveData<Event<Boolean>>()


    val command=BindingCommand(object :BindingAction{
        override fun call() {
            onRefresh()
        }

    })

    fun getFollowEvent(pageNum:Int){
        requestNoRestful(repo.getFollow("cwh_rookie",pageNum =pageNum ),
            object :RequestCallBackNoRestful<List<EventBean>>{
                override fun onRequsetSuccess(t: List<EventBean>) {
                    mLoadMoreList.value= Event(t)
                    mState.value= Event(true)
                }

                override fun onStart() {
                }

                override fun onError(throwable: Throwable) {
                    mState.value= Event(false)
                }

                override fun onFinally() {
                }

            })


    }

    fun onRefresh(){
        requestNoRestful(repo.getFollow("Season",pageNum =1 ),
            object :RequestCallBackNoRestful<List<EventBean>>{
                override fun onRequsetSuccess(t: List<EventBean>) {
                    mRefreshList.value= Event(t)
                    mState.value= Event(true)
                }

                override fun onStart() {
                }

                override fun onError(throwable: Throwable) {
                    mState.value= Event(false)
                }

                override fun onFinally() {
                }

            })
    }


}