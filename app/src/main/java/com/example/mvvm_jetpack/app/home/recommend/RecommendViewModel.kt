package com.example.mvvm_jetpack.app.home.recommend

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.mvvm_jetpack_lib.base.Event
import com.example.mvvm_jetpack_lib.base.command.BindingAction
import com.example.mvvm_jetpack_lib.base.command.BindingCommand
import com.example.mvvm_jetpack_lib.base.viewmodel.BaseViewModel
import com.example.mvvm_jetpack_lib.base.viewmodel.RequestCallBackNoRestful
import com.m4coding.coolhub.api.datasource.bean.RecommendBean

/**
 * Description:
 * Date：2019/7/22-21:03
 * Author: cwh
 */
class RecommendViewModel(application: Application) :
    BaseViewModel<RecommendRepository>(application) {
    override val repo: RecommendRepository
        get() = RecommendRepository()

    //刷新数据后的集合
    val mRefreshList = MutableLiveData<Event<java.util.ArrayList<RecommendBean>>>()

    //加载更多数据变化
    val mLoadMoreList = MutableLiveData<Event<java.util.ArrayList<RecommendBean>>>()

    //加载数据时的状态的变化,true 加载成功， false 加载失败
    val mState = MutableLiveData<Event<Boolean>>()

    /**
     * 获取更多数据
     */
    fun getRecommendList(pageNum: Int) {
        requestNoRestful(repo.getRecommendList(pageNum),
            object : RequestCallBackNoRestful<ArrayList<RecommendBean>> {
                override fun onRequestSuccess(t: ArrayList<RecommendBean>) {
                    mLoadMoreList.value = Event(t)
                    mState.value = Event(true)
                }

                override fun onStart() {
                }

                override fun onError(throwable: Throwable) {
                    mState.value = Event(false)
                }

                override fun onFinally() {
                }

            })
    }

    val command= BindingCommand(object : BindingAction {
        override fun call() {
            onRefresh()
        }

    })

    /**
     * 刷新数据
     */
    fun onRefresh() {
        requestNoRestful(repo.getRecommendList(1),
            object : RequestCallBackNoRestful<ArrayList<RecommendBean>> {
                override fun onRequestSuccess(t: ArrayList<RecommendBean>) {
                    mRefreshList.value= Event(t)
                    mState.value = Event(true)
                }

                override fun onStart() {
                }

                override fun onError(throwable: Throwable) {
                    mState.value = Event(false)
                }

                override fun onFinally() {
                }

            })
    }


}