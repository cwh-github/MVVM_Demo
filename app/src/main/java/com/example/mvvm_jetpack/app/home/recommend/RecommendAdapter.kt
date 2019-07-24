package com.example.mvvm_jetpack.app.home.recommend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.example.mvvm_jetpack.R
import com.example.mvvm_jetpack_lib.base.adapter.BaseLoadingAdapter
import com.example.mvvm_jetpack_lib.utils.find
import com.google.android.flexbox.FlexboxLayout
import com.m4coding.coolhub.api.datasource.bean.RecommendBean

/**
 * Description:
 * Date：2019/7/23-17:22
 * Author: cwh
 */
class RecommendAdapter(context: Context, mData: MutableList<RecommendBean>) :
    BaseLoadingAdapter<RecommendBean>(context, mData) {
    override fun onCreateNormalViewHolder(parent: ViewGroup, itemViewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recommend, parent, false)
        return RecommendViewHolder(view)
    }

    override fun onBindNormalViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val recommendBean = mData[position]
        val mViewHolder = viewHolder as RecommendViewHolder
        mViewHolder.mTvContent.text = recommendBean.repositoryName ?: " "
        mViewHolder.mTvLanguage.text = recommendBean.language ?: ""
        mViewHolder.mTvStart.text = "${recommendBean.starNum ?: "0"} "
        mViewHolder.mFlexBox.removeAllViews()
        if (!recommendBean.tagsList.isNullOrEmpty()) {
            mViewHolder.mFlexBox.visibility = View.VISIBLE
            addFlexView(mViewHolder.mFlexBox, recommendBean.tagsList!!)
        } else {
            mViewHolder.mFlexBox.visibility = View.GONE
        }
        Glide.with(context)
            .load(recommendBean.avatar)
            .apply(
                RequestOptions.placeholderOf(R.mipmap.ic_github)
                    .error(R.mipmap.ic_github)
            )
            .apply(centerCropTransform().circleCrop())
            //.apply(bitmapTransform(RoundedCorners(5)))
            .into(mViewHolder.mImgUser)


    }

    private fun addFlexView(mFlexBox: FlexboxLayout, tagsList: List<String>) {
        for (item in tagsList) {
            val view = LayoutInflater.from(context).inflate(R.layout.flex_item, null)
            (view.find<TextView>(R.id.mTvT)).text = item
            mFlexBox.addView(view)
        }
    }

    override fun getItemType(position: Int): Int {
        return 0
    }


    class RecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImgUser = itemView.find<ImageView>(R.id.mImgUser)
        val mTvContent = itemView.find<TextView>(R.id.mTvContent)
        val mTvLanguage = itemView.find<TextView>(R.id.mTvLanguage)
        val mTvStart = itemView.find<TextView>(R.id.mTvStart)
        val mFlexBox = itemView.find<FlexboxLayout>(R.id.mFlexBox)
    }

}