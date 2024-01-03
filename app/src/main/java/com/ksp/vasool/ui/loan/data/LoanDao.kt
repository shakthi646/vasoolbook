package com.ksp.vasool.ui.loan.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ksp.vasool.ui.loan.model.Installment
import com.ksp.vasool.ui.loan.model.Loan


/**
 * Created by sathya-6501 on 22/08/23.
 */
@Dao
interface LoanDao
{
    /**Loan Table Query*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoan(loan:Loan)

    @Update
    suspend fun updateLoan(loan: Loan)

    @Query("SELECT COUNT(*) FROM loans WHERE contact_id = :contactId AND is_loan_active = 1")
    suspend fun hasActiveLoan(contactId : String) : Int

    @Query("SELECT * FROM loans where loan_id=:loanId")
    suspend fun getLoanDetails(loanId: String) : Loan

    @Query("SELECT * FROM loans where is_loan_active = 1")
    suspend fun getActiveLoanDetails() : Loan?

    @Query("SELECT loan_id FROM loans WHERE contact_id = :contactId AND is_loan_active = 1")
    suspend fun getActiveLoanId(contactId : String) : String

    @Query("SELECT * FROM loans WHERE loan_id = :loanId")
    fun getLoanLiveData(loanId: String):LiveData<Loan>

    @Query("Delete FROM loans WHERE loan_id = :loanId")
    fun deleteLoan(loanId: String): Int

    @Query("SELECT SUM(loan_balance_amount) FROM loans where line_id=:lineId AND is_loan_active = 1")
    fun observeTotalBalanceAmount(lineId : String):LiveData<Int>

    @Query("SELECT * FROM loans WHERE contact_id = :contactId AND is_loan_active = 0")
    suspend fun getClosedLoanList(contactId: String) : List<Loan>?

    /**Installments Table Query*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstallment(installment: Installment)

    @Update
    suspend fun updateInstallment(installment: Installment)

    @Delete
    suspend fun deleteInstallment(installment: Installment)

    @Query("SELECT * FROM Installment WHERE loan_id = :loanId")
    suspend fun getInstallmentsList(loanId : String) : List<Installment>

    @Query("SELECT SUM(installment_amount) FROM Installment where line_id=:lineId AND installment_date=:date")
    fun observeTotalTodayInstallmentsAmount(date : String, lineId : String):LiveData<Int>
    @Query("SELECT * FROM Installment WHERE line_id = :lineId AND installment_date=:date")
    fun getTodayPaidInstallmentsList(date: String, lineId: String): List<Installment>

}