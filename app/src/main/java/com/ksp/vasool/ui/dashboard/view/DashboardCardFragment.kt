package com.ksp.vasool.ui.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ksp.vasool.base.BaseFragment
import com.ksp.vasool.base.BaseViewModelFactory
import com.ksp.vasool.database.AppDatabase
import com.ksp.vasool.databinding.DashboardCardFragmentBinding
import com.ksp.vasool.ui.collection.model.Line
import com.ksp.vasool.ui.loan.data.LoanRepository
import com.ksp.vasool.ui.loan.viewmodel.LoanViewModel
import com.ksp.vasool.util.VasoolUtil

/**
 * Created by sathya-6501 on 11/11/23.
 */
class DashboardCardFragment(line: Line) : BaseFragment()
{
    lateinit var mBinding : DashboardCardFragmentBinding
    private lateinit var loanViewModel : LoanViewModel
    private val localLine = line

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLoanViewModel()
        loanViewModel.setLineId(lineId = localLine.lineId!!)
    }

    private fun setLoanViewModel() {
        val loanRepository = LoanRepository(AppDatabase.getInstance(requireContext()).loanDao())
        val loanViewModelFactory = BaseViewModelFactory(loanRepository)
        loanViewModel = ViewModelProvider(this, loanViewModelFactory)[LoanViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DashboardCardFragmentBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.textViewCollectionName.text = localLine.lineName
        setUpObservers()
    }

    private fun setUpObservers() {

        loanViewModel.setLineId(lineId = localLine.lineId!!)

        loanViewModel.totalLoanBalanceAmountLD.observe(viewLifecycleOwner, Observer {totalBalanceAmount ->
            totalBalanceAmount?.let {
                mBinding.totalReceivable.text = VasoolUtil.formatToIndianRupees(it)
            }
        })

        loanViewModel.todayTotalInstallmentsAmount.observe(viewLifecycleOwner, Observer {totalBalanceAmount ->
            totalBalanceAmount?.let {
//                mBinding.todayTotalInstallments.text = VasoolUtil.formatToIndianRupees(it)
            }
        })


    }

    companion object
    {
        fun newInstance(line: Line): DashboardCardFragment
        {
            return DashboardCardFragment(line)
        }
    }
}