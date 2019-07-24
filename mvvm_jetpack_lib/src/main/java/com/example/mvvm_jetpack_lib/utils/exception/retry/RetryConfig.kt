package com.example.mvvm_jetpack_lib.utils.exception.retry

import io.reactivex.Single

/**
 * Description:
 *
 * maxTime 最大重试次数
 *
 * delay 延迟时间
 *
 * isRetry 发送true 重试  false  不重试
 *
 *
 * Date：2019/7/16-16:01
 * Author: cwh
 */
data class RetryConfig ( val maxTime:Int, val delay:Long,
                         val isRetry:()->Single<Boolean>){

    companion object{

        /**
         * 返回不做retry处理的RetryConfig
         */
        fun none(): RetryConfig =
            RetryConfig(0, 0) {
                Single.just(false)
            }

        /**
         * 返回定制的RetryConfig ，指定重试的次数 ，delay  和是否重试的操作
         */
        fun simpleInstance(maxTime: Int=1,delay: Long=1000,isRetryOpe:() -> Single<Boolean>)
                = RetryConfig(maxTime, delay) {
            isRetryOpe()
        }
    }

}