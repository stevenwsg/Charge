package com.cursor.charge.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.cursor.charge.data.entity.ExpenseEntity
import com.cursor.charge.data.repository.ExpenseRepository
import com.cursor.charge.util.DateUtils
import kotlinx.coroutines.launch
import java.util.Date

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {
    
    companion object {
        private const val TAG = "ExpenseViewModel"
    }
    
    // All expenses
    val allExpenses: LiveData<List<ExpenseEntity>> = repository.allExpenses.also {
        Log.d(TAG, "初始化 allExpenses LiveData")
    }
    
    // Selected date range
    private val _startDate = MutableLiveData<Date>()
    val startDate: LiveData<Date> = _startDate
    
    private val _endDate = MutableLiveData<Date>()
    val endDate: LiveData<Date> = _endDate
    
    // 使用switchMap来转换日期范围
    private val dateRangeTuple = MutableLiveData<Pair<Date, Date>>()
    
    // Expenses for the selected date range
    val expensesForRange: LiveData<List<ExpenseEntity>> = dateRangeTuple.switchMap { (start, end) ->
        Log.d(TAG, "switchMap: 获取日期范围的费用: ${DateUtils.formatDateForDisplay(start)} - ${DateUtils.formatDateForDisplay(end)}")
        repository.getExpensesByDateRange(start, end).also { liveData ->
            liveData.observeForever { expenses ->
                Log.d(TAG, "数据库返回费用数量: ${expenses.size}")
                expenses.forEach { expense ->
                    Log.d(TAG, "费用: ID=${expense.id}, 金额=${expense.amount}, 日期=${DateUtils.formatDateForDisplay(expense.date)}")
                }
            }
        }
    }
    
    // Category summary
    val categorySummary: LiveData<Map<String, Double>> = dateRangeTuple.switchMap { (start, end) ->
        repository.getExpenseSummaryByCategory(start, end)
    }
    
    // Total expenses for the range
    val totalExpensesForRange: LiveData<Double> = dateRangeTuple.switchMap { (start, end) ->
        repository.getTotalExpensesForRange(start, end)
    }
    
    // Get expense by ID
    fun getExpenseById(id: Long): LiveData<ExpenseEntity> {
        return repository.getExpenseById(id)
    }
    
    // Insert a new expense
    fun insert(expense: ExpenseEntity) = viewModelScope.launch {
        Log.d(TAG, "开始插入新费用: 金额=${expense.amount}, 日期=${DateUtils.formatDateForDisplay(expense.date)}")
        val id = repository.insert(expense)
        Log.d(TAG, "费用插入成功，ID=$id")
        
        // 刷新日期范围数据
        refreshDateRange()
    }
    
    // Update an existing expense
    fun update(expense: ExpenseEntity) = viewModelScope.launch {
        Log.d(TAG, "开始更新费用: ID=${expense.id}")
        repository.update(expense)
        Log.d(TAG, "费用更新成功")
        
        // 刷新日期范围数据
        refreshDateRange()
    }
    
    // Delete an expense
    fun delete(expense: ExpenseEntity) = viewModelScope.launch {
        Log.d(TAG, "开始删除费用: ID=${expense.id}")
        repository.delete(expense)
        Log.d(TAG, "费用删除成功")
        
        // 刷新日期范围数据
        refreshDateRange()
    }
    
    private fun refreshDateRange() {
        val start = _startDate.value ?: DateUtils.getStartOfMonth(Date())
        val end = _endDate.value ?: DateUtils.getEndOfMonth(Date())
        Log.d(TAG, "刷新日期范围数据: ${DateUtils.formatDateForDisplay(start)} - ${DateUtils.formatDateForDisplay(end)}")
        dateRangeTuple.value = Pair(start, end)
    }
    
    // Setters for date range
    fun setDateRange(start: Date, end: Date) {
        Log.d(TAG, "设置日期范围: ${DateUtils.formatDateForDisplay(start)} - ${DateUtils.formatDateForDisplay(end)}")
        _startDate.value = start
        _endDate.value = end
        dateRangeTuple.value = Pair(start, end)
    }
    
    // Set to show data for today
    fun setToday() {
        val today = Date()
        val start = DateUtils.getStartOfDay(today)
        val end = DateUtils.getEndOfDay(today)
        
        Log.d(TAG, "设置为今日: ${DateUtils.formatDateForDisplay(start)} - ${DateUtils.formatDateForDisplay(end)}")
        _startDate.value = start
        _endDate.value = end
        dateRangeTuple.value = Pair(start, end)
    }
    
    // Set to show data for current week
    fun setCurrentWeek() {
        val today = Date()
        val start = DateUtils.getStartOfWeek(today)
        val end = DateUtils.getEndOfWeek(today)
        
        Log.d(TAG, "设置为本周: ${DateUtils.formatDateForDisplay(start)} - ${DateUtils.formatDateForDisplay(end)}")
        _startDate.value = start
        _endDate.value = end
        dateRangeTuple.value = Pair(start, end)
    }
    
    // Set to show data for current month
    fun setCurrentMonth() {
        val today = Date()
        val start = DateUtils.getStartOfMonth(today)
        val end = DateUtils.getEndOfMonth(today)
        
        Log.d(TAG, "设置为本月: ${DateUtils.formatDateForDisplay(start)} - ${DateUtils.formatDateForDisplay(end)}")
        _startDate.value = start
        _endDate.value = end
        dateRangeTuple.value = Pair(start, end)
    }
    
    // Set to show data for current year
    fun setCurrentYear() {
        val today = Date()
        val start = DateUtils.getStartOfYear(today)
        val end = DateUtils.getEndOfYear(today)
        
        Log.d(TAG, "设置为本年: ${DateUtils.formatDateForDisplay(start)} - ${DateUtils.formatDateForDisplay(end)}")
        _startDate.value = start
        _endDate.value = end
        dateRangeTuple.value = Pair(start, end)
    }
    
    // Initialize the view model with default dates
    init {
        Log.d(TAG, "初始化ViewModel")
        // 初始化为当月
        setCurrentMonth()
    }
    
    // Factory to create the view model with the repository
    class ExpenseViewModelFactory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ExpenseViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 