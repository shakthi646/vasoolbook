package com.ksp.vasool.ui.loan.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ksp.vasool.databinding.AddInstallmentBinding
import com.ksp.vasool.ui.contact.view.ContactDetailsFragment
import com.ksp.vasool.util.VasoolUtil
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


/**
 * Created by sathya-6501 on 23/08/23.
 */
class InstallmentBottomSheetFragment(private val loanBalanceAmount : Int) : BottomSheetDialogFragment() {

    val calendar = Calendar.getInstance()
    private lateinit var mBinding:AddInstallmentBinding
    override fun onCreateView(inflater:LayoutInflater , container: ViewGroup? , savedInstanceState: Bundle?): View? {
        mBinding = AddInstallmentBinding.inflate(inflater, container, false)

        val startDatePicker = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->

                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateEmiDateLabel()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        updateEmiDateLabel()
        mBinding.editTextInstallmentAmount.requestFocus()

        mBinding.tvEmiDate.setOnClickListener {
            startDatePicker.show()
        }

        mBinding.buttonAddEmi.setOnClickListener{
            val installmentAmount = mBinding.editTextInstallmentAmount.text.toString().toInt()
            val installmentDate = VasoolUtil.getDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            if(installmentAmount <= loanBalanceAmount)
            {
                val parentFragment = parentFragment as? ContactDetailsFragment
                parentFragment?.onInstallmentAdded(installmentAmount, installmentDate)

                dismiss()
            }
            else
            {
                mBinding.editTextInstallmentAmount.error = "EMI amount should be less then balance amount ($loanBalanceAmount)"
            }
        }

        return mBinding.root
    }

    private fun updateEmiDateLabel()
    {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        mBinding.tvEmiDate.text = dateFormat.format(calendar.time)
    }
}
