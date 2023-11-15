package com.ksp.vasool.util

import android.content.Context
import android.graphics.Typeface


/**
 * Created by sathya-6501 on 14/08/23.
 */
object ViewUtils
{
    private var robotoRegularTypeFace:Typeface? = null
    private var robotoMediumTypeFace:Typeface? = null
    
    fun getRobotoRegularTypeface(context:Context):Typeface? {
        if (robotoRegularTypeFace == null) {
            robotoRegularTypeFace = Typeface.createFromAsset(context.assets ,
                "fonts/Roboto-Regular.ttf") // No I18N
        }
        return robotoRegularTypeFace
    }

    fun getRobotoMediumTypeface(context:Context):Typeface? {
        if (robotoMediumTypeFace == null) {
            robotoMediumTypeFace = Typeface.createFromAsset(context.assets ,
                "fonts/Roboto-Medium.ttf") // No I18N
        }
        return robotoMediumTypeFace
    }
}