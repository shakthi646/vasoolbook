package com.ksp.vasool.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.ksp.vasool.util.ViewUtils


/**
 * Created by sathya-6501 on 14/08/23.
 */
class RobotoRegularTextView(context:Context , attrs:AttributeSet) : AppCompatTextView(context, attrs)
{
    override fun setTypeface(tf: Typeface?)
    {
        super.setTypeface(ViewUtils.getRobotoRegularTypeface(context))
    }
}