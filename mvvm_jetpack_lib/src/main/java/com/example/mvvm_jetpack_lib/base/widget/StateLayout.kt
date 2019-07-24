package com.example.mvvm_jetpack_lib.base.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.mvvm_jetpack_lib.R
import com.example.mvvm_jetpack_lib.utils.LogUtils
import com.example.mvvm_jetpack_lib.utils.click

/**
 * Description: 加载数据时的状态Layout,根据加载状态的不同展示不同的View
 * Date：2019/7/22-10:53
 * Author: cwh
 */
class StateLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * 加载中view
     */
    private var mLoadingView: View = LayoutInflater.from(context).inflate(
        R.layout.loading_state_layout,
        this, false
    )
    /**
     * 加载成功，无数据view
     */
    private var mEmptyView: View = LayoutInflater.from(context).inflate(
        R.layout.empry_state_layout,
        this, false
    )

    /**
     * 加载失败view
     */
    private var mErrorView: View = LayoutInflater.from(context).inflate(
        R.layout.error_state_layout,
        this, false
    )

    /**
     * content view
     */
    private var mContentView: View? = null

    /**
     * 是否在展示状态view时，显示content view 的背景
     */
    private var isShowContentViewBg = true

    /**
     * 是否启用加载状态时的半透明阴影
     */
    private var isEnableLoadingShadow = false

    /**
     * 展示error view 时，重试的回调
     */
    var onErrorClick: ((View) -> Unit)? = null


    private var mState: State = State.NONE

    /**
     * 默认动画执行时间
     */
    private var mDuration: Long = 250L


    /**
     * 将给定的加载成功content显示的view,放到StateLayout
     *
     * 将stateLayout 作为整体需要显示的View
     */
    fun wrap(view: View?): StateLayout {
        if (view == null) {
            throw NullPointerException("Content view can not null")
        }

        //init loading view
        with(mLoadingView) {
            visibility = View.GONE
            alpha = 0f
        }

        with(mEmptyView) {
            visibility = View.GONE
            alpha = 0f
        }

        with(mErrorView) {
            visibility = View.GONE
            alpha = 0f
            click {
                if (onErrorClick != null) {
                    showLoading()
                    onErrorClick!!(it)
                }
            }
        }

        with(view) {
            visibility = View.GONE
            alpha = 0f
        }

        //add contentview to parent

        mContentView = view
        if (isShowContentViewBg) {
            mContentView?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    background = it.background
                }
            }
        }

        addViewWithLayout(mLoadingView)
        addViewWithLayout(mEmptyView)
        addViewWithLayout(mErrorView)


        if (view.parent == null) {
            LogUtils.d("Has not Parent")
            addView(
                view, LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        } else {
            LogUtils.d("Has Parent")
            val parent = view.parent as ViewGroup
            val params = view.layoutParams
            val index = parent.indexOfChild(view)
            parent.removeView(view)
            addView(
                view, LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            parent.addView(this, index, params)
        }

        //默认显示loading View
        showStateView(State.LOADING)
        return this
    }

    fun wrap(activity: Activity): StateLayout {
        return wrap((activity.findViewById(android.R.id.content) as ViewGroup).getChildAt(0))
    }

    fun wrap(fragment: Fragment): StateLayout {
        return wrap(fragment.view)
    }

    /**
     * 展示Loading View
     */
    fun showLoading(): StateLayout {
        showStateView(State.LOADING)
        return this
    }

    /**
     * 展示empty View
     */
    fun showEmptyView(): StateLayout {
        showStateView(State.EMPTY)
        return this
    }


    /**
     * 展示Error view
     */
    fun showErrorView(): StateLayout {
        showStateView(State.ERROR)
        return this
    }

    /**
     * 展示 Content view
     */
    fun showContentView(): StateLayout {
        showStateView(State.CONTENT)
        return this
    }

    /**
     * 配置StateLayout
     * @param loadingView loading view
     * @param emptyView Empty view
     * @param errorView Error view
     * @param showContentViewBg 是否显示Content view 的背景
     * @param enableLoadingShadow 是否在加载时显示加载阴影
     * @param animationDuration 显示与隐藏动画的执行时间
     *@param errorClick Error view 显示时，点击的回调
     */
    fun config(
        loadingView: View = mLoadingView, emptyView: View = mEmptyView,
        errorView: View = mErrorView, showContentViewBg: Boolean = true,
        enableLoadingShadow: Boolean = false, animationDuration: Long = 250,
        errorClick: ((View) -> Unit)? = onErrorClick
    ): StateLayout {
        mLoadingView = loadingView
        mEmptyView = emptyView
        mErrorView = errorView
        isShowContentViewBg = showContentViewBg
        isEnableLoadingShadow = enableLoadingShadow
        mDuration = animationDuration
        onErrorClick = errorClick
        return this
    }

    /**
     * 配置StateLayout
     * @param loadingViewId loading view Id
     * @param emptyViewId Empty view Id
     * @param errorViewId Error view Id
     * @param showContentViewBg 是否显示Content view 的背景
     * @param enableLoadingShadow 是否在加载时显示加载阴影
     * @param animationDuration 显示与隐藏动画的执行时间
     *@param errorClick Error view 显示时，点击的回调
     */
    fun config(
        loadingViewId: Int = 0, emptyViewId: Int = 0,
        errorViewId: Int = 0, showContentViewBg: Boolean = true,
        enableLoadingShadow: Boolean = false, animationDuration: Long = 250,
        errorClick: ((View) -> Unit)? = onErrorClick
    ): StateLayout {
        if (loadingViewId != 0) {
            mLoadingView = inflater(loadingViewId)
        }
        if (emptyViewId != 0) {
            mEmptyView = inflater(emptyViewId)
        }
        if (errorViewId != 0) {
            mErrorView = inflater(errorViewId)
        }
        isShowContentViewBg = showContentViewBg
        isEnableLoadingShadow = enableLoadingShadow
        mDuration = animationDuration
        onErrorClick = errorClick
        return this
    }


    private fun inflater(id: Int): View = LayoutInflater.from(context).inflate(id, this, false)


    override fun onFinishInflate() {
        super.onFinishInflate()
        if (mContentView == null) {
            throw IllegalArgumentException("contentView can not be null!")
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (mState == State.LOADING && mLoadingView != null
            && mLoadingView.visibility == View.VISIBLE
        ) {
            return true
        }
        return super.dispatchTouchEvent(ev)
    }

    //展示状态view
    private fun showStateView(state: State): StateLayout {
        if(state.value==mState.value){
            return this
        }
        this.mState = state
        when (state) {
            State.LOADING -> {
                if (!isEnableLoadingShadow) {
                    mLoadingView.setBackgroundResource(0)
                } else {
                    mLoadingView.setBackgroundColor(Color.parseColor("#66000000"))
                }
                showView(mLoadingView)
            }

            State.EMPTY -> {
                showView(mEmptyView)
            }

            State.ERROR -> {
                showView(mErrorView)
            }

            State.CONTENT -> {
                mContentView?.let { showView(it) }
            }
        }

        return this
    }

    private fun showView(view: View) {
        if (mShowTask != null) {
            removeCallbacks(mShowTask)
        }
        mShowTask = ShowTask(view)
        post(mShowTask)
    }

    private var mShowTask: ShowTask? = null

    inner class ShowTask(private var target: View) : Runnable {
        override fun run() {
            for (i in 0 until childCount) {
                if (mState == State.LOADING && isEnableLoadingShadow && getChildAt(i) === mContentView) {
                    getChildAt(i)?.let {
                        showAnim(it)
                    }
                    continue
                }
                if (getChildAt(i) !== target) {
                    hideAnim(getChildAt(i))
                }

            }
            showAnim(target)
        }
    }

    private fun showAnim(view: View?) {
        if (view == null) {
            return
        }
        view.clearAnimation()
        val animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0f)
        animator.duration = mDuration
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                view.visibility = View.VISIBLE
            }
        })
        animator.start()


    }

    private fun hideAnim(view: View?) {
        if (view == null) {
            return
        }
        view.clearAnimation()
        val animator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f)
        animator.duration = mDuration
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                view.visibility = View.INVISIBLE
            }
        })
        animator.start()
    }


    private fun addViewWithLayout(view: View?) {
        if (view == null) {
            return
        }
        addView(view)
        val params = view.layoutParams
        (params as LayoutParams).gravity = Gravity.CENTER
        view.layoutParams = params
    }
}