package com.ksp.vasool.util.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.ksp.vasool.constants.PreferenceConstants
import com.ksp.vasool.ui.accounts.CompanyDetails
import com.zoho.payslipgenerator.util.sharedpreference.PreferenceAccessor.get
import com.zoho.payslipgenerator.util.sharedpreference.PreferenceAccessor.getPrefs
import com.zoho.payslipgenerator.util.sharedpreference.PreferenceAccessor.put

/**
 * Created by sathya-6501 on 30/11/23.
 */
object PreferenceUtil
{
    fun Context.getSharedPreference(): SharedPreferences
    {
        return getPrefs(PreferenceConstants.sharedPreferences)
    }

    fun Context.isCompanyNameAvailable(): Boolean = getSharedPreference()[PreferenceConstants.orgName, ""].isNullOrBlank()

    fun Context.updateCompanyDetailsInSP(companyDetails: CompanyDetails)
    {
        getSharedPreference().edit {

            companyDetails.apply {
                put(PreferenceConstants.orgName, companyName)
                put(PreferenceConstants.orgAddress, companyAddress)
                put(PreferenceConstants.orgCity, companyCity)
                put(PreferenceConstants.orgCountry, companyCountry)
            }
        }
    }

    fun Context.getCompanyDetailsFromSP() : CompanyDetails
    {
        val sharedPreferences = getSharedPreference()

        val companyName = sharedPreferences[PreferenceConstants.orgName, ""]
        val companyAddress = sharedPreferences[PreferenceConstants.orgAddress, ""]

        return CompanyDetails(companyName,companyAddress)
    }
}