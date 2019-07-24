package com.example.mvvm_jetpack_lib.base.widget

/**
 * Description:
 * Date：2019/7/22-10:51
 * Author: cwh
 */
enum class State(val value:Int) {
    NONE(-1),
    //正在加载
    LOADING(0),
    //数据为空
    EMPTY(1),
    //加载失败
    ERROR(2),
    //加载成功，显示content
    CONTENT(3)
}