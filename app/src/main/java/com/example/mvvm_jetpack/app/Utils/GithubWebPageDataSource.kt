package com.example.mvvm_jetpack.app.Utils

import com.example.mvvm_jetpack.app.Utils.GitHubHtmlParseUtils
import com.example.mvvm_jetpack.app.api.ApiService
import com.example.mvvm_jetpack_lib.utils.net.RetrofitUtils
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * @author mochangsheng
 * @description
 */
object GithubWebPageDataSource {

    fun login(username: String, password: String): Observable<Response<ResponseBody>> {
        val webpageService = RetrofitUtils.createServiceInstance(ApiService::class.java)

        return webpageService.getLoginPage()
                .subscribeOn(Schedulers.io())
                .flatMap { it ->
                    if (it.raw().request().url().toString() == "https://github.com/") {
                        //get login，如果是登录状态会重定向到github.com
                        Observable.just(it)
                    } else {
                        val map: HashMap<String, String> =  HashMap()
                        map["authenticity_token"] = GitHubHtmlParseUtils.parseAuthenticityToken(it.body()?.string() ?: "") ?: ""
                        map["login"] = username
                        map["password"] = password
                        map["utf8"] = "✓"
                        map["commit"] = "Sign in"
                        webpageService.postSession(map)
                    }
                }
    }

}