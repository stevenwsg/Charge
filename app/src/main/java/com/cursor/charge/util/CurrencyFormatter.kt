package com.cursor.charge.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    
    fun format(amount: Double): String {
        return currencyFormat.format(amount)
    }
    
    fun formatWithoutSymbol(amount: Double): String {
        return format(amount).replace(currencyFormat.currency?.symbol ?: "$", "").trim()
    }
} 