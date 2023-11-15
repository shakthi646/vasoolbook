package com.ksp.vasool.ui.contact.view

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksp.vasool.MainNavigationActivity
import com.ksp.vasool.adapter.loan.InstallmentsAdapter
import com.ksp.vasool.base.BaseFragment
import com.ksp.vasool.common.CustomDividerItemDecoration
import com.ksp.vasool.databinding.FragmentContactDetailsBinding
import com.ksp.vasool.ui.collection.viewmodel.CollectionViewModel
import com.ksp.vasool.ui.contact.model.Contact
import com.ksp.vasool.ui.contact.viewmodel.ContactViewModel
import com.ksp.vasool.ui.loan.model.Installment
import com.ksp.vasool.ui.loan.model.Loan
import com.ksp.vasool.ui.loan.view.InstallmentBottomSheetFragment
import com.ksp.vasool.ui.loan.viewmodel.LoanViewModel
import com.ksp.vasool.util.VasoolUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ContactDetailsFragment : BaseFragment() {

    private lateinit var mBinding:FragmentContactDetailsBinding
    private lateinit var contactViewModel :ContactViewModel
    private lateinit var loanViewModel :LoanViewModel
    private lateinit var lineViewModel : CollectionViewModel
    private lateinit var mContactId : String
    private var contactDetails : Contact? = null
    private var activeLoanDetails : Loan? = null

    private val args: ContactDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)

        mContactId = args.contactId

        initializeVM()
    }

    override fun onCreateView(inflater:LayoutInflater , container:ViewGroup? , savedInstanceState:Bundle?):View? {

        mBinding = FragmentContactDetailsBinding.inflate(layoutInflater)
        return mBinding.root
    }



    override fun onViewCreated(view:View , savedInstanceState:Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        setupOnClickListeners()
        getContactDetails()
        setUpObservers()
        setUpActionMenu()

    }

    private fun setUpActionMenu()
    {
//        val menuHost: MenuHost = requireActivity()
//
//        menuHost.addMenuProvider(object : MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.example_menu, menu)
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                // Handle the menu selection
//                return when (menuItem.itemId) {
//                    R.id.menu_clear -> {
//                        // clearCompletedTasks()
//                        true
//                    }
//                    else -> false
//                }
//            }
//        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setUpObservers()
    {
        lifecycleScope.launch{

            val hasActiveLoan = loanViewModel.hasActiveLoan(mContactId)

            if(hasActiveLoan)
            {
                mBinding.emptyView.visibility = View.GONE
                val loanId = loanViewModel.getActiveLoanId(mContactId)

                withContext(Dispatchers.Main)
                {
                    loanViewModel.getLoanLiveData(loanId).observe(viewLifecycleOwner, Observer {
                        it?.let {
                            updateActiveLoanUI(it)
                            contactViewModel.updateContact(mContactId, it.loanBalanceAmount!!)
                        }
                    })
                }
            }
            else
            {
                mBinding.emptyView.visibility = View.VISIBLE
            }
        }
    }

    private fun updateActiveLoanUI(loanDetails:Loan)
    {
        mBinding.loanRepaymentDetailsCard.visibility = View.VISIBLE
        activeLoanDetails = loanDetails

        updateLoanDetailsCard(loanDetails)

        lifecycleScope.launch {
            val installmentList = loanDetails.loanId?.let { loanViewModel.getInstallmentList(it) }

            withContext(Dispatchers.Main)
            {
                installmentList?.let { updateInstallmentsRecyclerView(it) }
            }
        }

        if(activeLoanDetails?.isLoanActive == true)
        {
            mBinding.addLoanFaf.text = "Add EMI"
        }
        else
        {
            mBinding.addLoanFaf.text = "Add Loan"
        }
    }

    private fun updateLoanDetailsCard(loanDetails:Loan) {

        mBinding.totalAmountValue.text = VasoolUtil.formatToIndianRupees(loanDetails.totalLoanAmount)
        mBinding.totalRepaidAmountValue.text = VasoolUtil.formatToIndianRupees(loanDetails.loanRepaidAmount)
        mBinding.balanceAmountValue.text = VasoolUtil.formatToIndianRupees(loanDetails.loanBalanceAmount)
        mBinding.loanStartDateValue.text = VasoolUtil.convertDateFormat(loanDetails.startDate!!)
        mBinding.loanEndDateValue.text = loanDetails.endDate?:"-"
        mBinding.paidDaysValue.text = loanDetails.installmentsCount.toString()
        mBinding.emiAmountValue.text = VasoolUtil.formatToIndianRupees(loanDetails.loanEmiAmount)
        mBinding.interestRateValue.text = loanDetails.loanInterestRate.toString()
        mBinding.isInterestDebitedValue.text = if(loanDetails.isInterestDebited) "Yes" else "No"

    }



    private fun updateInstallmentsRecyclerView(installmentsList: List<Installment>)
    {
        val installmentsAdapter = InstallmentsAdapter(installmentsList){
            Toast.makeText(requireContext(), it.installmentAmount.toString(), Toast.LENGTH_LONG).show()
        }

        mBinding.instalmentListRecycler.apply{
            layoutManager = (LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
            addItemDecoration(CustomDividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            setHasFixedSize(true)
            adapter = installmentsAdapter
        }
    }

    private fun setupOnClickListeners()
    {
        lifecycleScope.launch {
            mBinding.addLoanFaf.setOnClickListener {
                checkActiveLoanAndNavigate()
            }
        }

        mBinding.closedLoansBtn.setOnClickListener {
            openClosedLoansListFragment()
        }
    }

    private fun openClosedLoansListFragment()
    {
        val action = ContactDetailsFragmentDirections.actionContactDetailsToClosedLoanListFragment(contactDetails!!)
        findNavController().navigate(action)
    }

    private fun checkActiveLoanAndNavigate() {
        lifecycleScope.launch {
            val hasActiveLoan = loanViewModel.hasActiveLoan(mContactId)
            if (hasActiveLoan)
            {
                openInstallmentBottomSheet()
            } else {
                // Navigate to the new loan creation fragment
                val action = ContactDetailsFragmentDirections.actionContactDetailsToAddLoanFragment(mContactId, contactDetails?.lineId!!)
                findNavController().navigate(action)
            }
        }
    }

    private fun openInstallmentBottomSheet()
    {
        val bottomSheetFragment = InstallmentBottomSheetFragment(activeLoanDetails?.loanBalanceAmount!!)
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun getContactDetails() {
        contactViewModel.getContactDetails(mContactId)

        contactViewModel.contactDetailsLD.observe(viewLifecycleOwner, Observer {
            contactDetails = it
            setUpActionBar(mBinding.toolbar, contactDetails?.contactName?:"Contact Details")
        })
    }

    private fun initializeVM() {
        contactViewModel = (activity as MainNavigationActivity).getContactViewModel()
        loanViewModel = (activity as MainNavigationActivity).getLoanViewModel()
        lineViewModel = (activity as MainNavigationActivity).getCollectionViewModel()
    }

    fun onInstallmentAdded(installmentAmount:Int , installmentDate:String)
    {
        val installment = Installment().apply {
            this.installmentAmount = installmentAmount
            this.installmentDate = installmentDate
            this.loanRepaidAmount = activeLoanDetails?.loanRepaidAmount?.plus(installmentAmount)
            this.loanId = activeLoanDetails?.loanId
            this.contactId = mContactId
            this.lineId = activeLoanDetails?.lineId
        }

        loanViewModel.insertInstallmentAndUpdateLoan(installment)
    }
}