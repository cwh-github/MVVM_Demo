package com.example.mvvm_jetpack_lib.base.viewmodel

/**
 * Description:
 * Date：2019/7/21-15:10
 * Author: cwh
 */
interface RequestCallBackNoRestful<T> : CallBack {

    override fun onStart()

    /**
     * 请求成功的回调
     *
     * 请求服务器成功，但不一定返回成功数据
     *
     * 在此函数中需要对结果进行判断，是否返回了需要的结果
     *
     */
    fun onRequestSuccess(t: T)

}