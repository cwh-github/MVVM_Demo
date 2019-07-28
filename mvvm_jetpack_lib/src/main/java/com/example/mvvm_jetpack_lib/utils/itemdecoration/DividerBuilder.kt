package com.example.mvvm_jetpack_lib.utils.itemdecoration

import android.graphics.Color


/**
 * Description:
 * Date：2019/7/28-16:42
 * Author: cwh
 */
class DividerBuilder {

    private var mLeftSildeLine: SideLine? = null

    private var mTopSideLine: SideLine? = null

    private var mRightSideLine: SideLine? = null

    private var mBottomSideLine: SideLine? = null


    fun setLeftSideLine(leftSideLine: SideLine): DividerBuilder {
        this.mLeftSildeLine = leftSideLine
        return this
    }

    fun setTopSideLine(topSideLine: SideLine): DividerBuilder {
        this.mTopSideLine = topSideLine
        return this
    }

    fun setRightSideline(rightSideLine: SideLine): DividerBuilder {
        this.mRightSideLine = rightSideLine
        return this
    }

    fun setBottomSideLine(bottomSideLine: SideLine): DividerBuilder {
        this.mBottomSideLine = bottomSideLine
        return this
    }


    fun create(): Divider {
        val defaultSideLine = SideLine(false, Color.TRANSPARENT, null, 0f, 0f, 0f)
        mLeftSildeLine = mLeftSildeLine ?: defaultSideLine
        mTopSideLine = mTopSideLine ?: defaultSideLine
        mRightSideLine = mRightSideLine ?: defaultSideLine
        mBottomSideLine = mBottomSideLine ?: defaultSideLine
        return Divider(mLeftSildeLine!!, mTopSideLine!!, mRightSideLine!!, mBottomSideLine!!)
    }


}