package com.example.mvvm_jetpack.app.login

import com.example.mvvm_jetpack.app.Utils.GithubWebPageDataSource
import com.example.mvvm_jetpack.app.api.ApiService
import com.example.mvvm_jetpack.app.bean.AuthRequestBean
import com.example.mvvm_jetpack.app.bean.AuthUser
import com.example.mvvm_jetpack_lib.base.repository.BaseRepositoryRemote
import com.example.mvvm_jetpack_lib.utils.net.RetrofitUtils
import io.reactivex.Observable
import okhttp3.Credentials

/**
 * Description:
 * Date：2019/7/19-16:55
 * Author: cwh
 */
class LoginRepository : BaseRepositoryRemote() {


    private val apiService = RetrofitUtils.createServiceInstance(ApiService::class.java)!!

    fun login(userName: String, password: String): Observable<AuthUser> {
        val value = Credentials.basic(userName, password)
        val authHeader = if (value.startsWith("Basic")) value else "token $value"

        //网页登录
        GithubWebPageDataSource.login(userName, password).subscribe({ }, { it.printStackTrace()})

        return apiService.login(authHeader, AuthRequestBean.create())
    }

}