package com.ksp.vasool.ui.contact.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import com.ksp.vasool.ui.contact.model.Contact


/**
 * Created by sathya-6501 on 14/08/23.
 */
class ContactRepository(private val contactDao: ContactDao) {

    suspend fun insertContact(contact:Contact) {
        contactDao.insert(contact)
    }

    suspend fun upDateContact(contact:Contact) {
        contactDao.update(contact)
    }

    suspend fun getAllContacts(lineId: String): List<Contact> {
        return contactDao.getAllContacts(lineId)
    }

    suspend fun getContactDetails(contactId : String) : Contact
    {
        return contactDao.getContactDetails(contactId)
    }

    suspend fun getContactName(contactId: String) :String
    {
        return contactDao.getContactName(contactId)
    }

    suspend fun deleteContact(contactId: String) {
        return contactDao.deleteContact(contactId)
    }

    fun getAllContactsPaging(): PagingSource<Int, Contact> {
        return contactDao.getAllContactsPaging()
    }

    fun getAllContactsPaging(lineId : String, currentFilter : String): PagingSource<Int, Contact> {
        return contactDao.getAllContactsPaging(lineId, currentFilter)
    }
}