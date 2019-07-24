package com.example.mvvm_jetpack_lib.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader


/**
 * Description:
 * Date：2019/7/15-12:12
 * Author: cwh
 */
object NetWorkUtils {
    /**
     * check NetworkAvailable
     * @param context
     * @return
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val manager = context.applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager ?: return false

        val info = manager.activeNetworkInfo
        return !(null == info || !info.isAvailable)
    }


    /**
     * check is3G
     * @param context
     * @return boolean
     */
    fun is3G(context: Context): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_MOBILE
    }

    /**
     * isWifi
     * @param context
     * @return boolean
     */
    fun isWifi(context: Context): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * is2G
     * @param context
     * @return boolean
     */
    fun is2G(context: Context): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return activeNetInfo != null && (activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_EDGE
                || activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_GPRS || activeNetInfo
            .subtype == TelephonyManager.NETWORK_TYPE_CDMA)
    }

    /**
     * is 4G
     */
    fun is4G(context: Context): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return activeNetInfo != null
                && activeNetInfo.isAvailable
                && activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_LTE
    }

    /**
     * is wifi on
     */
    fun isWifiEnabled(context: Context): Boolean {
        val mgrConn = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mgrTel = context
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return mgrConn.activeNetworkInfo != null && mgrConn
            .activeNetworkInfo!!.state == NetworkInfo.State.CONNECTED || mgrTel
            .networkType == TelephonyManager.NETWORK_TYPE_UMTS
    }

    /**
     * Open the settings of wireless.
     * 打开WiFi设置，去开启wifi
     */
    fun openWirelessSettings(context: Context) {
        context.applicationContext.startActivity(
            Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    /**
     * 通过ping来判断网络是否可用
     * 需要申请网络权限
     *
     * @param ip ping的ip地址，默认223.5.5.5
     */
    fun isNetWorkAvailableByPing(ip: String = "223.5.5.5"): Boolean {
        val result = execCmd(
            arrayOf(String.format("ping -c 1 %s", ip)),
            isRooted = false, isNeedResultMsg = true
        )
        val ret = result.result == 0
        if (result.errorMsg != null) {
            Log.d("NetworkUtils", "isAvailableByPing() called" + result.errorMsg)
        }
        if (result.successMsg != null) {
            Log.d("NetworkUtils", "isAvailableByPing() called" + result.successMsg)
        }
        return ret
    }

    /**
     * 是否真正的有网络 在 M 以上版本才可以调用
     *
     * 需要申请网络权限
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun isNetworkAvailableOnM(context: Context): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }


    private val LINE_SEP = System.getProperty("line.separator")


    /**
     * 执行linux命令行
     * @param commands 命令
     * @param isRooted 是否root
     * @param isNeedResultMsg 是否需要返回星系
     */
    fun execCmd(
        commands: Array<String>?,
        isRooted: Boolean,
        isNeedResultMsg: Boolean
    ): CommandResult {
        var result = -1
        if (commands == null || commands.isEmpty()) {
            return CommandResult(result, "", "")
        }
        var process: Process? = null
        var successResult: BufferedReader? = null
        var errorResult: BufferedReader? = null
        var successMsg: StringBuilder? = null
        var errorMsg: StringBuilder? = null
        var os: DataOutputStream? = null
        try {
            process = Runtime.getRuntime().exec(if (isRooted) "su" else "sh")
            os = DataOutputStream(process!!.outputStream)
            for (command in commands) {
                if (command == null) continue
                os!!.write(command.toByteArray())
                os!!.writeBytes(LINE_SEP)
                os!!.flush()
            }
            os!!.writeBytes("exit$LINE_SEP")
            os!!.flush()
            result = process!!.waitFor()
            if (isNeedResultMsg) {
                successMsg = StringBuilder()
                errorMsg = StringBuilder()
                successResult = BufferedReader(
                    InputStreamReader(process.inputStream, "UTF-8")
                )
                errorResult = BufferedReader(
                    InputStreamReader(process.errorStream, "UTF-8")
                )
                var line: String?
                successResult?.let {
                    line= successResult!!.readLine()
                    if (line != null) {
                        successMsg.append(line)
                        line = successResult!!.readLine()
                        while (line != null) {
                            successMsg.append(LINE_SEP).append(line)
                            line = successResult.readLine()
                        }
                    }
                }
                errorResult?.let {
                    line = errorResult!!.readLine()
                    if (line != null) {
                        errorMsg.append(line)
                        line = errorResult!!.readLine()
                        while (line != null) {
                            errorMsg.append(LINE_SEP).append(line)
                            line = errorResult!!.readLine()
                        }
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (os != null) {
                    os!!.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                if (successResult != null) {
                    successResult!!.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                if (errorResult != null) {
                    errorResult!!.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            process?.destroy()
        }
        return CommandResult(
            result,
            successMsg?.toString() ?: "",
            errorMsg?.toString() ?: ""
        )
    }

    /**
     * The result of command.
     */
    class CommandResult(var result: Int, var successMsg: String, var errorMsg: String) {

        override fun toString(): String {
            return "result: " + result + "\n" +
                    "successMsg: " + successMsg + "\n" +
                    "errorMsg: " + errorMsg
        }
    }
}