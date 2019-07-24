package com.example.mvvm_jetpack.app.home.recommend

import com.example.mvvm_jetpack.app.Utils.GitHubHtmlParseUtils
import com.example.mvvm_jetpack.app.api.ApiService
import com.example.mvvm_jetpack_lib.base.repository.BaseRepositoryRemote
import com.example.mvvm_jetpack_lib.utils.net.RetrofitUtils
import com.m4coding.coolhub.api.datasource.bean.RecommendBean
import io.reactivex.Observable
import java.util.ArrayList


/**
 * Description:
 * Date：2019/7/22-21:02
 * Author: cwh
 */
class RecommendRepository : BaseRepositoryRemote() {


    private val apiService=RetrofitUtils.createServiceInstance(ApiService::class.java)!!

    /**
     * 获取推荐repo
     */
    fun getRecommendList(pageNum:Int):Observable<ArrayList<RecommendBean>>{
       return apiService.getDiscover(pageNum)
            .map {
                GitHubHtmlParseUtils.parseDiscoverData(it.body()?.string()?:"")
            }
    }
}