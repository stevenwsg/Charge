package com.cursor.charge.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.cursor.charge.data.dao.ExpenseDao
import com.cursor.charge.data.entity.CategorySum
import com.cursor.charge.data.entity.ExpenseEntity
import java.util.Date

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    
    val allExpenses: LiveData<List<ExpenseEntity>> = expenseDao.getAllExpenses()
    
    suspend fun insert(expense: ExpenseEntity): Long {
        return expenseDao.insert(expense)
    }
    
    suspend fun update(expense: ExpenseEntity) {
        expenseDao.update(expense)
    }
    
    suspend fun delete(expense: ExpenseEntity) {
        expenseDao.delete(expense)
    }
    
    fun getExpenseById(id: Long): LiveData<ExpenseEntity> {
        return expenseDao.getExpenseById(id)
    }
    
    fun getExpensesByDateRange(startDate: Date, endDate: Date): LiveData<List<ExpenseEntity>> {
        return expenseDao.getExpensesByDateRange(startDate, endDate)
    }
    
    fun getExpenseSummaryByCategory(startDate: Date, endDate: Date): LiveData<Map<String, Double>> {
        return expenseDao.getExpenseSummaryByCategory(startDate, endDate).map { categorySums ->
            categorySums.associate { it.category to it.total }
        }
    }
    
    fun getTotalExpensesForRange(startDate: Date, endDate: Date): LiveData<Double> {
        return expenseDao.getTotalExpensesForRange(startDate, endDate)
    }
    
    // Debug methods
    suspend fun getExpenseCount(): Int {
        return expenseDao.getExpenseCount()
    }
    
    suspend fun getEarliestExpenseDate(): Date? {
        return expenseDao.getEarliestExpenseDate()
    }
    
    suspend fun getLatestExpenseDate(): Date? {
        return expenseDao.getLatestExpenseDate()
    }
    
    suspend fun getRecentExpenses(date: Date): List<ExpenseEntity> {
        return expenseDao.getRecentExpenses(date)
    }
    
    suspend fun getExpenseCountInRange(startDate: Date, endDate: Date): Int {
        return expenseDao.getExpenseCountInRange(startDate, endDate)
    }
} 