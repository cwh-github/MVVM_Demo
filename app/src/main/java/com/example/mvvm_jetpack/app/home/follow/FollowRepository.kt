package com.example.mvvm_jetpack.app.home.follow

import com.example.mvvm_jetpack.app.api.ApiService
import com.example.mvvm_jetpack.app.bean.EventBean
import com.example.mvvm_jetpack_lib.base.repository.BaseRepositoryRemote
import com.example.mvvm_jetpack_lib.utils.net.RetrofitUtils
import io.reactivex.Observable

/**
 * Description:
 * Date：2019/7/24-14:45
 * Author: cwh
 */
class FollowRepository : BaseRepositoryRemote() {

    private val apiService = RetrofitUtils.createServiceInstance(ApiService::class.java)!!

    fun getFollow(userName: String, pageNum: Int): Observable<List<EventBean>> {
        return apiService.getReceivedEvents(userName, pageNum)
    }

}