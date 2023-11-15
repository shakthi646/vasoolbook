package com.ksp.vasool.common

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.ksp.vasool.R


/**
 * Created by sathya-6501 on 18/08/23.
 */
class CustomDividerItemDecoration(context:Context , orientation: Int) :
    DividerItemDecoration(context, orientation) {

    init {
        val divider: Drawable? = ContextCompat.getDrawable(context, R.drawable.custom_divider)
        setDrawable(divider!!)
    }
}