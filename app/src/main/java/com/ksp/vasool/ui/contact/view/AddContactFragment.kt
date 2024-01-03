package com.ksp.vasool.ui.contact.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ksp.vasool.base.BaseViewModelFactory
import com.ksp.vasool.constants.StringConstants
import com.ksp.vasool.database.AppDatabase
import com.ksp.vasool.databinding.FragmentAddContactBinding
import com.ksp.vasool.ui.contact.data.ContactRepository
import com.ksp.vasool.ui.contact.model.Contact
import com.ksp.vasool.ui.contact.viewmodel.ContactViewModel

class AddContactFragment : Fragment() {

    private lateinit var mBinding:FragmentAddContactBinding
    private lateinit var contactViewModel :ContactViewModel

    private val args: AddContactFragmentArgs by navArgs()
    private var mLineId : String? = null
    private var mContactId : String? =  null

    private var contactDetails : Contact? = null

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)

        args.lineId.let { mLineId = it }
        args.contactId.let { mContactId = it }
    }

    override fun onCreateView(inflater:LayoutInflater , container:ViewGroup? , savedInstanceState:Bundle?):View? {

        mBinding = FragmentAddContactBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view:View , savedInstanceState:Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        // Enable up navigation
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(mBinding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setHasOptionsMenu(true)

        initializeVM()
        setUpOnClickListeners()

        getInitialData()

    }

    private fun getInitialData() {

        mContactId?.let { contactViewModel.getContactDetails(it) }

        contactViewModel.contactDetailsLD.observe(viewLifecycleOwner, Observer {

            if(it!= null)
            {
                contactDetails = it
                mBinding.etName.setText(it.contactName)
                mBinding.etPhoneNumber.setText(it.phoneNumber)
            }
        })
    }

    private fun setUpOnClickListeners() {
        mBinding.btnCreateContact.setOnClickListener{

            if(validateContact())
            {
                createContact()
                findNavController().navigateUp()
            }
        }
    }

    private fun validateContact():Boolean {

        if(mBinding.etName.text.toString().isEmpty())
        {
            mBinding.etName.error = "Name is required"
            mBinding.etName.requestFocus()
            return false
        }
        else if(mBinding.etPhoneNumber.text.toString().isNotEmpty() && mBinding.etPhoneNumber.text.toString().length!=10)
        {
            mBinding.etPhoneNumber.error = "Kindly enter a valid phone number"
            mBinding.etPhoneNumber.requestFocus()
            return false
        }

        return true
    }

    private fun createContact()
    {
        val name = mBinding.etName.text.toString()
        val phoneNumber = mBinding.etPhoneNumber.text.toString()
        val address = mBinding.etAddress.text.toString()

        val timeInMillis = System.currentTimeMillis()

        var localContact = if(contactDetails!= null)
        {
            contactDetails!!.apply {
                this.contactName = name[0].uppercaseChar() + name.substring(1)
                this.phoneNumber = phoneNumber
                this.address = address
            }
        }
        else
        {
            Contact().apply {
                this.contactName = name[0].uppercaseChar() + name.substring(1)
                this.phoneNumber = phoneNumber
                this.address = address
                this.lineId = mLineId

                if(this.contactId == null) // its for only create case
                {
                    this.createdTime = timeInMillis
                    this.contactId = StringConstants.contactIDPrefix+timeInMillis
                }
            }
        }

        contactViewModel.insertContact(localContact)
    }

    private fun initializeVM() {
        val contactRepository = ContactRepository(AppDatabase.getInstance(requireContext().applicationContext).contactDao())
        val contactViewModelFactory = BaseViewModelFactory(contactRepository)

        contactViewModel = ViewModelProvider(this, contactViewModelFactory)[ContactViewModel::class.java]

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {

        fun newInstance() = AddContactFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}