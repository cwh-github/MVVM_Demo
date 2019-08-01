package com.example.mvvm_jetpack_lib.base.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvm_jetpack_lib.REQ_SUC
import com.example.mvvm_jetpack_lib.base.Entity
import com.example.mvvm_jetpack_lib.base.Event
import com.example.mvvm_jetpack_lib.base.repository.IRepository
import com.example.mvvm_jetpack_lib.utils.exception.DefaultGlobalErrorHandle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Description:
 * Date：2019/7/17-17:37
 * Author: cwh
 */

abstract class BaseViewModel<T : IRepository>(application: Application) :
    AndroidViewModel(application), IBaseViewModel {
    abstract val repo:T

    val mUIChangeEvent = NormalUIChangeLiveData()

    private lateinit var mCompositeDisposable: CompositeDisposable

    init {
        if (!::mCompositeDisposable.isInitialized) {
            mCompositeDisposable = CompositeDisposable()
        }
    }

    override fun onCleared() {
        super.onCleared()
        repo.onClear()
        if (::mCompositeDisposable.isInitialized && !mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }

    /**
     * 符合RESTful风格的返回数据，可以直接用此默认处理请求
     */
    protected fun <T : Entity<*>> request(observable: Observable<T>, callBack: RequestCallBack<T>) {
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
            .doOnSubscribe {
                callBack.onStart()
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                callBack.onFinally()
            }
            .compose(DefaultGlobalErrorHandle.creatreGlobalTransfomer<T>())
            .subscribe({
                if (it.code == REQ_SUC) {
                    callBack.onSuccess(it)
                } else {
                    callBack.onFail(it.msg, it.code)
                }
            }, {
                callBack.onError(it)
            })
        )
    }

    /**
     * 不符合RESTful风格数据的请求
     */
    protected fun <T> requestNoRestful(observable: Observable<T>, callBackNoRestful: RequestCallBackNoRestful<T>) {
        addSubscribe(observable.subscribeOn(Schedulers.io())
            .doOnSubscribe {
                callBackNoRestful.onStart()
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                callBackNoRestful.onFinally()
            }
            .compose(DefaultGlobalErrorHandle.creatreGlobalTransfomer<T>())
            .subscribe({
                callBackNoRestful.onRequestSuccess(it)
            }, {
                callBackNoRestful.onError(it)
            })
        )
    }

    /**
     * 将Disposable添加进集合，在onClear()时进行解绑
     */
    protected fun addSubscribe(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun onAny() {
    }

    override fun onCreate() {
    }

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onDestroy() {
    }


    /**
     * 一些常见的UI 改变的Livedata 如：加载框的显示与隐藏，跳转Activity,onBack ,finish等
     */
    class NormalUIChangeLiveData {
        companion object {
            const val clazz: String = "Clazz"

            const val bundle: String = "Bundle"
        }


        /**
         * 是否显示加载View true 显示 ， false 隐藏
         */
        val isShowLoadView = MutableLiveData<Event<Boolean>>()

        /**
         * 跳转Activity
         * map 中存放需要跳转的数据
         * 如：clazz:Class 和 Bundle
         *
         */
        val startActivityEventWithMap: MutableLiveData<Event<Map<String, Any>>> = MutableLiveData()

        /**
         * 跳转Activity with Intent
         */
        val startActivityEventWithIntent: MutableLiveData<Event<Intent>> = MutableLiveData()

        /**
         * 跳转Activity with class ,不携带数据
         */
        val startActivityEventWithClass:MutableLiveData<Event<Class<*>>> = MutableLiveData()

        /**
         * onBackPress
         */
        val onBackEvent: MutableLiveData<Event<Unit>> = MutableLiveData()

        /**
         * Finish 当前activity
         */
        val onFinishEvent: MutableLiveData<Event<Unit>> = MutableLiveData()


    }
}