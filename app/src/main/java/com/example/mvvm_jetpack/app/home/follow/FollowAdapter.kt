package com.example.mvvm_jetpack.app.home.follow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mvvm_jetpack.R
import com.example.mvvm_jetpack.app.bean.EventBean
import com.example.mvvm_jetpack_lib.base.adapter.BaseLoadingAdapter
import com.example.mvvm_jetpack_lib.utils.find

/**
 * Description:
 * Date：2019/7/24-17:12
 * Author: cwh
 */
class FollowAdapter(context: Context, mData: MutableList<EventBean>) :
    BaseLoadingAdapter<EventBean>(context, mData) {
    override fun onCreateNormalViewHolder(parent: ViewGroup, itemViewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_follow, parent, false)
        return FollowViewHolder(view)
    }

    override fun onBindNormalViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val eventBean = mData[position]
        val mViewHolder = viewHolder as FollowViewHolder
        Glide.with(context)
            .load(eventBean.actor?.avatarUrl)
            .apply(
                RequestOptions.placeholderOf(R.mipmap.ic_github)
                    .error(R.mipmap.ic_github)
            )
            .apply(RequestOptions.centerCropTransform().circleCrop())
            //.apply(bitmapTransform(RoundedCorners(5)))
            .into(mViewHolder.mImageUser)

        mViewHolder.mTvContent.text = eventBean.actor?.login ?: ""
        mViewHolder.mTvTime.text = eventBean.createdAt?.toLocaleString()
        mViewHolder.mTvEvent.text="${eventBean.type} / ${eventBean.repo?.name}"

    }

    fun parseTime(time: String): String {
        if (time.contains("T")) {
            return time.substring(0, time.indexOf("T"))
        }
        return time
    }

    override fun getItemType(position: Int): Int {
        return 0
    }

    inner class FollowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImageUser = itemView.find<ImageView>(R.id.mImgUser)
        val mTvContent = itemView.find<TextView>(R.id.mTvContent)
        val mTvTime = itemView.find<TextView>(R.id.mTvTime)
        val mTvEvent = itemView.find<TextView>(R.id.mTvEvent)
    }

}