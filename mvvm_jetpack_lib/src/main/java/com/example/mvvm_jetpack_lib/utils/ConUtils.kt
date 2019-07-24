package com.example.mvvm_jetpack_lib.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import java.lang.NullPointerException

/**
 * Description:
 * Date：2019/7/16-18:51
 * Author: cwh
 */
class ConUtils {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mContext: Context? = null

        private var mApplication:Application?=null

        fun init(context: Application) {
            mContext = context.applicationContext
            mApplication= context
        }

        fun mContext():Context {
            if (mContext !=null) {
                return mContext!!
            }else{
                throw NullPointerException("Context is null,you need init it on Application First")
            }
        }

        fun mApplication():Application{
            if (mApplication !=null) {
                return mApplication!!
            }else{
                throw NullPointerException(" Application is null,you need init it on Application First")
            }
        }

    }

}