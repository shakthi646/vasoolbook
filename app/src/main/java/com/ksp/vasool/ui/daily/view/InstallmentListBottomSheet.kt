package com.ksp.vasool.ui.daily.view

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ksp.vasool.databinding.InstallmentListBottomsheetBinding
import com.ksp.vasool.databinding.ItemInstallmentWithNameBinding
import com.ksp.vasool.ui.contact.viewmodel.ContactViewModel
import com.ksp.vasool.ui.loan.model.Installment
import com.ksp.vasool.util.VasoolUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InstallmentListBottomSheet(val installmentList: List<Installment>) : BottomSheetDialogFragment()
{
    private lateinit var contactViewModel : ContactViewModel
    lateinit var mBinding: InstallmentListBottomsheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contactViewModel = (parentFragment as CollectionBaseFragment).contactViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding =  InstallmentListBottomsheetBinding.inflate(inflater , container , false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadList(installmentList)
    }

    private fun loadList(installmentList: List<Installment>) {
        mBinding.leaveListContainer.removeAllViews()

        installmentList.forEach{
            mBinding.leaveListContainer.addView(inflateInstallmentItemView(it))
        }
    }

    private fun inflateInstallmentItemView(it: Installment): View {

        val binding = ItemInstallmentWithNameBinding.inflate(layoutInflater)
        binding.amount.text = VasoolUtil.formatToIndianRupees(it.installmentAmount)

        lifecycleScope.launch(Dispatchers.IO) {
            val cName = contactViewModel.getContactName(it.contactId!!)

            withContext(Dispatchers.Main)
            {
                binding.name.text = cName
            }
        }



        return binding.root
    }
}
