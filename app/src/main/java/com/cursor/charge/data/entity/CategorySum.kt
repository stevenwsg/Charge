package com.cursor.charge.data.entity

import androidx.room.ColumnInfo

data class CategorySum(
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "total") val total: Double
) 