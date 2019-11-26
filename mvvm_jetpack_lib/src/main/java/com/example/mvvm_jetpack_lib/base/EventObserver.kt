package com.example.mvvm_jetpack_lib.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Description:主要用于对Event包装的LiveData数的观察，处理
 * Date：2019/11/26-23:43
 * Author: cwh
 * @param onEventUnHandle 观察到Event包装的数据发生变化时的处理函数
 */

class EventObserver <T>(private var onEventUnHandle:(T)->Unit):
    Observer<Event<T>>{
    override fun onChanged(t: Event<T>?) {
        t?.getContentIfNotHandled()?.let {
            onEventUnHandle(it)
        }
    }
}

/**
 * 扩展函数，对于Event包装的LiveDate数据的观察，处理
 * @param onEventUnHandle 观察到Event包装的数据发生变化时的处理函数
 *不需要再进行一些模板的判空处理
 */
inline fun <T> LiveData<Event<T>>.
        observeEvent(owner: LifecycleOwner, crossinline onEventUnHandle: (T) -> Unit){
    observe(owner, Observer {
        it.getContentIfNotHandled()?.let {content->
            onEventUnHandle(content)
        }
    })
}