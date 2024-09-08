package com.ksp.vasool.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ksp.vasool.R
import com.ksp.vasool.databinding.ContactListItemBinding
import com.ksp.vasool.ui.contact.model.Contact
import com.ksp.vasool.util.VasoolUtil


/**
 * Created by sathya-6501 on 17/08/23.
 */
class ContactListViewHolder(private val binding : ContactListItemBinding, val context:Context) : RecyclerView.ViewHolder(binding.root)
{
    private val colorArray = intArrayOf(R.color.contact_circle1 ,
        R.color.contact_circle2 ,
        R.color.contact_circle3 ,
        R.color.contact_circle4 ,
        R.color.contact_circle5)

    fun bind(details:Contact , position : Int)
    {
        binding.circularTextView.text = details.contactName?.first()?.toUpperCase().toString()
        binding.contactNameTV.text = details.contactName
        binding.contactPhoneNumberTV.text = if(!TextUtils.isEmpty(details.address)) details.address else "No Address Found"
        binding.totalAmountTV.text = VasoolUtil.formatToIndianRupees(details.balanceAmount)

        (binding.circularTextView.background as GradientDrawable).setColor(ContextCompat.getColor(context ,
            colorArray[position % colorArray.size]))
    }

    companion object
    {
        fun create(parent:ViewGroup): ContactListViewHolder {
            val binding = ContactListItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
            return ContactListViewHolder(binding, parent.context)
        }
    }
}