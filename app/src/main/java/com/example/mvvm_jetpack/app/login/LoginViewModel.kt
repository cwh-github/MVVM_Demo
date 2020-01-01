package com.example.mvvm_jetpack.app.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.mvvm_jetpack.app.MainActivity
import com.example.mvvm_jetpack.app.utils.SPUtils
import com.example.mvvm_jetpack.app.bean.AuthUser
import com.example.mvvm_jetpack_lib.base.Event
import com.example.mvvm_jetpack_lib.base.command.BindingAction
import com.example.mvvm_jetpack_lib.base.command.BindingCommand
import com.example.mvvm_jetpack_lib.base.viewmodel.BaseViewModel
import com.example.mvvm_jetpack_lib.utils.LogUtils
import com.example.mvvm_jetpack_lib.utils.exception.DefaultGlobalErrorHandle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Description:
 * Date：2019/7/19-17:01
 * Author: cwh
 */
class LoginViewModel(application: Application) :
    BaseViewModel<LoginRepository>(application) {
    override val repo: LoginRepository = LoginRepository()

    //用户名
    val userName = MutableLiveData<String>()

    //password
    val password = MutableLiveData<String>()

    //登录提示
    val tips = MutableLiveData<Event<String>>()

    init {
        userName.value = SPUtils.getAccount(application.applicationContext)[0]
        password.value = SPUtils.getAccount(application.applicationContext)[1]
    }


    val mGoActivityCommand = BindingCommand(object : BindingAction {
        override fun call() {
            mUIChangeEvent.startActivityEventWithClass.value=
                Event(NoViewDataBindingActivity::class.java)
        }

    })


    /**
     * 登录
     */
    fun login() {
        if (userName.value.isNullOrEmpty()) {
            tips.value = Event("用户名不能为空!")
            return
        }
        if (password.value.isNullOrEmpty()) {
            tips.value = Event("密码不能为空!")
            return
        }

        addSubscribe(repo.login(userName.value!!, password.value!!)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                mUIChangeEvent.isShowLoadView.value = Event(true)
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                mUIChangeEvent.isShowLoadView.value = Event(false)
                LogUtils.d("Thread is ${Thread.currentThread()}")
            }
            .compose(DefaultGlobalErrorHandle.createGlobalTransformer<AuthUser>())
            .subscribe({
                tips.value = Event("登录成功!")
                SPUtils.saveAccount(getApplication(), userName.value!!, password.value!!)
                SPUtils.saveToken(getApplication(), it.token)
                //开启MainActivity
                val map = mapOf(NormalUIChangeLiveData.clazz to MainActivity::class.java)
                mUIChangeEvent.startActivityEventWithMap.value = Event(map)

                //finish LoginActivity
                mUIChangeEvent.onFinishEvent.value = Event(Unit)


            }, {
                LogUtils.d("it is ${it.message}  Thread is ${Thread.currentThread()} onError")
            })
        )


    }


}