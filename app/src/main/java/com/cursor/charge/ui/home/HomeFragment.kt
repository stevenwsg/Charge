package com.cursor.charge.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cursor.charge.ChargeApplication
import com.cursor.charge.R
import com.cursor.charge.data.entity.ExpenseEntity
import com.cursor.charge.databinding.FragmentHomeBinding
import com.cursor.charge.ui.adapters.ExpenseAdapter
import com.cursor.charge.util.CurrencyFormatter
import com.cursor.charge.util.DateUtils
import com.cursor.charge.util.ExpenseCategories
import com.cursor.charge.util.StatusBarUtil
import com.cursor.charge.viewmodel.ExpenseViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import com.google.android.material.chip.Chip
import kotlinx.coroutines.delay
import kotlin.random.Random

class HomeFragment : Fragment() {
    
    companion object {
        private const val TAG = "HomeFragment"
    }
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseViewModel.ExpenseViewModelFactory((requireActivity().application as ChargeApplication).repository)
    }
    
    private lateinit var expenseAdapter: ExpenseAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // 启用菜单
        Log.d(TAG, "onCreate")
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        
        // 获取AppBarLayout并设置状态栏高度的顶部内边距
        val appBarLayout = view.findViewById<View>(R.id.appBarLayout)
        if (appBarLayout != null) {
            StatusBarUtil.applyStatusBarPadding(requireContext(), appBarLayout)
        }
        
        setupRecyclerView()
        setupChipGroup()
        setupFab()
        observeViewModel()
        
        // 默认选中"本月"筛选，确保触发数据加载
        binding.monthChip.isChecked = true
        viewModel.setCurrentMonth()
        
        // 调试：输出当前的日期范围
        val today = Date()
        val startOfMonth = DateUtils.getStartOfMonth(today)
        val endOfMonth = DateUtils.getEndOfMonth(today)
        Log.d("HomeFragment", "当前月份起始日期: ${DateUtils.formatDateForDisplay(startOfMonth)}")
        Log.d("HomeFragment", "当前月份结束日期: ${DateUtils.formatDateForDisplay(endOfMonth)}")
        
        // 获取所有费用数据进行调试
        viewModel.allExpenses.observe(viewLifecycleOwner) { allExpenses ->
            Log.d("HomeFragment", "所有费用数据数量: ${allExpenses.size}")
            if (allExpenses.isNotEmpty()) {
                for (expense in allExpenses) {
                    Log.d("HomeFragment", "费用ID: ${expense.id}, 金额: ${expense.amount}, 日期: ${DateUtils.formatDateForDisplay(expense.date)}")
                }
                
                // 如果有数据但expensesForRange为空，强制刷新当前筛选
                if (viewModel.expensesForRange.value?.isEmpty() == true) {
                    when {
                        binding.todayChip.isChecked -> viewModel.setToday()
                        binding.weekChip.isChecked -> viewModel.setCurrentWeek()
                        binding.monthChip.isChecked -> viewModel.setCurrentMonth()
                        binding.yearChip.isChecked -> viewModel.setCurrentYear()
                    }
                }
            }
        }
        
        // 在onViewCreated时检查数据库状态
        checkDatabaseState()
    }
    
    // 为Fragment添加菜单
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    
    // 处理菜单项选择
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_test_data -> {
                createTestData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    // 创建测试数据
    private fun createTestData() {
        Log.d(TAG, "开始创建测试数据")
        lifecycleScope.launch {
            // 创建今天的测试数据
            val today = Date()
            val testExpenses = mutableListOf<ExpenseEntity>()
            
            // 添加今日数据
            testExpenses.add(
                ExpenseEntity(
                    amount = 25.50,
                    category = ExpenseCategories.FOOD,
                    description = "午餐 - 测试数据",
                    date = today
                )
            )
            
            testExpenses.add(
                ExpenseEntity(
                    amount = 15.00,
                    category = ExpenseCategories.TRANSPORTATION,
                    description = "打车 - 测试数据",
                    date = today
                )
            )
            
            // 添加本月其他日期的数据
            val calendar = Calendar.getInstance()
            calendar.time = today
            calendar.add(Calendar.DAY_OF_MONTH, -2) // 两天前
            
            testExpenses.add(
                ExpenseEntity(
                    amount = 35.80,
                    category = ExpenseCategories.SHOPPING,
                    description = "超市购物 - 测试数据",
                    date = calendar.time
                )
            )
            
            calendar.add(Calendar.DAY_OF_MONTH, -3) // 再往前三天
            testExpenses.add(
                ExpenseEntity(
                    amount = 50.00,
                    category = ExpenseCategories.ENTERTAINMENT,
                    description = "电影院 - 测试数据",
                    date = calendar.time
                )
            )
            
            // 添加上月数据
            calendar.time = today
            calendar.add(Calendar.MONTH, -1) // 上个月
            testExpenses.add(
                ExpenseEntity(
                    amount = 120.00,
                    category = ExpenseCategories.UTILITIES,
                    description = "水电费 - 测试数据",
                    date = calendar.time
                )
            )
            
            // 添加测试数据
            for (expense in testExpenses) {
                viewModel.insert(expense)
                // 短暂延迟，避免同时插入
                delay(100)
            }
            
            Toast.makeText(requireContext(), "已添加${testExpenses.size}条测试数据", Toast.LENGTH_SHORT).show()
            
            // 根据当前选中的过滤条件刷新数据
            when {
                binding.todayChip.isChecked -> viewModel.setToday()
                binding.weekChip.isChecked -> viewModel.setCurrentWeek()
                binding.monthChip.isChecked -> viewModel.setCurrentMonth()
                binding.yearChip.isChecked -> viewModel.setCurrentYear()
            }
            
            // 插入完成后检查数据库状态
            checkDatabaseState()
        }
    }
    
    private fun setupRecyclerView() {
        Log.d(TAG, "设置RecyclerView")
        expenseAdapter = ExpenseAdapter { expense ->
            val action = HomeFragmentDirections.actionHomeFragmentToExpenseDetailFragment(expense.id)
            findNavController().navigate(action)
        }
        
        binding.expensesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = expenseAdapter
        }
        
        // 设置初始可见性
        binding.expensesRecyclerView.visibility = View.VISIBLE
        binding.noExpensesTextView.visibility = View.GONE
    }
    
    private fun setupChipGroup() {
        binding.dateFilterChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.todayChip -> {
                    viewModel.setToday()
                    Log.d("HomeFragment", "切换到今日筛选")
                }
                R.id.weekChip -> {
                    viewModel.setCurrentWeek()
                    Log.d("HomeFragment", "切换到本周筛选")
                }
                R.id.monthChip -> {
                    viewModel.setCurrentMonth()
                    Log.d("HomeFragment", "切换到本月筛选")
                }
                R.id.yearChip -> {
                    viewModel.setCurrentYear()
                    Log.d("HomeFragment", "切换到本年筛选")
                }
                R.id.customChip -> {
                    showDateRangePicker()
                    Log.d("HomeFragment", "切换到自定义筛选")
                }
            }
        }
    }
    
    private fun setupFab() {
        binding.addExpenseFab.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddExpenseFragment()
            )
        }
        
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> true
                R.id.statisticsFragment -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToStatisticsFragment()
                    )
                    false
                }
                else -> false
            }
        }
    }
    
    private fun observeViewModel() {
        viewModel.expensesForRange.observe(viewLifecycleOwner) { expenses ->
            Log.d("HomeFragment", "当前筛选费用数据数量: ${expenses.size}")
            
            expenseAdapter.submitList(expenses)
            binding.noExpensesTextView.visibility = if (expenses.isEmpty()) View.VISIBLE else View.GONE
            binding.expensesRecyclerView.visibility = if (expenses.isEmpty()) View.GONE else View.VISIBLE
            
            if (expenses.isEmpty()) {
                Toast.makeText(requireContext(), "当前时间范围内没有费用数据", Toast.LENGTH_SHORT).show()
            }
        }
        
        viewModel.totalExpensesForRange.observe(viewLifecycleOwner) { total ->
            binding.totalAmountTextView.text = CurrencyFormatter.format(total ?: 0.0)
        }
        
        viewModel.startDate.observe(viewLifecycleOwner) { startDate ->
            Log.d("HomeFragment", "开始日期更改为: ${DateUtils.formatDateForDisplay(startDate)}")
            updateDateFilterChip(startDate)
        }
        
        viewModel.endDate.observe(viewLifecycleOwner) { endDate ->
            Log.d("HomeFragment", "结束日期更改为: ${DateUtils.formatDateForDisplay(endDate)}")
        }
    }
    
    private fun updateDateFilterChip(startDate: Date) {
        val today = Date()
        
        when {
            DateUtils.getStartOfDay(today) == DateUtils.getStartOfDay(startDate) -> {
                binding.todayChip.isChecked = true
            }
            DateUtils.getStartOfWeek(today) == DateUtils.getStartOfWeek(startDate) -> {
                binding.weekChip.isChecked = true
            }
            DateUtils.getStartOfMonth(today) == DateUtils.getStartOfMonth(startDate) -> {
                binding.monthChip.isChecked = true
            }
            DateUtils.getStartOfYear(today) == DateUtils.getStartOfYear(startDate) -> {
                binding.yearChip.isChecked = true
            }
            else -> {
                binding.customChip.isChecked = true
            }
        }
    }
    
    private fun showDateRangePicker() {
        // For simplicity, we'll just set it to current month when custom is clicked
        // In a real app, you would implement a date range picker dialog
        viewModel.setCurrentMonth()
    }
    
    private fun checkDatabaseState() {
        lifecycleScope.launch {
            val repository = (requireActivity().application as ChargeApplication).repository
            
            val count = repository.getExpenseCount()
            val earliest = repository.getEarliestExpenseDate()
            val latest = repository.getLatestExpenseDate()
            
            Log.d(TAG, "数据库状态:")
            Log.d(TAG, "总记录数: $count")
            Log.d(TAG, "最早记录: ${earliest?.let { DateUtils.formatDateTimeForDebug(it) }}")
            Log.d(TAG, "最新记录: ${latest?.let { DateUtils.formatDateTimeForDebug(it) }}")
            
            latest?.let { latestDate ->
                val recentExpenses = repository.getRecentExpenses(latestDate)
                Log.d(TAG, "最近的 ${recentExpenses.size} 条记录:")
                recentExpenses.forEach { expense ->
                    Log.d(TAG, "费用: ID=${expense.id}, 金额=${expense.amount}, 日期=${DateUtils.formatDateTimeForDebug(expense.date)}")
                }
            }
        }
    }
    
    private fun checkTodayData() {
        lifecycleScope.launch {
            val repository = (requireActivity().application as ChargeApplication).repository
            
            val today = Date()
            val startOfDay = DateUtils.getStartOfDay(today)
            val endOfDay = DateUtils.getEndOfDay(today)
            
            val count = repository.getExpenseCountInRange(startOfDay, endOfDay)
            Log.d(TAG, "今日数据检查:")
            Log.d(TAG, "日期范围: ${DateUtils.formatDateTimeForDebug(startOfDay)} - ${DateUtils.formatDateTimeForDebug(endOfDay)}")
            Log.d(TAG, "记录数量: $count")
            
            if (count > 0) {
                val expenses = repository.getRecentExpenses(startOfDay)
                Log.d(TAG, "今日记录:")
                expenses.forEach { expense ->
                    Log.d(TAG, "费用: ID=${expense.id}, 金额=${expense.amount}, 日期=${DateUtils.formatDateTimeForDebug(expense.date)}")
                }
            } else {
                Log.d(TAG, "今日暂无记录")
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
        _binding = null
    }
} 