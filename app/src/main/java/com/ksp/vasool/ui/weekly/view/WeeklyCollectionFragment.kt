package com.ksp.vasool.ui.weekly.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ksp.vasool.databinding.FragmentWeeklyCollectionBinding
import com.ksp.vasool.ui.contact.viewmodel.ContactViewModel

class WeeklyCollectionFragment : Fragment() {

    private lateinit var mBinding:FragmentWeeklyCollectionBinding
    private lateinit var contactViewModel :ContactViewModel

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater:LayoutInflater , container:ViewGroup? , savedInstanceState:Bundle?):View? {

        mBinding = FragmentWeeklyCollectionBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view:View , savedInstanceState:Bundle?) {
        super.onViewCreated(view , savedInstanceState)
    }

    companion object {

        fun newInstance() = WeeklyCollectionFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}