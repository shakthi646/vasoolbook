package com.ksp.vasool.ui.collection.data

import androidx.lifecycle.LiveData
import com.ksp.vasool.ui.collection.model.Line


/**
 * Created by sathya-6501 on 14/08/23.
 */
class CollectionRepository(private val lineDao: LineDao) {

    val allLines: LiveData<List<Line>> = lineDao.getAllLinesLD()

    suspend fun insertLine(collectionTable:Line) {
        lineDao.insert(collectionTable)
    }

    suspend fun upDateLine(collectionTable:Line) {
        lineDao.update(collectionTable)
    }

    suspend fun getAllLines(): List<Line> {
        return lineDao.getAllLine()
    }

    suspend fun getLineDetails(lineId : String): Line {
        return lineDao.getLineDetails(lineId)
    }

    suspend fun deleteLine(lineId: String)
    {
        return lineDao.deleteLine(lineId)
    }
}