package com.example.mvvm_jetpack.app.api

import com.example.mvvm_jetpack.app.bean.AuthRequestBean
import com.example.mvvm_jetpack.app.bean.AuthUser
import com.example.mvvm_jetpack.app.bean.EventBean
import com.m4coding.coolhub.api.datasource.bean.UserBean
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response

import retrofit2.http.*

/**
 * Description:
 * Date：2019/7/19-16:27
 * Author: cwh
 */
interface ApiService {

    /**
     * 登录请求
     *
     * @param  authHeader 由password 和 username 生成
     */
    @POST("authorizations")
    @Headers("Accept: application/json")
    fun login(
        @Header("Authorization") authHeader: String,
        @Body authRequestBean: AuthRequestBean
    ): Observable<AuthUser>

    /**
     * 获取登录网页的内容
     */
    @GET("https://github.com/login")
    fun getLoginPage(): Observable<Response<ResponseBody>>

    @POST("https://github.com/session")
    @FormUrlEncoded
    fun postSession(@FieldMap map: Map<String, String>): Observable<Response<ResponseBody>>

    /**
     * 获取发现页的内容
     */
    @GET("https://github.com/discover")
    fun getDiscover(@Query("recommendations_after") index: Int): Observable<Response<ResponseBody>>


    /**
     * 获取接受到的事件  （例如github首页中的browse动态信息 关注者的动态事件）
     */
    @GET("users/{username}/received_events")
    fun getReceivedEvents(@Path("username")userName:String,
                          @Query("page") page: Int): Observable<List<EventBean>>


    /**
     * 获取趋势内容
     * @param language  指定要获取的语言 ("" Java Kotlin ....)
     * @param since   时间   （daily  weekly  monthly）
     */
    @GET("https://github.com/trending/kotlin")
    fun getTrending(@Query("since") since: String): Observable<Response<ResponseBody>>


    /**
     * 获取个人信息
     */
    @GET("user")
    fun getMineInfo(): Observable<UserBean>
}