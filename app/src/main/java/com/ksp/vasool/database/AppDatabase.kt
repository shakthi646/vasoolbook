package com.ksp.vasool.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ksp.vasool.common.DateConverter
import com.ksp.vasool.constants.StringConstants
import com.ksp.vasool.ui.collection.data.LineDao
import com.ksp.vasool.ui.collection.model.Line
import com.ksp.vasool.ui.contact.data.ContactDao
import com.ksp.vasool.ui.contact.model.Contact
import com.ksp.vasool.ui.loan.data.LoanDao
import com.ksp.vasool.ui.loan.model.Installment
import com.ksp.vasool.ui.loan.model.InstallmentListConverter
import com.ksp.vasool.ui.loan.model.Loan


/**
 * Created by sathya-6501 on 16/08/23.
 */
@Database(entities = [
    Contact::class,
    Loan::class,
    Installment::class,
    Line::class] , version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, InstallmentListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao():ContactDao
    abstract fun loanDao():LoanDao
    abstract fun collectionDao() : LineDao

    companion object
    {
        private var instance: AppDatabase? = null

        fun getInstance(context:Context): AppDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, StringConstants.DB_NAME
                ).fallbackToDestructiveMigration().build()
                instance = newInstance
                newInstance
            }
        }
    }
}


