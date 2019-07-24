package com.example.mvvm_jetpack.app.interceptor

import com.example.mvvm_jetpack.app.Utils.SPUtils
import com.example.mvvm_jetpack_lib.utils.ConUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Description:
 * Date：2019/7/23-14:54
 * Author: cwh
 */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        if (oldRequest.header("Authorization")==null &&
            !SPUtils.getToken(ConUtils.mContext()).isNullOrEmpty()
        ) {
            val token = if (SPUtils.getToken(ConUtils.mContext()).startsWith("Basic")) {
                SPUtils.getToken(ConUtils.mContext())
            } else {
                "token ${SPUtils.getToken(ConUtils.mContext())}"
            }


            val request = oldRequest.newBuilder().addHeader("Authorization", token)
                .build()
            return chain.proceed(request)
        }

        return chain.proceed(oldRequest)
    }
}