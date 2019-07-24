package com.example.mvvm_jetpack_lib.base.viewmodel

/**
 * Description: 请求回调 正常返回结果是RESTful的请求的回调
 * Date：2019/7/17-14:02
 * Author: cwh
 */
interface RequestCallBack<T>:CallBack {
    /**
     * 开始加载
     */
    override fun onStart()

    /**
     * 获取数据成功的回调
     */
    fun onSuccess(t:T)

    /**
     * 请求失败,服务器返回信息
     * @param msg 服务器返回msg
     * @param code 服务器返回code
     */
    fun onFail(msg:String,code:Int)

    /**
     * 出错
     */
    override fun onError(throwable: Throwable){

    }

    /**
     * 请求完成的回调 不管成功 失败 或 出错
     *
     * 都会执行到
     */
    override fun onFinally()
}