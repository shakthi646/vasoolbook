package com.ksp.vasool.ui.contact.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by sathya-6501 on 14/08/23.
 */
@Entity(tableName = "contacts", indices = [Index(value = ["contact_id"], unique = true)])
class Contact : Serializable
{
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0

    @ColumnInfo(name = "contact_name")
    var contactName: String? = null

    @ColumnInfo(name = "contact_id")
    var contactId: String? = null

    @ColumnInfo(name = "phone_number")
    var phoneNumber: String? = null

    @ColumnInfo(name = "address")
    var address: String? = null

    @ColumnInfo(name = "created_time")
    var createdTime : Long? = null

    @ColumnInfo(name = "balance_amount")
    var balanceAmount : Int = 0

    @ColumnInfo(name = "line_id")
    var lineId: String? = null
}