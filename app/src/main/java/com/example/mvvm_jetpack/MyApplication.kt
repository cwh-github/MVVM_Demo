package com.example.mvvm_jetpack

import com.example.mvvm_jetpack.app.interceptor.AuthInterceptor
import com.example.mvvm_jetpack_lib.base.BaseApplication
import com.example.mvvm_jetpack_lib.utils.ConUtils
import com.example.mvvm_jetpack_lib.utils.CrashHandlerUtils
import com.example.mvvm_jetpack_lib.utils.net.RetrofitUtils
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor

/**
 * Description:
 * Date：2019/7/19-14:34
 * Author: cwh
 */
class MyApplication :BaseApplication(){
    override fun init() {
        RetrofitUtils.addInterceptor(AuthInterceptor())
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(this))
        RetrofitUtils.addCookJar(cookieJar)

    }

}