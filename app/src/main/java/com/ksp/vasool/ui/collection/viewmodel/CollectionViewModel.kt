package com.ksp.vasool.ui.collection.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksp.vasool.ui.collection.data.CollectionRepository
import com.ksp.vasool.ui.collection.model.Line
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Created by sathya-6501 on 31/08/23.
 */

class CollectionViewModel(private val collectionRepository:CollectionRepository) : ViewModel()
{

    val allLines: LiveData<List<Line>> = collectionRepository.allLines

    suspend fun getAllLines() : List<Line>
    {
        return withContext(Dispatchers.IO) {
            collectionRepository.getAllLines()
        }
    }

    fun insertNewLine(line: Line)
    {
        viewModelScope.launch {
            collectionRepository.insertLine(line)
        }
    }

    suspend fun getLineDetails(lineId : String) : Line
    {
        return withContext(Dispatchers.IO) {
            collectionRepository.getLineDetails(lineId)
        }
    }

    suspend fun deleteLine(lineId : String)
    {
        return withContext(Dispatchers.IO) {
            collectionRepository.deleteLine(lineId)
        }
    }
}