package com.ksp.vasool.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ksp.vasool.ui.contact.model.Contact


/**
 * Created by sathya-6501 on 17/08/23.
 */
class ContactListAdapter(private val onItemClick: (Contact) -> Unit) : PagingDataAdapter<Contact, ContactListViewHolder>(POST_COMPARATOR)
{
    override fun onBindViewHolder(holder:ContactListViewHolder , position:Int) {

        val contact = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClick(contact!!)
        }

        return holder.bind(getItem(position)!!, position)
    }

    override fun onCreateViewHolder(parent:ViewGroup , viewType:Int):ContactListViewHolder {
        return ContactListViewHolder.create(parent)
    }

    companion object
    {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Contact>()
        {
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Contact , newItem: Contact): Boolean =
                    oldItem == newItem

            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
                    oldItem.contactId == newItem.contactId

            override fun getChangePayload(oldItem: Contact, newItem: Contact): Any?
            {
                return null
            }
        }
    }
}