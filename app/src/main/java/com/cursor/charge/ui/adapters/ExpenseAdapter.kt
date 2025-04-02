package com.cursor.charge.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cursor.charge.R
import com.cursor.charge.data.entity.ExpenseEntity
import com.cursor.charge.databinding.ItemExpenseBinding
import com.cursor.charge.util.CurrencyFormatter
import com.cursor.charge.util.DateUtils
import com.cursor.charge.util.ExpenseCategories

class ExpenseAdapter(private val onItemClicked: (ExpenseEntity) -> Unit) :
    ListAdapter<ExpenseEntity, ExpenseAdapter.ExpenseViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
    }

    class ExpenseViewHolder(
        private val binding: ItemExpenseBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: ExpenseEntity) {
            binding.apply {
                categoryTextView.text = expense.category
                descriptionTextView.text = expense.description.ifEmpty { expense.category }
                amountTextView.text = CurrencyFormatter.format(expense.amount)
                dateTextView.text = DateUtils.formatDateForDisplay(expense.date)
                
                // Set category icon and background color
                val iconResId = getCategoryIconResId(expense.category)
                categoryIcon.setImageResource(iconResId)
                
                val backgroundColorResId = getCategoryColorResId(expense.category)
                val backgroundDrawable = categoryIcon.background.mutate()
                backgroundDrawable.setTint(ContextCompat.getColor(context, backgroundColorResId))
                categoryIcon.background = backgroundDrawable
            }
        }
        
        private fun getCategoryIconResId(category: String): Int {
            return when (category) {
                ExpenseCategories.HOUSING -> R.drawable.ic_home
                ExpenseCategories.FOOD -> R.drawable.ic_food
                ExpenseCategories.TRANSPORTATION -> R.drawable.ic_transportation
                ExpenseCategories.ENTERTAINMENT -> R.drawable.ic_entertainment
                ExpenseCategories.SHOPPING -> R.drawable.ic_shopping
                ExpenseCategories.UTILITIES -> R.drawable.ic_utilities
                ExpenseCategories.HEALTH -> R.drawable.ic_health
                ExpenseCategories.EDUCATION -> R.drawable.ic_education
                ExpenseCategories.TRAVEL -> R.drawable.ic_travel
                else -> R.drawable.ic_others
            }
        }
        
        private fun getCategoryColorResId(category: String): Int {
            return when (category) {
                ExpenseCategories.HOUSING -> R.color.category_housing
                ExpenseCategories.FOOD -> R.color.category_food
                ExpenseCategories.TRANSPORTATION -> R.color.category_transportation
                ExpenseCategories.ENTERTAINMENT -> R.color.category_entertainment
                ExpenseCategories.SHOPPING -> R.color.category_shopping
                ExpenseCategories.UTILITIES -> R.color.category_utilities
                ExpenseCategories.HEALTH -> R.color.category_health
                ExpenseCategories.EDUCATION -> R.color.category_education
                ExpenseCategories.TRAVEL -> R.color.category_travel
                else -> R.color.category_others
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ExpenseEntity>() {
            override fun areItemsTheSame(oldItem: ExpenseEntity, newItem: ExpenseEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ExpenseEntity, newItem: ExpenseEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
} 