package com.ksp.vasool.common

import android.app.Application
import com.ksp.vasool.constants.PreferenceConstants
import com.ksp.vasool.constants.SessionVariable
import com.ksp.vasool.util.sharedpreference.PreferenceUtil.getSharedPreference

/**
 * Created by sathya-6501 on 22/12/23.
 */
class VasoolApplication : Application()
{
    var companyName             = ""

    override fun onCreate() {
        super.onCreate()

        val preference = getSharedPreference()
        companyName = preference.getString(PreferenceConstants.orgName, "")?:"Welcome !"
        SessionVariable.companyName = companyName
    }


}