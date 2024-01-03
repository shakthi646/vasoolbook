package com.ksp.vasool.ui.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.ksp.vasool.R
import com.ksp.vasool.base.BaseFragment
import com.ksp.vasool.constants.SessionVariable
import com.ksp.vasool.constants.StringConstants
import com.ksp.vasool.databinding.FragmentSettingsBinding

/**
 * Created by sathya-6501 on 22/12/23.
 */
class SettingsFragment : BaseFragment()
{
    private lateinit var mBinding: FragmentSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentSettingsBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBar(mBinding.toolbar, "Settings")

        setUpOnCLickListeners()
    }

    private fun setUpOnCLickListeners() {

        mBinding.organizationProfile.setOnClickListener{

            val action = SettingsFragmentDirections.actionSettingsToCompanyDetails()
            findNavController().navigate(action)
        }

        mBinding.privacyLayout.setOnClickListener{
            try
            {
                val intent = Intent(requireContext(), OssLicensesMenuActivity::class.java)
                startActivity(intent)
            }
            catch (e: ActivityNotFoundException)
            {
            }
        }

        mBinding.feedbackLayout.setOnClickListener{
            sendFeedback()
        }

        mBinding.shareAppLayout.setOnClickListener {
            shareApp()
        }

        mBinding.rateUsLayout.setOnClickListener {
            rateUsClicked()
        }
    }

    private fun rateUsClicked()
    {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.data = Uri.parse(StringConstants.app_uri)
        try
        {
            startActivity(intent)
        }
        catch (e: ActivityNotFoundException)
        {
//            Toast.makeText(context, resources.getString(R.string.em_no), Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareApp() {
        try {
            val appPackageName = requireActivity().getPackageName()
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))

            //app playstore url
            val appUrl = StringConstants.app_url

            val shortDescription = getString(R.string.short_description)

            shareIntent.putExtra(Intent.EXTRA_TEXT, "$shortDescription\n\nCheck out Vasool Book Android App at:\n$appUrl")
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        }
        catch (e: Exception)
        {

        }
    }

    private fun sendFeedback()
    {
        val mailto = arrayOf(resources.getString(R.string.app_support_email))
        val sendIntent = Intent(Intent.ACTION_SENDTO)
        sendIntent.setData(Uri.parse("mailto:"))
        sendIntent.putExtra(Intent.EXTRA_EMAIL, mailto)
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.feedback_subject_from, SessionVariable.companyName))
        try
        {
            sendIntent.putExtra(Intent.EXTRA_TEXT, feedbackDetails)
            try
            {
                startActivity(Intent.createChooser(sendIntent, "Send email via"))
            }
            catch (e: ActivityNotFoundException)
            {
                val ab = AlertDialog.Builder(requireActivity().applicationContext)
                ab.setMessage("Please send us an email to developer.nativeapps@gmail.com")
                ab.setPositiveButton("Okay", null)
                ab.show()
            }

        }
        catch (e: PackageManager.NameNotFoundException)
        {

        }
    }

    val feedbackDetails: String
        @Throws(PackageManager.NameNotFoundException::class) get()
        {
            val details = StringBuilder()
            details.append("\n\n\n")
            details.append("===== Details =====")
            details.append("\n")
            details.append(requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0).versionName)
            details.append("/")
            details.append(SessionVariable.companyName)
            details.append("\n")
            details.append("    ")
            details.append(Build.MANUFACTURER)
            details.append("/")
            details.append(Build.MODEL)
            details.append("/")
            details.append(android.os.Build.VERSION.SDK_INT)
            details.append("\n")
            var appstore = requireActivity().packageManager.getInstallerPackageName(requireActivity().packageName)
            if (TextUtils.isEmpty(appstore))
            {
                appstore = "UnKnown"
            }
            details.append(appstore)
            details.append("\n")
            details.append("======================")
            return details.toString()
        }
}