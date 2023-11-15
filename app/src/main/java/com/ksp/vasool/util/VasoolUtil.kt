package com.ksp.vasool.util

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


/**
 * Created by sathya-6501 on 23/08/23.
 */
object VasoolUtil
{
    fun formatToIndianRupees(amount: Int?): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        return numberFormat.format(amount)
    }

    fun formatDateFromMillis(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }

    fun getDaysDifference(startTimeMillis: Long, endTimeMillis: Long): Long {
        val differenceInMillis = getDaysDifferenceInMillis(startTimeMillis, endTimeMillis)
        return TimeUnit.MILLISECONDS.toDays(differenceInMillis)
    }

    private fun getDaysDifferenceInMillis(startTimeMillis: Long, endTimeMillis: Long): Long {
        return endTimeMillis - startTimeMillis
    }

    fun getDateString(year: Int, month: Int, day: Int): String
    {
        val nft = DecimalFormat("#00.###")
        return year.toString() + "-" + nft.format((month + 1).toLong()) + "-" + nft.format(day.toLong()) // "yyyy-MM-dd"
    }

    fun addDaysToDate(originalDate: String, daysToAdd: Int): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(originalDate)

        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd)

        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return outputFormat.format(calendar.time)
    }

    fun convertDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        try {
            val date = inputFormat.parse(inputDate)
            if (date != null) {
                return outputFormat.format(date)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        // Return the original string if parsing fails
        return inputDate
    }
}