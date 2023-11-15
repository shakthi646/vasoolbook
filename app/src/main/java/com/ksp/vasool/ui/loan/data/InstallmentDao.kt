package com.ksp.vasool.ui.loan.data

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.ksp.vasool.ui.loan.model.Installment

/**
 * Created by sathya-6501 on 09/11/23.
 */
interface InstallmentDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstallment(installment: Installment)

    @Update
    suspend fun updateInstallment(installment: Installment)
}