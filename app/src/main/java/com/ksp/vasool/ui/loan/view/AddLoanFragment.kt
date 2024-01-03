package com.ksp.vasool.ui.loan.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ksp.vasool.MainNavigationActivity
import com.ksp.vasool.base.BaseFragment
import com.ksp.vasool.constants.StringConstants
import com.ksp.vasool.databinding.FragmentAddLoanBinding
import com.ksp.vasool.ui.contact.viewmodel.ContactViewModel
import com.ksp.vasool.ui.loan.model.Loan
import com.ksp.vasool.ui.loan.viewmodel.LoanViewModel
import com.ksp.vasool.util.VasoolUtil
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddLoanFragment : BaseFragment() {

    private lateinit var mBinding:FragmentAddLoanBinding
    private lateinit var contactViewModel :ContactViewModel
    private lateinit var loanViewModel: LoanViewModel
    private lateinit var mContactId : String
    private lateinit var mLineId : String
    lateinit var calendar : Calendar

    private val args:AddLoanFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        mContactId = args.contactId
        mLineId = args.lineId

        calendar = Calendar.getInstance()
        setUpViewModel()
    }

    override fun onCreateView(inflater:LayoutInflater , container:ViewGroup? , savedInstanceState:Bundle?):View? {
        mBinding = FragmentAddLoanBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view:View , savedInstanceState:Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        setUpActionBar(mBinding.toolbar, "Create Loan")

        setUpDatePicker()
        setUpDefaultDisplay()
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners()
    {
        mBinding.switchInterestDebited.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
            {
//                mBinding.givenAmountTV.setText("0")
            }
            else
            {
//                mBinding.givenAmountTV.setText("0")
            }
        }

        mBinding.addLoanButton.setOnClickListener {
            if(validateInputFields())
            {
                createLoan()
                findNavController().navigateUp()
            }
        }
    }

    private fun validateInputFields():Boolean {

        if(mBinding.etLoanAmount.text.toString().isEmpty())
        {
            mBinding.etLoanAmount.error = "Enter the Loan Amount"
            mBinding.etLoanAmount.requestFocus()
            return false
        }
        else if(mBinding.etInterestAmount.text.toString().isEmpty())
        {
            mBinding.etInterestAmount.error = "Enter the Interest Rate"
            mBinding.etInterestAmount.requestFocus()
            return false
        }
        else if(mBinding.etInstallments.text.toString().isEmpty())
        {
            mBinding.etInstallments.error = "Enter the Installments"
            mBinding.etInstallments.requestFocus()
            return false
        }

        return true
    }

    private fun createLoan()
    {
        val loanPrincipalAmount : Int= mBinding.etLoanAmount.text.toString().toInt()
        val interestRate : Int = mBinding.etInterestAmount.text.toString().toInt()
        val installmentsCount : Int = mBinding.etInstallments.text.toString().toInt()

        val currentDate = VasoolUtil.getDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        val startDateString = currentDate
        val endDateString = VasoolUtil.addDaysToDate(currentDate, if(mBinding.switchInterestDebited.isChecked) installmentsCount else 7*installmentsCount)
        val totalInterestAmount = (interestRate*loanPrincipalAmount)/100
        val totalLoanAmount = if(mBinding.switchInterestDebited.isChecked)
            loanPrincipalAmount else loanPrincipalAmount+totalInterestAmount
        val emiAmount = totalLoanAmount/installmentsCount

        val loan = Loan()
        loan.apply {
            this.loanPrincipalAmount = loanPrincipalAmount
            this.contactId = mContactId
            this.lineId = mLineId
            this.loanId = StringConstants.loanIDPrefix+System.currentTimeMillis()
            this.loanInterestRate = interestRate
            this.installments = emptyList()
            this.totalLoanAmount = totalLoanAmount
            this.loanBalanceAmount = totalLoanAmount
            this.loanEmiAmount = emiAmount
            this.loanRepaidAmount = 0
            this.startDate = startDateString
            this.endDate = endDateString
            this.isInterestDebited = mBinding.switchInterestDebited.isChecked
        }

        loanViewModel.insertLoan(loan)
    }

    private fun setUpViewModel()
    {
        loanViewModel = (activity as MainNavigationActivity).getLoanViewModel()
        contactViewModel = (activity as MainNavigationActivity).getContactViewModel()
    }

    private fun setUpDefaultDisplay()
    {
        updateStartDateLabel()
        mBinding.etInstallments.setText("10")
    }

    private fun setUpDatePicker()
    {
        val startDatePicker = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateStartDateLabel()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        mBinding.tvStartDate.setOnClickListener {
            startDatePicker.show()
        }
    }

    private fun updateStartDateLabel() {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        mBinding.tvStartDate.text = dateFormat.format(loanViewModel.calendar.time)
    }

}