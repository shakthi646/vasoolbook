package com.ksp.vasool.ui.loan.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ksp.vasool.ui.loan.model.Installment
import com.ksp.vasool.ui.loan.model.Loan
import kotlinx.coroutines.launch


/**
 * Created by sathya-6501 on 22/08/23.
 */
class LoanRepository(private val loanDao:LoanDao)
{
    suspend fun insertLoan(loan:Loan) {
        loanDao.insertLoan(loan)
    }

    suspend fun updateLoan(loan: Loan) {
        loanDao.updateLoan(loan)
    }

     suspend fun hasActiveLoan(contactId:String) : Boolean{
        return loanDao.hasActiveLoan(contactId) > 0
    }

    suspend fun getLoanDetails(loanId: String) : Loan
    {
        return loanDao.getLoanDetails(loanId)
    }

    suspend fun getActiveLoanDetails() : Loan?
    {
        return loanDao.getActiveLoanDetails()
    }

    suspend fun getActiveLoanId(contactId: String) : String
    {
        return loanDao.getActiveLoanId(contactId)
    }

    fun getLoanLiveData(loanId: String):LiveData<Loan> {
        return loanDao.getLoanLiveData(loanId)
    }

    suspend fun getClosedLoanList(contactId: String) : List<Loan>?
    {
        return loanDao.getClosedLoanList(contactId)
    }


    suspend fun insertInstallment(installment: Installment)
    {
        return loanDao.insertInstallment(installment)
    }

    suspend fun updateInstallment(installment: Installment)
    {
        return loanDao.updateInstallment(installment)
    }

    suspend fun getInstallmentsList(loanId: String) : List<Installment>
    {
        return loanDao.getInstallmentsList(loanId)
    }

    fun observeTotalBalanceAmount(lineId : String) : LiveData<Int>
    {
        return loanDao.observeTotalBalanceAmount(lineId)
    }

    fun observeTotalTodayInstallmentsAmount(date : String, lineId : String) : LiveData<Int>
    {
        return loanDao.observeTotalTodayInstallmentsAmount(date, lineId)
    }
}