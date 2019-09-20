package com.example.mvvm_jetpack.app.hot

import com.example.mvvm_jetpack.app.utils.GitHubHtmlParseUtils
import com.example.mvvm_jetpack.app.api.ApiService
import com.example.mvvm_jetpack_lib.base.repository.BaseRepositoryRemote
import com.example.mvvm_jetpack_lib.utils.net.RetrofitUtils
import com.m4coding.coolhub.api.datasource.bean.HotDataBean
import io.reactivex.Observable

/**
 * Description:
 * Date：2019/7/24-17:50
 * Author: cwh
 */
class HotRepository : BaseRepositoryRemote() {

    private val apiService=RetrofitUtils.createServiceInstance(ApiService::class.java)

    /**
     * 获取趋势内容
     * @param language  指定要获取的语言 (all Java Kotlin ....)
     * @param since   时间   （daily  weekly  monthly）
     */
    fun getTrending(language:String,time:String):Observable<List<HotDataBean>>{
        return apiService.getTrending(time)
            .map {
                GitHubHtmlParseUtils.parseTrendData(it.body()?.string()?:"",time)
            }
    }

}