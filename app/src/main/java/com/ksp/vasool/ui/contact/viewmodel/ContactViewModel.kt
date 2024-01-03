package com.ksp.vasool.ui.contact.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
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

    private val _contactDetailsLD = MutableLiveData<Contact>()
    val contactDetailsLD:LiveData<Contact> get() = _contactDetailsLD


    private val _lineId = MutableLiveData<String>()
    private val _filter = MutableLiveData<String>()

    // Use MediatorLiveData to observe changes in lineId and filter simultaneously
    private val _combinedData = MediatorLiveData<Pair<String, String>>().apply {
        addSource(_lineId) { value = Pair(it, _filter.value.orEmpty()) }
        addSource(_filter) { value = Pair(_lineId.value.orEmpty(), it) }
    }

    // LiveData to observe the combined data and update the contact list
    val mContactListLD: LiveData<PagingData<Contact>> = _combinedData.switchMap { (lineId, filter) ->
        Pager(PagingConfig(pageSize = 200, initialLoadSize = 200, enablePlaceholders = false)) {
            repository.getAllContactsPaging(lineId, filter)
        }.liveData
    }

    fun setLineId(lineId : String)
    {
        _lineId.value = lineId
    }

    fun setFilter(filter: String) {
        _filter.value = filter
    }

    fun insertContact(contact:Contact) {
        viewModelScope.launch {
            repository.insertContact(contact)
        }
    }

    suspend fun getAllContacts(lineId: String):List<Contact> {
        return repository.getAllContacts(lineId)
    }

//    fun getList() {
//        val config = PagingConfig(pageSize = 200 ,
//            initialLoadSize = 200 ,
//            enablePlaceholders = false)
//
//        mContactListLD = Pager(config = config ,
//            pagingSourceFactory = { repository.getAllContactsPaging(lineId, currentFilter) }).liveData
//    }

    fun getContactDetails(mContactId:String) {
        viewModelScope.launch {
            _contactDetailsLD.value = repository.getContactDetails(mContactId)
        }
    }

    suspend fun getContactName(contactId : String) : String{
        return repository.getContactName(contactId)
    }


    fun updateContact(mContactId:String , loanBalanceAmount:Int = 0) {
        viewModelScope.launch {

            val contact = repository.getContactDetails(mContactId)
            contact.balanceAmount = loanBalanceAmount
            repository.upDateContact(contact)
        }
    }

    suspend fun deleteContact(mContactId: String)
    {
        return repository.deleteContact(mContactId)
    }
}