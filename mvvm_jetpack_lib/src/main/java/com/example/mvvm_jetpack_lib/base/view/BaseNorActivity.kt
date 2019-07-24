package com.example.mvvm_jetpack_lib.base.view

import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * Description:正常不涉及MVVM的BaseActivity ,
 * 主要用于一些不需要使用MVVM模式，主要用于常规的View的显示
 * Date：2019/7/22-19:01
 * Author: cwh
 */
abstract class BaseNorActivity :AppCompatActivity(){

    private var mPermissions: ArrayList<String> = ArrayList()
    private val REQUEST_PERMISSION_CODE = 100





    /**
     * 检测权限,并在有权限未通过时进行权限请求
     * @return true 权限通过  false 权限未通过
     */
    protected fun checkPermission(permissions: Array<String>): Boolean {
        var result: Boolean = true
        //6.0以下，直接权限通过
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED) {
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