package com.ksp.vasool.ui.loan.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ksp.vasool.ui.contact.model.Contact
import java.util.Date


/**
 * Created by sathya-6501 on 19/08/23.
 */

@Entity(
    tableName = "loans",
    foreignKeys = [ForeignKey(entity = Contact::class,
        parentColumns = ["contact_id"],
        childColumns = ["contact_id"],
        onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["loan_id"], unique = true)])
class Loan
{
    @PrimaryKey(autoGenerate = true)
    var _id : Int = 0

    @ColumnInfo(name = "contact_id")
    var contactId: String? = null

    @ColumnInfo(name = "line_id")
    var lineId: String? = null

    @ColumnInfo(name = "loan_name")
    var loanName: String? = null

    @ColumnInfo(name = "loan_id")
    var loanId: String? = null

    @ColumnInfo(name = "loan_principal_amount") // what i given to the contact
    var loanPrincipalAmount : Int? = null

    @ColumnInfo(name = "total_loan_amount")
    var totalLoanAmount : Int? = null

    @ColumnInfo(name = "loan_interest_amount") // (loan interest amount for Rs. 100)
    var loanInterestRate : Int? = null

    @ColumnInfo(name = "loan_paid_amount")
    var loanRepaidAmount : Int? = null

    @ColumnInfo(name = "loan_balance_amount")
    var loanBalanceAmount : Int? = null

    @ColumnInfo(name = "loan_emi_amount")
    var loanEmiAmount : Int? = null

    @ColumnInfo(name = "start_date_long")
    var startDate : String? = null

    @ColumnInfo(name = "end_date_long")
    var endDate : String? = null

    @ColumnInfo(name = "installments")
    var installments: List<Installment> = emptyList()

    @ColumnInfo(name = "installments_count")
    var installmentsCount: Int? = 0

    @ColumnInfo(name = "is_loan_active")
    var isLoanActive : Boolean = true

    @ColumnInfo(name = "is_interest_debited")
    var isInterestDebited : Boolean = false

}