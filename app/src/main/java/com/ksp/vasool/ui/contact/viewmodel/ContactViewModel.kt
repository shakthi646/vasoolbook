package com.ksp.vasool.ui.contact.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ksp.vasool.ui.collection.model.Line
import com.ksp.vasool.ui.contact.data.ContactRepository
import com.ksp.vasool.ui.contact.model.Contact
import kotlinx.coroutines.launch


/**
 * Created by sathya-6501 on 14/08/23.
 */
class ContactViewModel(private val repository:ContactRepository) : ViewModel() {

    private var lineId : String = ""
    var mContactListLD:LiveData<PagingData<Contact>> = MutableLiveData()
    private val _contactDetailsLD = MutableLiveData<Contact>()
    val contactDetailsLD:LiveData<Contact> get() = _contactDetailsLD


    fun setLineId(lineId : String)
    {
        this.lineId = lineId
    }

    fun insertContact(contact:Contact) {
        viewModelScope.launch {
            repository.insertContact(contact)
        }
    }

    suspend fun getAllContacts(lineId: String):List<Contact> {
        return repository.getAllContacts(lineId)
    }

    fun getList(lineId : String) {
        val config = PagingConfig(pageSize = 30 ,
            initialLoadSize = 30 ,
            enablePlaceholders = false)

        mContactListLD = Pager(config = config ,
            pagingSourceFactory = { repository.getAllContactsPaging(lineId) }).liveData
    }

    fun getContactDetails(mContactId:String) {
        viewModelScope.launch {
            _contactDetailsLD.value = repository.getContactDetails(mContactId)
        }
    }

    fun updateContact(mContactId:String , loanBalanceAmount:Int = 0) {
        viewModelScope.launch {

            val contact = repository.getContactDetails(mContactId)
            contact.balanceAmount = loanBalanceAmount
            repository.upDateContact(contact)
        }
    }
}