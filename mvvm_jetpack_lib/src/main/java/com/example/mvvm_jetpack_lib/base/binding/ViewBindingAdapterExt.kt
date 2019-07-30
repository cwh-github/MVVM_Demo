package com.example.mvvm_jetpack_lib.base.binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.example.mvvm_jetpack_lib.base.command.BindingComWithParams
import com.example.mvvm_jetpack_lib.base.command.BindingCommand
import com.example.mvvm_jetpack_lib.utils.click

/**
 * Description:
 * Date：2019/7/29-22:02
 * Author: cwh
 */


/**
 * View通用点击事件，防止重复点击
 */
@BindingAdapter("onClickCommand")
fun onClickCommand(view: View, command: BindingCommand) {
    view.click {
        command.execute()
    }
}