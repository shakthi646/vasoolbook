package com.ksp.vasool.ui.collection.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ksp.vasool.ui.collection.model.Line


/**
 * Created by sathya-6501 on 31/08/23.
 */

@Dao
interface LineDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(line :Line)

    @Update
    suspend fun update(line :Line)

    @Delete
    suspend fun delete(line :Line)

    @Query("SELECT * FROM line")
    suspend fun getAllLine(): List<Line>

    @Query("SELECT * FROM line")
    fun getAllLinesLD(): LiveData<List<Line>>

    @Query("SELECT * FROM line where line_id=:lineId")
    suspend fun getLineDetails(lineId : String): Line
}