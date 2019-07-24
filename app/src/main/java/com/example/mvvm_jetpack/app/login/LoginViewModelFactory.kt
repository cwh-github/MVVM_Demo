package com.example.mvvm_jetpack.app.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm_jetpack.app.login.LoginRepository
import com.example.mvvm_jetpack.app.login.LoginViewModel

/**
 * Description:
 * Date：2019/7/19-17:34
 * Author: cwh
 */
class LoginViewModelFactory private constructor(): ViewModelProvider.NewInstanceFactory() {



    companion object {
        @Volatile
        private var instance:LoginViewModelFactory?=null

        lateinit var application:Application

        fun instance(application: Application):LoginViewModelFactory {
            this.application=application
            return instance?: synchronized(this){
                instance ?: LoginViewModelFactory().also {
                    instance=it
                }
            }
        }
    }


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(application) as T
    }
}