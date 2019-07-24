package com.example.mvvm_jetpack_lib.base.repository

/**
 * Description:
 * Date：2019/7/17-17:36
 * Author: cwh
 */
open class BaseRepositoryBoth(private val remote: BaseRepositoryRemote,
                              private val local: BaseRepositoryLocal
) : IRepository {
    override fun onClear() {
        remote.onClear()
        local.onClear()
    }
}