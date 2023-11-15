package com.ksp.vasool.common

import androidx.room.TypeConverter
import java.util.Date


/**
 * Created by sathya-6501 on 19/08/23.
 */
object DateConverter
{
    @TypeConverter
    fun toDate(timestamp:Long?):Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimeStamp(date:Date?):Long? {
        return date?.time
    }
}