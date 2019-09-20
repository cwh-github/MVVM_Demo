package com.example.mvvm_jetpack.app.utils

import android.content.Context
import androidx.core.content.edit


/**
 * Description:
 * Date：2019/7/22-17:31
 * Author: cwh
 */
class SPUtils {

    companion object {
        private const val SP_CONFIG = "config"

        private const val USER_NAME = "user_name"

        private const val PASSWORD = "password"

        private const val TOKEN = "token"


        fun saveAccount(context: Context, userName: String, password: String) {
            val sharedPreferences = context.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE)
            sharedPreferences.edit {
                putString(USER_NAME, userName)
                putString(PASSWORD, password)
            }
        }

        fun getAccount(context: Context): Array<String?> {
            val sharedPreferences = context.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE)
            return arrayOf(sharedPreferences.getString(USER_NAME, ""), sharedPreferences.getString(PASSWORD, ""))
        }


        fun saveToken(context: Context, token: String) {
            val sharedPreferences = context.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE)
            sharedPreferences.edit {
                putString(TOKEN, token)
            }
        }


        fun getToken(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE)
            return sharedPreferences.getString(TOKEN, "")!!
        }


    }

}