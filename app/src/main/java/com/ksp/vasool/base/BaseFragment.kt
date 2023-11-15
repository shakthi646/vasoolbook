package com.ksp.vasool.base

import android.text.TextUtils
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ksp.vasool.MainNavigationActivity


/**
 * Created by sathya-6501 on 18/08/23.
 */
open class BaseFragment : Fragment()
{
    protected fun setUpActionBar(toolbar :Toolbar , titleString : String ?= null)
    {
        (activity as MainNavigationActivity).setSupportActionBar(toolbar)


        val mActionBar = (activity as MainNavigationActivity).supportActionBar!!

        mActionBar.setDisplayHomeAsUpEnabled(true)
        toolbar.contentInsetStartWithNavigation = 1

        if (!TextUtils.isEmpty(titleString))
        {
            mActionBar.title = titleString
        }
        else
        {
            mActionBar.setDisplayShowTitleEnabled(false)
        }
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item:MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}