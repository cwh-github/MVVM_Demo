package com.example.mvvm_jetpack_lib.utils.exception

import com.example.mvvm_jetpack_lib.base.Entity
import com.example.mvvm_jetpack_lib.utils.ConUtils
import com.example.mvvm_jetpack_lib.utils.LogUtils
import com.example.mvvm_jetpack_lib.utils.ToastUtils
import com.example.mvvm_jetpack_lib.utils.exception.core.GlobalErrorTransformer
import io.reactivex.Observable

/**
 * Description:
 * Date：2019/7/16-18:27
 * Author: cwh
 */
object DefaultGlobalErrorHandle {


    fun <T> creatreGlobalTransfomer(): GlobalErrorTransformer<T> =
        GlobalErrorTransformer<T>(onErrorResumeNext = {
            //做一些自己需要的处理
            val exceptionHandle = ExceptionHandle.handleException(it)
            Observable.error(it)
        }, onErrorOpe = {
            ToastUtils.showToast(
                ConUtils.mContext(),
                ExceptionHandle.handleException(it)
            )
        })


}