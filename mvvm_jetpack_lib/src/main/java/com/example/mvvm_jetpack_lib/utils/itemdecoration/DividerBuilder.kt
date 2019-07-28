package com.example.mvvm_jetpack_lib.utils.itemdecoration

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt


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


    fun setLeftSideLine(
        visibility: Boolean,
        @ColorInt color: Int,
        drawable: Drawable? = null,
        lineWidth: Float,
        startPadding: Float,
        endPadding: Float
    ): DividerBuilder {
        this.mLeftSildeLine = SideLine(visibility,color,drawable,lineWidth,startPadding,endPadding)
        return this
    }

    fun setTopSideLine(
        visibility: Boolean,
        @ColorInt color: Int,
        drawable: Drawable? = null,
        lineWidth: Float,
        startPadding: Float,
        endPadding: Float
    ): DividerBuilder {
        this.mTopSideLine = SideLine(visibility,color,drawable,lineWidth,startPadding,endPadding)
        return this
    }

    fun setRightSideline(
        visibility: Boolean,
        @ColorInt color: Int,
        drawable: Drawable? = null,
        lineWidth: Float,
        startPadding: Float,
        endPadding: Float
    ): DividerBuilder {
        this.mRightSideLine = SideLine(visibility,color,drawable,lineWidth,startPadding,endPadding)
        return this
    }

    fun setBottomSideLine(
        visibility: Boolean,
        @ColorInt color: Int,
        drawable: Drawable? = null,
        lineWidth: Float,
        startPadding: Float,
        endPadding: Float
    ): DividerBuilder {
        this.mBottomSideLine = SideLine(visibility,color,drawable,lineWidth,startPadding,endPadding)
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