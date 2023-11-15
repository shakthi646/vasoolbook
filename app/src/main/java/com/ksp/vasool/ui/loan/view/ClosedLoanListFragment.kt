package com.ksp.vasool.ui.loan.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.ksp.vasool.MainNavigationActivity
import com.ksp.vasool.base.BaseFragment
import com.ksp.vasool.databinding.ClosedLoanListFragmentBinding
import com.ksp.vasool.databinding.LoanDetailsCardBinding
import com.ksp.vasool.ui.loan.model.Loan
import com.ksp.vasool.ui.loan.viewmodel.LoanViewModel
import com.ksp.vasool.util.VasoolUtil
import kotlinx.coroutines.launch


/**
 * Created by sathya-6501 on 24/10/23.
 */
class ClosedLoanListFragment : BaseFragment()
{
    lateinit var mBinding : ClosedLoanListFragmentBinding
    private lateinit var loanViewModel : LoanViewModel
    val args: ClosedLoanListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = ClosedLoanListFragmentBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpActionBar(mBinding.toolbar, "Closed Loans List")

        val contactDetails = args.contactDetails
        loanViewModel = (activity as MainNavigationActivity).getLoanViewModel()

        lifecycleScope.launch {
            val loanList = loanViewModel.getClosedLoanList(contactDetails.contactId!!)
            loadLoanDetailsList(loanList)
        }
    }

    private fun loadLoanDetailsList(loanList: List<Loan>?) {
        mBinding.loanListDetailsContainer.removeAllViews()

        loanList?.forEach {loanDetails ->
            mBinding.loanListDetailsContainer.addView(inflateLoanDetailsCard(loanDetails))
        }
    }

    private fun inflateLoanDetailsCard(loanDetails: Loan): View
    {
        val binding = LoanDetailsCardBinding.inflate(layoutInflater)

        binding.totalAmountValue.text = VasoolUtil.formatToIndianRupees(loanDetails.totalLoanAmount)
        binding.totalRepaidAmountValue.text = VasoolUtil.formatToIndianRupees(loanDetails.loanRepaidAmount)
        binding.balanceAmountValue.text = VasoolUtil.formatToIndianRupees(loanDetails.loanBalanceAmount)
        binding.loanStartDateValue.text = loanDetails.startDate?.let { VasoolUtil.convertDateFormat(it) }
        binding.loanEndDateValue.text = loanDetails.endDate?.let { VasoolUtil.convertDateFormat(it) }
        binding.paidDaysValue.text = loanDetails.installmentsCount.toString()
        binding.emiAmountValue.text = VasoolUtil.formatToIndianRupees(loanDetails.loanEmiAmount)
        binding.interestRateValue.text = loanDetails.loanInterestRate.toString()
        binding.isInterestDebitedValue.text = if(loanDetails.isInterestDebited) "Yes" else "No"

        return binding.root
    }
}