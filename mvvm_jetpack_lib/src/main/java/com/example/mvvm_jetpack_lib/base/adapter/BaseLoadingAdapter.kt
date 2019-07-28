package com.chainspower.mywechat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mvvm_jetpack_lib.R
import java.util.ArrayList


private const val HEADER_VIEW_TYPE: Int = 0x00011111
private const val FOOTER_VIEW_TYPE: Int = 0x00022222
private const val LOADING_VIEW_TYPE: Int = 0x00033333

abstract class BaseLoadingAdapter<T>(var context: Context, var mData: MutableList<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * 加载的状态  0 loading  1 loading fail  2 loading success  3 no more data
     */
    private var mLoadingStatus: Int = 0


    /**
     * 是否正在加载
     */
    private var isLoading = false

    /**
     * 是否是第一次初始化完成，避免第一次加载时，
     * 已显示了最后一条item导致一直加载
     */
    private var isFirstInit = true

    //head views
    private var mHeaderView: MutableList<View> = ArrayList()

    //footer views
    private var mFooterView: MutableList<View> = ArrayList()

    lateinit var onLoadingListener: () -> Unit


    /**
     * 添加header view
     */
    public fun addHeaderView(view: View) {
        mHeaderView.add(view)
        notifyDataSetChanged()
    }

    /**
     * 刷新数据完成时，调用此方法，重置加载更多状态
     */
    public fun refreshSuccess() {
        isLoading = false
        mLoadingStatus = 0
        notifyItemChanged(itemCount - 1)
    }

    /**
     * 指定位置添加header view (这里添加的header view 为固定占一行的宽度，
     * 若希望只占一个item宽度的header view ,设置item view type，自行添加)
     */
    public fun addHeaderView(view: View, index: Int) {
        if (index < 0) {
            throw IllegalArgumentException("index must > 0")
        }
        mHeaderView.add(index, view)
        notifyDataSetChanged()
    }

    /**
     * 添加footer view (这里添加的footer view 为固定占一行的宽度，
     * 若希望只占一个item的宽度的footer view,设置 item view type,自行添加)
     */
    public fun addFooterView(view: View) {
        mFooterView.add(view)
        notifyDataSetChanged()
    }


    /**
     * 指定位置添加footer view
     */
    public fun addFooterView(view: View, index: Int) {
        if (index < 0) {
            throw IllegalArgumentException("index must > 0")
        }
        mFooterView.add(index, view)
        notifyDataSetChanged()
    }

    public abstract fun onCreateNormalViewHolder(parent: ViewGroup, itemViewType: Int): RecyclerView.ViewHolder

    public abstract fun onBindNormalViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int)

    /**
     * @param position 已经过处理。指定的为实际数据的position
     *
     * 重写时，如果不需要，直接返回 0 即可
     *
     */
    public abstract fun getItemType(position: Int): Int


    /**
     * 在新数据添加到list后，再添设置状态
     */
    public fun loadFail() {
        isLoading = false
        mLoadingStatus = 1
        notifyItemChanged(itemCount - 1)
    }

    /**
     * 在新数据添加到list后，再添设置状态
     */
    public fun loadNoMoreData() {
        isLoading = false
        mLoadingStatus = 3
        notifyItemChanged(itemCount - 1)
    }

    /**
     * 在新数据添加到list后，再添设置状态
     */
    public fun enableLoadMore() {
        isLoading = false
        mLoadingStatus = 2
        notifyItemChanged(itemCount - 1)
    }

    /**
     * 是否正在加载更多，避免此时刷新
     */
    public fun isLoading(): Boolean {
        return isLoading
    }


    /**
     * @param position 对应的实际数据的position
     *
     * 当有多种itemType时，重写该方法
     */
    private fun getItemNormalType(position: Int): Int {
        //避免type指定的数字重复
        return itemCount + getItemType(position) + 1
    }


//    fun test(){
//        BaseQuickAdapter
//    }


    //用于判断是否是Grid View,设置 header 或 footer 和loading more view为占一行
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        layoutManager?.let {
            if (it is GridLayoutManager) {
                it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when {
                            position < mHeaderView.size -> it.spanCount
                            position < itemCount - mFooterView.size - 1 -> 1
                            position < itemCount - 1 -> it.spanCount
                            else -> it.spanCount
                        }
                    }

                }
            }


        }

    }

    //用于判断是否是staggered view,设置header 或 footer 和 loading more view 为占一行
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        val layoutParams = holder.itemView.layoutParams
        layoutParams?.let {
            if (it is StaggeredGridLayoutManager.LayoutParams) {
                val position = holder.layoutPosition
                when {
                    position < mHeaderView.size -> it.isFullSpan = true
                    position < itemCount - mFooterView.size - 1 -> it.isFullSpan = false
                    position < itemCount - 1 -> it.isFullSpan = true
                    else -> it.isFullSpan = true
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType < mHeaderView.size) {
            return HeaderOrFooterViewHolder(mHeaderView[viewType])
        }
        if (viewType >= itemCount - mFooterView.size - 1 && viewType < itemCount - 1) {
            return HeaderOrFooterViewHolder(mFooterView[viewType - mHeaderView.size - mData.size])
        }
        if (viewType == LOADING_VIEW_TYPE) {
            Log.d("Load", "LOADING_VIEW_TYPE")
            val view = LayoutInflater.from(context).inflate(
                R.layout.loading_more_view,
                parent, false
            )
            return LoadingViewHolder(view)
        }
        return onCreateNormalViewHolder(parent, viewType)

    }

    /**
     * 此方法已计算好item count,不需要再重写，重写可能会导致，loading more view等的错乱
     */
    override fun getItemCount() = mHeaderView.size + mData.size + mFooterView.size + 1

    override fun onBindViewHolder(holer: RecyclerView.ViewHolder, position: Int) {
        when {
            //head view
            position < mHeaderView.size -> {}
            //normal view
            position < itemCount - mFooterView.size - 1 -> onBindNormalViewHolder(holer, position - mHeaderView.size)
            //foot view
            position < itemCount - 1 -> {}
            //loading view
            else -> {
                Log.d("Load", "Loading More ViewHolder")
                val mViewHolder=holer as BaseLoadingAdapter<*>.LoadingViewHolder
                setLoadingStatus(mViewHolder)
                //loading more
                if (mLoadingStatus == 0) {
                    if (!isLoading) {
                        isLoading = true
                        if (::onLoadingListener.isInitialized) {
                            onLoadingListener()
                        }
                    }
                } else if (mLoadingStatus == 1) {
                    mViewHolder.mLoadingFailView.setOnClickListener {
                        if (!isLoading) {
                            isLoading = true
                            mLoadingStatus = 0
                            notifyItemChanged(itemCount - 1)
                            if (::onLoadingListener.isInitialized) {
                                onLoadingListener()
                            }
                        } else {
                            mLoadingStatus = 0
                            notifyItemChanged(itemCount - 1)
                        }
                    }

                } else if (mLoadingStatus == 2) {
                    if (!isLoading) {
                        isLoading = true
                        if (::onLoadingListener.isInitialized) {
                            onLoadingListener()
                        }
                    }
                } else if (mLoadingStatus == 3) {
                    isLoading = false
                } else {

                }
            }
        }


//        if (holer !is BaseLoadingAdapter<*>.HeaderOrFooterViewHoler &&
//            holer !is BaseLoadingAdapter<*>.LoadingViewHolder
//        ) {
//            onBindNormalViewHolder(holer, position - mHeaderView.size)
//        } else if (holer is BaseLoadingAdapter<*>.LoadingViewHolder) {
//            Log.d("Load", "Loading More ViewHolder")
//            setLoadingStatus(holer)
//            //loading more
//            if (mLoadingStatus == 0) {
//                if (!isLoading) {
//                    isLoading = true
//                    if (::onLoadingListener.isInitialized) {
//                        onLoadingListener()
//                    }
//                }
//            } else if (mLoadingStatus == 1) {
//                holer.mLoadingFailView.setOnClickListener {
//                    if (!isLoading) {
//                        isLoading = true
//                        mLoadingStatus = 0
//                        notifyItemChanged(itemCount - 1)
//                        if (::onLoadingListener.isInitialized) {
//                            onLoadingListener()
//                        }
//                    } else {
//                        mLoadingStatus = 0
//                        notifyItemChanged(itemCount - 1)
//                    }
//                }
//
//            } else if (mLoadingStatus == 2) {
//                if (!isLoading) {
//                    isLoading = true
//                    if (::onLoadingListener.isInitialized) {
//                        onLoadingListener()
//                    }
//                }
//            } else if (mLoadingStatus == 3) {
//                isLoading = false
//            } else {
//
//            }
//        } else {
//
//        }
    }


    private fun setLoadingStatus(holder: BaseLoadingAdapter<*>.LoadingViewHolder) {
        holder.mLoadingView.visibility = View.INVISIBLE
        holder.mLoadingFailView.visibility = View.INVISIBLE
        holder.mNoMoreDataView.visibility = View.INVISIBLE
        when (mLoadingStatus) {
            0 -> holder.mLoadingView.visibility = View.VISIBLE
            1 -> holder.mLoadingFailView.visibility = View.VISIBLE
            2 -> holder.mLoadingView.visibility = View.VISIBLE
            3 -> holder.mNoMoreDataView.visibility = View.VISIBLE

        }

    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < mHeaderView.size -> position
            position < itemCount - mFooterView.size - 1 -> getItemNormalType(position - mHeaderView.size)
            position < itemCount - 1 -> position
            else -> LOADING_VIEW_TYPE
        }
    }

    inner class HeaderOrFooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mLoadingView = itemView.findViewById<RelativeLayout>(R.id.mLoading)!!
        var mLoadingFailView = itemView.findViewById<RelativeLayout>(R.id.mLoadingFail)!!
        var mNoMoreDataView = itemView.findViewById<RelativeLayout>(R.id.mNoMoreData)!!
    }


}