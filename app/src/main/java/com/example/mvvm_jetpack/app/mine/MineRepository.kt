package com.example.mvvm_jetpack.app.mine

import com.example.mvvm_jetpack.app.api.ApiService
import com.example.mvvm_jetpack_lib.base.repository.BaseRepositoryRemote
import com.example.mvvm_jetpack_lib.utils.net.RetrofitUtils
import com.m4coding.coolhub.api.datasource.bean.UserBean
import io.reactivex.Observable

/**
 * Description:
 * Date：2019/7/24-19:10
 * Author: cwh
 */
class MineRepository : BaseRepositoryRemote() {

    private val apiService = RetrofitUtils.createServiceInstance(ApiService::class.java)

    fun getMineInfo(): Observable<UserBean> {
        return apiService.getMineInfo()
    }

}