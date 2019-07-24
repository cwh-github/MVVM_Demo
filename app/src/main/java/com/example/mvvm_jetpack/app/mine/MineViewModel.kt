package com.example.mvvm_jetpack.app.mine

import android.app.Application
import android.util.EventLog
import androidx.lifecycle.MutableLiveData
import com.example.mvvm_jetpack_lib.base.Event
import com.example.mvvm_jetpack_lib.base.viewmodel.BaseViewModel
import com.example.mvvm_jetpack_lib.base.viewmodel.RequestCallBackNoRestful
import com.m4coding.coolhub.api.datasource.bean.UserBean

/**
 * Description:
 * Date：2019/7/24-19:11
 * Author: cwh
 */
class MineViewModel(application: Application) : BaseViewModel<MineRepository>(application) {
    override val repo: MineRepository
        get() = MineRepository()

    val mUrl = MutableLiveData<String>()
    val mUserName = MutableLiveData<String>()
    val mTips = MutableLiveData<String>()
    val mLocation = MutableLiveData<String>()
    val mEmail = MutableLiveData<String>()
    val mRepos = MutableLiveData<Int>()
    val mFollows = MutableLiveData<Int>()
    val mFollowing = MutableLiveData<Int>()

    val mState = MutableLiveData<Event<Boolean>>()

    fun getMineInfo() {
        requestNoRestful(repo.getMineInfo(), object : RequestCallBackNoRestful<UserBean> {
            override fun onRequsetSuccess(t: UserBean) {
                mState.value = Event(true)
                mUrl.value = t.avatarUrl ?: ""
                mUserName.value = t.login ?: ""
                mTips.value = t.bio ?: ""
                mLocation.value = t.location ?: ""
                mEmail.value = t.email ?: ""
                mRepos.value = t.publicRepos ?: 0
                mFollows.value = t.followers ?: 0
                mFollowing.value = t.following ?: 0
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