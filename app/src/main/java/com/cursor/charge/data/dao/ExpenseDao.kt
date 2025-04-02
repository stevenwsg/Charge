package com.cursor.charge.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cursor.charge.data.entity.ExpenseEntity
import com.cursor.charge.data.entity.CategorySum
import java.util.Date

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity): Long

    @Update
    suspend fun update(expense: ExpenseEntity)

    @Delete
    suspend fun delete(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): LiveData<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpenseById(id: Long): LiveData<ExpenseEntity>

    // Get expenses for a specific day
    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesByDateRange(startDate: Date, endDate: Date): LiveData<List<ExpenseEntity>>

    // Sum expenses by category for a date range
    @Query("SELECT category, SUM(amount) as total FROM expenses WHERE date BETWEEN :startDate AND :endDate GROUP BY category ORDER BY total DESC")
    fun getExpenseSummaryByCategory(startDate: Date, endDate: Date): LiveData<List<CategorySum>>

    // Sum all expenses for a date range
    @Query("SELECT SUM(amount) FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    fun getTotalExpensesForRange(startDate: Date, endDate: Date): LiveData<Double>
    
    // Debug queries
    @Query("SELECT COUNT(*) FROM expenses")
    suspend fun getExpenseCount(): Int
    
    @Query("SELECT MIN(date) FROM expenses")
    suspend fun getEarliestExpenseDate(): Date?
    
    @Query("SELECT MAX(date) FROM expenses")
    suspend fun getLatestExpenseDate(): Date?
    
    @Query("SELECT * FROM expenses WHERE date >= :date ORDER BY date DESC LIMIT 10")
    suspend fun getRecentExpenses(date: Date): List<ExpenseEntity>
    
    @Query("SELECT COUNT(*) FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getExpenseCountInRange(startDate: Date, endDate: Date): Int
} 