package com.cursor.charge.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cursor.charge.ChargeApplication
import com.cursor.charge.R
import com.cursor.charge.data.entity.ExpenseEntity
import com.cursor.charge.databinding.FragmentAddExpenseBinding
import com.cursor.charge.util.DateUtils
import com.cursor.charge.util.ExpenseCategories
import com.cursor.charge.util.StatusBarUtil
import com.cursor.charge.viewmodel.ExpenseViewModel
import java.util.Calendar
import java.util.Date

class AddExpenseFragment : Fragment() {
    
    private var _binding: FragmentAddExpenseBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseViewModel.ExpenseViewModelFactory((requireActivity().application as ChargeApplication).repository)
    }
    
    private val args: AddExpenseFragmentArgs by navArgs()
    
    private var selectedDate: Date = Date()
    private var expenseToEdit: ExpenseEntity? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
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
        setupCategoryDropdown()
        setupDatePicker()
        setupSaveButton()
        
        if (args.expenseId != -1L) {
            // We're editing an existing expense
            viewModel.getExpenseById(args.expenseId).observe(viewLifecycleOwner) { expense ->
                expenseToEdit = expense
                populateFields(expense)
            }
        }
    }
    
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        
        // Update title if editing
        if (args.expenseId != -1L) {
            binding.toolbar.title = getString(R.string.edit_expense)
            binding.titleTextView.text = getString(R.string.edit_expense_details)
        }
    }
    
    private fun setupCategoryDropdown() {
        val categories = ExpenseCategories.ALL_CATEGORIES
        val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, categories)
        binding.categoryAutoCompleteTextView.setAdapter(adapter)
    }
    
    private fun setupDatePicker() {
        binding.dateEditText.setText(DateUtils.formatDateForDisplay(selectedDate))
        
        binding.dateEditText.setOnClickListener {
            showDatePicker()
        }
    }
    
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                selectedDate = calendar.time
                binding.dateEditText.setText(DateUtils.formatDateForDisplay(selectedDate))
            },
            year,
            month,
            day
        )
        
        datePickerDialog.show()
    }
    
    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            if (validateInput()) {
                saveExpense()
            }
        }
    }
    
    private fun validateInput(): Boolean {
        var isValid = true
        
        // Validate amount
        val amountStr = binding.amountEditText.text.toString()
        if (amountStr.isEmpty()) {
            binding.amountInputLayout.error = getString(R.string.enter_amount)
            isValid = false
        } else {
            binding.amountInputLayout.error = null
        }
        
        // Validate category
        val category = binding.categoryAutoCompleteTextView.text.toString()
        if (category.isEmpty()) {
            binding.categoryInputLayout.error = getString(R.string.select_category)
            isValid = false
        } else {
            binding.categoryInputLayout.error = null
        }
        
        return isValid
    }
    
    private fun saveExpense() {
        val amount = binding.amountEditText.text.toString().toDoubleOrNull() ?: 0.0
        val category = binding.categoryAutoCompleteTextView.text.toString()
        val description = binding.descriptionEditText.text.toString()
        
        if (expenseToEdit != null) {
            // Update existing expense
            val updatedExpense = expenseToEdit!!.copy(
                amount = amount,
                category = category,
                description = description,
                date = selectedDate
            )
            
            viewModel.update(updatedExpense)
            Toast.makeText(requireContext(), R.string.expense_updated, Toast.LENGTH_SHORT).show()
        } else {
            // Create new expense
            val newExpense = ExpenseEntity(
                amount = amount,
                category = category,
                description = description,
                date = selectedDate
            )
            
            viewModel.insert(newExpense)
            Toast.makeText(requireContext(), R.string.expense_added, Toast.LENGTH_SHORT).show()
        }
        
        findNavController().navigateUp()
    }
    
    private fun populateFields(expense: ExpenseEntity) {
        binding.amountEditText.setText(expense.amount.toString())
        binding.categoryAutoCompleteTextView.setText(expense.category)
        binding.descriptionEditText.setText(expense.description)
        selectedDate = expense.date
        binding.dateEditText.setText(DateUtils.formatDateForDisplay(selectedDate))
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 