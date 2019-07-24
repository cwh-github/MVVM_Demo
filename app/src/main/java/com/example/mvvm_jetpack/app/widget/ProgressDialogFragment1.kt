package com.example.mvvm_jetpack.app.widget

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

/**
 * Description:
 * Date：2019/7/21-17:38
 * Author: cwh
 */
class ProgressDialogFragment1 : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val progressDialog = ProgressDialog(activity!!)
        return progressDialog.mDialog
    }

    companion object {
        fun onNewInstance(tag: String): ProgressDialogFragment1 {
            val fragment = ProgressDialogFragment1()
            val bundle = Bundle()
            bundle.putString("tag", tag)
            fragment.arguments = bundle
            return fragment
        }
    }

}