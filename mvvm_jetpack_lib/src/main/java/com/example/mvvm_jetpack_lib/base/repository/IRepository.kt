package com.example.mvvm_jetpack_lib.base.repository

import android.app.Application

/**
 * Description:
 * Date：2019/7/17-13:28
 * Author: cwh
 */

/**
 * Repository
 */
interface IRepository{
    /**
     * ViewModel clear 时执行，清除
     */
    fun onClear()
}