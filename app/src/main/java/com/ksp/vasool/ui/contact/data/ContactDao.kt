package com.ksp.vasool.ui.contact.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ksp.vasool.ui.contact.model.Contact


/**
 * Created by sathya-6501 on 14/08/23.
 */
@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact:Contact)

    @Update
    suspend fun update(contact:Contact)

    @Query("SELECT * FROM contacts where line_id=:lineId")
    suspend fun getAllContacts(lineId: String): List<Contact>

    @Query("DELETE from contacts where contact_id=:contactId")
    suspend fun deleteContact(contactId : String)

    @Query("SELECT * FROM contacts where contact_id=:contactId")
    suspend fun getContactDetails(contactId : String) : Contact

    @Query("SELECT contact_name FROM contacts where contact_id=:contactId")
    suspend fun getContactName(contactId : String) : String
    @Query("SELECT * FROM contacts")
    fun getAllContactsPaging():PagingSource<Int, Contact>

    @Query("SELECT * FROM contacts where line_id=:lineId")
    fun getAllContactsPaging(lineId : String):PagingSource<Int, Contact>

    @Query("SELECT * FROM contacts WHERE line_id = :lineId AND contact_name LIKE '%' || :filterString || '%'")
    fun getAllContactsPaging(lineId: String, filterString: String): PagingSource<Int, Contact>

}