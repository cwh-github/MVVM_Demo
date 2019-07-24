package com.example.mvvm_jetpack.app.hot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_jetpack.R
import com.example.mvvm_jetpack_lib.utils.find
import com.google.android.flexbox.FlexboxLayout
import com.m4coding.coolhub.api.datasource.bean.HotDataBean

/**
 * Description:
 * Date：2019/7/24-18:11
 * Author: cwh
 */
class HotAdapter(val context: Context, var mData: MutableList<HotDataBean>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recommend, parent, false)
        return HotViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val hotDataBean = mData[position]
        val mViewHolder = holder as HotViewHolder
        mViewHolder.mTvContent.text = "${hotDataBean.ownerName} / ${hotDataBean.repositoryName}"
        mViewHolder.mTvDes.text = "${hotDataBean.describe}"
        mViewHolder.mTvLanguage.text = hotDataBean.language ?: ""
        mViewHolder.mTvStart.text = "${hotDataBean.allStarNum}"
        mViewHolder.mTvFork.text = "${hotDataBean.forkNum}"

    }

    inner class HotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImgUser = itemView.find<ImageView>(R.id.mImgUser)
        val mTvContent = itemView.find<TextView>(R.id.mTvContent)
        val mTvDes = itemView.find<TextView>(R.id.mTvDes)
        val mTvLanguage = itemView.find<TextView>(R.id.mTvLanguage)
        val mTvStart = itemView.find<TextView>(R.id.mTvStart)
        val mTvFork = itemView.find<TextView>(R.id.mTvFork)
    }

}