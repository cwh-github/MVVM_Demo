package com.example.mvvm_jetpack.app

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mvvm_jetpack.R
import com.example.mvvm_jetpack_lib.base.command.BindingCommand

/**
 * Description:
 * Date：2019/7/23-17:15
 * Author: cwh
 */

@BindingAdapter("android:onRefresh")
fun onRefresh(refreshLayout: SwipeRefreshLayout, command: BindingCommand) {
    refreshLayout.setOnRefreshListener {
        command.execute()
    }
}

@BindingAdapter("app:url")
fun url(imageView: ImageView,url:String?){
    Glide.with(imageView.context)
        .load(url)
        .apply(
            RequestOptions.placeholderOf(R.mipmap.ic_github)
                .error(R.mipmap.ic_github)
        )
        .apply(RequestOptions.centerCropTransform().circleCrop())
        //.apply(bitmapTransform(RoundedCorners(5)))
        .into(imageView)
}