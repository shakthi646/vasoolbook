package com.ksp.vasool.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Created by sathya-6501 on 07/11/23.
 */
class CustomViewPager : ViewPager
{
    private var disable: Boolean? = false

    constructor(context: Context) : super(context)
    {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean
    {
        if(disable!! || currentItem == 0 && childCount == 0)
        {
            return false
        }

        return super.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        if(disable!! || currentItem == 0 && childCount == 0)
        {
            return false
        }

        return super.onTouchEvent(event)
    }

    // this method is used for Enable or Disable the viewPager scroll. (Eg. when use nested viewPager this method is used to disable the parent viewPager scroll)
    fun disableScroll(disable: Boolean?)
    {
        //When disable = true not work the scroll and when disble = false work the scroll
        this.disable = disable
    }
}