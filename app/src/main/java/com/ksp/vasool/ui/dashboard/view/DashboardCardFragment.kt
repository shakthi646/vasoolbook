package com.ksp.vasool.ui.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ksp.vasool.base.BaseFragment
import com.ksp.vasool.databinding.DashboardCardFragmentBinding
import com.ksp.vasool.ui.collection.model.Line

/**
 * Created by sathya-6501 on 11/11/23.
 */
class DashboardCardFragment : BaseFragment()
{
    lateinit var mBinding : DashboardCardFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DashboardCardFragmentBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object
    {
        fun newInstance(line: Line): DashboardCardFragment
        {
            return DashboardCardFragment()
        }
    }
}