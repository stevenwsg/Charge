package com.cursor.charge.util

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import kotlin.math.roundToInt

/**
 * 状态栏工具类
 */
object StatusBarUtil {
    
    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        // 尝试获取status_bar_height资源ID
        val resourceId = context.resources.getIdentifier(
            "status_bar_height", "dimen", "android"
        )
        
        return if (resourceId > 0) {
            // 如果找到了资源ID，就使用它
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            // 否则使用一个默认值，通常是24dp
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                24f,
                context.resources.displayMetrics
            ).roundToInt()
        }
    }
    
    /**
     * 应用状态栏高度作为视图的顶部内边距
     */
    fun applyStatusBarPadding(context: Context, topPaddingView: android.view.View) {
        val statusBarHeight = getStatusBarHeight(context)
        topPaddingView.setPadding(
            topPaddingView.paddingLeft,
            statusBarHeight,
            topPaddingView.paddingRight,
            topPaddingView.paddingBottom
        )
    }
} 