package com.cursor.charge.ui.detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cursor.charge.ChargeApplication
import com.cursor.charge.R
import com.cursor.charge.databinding.FragmentExpenseDetailBinding
import com.cursor.charge.util.CurrencyFormatter
import com.cursor.charge.util.DateUtils
import com.cursor.charge.util.ExpenseCategories
import com.cursor.charge.util.StatusBarUtil
import com.cursor.charge.viewmodel.ExpenseViewModel

class ExpenseDetailFragment : Fragment() {
    
    private var _binding: FragmentExpenseDetailBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseViewModel.ExpenseViewModelFactory((requireActivity().application as ChargeApplication).repository)
    }
    
    private val args: ExpenseDetailFragmentArgs by navArgs()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 获取AppBarLayout并设置状态栏高度的顶部内边距
        val appBarLayout = view.findViewById<View>(R.id.appBarLayout)
        if (appBarLayout != null) {
            StatusBarUtil.applyStatusBarPadding(requireContext(), appBarLayout)
        }
        
        setupToolbar()
        setupButtons()
        loadExpenseDetails()
    }
    
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
    
    private fun setupButtons() {
        binding.editButton.setOnClickListener {
            findNavController().navigate(
                ExpenseDetailFragmentDirections.actionExpenseDetailFragmentToAddExpenseFragment(args.expenseId)
            )
        }
        
        binding.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }
    
    private fun loadExpenseDetails() {
        viewModel.getExpenseById(args.expenseId).observe(viewLifecycleOwner) { expense ->
            binding.apply {
                categoryTextView.text = expense.category
                amountTextView.text = CurrencyFormatter.format(expense.amount)
                descriptionTextView.text = expense.description.ifEmpty { getString(R.string.no_description) }
                dateTextView.text = DateUtils.formatDateForDisplay(expense.date)
                
                // Set category icon
                val iconResId = getCategoryIconResId(expense.category)
                categoryIcon.setImageResource(iconResId)
                
                // Set category icon background color
                val backgroundColorResId = getCategoryColorResId(expense.category)
                val backgroundDrawable = categoryIcon.background.mutate()
                backgroundDrawable.setTint(ContextCompat.getColor(requireContext(), backgroundColorResId))
                categoryIcon.background = backgroundDrawable
            }
        }
    }
    
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete)
            .setMessage(R.string.confirm_delete)
            .setPositiveButton(R.string.confirm) { _, _ ->
                deleteExpense()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
    
    private fun deleteExpense() {
        viewModel.getExpenseById(args.expenseId).observe(viewLifecycleOwner) { expense ->
            viewModel.delete(expense)
            Toast.makeText(requireContext(), R.string.expense_deleted, Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
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
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 