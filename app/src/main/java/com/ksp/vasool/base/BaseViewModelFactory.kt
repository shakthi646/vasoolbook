package com.ksp.vasool.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ksp.vasool.ui.collection.data.CollectionRepository
import com.ksp.vasool.ui.collection.viewmodel.CollectionViewModel
import com.ksp.vasool.ui.contact.data.ContactRepository
import com.ksp.vasool.ui.contact.viewmodel.ContactViewModel
import com.ksp.vasool.ui.loan.data.LoanRepository
import com.ksp.vasool.ui.loan.viewmodel.LoanViewModel


/**
 * Created by sathya-6501 on 14/08/23.
 */


class BaseViewModelFactory<T>(private val repository: T) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass:Class<T>):T {

        if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
            return ContactViewModel(repository as ContactRepository) as T
        }
        else if (modelClass.isAssignableFrom(LoanViewModel::class.java)) {
            return LoanViewModel(repository as LoanRepository) as T
        }
        else if (modelClass.isAssignableFrom(CollectionViewModel::class.java)) {
            return CollectionViewModel(repository as CollectionRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


