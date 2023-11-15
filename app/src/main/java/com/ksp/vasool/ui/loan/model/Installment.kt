package com.ksp.vasool.ui.loan.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Created by sathya-6501 on 19/08/23.
 */
@Entity(tableName = "Installment", foreignKeys = [ForeignKey(entity = Loan::class,
    parentColumns = ["loan_id"],
    childColumns = ["loan_id"],
    onDelete = ForeignKey.CASCADE)], indices = [Index(value = ["loan_id"])])
class Installment
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "installment_id")
    var installmentId : Int = 0

    @ColumnInfo(name = "loan_id")
    var loanId: String? = null

    @ColumnInfo(name = "line_id")
    var lineId: String? = null

    @ColumnInfo(name = "contact_id")
    var contactId: String? = null

    @ColumnInfo(name = "installment_date")
    var installmentDate: String? = null

    @ColumnInfo(name = "installment_amount")
    var installmentAmount: Int? = null

    @ColumnInfo(name = "loan_paid_amount")
    var loanRepaidAmount : Int? = null

    @ColumnInfo(name = "loan_balance_amount")
    var loanBalanceAmount : Int? = null


}