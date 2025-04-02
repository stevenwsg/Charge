package com.cursor.charge

import android.app.Application
import com.cursor.charge.data.ExpenseDatabase
import com.cursor.charge.data.repository.ExpenseRepository

class ChargeApplication : Application() {
    // Lazy initialization of the database
    private val database by lazy { ExpenseDatabase.getDatabase(this) }
    
    // Repository is exposed to rest of the app
    val repository by lazy { ExpenseRepository(database.expenseDao()) }
} 