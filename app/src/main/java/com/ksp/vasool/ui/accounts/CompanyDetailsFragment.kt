package com.ksp.vasool.ui.accounts

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ksp.vasool.MainNavigationActivity
import com.ksp.vasool.base.BaseFragment
import com.ksp.vasool.common.VasoolApplication
import com.ksp.vasool.constants.SessionVariable
import com.ksp.vasool.databinding.FragmentCompanyDetailsBinding
import com.ksp.vasool.ui.dashboard.view.DashboardFragmentDirections
import com.ksp.vasool.util.sharedpreference.PreferenceUtil.getCompanyDetailsFromSP
import com.ksp.vasool.util.sharedpreference.PreferenceUtil.getSharedPreference
import com.ksp.vasool.util.sharedpreference.PreferenceUtil.updateCompanyDetailsInSP

/**
 * Created by sathya-6501 on 17/12/23.
 */
class CompanyDetailsFragment : BaseFragment()
{
    lateinit var mBinding : FragmentCompanyDetailsBinding
    lateinit var preference : SharedPreferences
    lateinit var companyDetails: CompanyDetails
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = (activity as MainNavigationActivity).getSharedPreference()
        companyDetails = (activity as MainNavigationActivity).getCompanyDetailsFromSP()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentCompanyDetailsBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadCompanyDetails()

        mBinding.saveButton.setOnClickListener {

            if(mBinding.etName.text.toString().isNullOrBlank())
            {
                mBinding.etName.error = "Kindly Enter your Company Name..."
            }
            else
            {
                companyDetails = companyDetails.copy(companyName = mBinding.etName.editableText.toString(), companyAddress = mBinding.etAddress.editableText.toString())
                (activity as MainNavigationActivity).updateCompanyDetailsInSP(companyDetails)
                SessionVariable.companyName = companyDetails.companyName

                val action = CompanyDetailsFragmentDirections.actionCompanyDetailsToDashboard()
                findNavController().navigate(action)
            }
        }
    }

    private fun loadCompanyDetails() {
        mBinding.etName.setText(companyDetails.companyName)
        mBinding.etAddress.setText(companyDetails.companyAddress)
    }


}