package com.cursor.charge.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private const val TAG = "DateUtils"
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val debugDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
    
    // Format date to display to user
    fun formatDateForDisplay(date: Date): String {
        return displayDateFormat.format(date)
    }
    
    // Format date for internal use
    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }
    
    // Format date with time for debugging
    fun formatDateTimeForDebug(date: Date): String {
        return debugDateFormat.format(date)
    }
    
    // Parse date from string
    fun parseDate(dateString: String): Date? {
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }
    
    // Get the start of day for a date
    fun getStartOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val result = calendar.time
        Log.d(TAG, "getStartOfDay: input=${formatDateTimeForDebug(date)}, output=${formatDateTimeForDebug(result)}")
        return result
    }
    
    // Get the end of day for a date
    fun getEndOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val result = calendar.time
        Log.d(TAG, "getEndOfDay: input=${formatDateTimeForDebug(date)}, output=${formatDateTimeForDebug(result)}")
        return result
    }
    
    // Get the start of week for a date
    fun getStartOfWeek(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val result = calendar.time
        Log.d(TAG, "getStartOfWeek: input=${formatDateTimeForDebug(date)}, output=${formatDateTimeForDebug(result)}")
        return result
    }
    
    // Get the end of week for a date
    fun getEndOfWeek(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = getStartOfWeek(date)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val result = calendar.time
        Log.d(TAG, "getEndOfWeek: input=${formatDateTimeForDebug(date)}, output=${formatDateTimeForDebug(result)}")
        return result
    }
    
    // Get the start of month for a date
    fun getStartOfMonth(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val result = calendar.time
        Log.d(TAG, "getStartOfMonth: input=${formatDateTimeForDebug(date)}, output=${formatDateTimeForDebug(result)}")
        return result
    }
    
    // Get the end of month for a date
    fun getEndOfMonth(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val result = calendar.time
        Log.d(TAG, "getEndOfMonth: input=${formatDateTimeForDebug(date)}, output=${formatDateTimeForDebug(result)}")
        return result
    }
    
    // Get the start of year for a date
    fun getStartOfYear(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_YEAR, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val result = calendar.time
        Log.d(TAG, "getStartOfYear: input=${formatDateTimeForDebug(date)}, output=${formatDateTimeForDebug(result)}")
        return result
    }
    
    // Get the end of year for a date
    fun getEndOfYear(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val result = calendar.time
        Log.d(TAG, "getEndOfYear: input=${formatDateTimeForDebug(date)}, output=${formatDateTimeForDebug(result)}")
        return result
    }
    
    // Add days to a date
    fun addDays(date: Date, days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, days)
        val result = calendar.time
        Log.d(TAG, "addDays: input=${formatDateTimeForDebug(date)}, days=$days, output=${formatDateTimeForDebug(result)}")
        return result
    }
    
    // Check if a date is within a range
    fun isDateInRange(date: Date, startDate: Date, endDate: Date): Boolean {
        val result = !date.before(startDate) && !date.after(endDate)
        Log.d(TAG, "isDateInRange: date=${formatDateTimeForDebug(date)}, start=${formatDateTimeForDebug(startDate)}, end=${formatDateTimeForDebug(endDate)}, result=$result")
        return result
    }
} 