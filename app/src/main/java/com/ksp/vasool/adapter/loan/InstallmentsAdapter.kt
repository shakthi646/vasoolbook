package com.ksp.vasool.adapter.loan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ksp.vasool.R
import com.ksp.vasool.databinding.ItemInstallmentBinding
import com.ksp.vasool.ui.loan.model.Installment
import com.ksp.vasool.util.VasoolUtil


/**
 * Created by sathya-6501 on 23/08/23.
 */
class InstallmentsAdapter(private val installments: List<Installment>,
                          private val onItemClick: (Installment) -> Unit) : RecyclerView.Adapter<InstallmentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent:ViewGroup , viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemInstallmentBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val installment = installments[position]
        holder.itemView.setOnClickListener {
            onItemClick(installment)
        }

        holder.bind(installment, position)
    }

    override fun getItemCount(): Int {
        return installments.size
    }

    inner class ViewHolder(private val binding: ItemInstallmentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(installment: Installment, position: Int) {

            binding.sNo.text = (position.plus(1)).toString()
            binding.emiDate.text = VasoolUtil.convertDateFormat(installment.installmentDate!!)
            binding.emiAmount.text = VasoolUtil.formatToIndianRupees(installment.installmentAmount)
            binding.repaidAmount.text = VasoolUtil.formatToIndianRupees(installment.loanRepaidAmount)
//             Set background color based on position
            if (position % 2 == 0) {
//                binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.even_row_color))
            } else {
                binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.surfaceDark))
            }
        }
    }
}
