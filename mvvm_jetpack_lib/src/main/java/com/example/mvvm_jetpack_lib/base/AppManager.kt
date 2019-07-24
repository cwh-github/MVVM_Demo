package com.example.mvvm_jetpack_lib.base

import android.app.Activity
import java.util.*

/**
 * Description:
 * Date：2019/7/16-19:01
 * Author: cwh
 */

class AppManager private constructor() {


    companion object {
        @Volatile
        private var instance: AppManager? = null

        val activityStack = Stack<Activity>()

        fun getAppManager(): AppManager {
            return instance
                ?: synchronized(AppManager::class.java) {
                instance
                    ?: AppManager().also {
                    instance = it
                }
            }

        }

    }

    /**
     * 添加activity
     */
    fun addActivityToStack(act: Activity) {
        activityStack.add(act)
    }

    /**
     * 移除activity
     */
    fun removeActivityForStack(act: Activity) {
        activityStack.remove(act)
    }

    /**
     * 当前栈中是否有activity
     */
    fun isStoreActivity(): Boolean {
        return !activityStack.isNullOrEmpty()
    }

    /**
     * 结束指定的activity
     */
    fun finishActivity(act: Activity) {
        if (!act.isFinishing) {
            act.finish()
        }

    }

    /**
     * 结束指定类型activity
     */
    fun finishActivity(clazz: Class<*>) {
        activityStack.forEach {
            if (it.javaClass == clazz) {
                finishActivity(it)
                return@forEach
            }
        }
    }

    /**
     * 获取当前栈顶的activity
     */
    fun currentActivity(): Activity? {
        return activityStack.lastElement()
    }

    /**
     * 结束当前栈顶的activity
     */
    fun finishCurrentActivity() {
        currentActivity()?.let {
            finishActivity(it)
        }
    }

    /**
     * 结束所有activity
     */
    fun finishAllActivity() {
        activityStack.forEach {
            finishActivity(it)
        }
        activityStack.clear()
    }

    /**
     * 获取指定activity
     */
    fun getActivity(clazz: Class<*>): Activity? {
        activityStack.forEach {
            return if (it.javaClass == clazz) {
                it
            } else {
                null
            }
        }
        return null
    }

    /**
     * 退出应用程序
     */
    fun AppExit() {
        try {
            finishAllActivity()
            // 杀死该应用进程
            //          android.os.Process.killProcess(android.os.Process.myPid());
            //            调用 System.exit(n) 实际上等效于调用：
            //            Runtime.getRuntime().exit(n)
            //            finish()是Activity的类方法，仅仅针对Activity，当调用finish()时，只是将活动推向后台，并没有立即释放内存，活动的资源并没有被清理；当调用System.exit(0)时，退出当前Activity并释放资源（内存），但是该方法不可以结束整个App如有多个Activty或者有其他组件service等不会结束。
            //            其实android的机制决定了用户无法完全退出应用，当你的application最长时间没有被用过的时候，android自身会决定将application关闭了。
            //System.exit(0);
        } catch (e: Exception) {
            activityStack.clear()
            e.printStackTrace()
        }

    }


}