package com.example.mvvm_jetpack_lib.utils

/**
 * Description:请求完成后的这状态，
 * 用于LiveData中确定是否请求成功，显示相应的状态，主要在ViewModel中使用
 * Date：2019/7/31-20:26
 * Author: cwh
 */
enum class ResponseState(val value: Int) {
    /**
     * 请求失败
     */
    ERROR(-1),
    /**
     * 请求成功
     */
    SUCCESS(0)
}