package com.example.mvvm_jetpack_lib.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.mvvm_jetpack_lib.isDebug
import com.example.mvvm_jetpack_lib.utils.ConUtils
import com.example.mvvm_jetpack_lib.utils.CrashHandlerUtils
import com.squareup.leakcanary.LeakCanary
import java.lang.NullPointerException

/**
 * Description:
 * Date：2019/7/16-18:57
 * Author: cwh
 */
abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ConUtils.init(this)
        setApplication(this)
        CrashHandlerUtils.getInstance().initHandler(this)

        //LeakCanary libary init
        if (isDebug) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return
            }
            LeakCanary.install(this)
            // Normal app init code...
        }

        init()
    }

    /**
     * 一些lib的初始化
     */
    abstract fun init()


    companion object {

        @Volatile
        private var instance: Application? = null

        val callback = object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(act: Activity, p1: Bundle?) {
                AppManager.getAppManager().addActivityToStack(act)
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityStarted(p0: Activity) {
            }


            override fun onActivityResumed(p0: Activity) {
            }

            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStopped(p0: Activity) {
            }


            override fun onActivityDestroyed(act: Activity) {
                AppManager.getAppManager().removeActivityForStack(act)
            }
        }

        @Synchronized
        fun setApplication(application: Application) {
            instance = application
            application.registerActivityLifecycleCallbacks(callback)

        }


        fun instance(): Application {
            if (instance == null) {
                throw NullPointerException("Application Instance is null")
            }
            return instance!!
        }

    }

    override fun onTerminate() {
        super.onTerminate()
        this.unregisterActivityLifecycleCallbacks(callback)
        instance = null
    }
}