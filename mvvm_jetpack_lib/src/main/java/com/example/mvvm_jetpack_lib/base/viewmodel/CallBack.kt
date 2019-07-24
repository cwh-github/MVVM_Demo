package com.example.mvvm_jetpack_lib.base.viewmodel

/**
 * Description:
 * Date：2019/7/21-15:11
 * Author: cwh
 */
interface CallBack {
    /**
     * 开始请求
     */
    fun onStart()

    /**
     * 请求过程中出错
     */
    fun onError(throwable: Throwable)

    /**
     * 请求完成的回调 不管成功 失败 或 出错
     *
     * 都会执行到
     */
    fun onFinally()
}