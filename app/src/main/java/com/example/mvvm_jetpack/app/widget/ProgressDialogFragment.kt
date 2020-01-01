package com.example.mvvm_jetpack.app.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.mvvm_jetpack.R
import com.example.mvvm_jetpack_lib.utils.DisplayUtils


/**
 * Description:
 * Date：2019/7/21-15:20
 * Author: cwh
 */
class ProgressDialogFragment : DialogFragment() {

    private val SAVED_DIALOG_STATE_TAG = "android:savedDialogState"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(STYLE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.attributes?.dimAmount=0f
        return inflater.inflate(R.layout.dialog_progress, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if(showsDialog){
            showsDialog=false
        }
        super.onActivityCreated(savedInstanceState)
        val view = view
        if (view != null) {
            if (view.parent != null) {
                throw IllegalStateException(
                    "DialogFragment can not be attached to a container view"
                )
            }
            dialog?.setContentView(view)
        }
        val activity = activity
        if (activity != null) {
            dialog?.setOwnerActivity(activity)
        }
        if (savedInstanceState != null) {
            val dialogState = savedInstanceState.getBundle(SAVED_DIALOG_STATE_TAG)
            if (dialogState != null) {
                dialog?.onRestoreInstanceState(dialogState)
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val layoutManager = root.layoutParams
//        val wd = DisplayUtils.getDeviceWidthAndHeight(activity!!)
//        layoutManager?.width = wd[0] / 3
//        layoutManager?.height = wd[1] / 4
//        root.layoutParams = layoutManager

    }

    override fun onResume() {
        super.onResume()
        val wd = DisplayUtils.getDeviceWidthAndHeight(activity!!)
        dialog?.window?.setLayout(wd[0]*2/5,wd[1]/4)
    }

    override fun onDestroyView() {
        if (view is ViewGroup) {
            (view as ViewGroup).removeAllViews()
        }
        super.onDestroyView()

    }

    companion object {
        fun onNewInstance(tag: String): ProgressDialogFragment {
            val fragment = ProgressDialogFragment()
            val bundle = Bundle()
            bundle.putString("tag", tag)
            fragment.arguments = bundle
            return fragment
        }
    }


}