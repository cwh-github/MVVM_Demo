package com.example.mvvm_jetpack_lib.base.view

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.ArrayList

/**
 * Description:
 * Date：2019/7/22-19:05
 * Author: cwh
 */
abstract class BaseNorFragment : Fragment() {

    private var mPermissions: ArrayList<String> = ArrayList()
    private val REQUEST_PERMISSION_CODE = 100

    protected open lateinit var mActivity: Activity



    override fun onAttach(context: Context) {
        super.onAttach(context)
        context?.let {
            mActivity = context as Activity
        }
    }



    /**
     * 检测权限,并在有权限未通过时进行权限请求
     * @return true 权限通过  false 权限未通过
     */
    protected open fun checkPermission(permissions: Array<String>): Boolean {
        var result: Boolean = true
        //6.0以下，直接权限通过
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(mActivity, it) == PackageManager.PERMISSION_DENIED) {
                result = false
                mPermissions.add(it)
            }
        }
        if (!result || mPermissions.size > 0) {
            requestPermissions(mPermissions.toTypedArray(), REQUEST_PERMISSION_CODE)
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                var result = true
                val mNeedReqPermissions = ArrayList<String>()
                grantResults.forEachIndexed { index, res ->
                    if (res == PackageManager.PERMISSION_DENIED) {
                        result = false
                        mNeedReqPermissions.add(permissions[index])
                    }
                }
                //所有权限都通过
                if (result) {
                    onPermissionGrant()
                } else {
                    //展示提示请求权限
                    var resultRational = true
                    mNeedReqPermissions.forEach {
                        if (!shouldShowRequestPermissionRationale(it)) {
                            resultRational = false
                            return@forEach
                        }
                    }
                    if (resultRational) {
                        showRationaleDialog(mNeedReqPermissions)
                    } else {
                        showOpenSettingDialog(mNeedReqPermissions)
                    }
                }


            }
        }
    }

    /**
     * 展示提示用户去设置项开启权限
     */
    protected open fun showOpenSettingDialog(mNeedReqPermissions: ArrayList<String>) {


    }

    /**
     * 展示请求权限提示，根据用户选择，是否再次进行权限请求
     */
    protected open fun showRationaleDialog(mNeedReqPermissions: ArrayList<String>) {

    }


    /**
     * 所有权限都通过时执行
     */
    protected open fun onPermissionGrant() {

    }
}