package com.ksp.vasool.ui.daily.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksp.vasool.MainNavigationActivity
import com.ksp.vasool.R
import com.ksp.vasool.adapter.ContactListAdapter
import com.ksp.vasool.base.BaseFragment
import com.ksp.vasool.common.CustomDividerItemDecoration
import com.ksp.vasool.databinding.FragmentDailyCollectionBinding
import com.ksp.vasool.ui.collection.viewmodel.CollectionViewModel
import com.ksp.vasool.ui.contact.viewmodel.ContactViewModel
import com.ksp.vasool.ui.loan.model.Installment
import com.ksp.vasool.ui.loan.viewmodel.LoanViewModel
import com.ksp.vasool.util.VasoolUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class CollectionBaseFragment : BaseFragment() {

    private lateinit var mBinding:FragmentDailyCollectionBinding
    lateinit var contactViewModel :ContactViewModel
    private lateinit var loanViewModel : LoanViewModel
    private lateinit var collectionViewModel: CollectionViewModel

    private lateinit var lineId : String
    private val args: CollectionBaseFragmentArgs by navArgs()
    private lateinit var contactListAdapter : ContactListAdapter

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)

        lineId = args.lineId
        initializeVM()
        loanViewModel.setLineId(lineId)
        contactViewModel.setLineId(lineId)
    }

    override fun onCreateView(inflater:LayoutInflater , container:ViewGroup? , savedInstanceState:Bundle?):View? {

        mBinding = FragmentDailyCollectionBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view:View , savedInstanceState:Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        setupOnClickListeners()
        updateUI()
//        contactViewModel.getList()
        setUpRecyclerView()
    }

    private fun updateUI() {
        loanViewModel.totalLoanBalanceAmountLD.observe(viewLifecycleOwner, Observer {totalBalanceAmount ->
            totalBalanceAmount?.let {
                mBinding.totalReceivable.text = VasoolUtil.formatToIndianRupees(it)
            }
        })

        loanViewModel.todayTotalInstallmentsAmount.observe(viewLifecycleOwner, Observer {totalBalanceAmount ->
            totalBalanceAmount?.let {
                mBinding.todayTotalInstallments.text = VasoolUtil.formatToIndianRupees(it)
            }
        })

        lifecycleScope.launch {
            val lineDetails = collectionViewModel.getLineDetails(lineId)
            mBinding.titleText.text = lineDetails.lineName
        }

        lifecycleScope.launch {
            val contactList = contactViewModel.getAllContacts(lineId)
            mBinding.contactStatusTv.text = "Active Contacts(${contactList.size})"
        }
    }

    private fun setupOnClickListeners()
    {
        mBinding.addContactFaf.setOnClickListener {
            val action = CollectionBaseFragmentDirections.actionDailyToAddContact(lineId = lineId)
            findNavController().navigate(action)
        }

        mBinding.searchTv.setOnClickListener {
            mBinding.homeAppBarLayout.setExpanded(false)
            mBinding.topBarCustomMenu.visibility = View.GONE
            mBinding.searchLayout.visibility = View.VISIBLE
            mBinding.searchInputTv.requestFocus()
        }

        mBinding.searchCloseButton.setOnClickListener {
            mBinding.homeAppBarLayout.setExpanded(true)
            mBinding.topBarCustomMenu.visibility = View.VISIBLE
            mBinding.searchLayout.visibility = View.GONE
            mBinding.searchInputTv.setText("")
            contactViewModel.setFilter("")
        }

        mBinding.searchInputTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that the characters within `s` are about to be replaced with new text
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that somewhere within `s`, the text has been changed
            }

            override fun afterTextChanged(s: Editable?) {
                val newText = s.toString()
                searchContact(newText)
            }
        })

        mBinding.todayLoanCard.setOnClickListener {
            getTodayEmiList()
        }
    }

    private fun getTodayEmiList() {

        val calendar = Calendar.getInstance()
        val date = VasoolUtil.getDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(
            Calendar.DAY_OF_MONTH))
        lifecycleScope.launch(Dispatchers.IO) {
            val installmentList = loanViewModel.getTodayPaidInstallmentList(date, lineId)

            withContext(Dispatchers.Main)
            {
                installmentList?.let { showTodayInstallments(it) }
            }
        }
    }

    private fun showTodayInstallments(installmentList: List<Installment>) {
        val bottomSheetDialogFragment = InstallmentListBottomSheet(installmentList)
        bottomSheetDialogFragment.setStyle(DialogFragment.STYLE_NORMAL , R.style.CustomBottomSheetDialog)
        bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)
    }

    private fun initializeVM() {
        contactViewModel = (activity as MainNavigationActivity).getContactViewModel()
        loanViewModel = (activity as MainNavigationActivity).getLoanViewModel()
        collectionViewModel = (activity as MainNavigationActivity).getCollectionViewModel()
    }

    private fun setUpRecyclerView()
    {
        contactListAdapter = ContactListAdapter(){

            openContactDetails(it.contactId)
        }
        mBinding.contactRecyclerView.apply{

            layoutManager = (LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
            addItemDecoration(CustomDividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            setHasFixedSize(true)
            adapter = contactListAdapter
        }

        contactViewModel.mContactListLD.observe(viewLifecycleOwner){ pagingData ->
            contactListAdapter.submitData(lifecycle, pagingData)
        }
    }

    fun searchContact(currentQuery : String)
    {
       contactViewModel.setFilter(currentQuery)
    }



    private fun openContactDetails(contactId:String?)
    {
        contactId?.let {
            val action = CollectionBaseFragmentDirections.actionDailyToContactDetails(contactId)
            findNavController().navigate(action)
        }
    }
}