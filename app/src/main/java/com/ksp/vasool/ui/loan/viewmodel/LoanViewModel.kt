package com.ksp.vasool.ui.loan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.room.Update
import com.ksp.vasool.ui.loan.data.LoanRepository
import com.ksp.vasool.ui.loan.model.Installment
import com.ksp.vasool.ui.loan.model.Loan
import com.ksp.vasool.util.VasoolUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar


/**
 * Created by sathya-6501 on 22/08/23.
 */
class LoanViewModel(private val repository:LoanRepository) : ViewModel() {
    var calendar:Calendar = Calendar.getInstance()

    private val _lineId = MutableLiveData<String>()
    val totalLoanBalanceAmountLD: LiveData<Int> = _lineId.switchMap { lineId ->
        repository.observeTotalBalanceAmount(lineId)
    }

    val todayTotalInstallmentsAmount: LiveData<Int> = _lineId.switchMap { lineId ->
        val todayStr = VasoolUtil.getDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        repository.observeTotalTodayInstallmentsAmount(todayStr, lineId)
    }

    fun setLineId(lineId: String) {
        _lineId.value = lineId
    }

    fun insertLoan(loan:Loan) {
        viewModelScope.launch {
            repository.insertLoan(loan)
        }
    }

    suspend fun hasActiveLoan(contactId:String):Boolean {
        return withContext(Dispatchers.IO) {
            repository.hasActiveLoan(contactId)
        }
    }

    suspend fun getActiveLoanId(contactId:String):String {
        return withContext(Dispatchers.IO) {
            repository.getActiveLoanId(contactId)
        }
    }


    suspend fun getLoanDetails(loanId : String) : Loan
    {
        return withContext(Dispatchers.IO) {
            repository.getLoanDetails(loanId)
        }
    }

    suspend fun getActiveLoanDetails() : Loan?
    {
        return withContext(Dispatchers.IO) {
            repository.getActiveLoanDetails()
        }
    }

    fun getLoanLiveData(loanId: String):LiveData<Loan> {
        return repository.getLoanLiveData(loanId)
    }


    fun insertInstallmentAndUpdateLoan(installment: Installment)
    {
        viewModelScope.launch {
            repository.insertInstallment(installment)

            val loanDetails: Loan? = installment.loanId?.let { repository.getLoanDetails(it) }
            loanDetails?.apply {
                loanRepaidAmount = loanRepaidAmount?.plus(installment.installmentAmount!!)
                loanBalanceAmount = loanBalanceAmount?.minus(installment.installmentAmount!!)
                installmentsCount = installmentsCount?.plus(1)

                if(loanBalanceAmount == 0)
                {
                    val calendar =  Calendar.getInstance()
                    val endDateString = VasoolUtil.getDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

                    isLoanActive = false
                    endDate = endDateString
                }
            }

            loanDetails?.let { repository.updateLoan(it) }
        }
    }

    fun updateInstallment(installment: Installment)
    {
        viewModelScope.launch {
            repository.updateInstallment(installment)
        }
    }

    suspend fun getInstallmentList(loanId: String) : List<Installment> {
        return repository.getInstallmentsList(loanId)
    }

    suspend fun getClosedLoanList(contactId: String) : List<Loan>?
    {
        return repository.getClosedLoanList(contactId)
    }
}