package com.ksp.vasool.ui.loan.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by sathya-6501 on 19/08/23.
 */
class InstallmentListConverter {
    @TypeConverter
    fun fromList(list: List<Installment>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(json: String): List<Installment> {
        val type = object : TypeToken<List<Installment>>() {}.type
        return Gson().fromJson(json, type)
    }
}