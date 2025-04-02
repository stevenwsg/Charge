package com.cursor.charge.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cursor.charge.ChargeApplication
import com.cursor.charge.R
import com.cursor.charge.databinding.FragmentStatisticsBinding
import com.cursor.charge.util.CurrencyFormatter
import com.cursor.charge.util.DateUtils
import com.cursor.charge.util.ExpenseCategories
import com.cursor.charge.viewmodel.ExpenseViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.Date

class StatisticsFragment : Fragment() {
    
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseViewModel.ExpenseViewModelFactory((requireActivity().application as ChargeApplication).repository)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupChipGroup()
        setupCharts()
        observeViewModel()
    }
    
    private fun setupChipGroup() {
        binding.dateFilterChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.todayChip -> viewModel.setToday()
                R.id.weekChip -> viewModel.setCurrentWeek()
                R.id.monthChip -> viewModel.setCurrentMonth()
                R.id.yearChip -> viewModel.setCurrentYear()
                R.id.customChip -> showDateRangePicker()
            }
        }
    }
    
    private fun setupCharts() {
        setupPieChart()
        setupBarChart()
    }
    
    private fun setupPieChart() {
        binding.pieChart.apply {
            description.isEnabled = false
            setUsePercentValues(true)
            setExtraOffsets(5f, 10f, 5f, 5f)
            dragDecelerationFrictionCoef = 0.95f
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            holeRadius = 58f
            transparentCircleRadius = 61f
            setDrawCenterText(true)
            rotationAngle = 0f
            isRotationEnabled = true
            isHighlightPerTapEnabled = true
            animateY(1400, Easing.EaseInOutQuad)
            legend.isEnabled = true
            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
            legend.setDrawInside(false)
        }
    }
    
    private fun setupBarChart() {
        binding.barChart.apply {
            description.isEnabled = false
            setFitBars(true)
            animateY(1400, Easing.EaseInOutQuad)
            
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            
            axisLeft.setDrawGridLines(false)
            axisRight.isEnabled = false
            
            legend.isEnabled = true
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.setDrawInside(false)
        }
    }
    
    private fun observeViewModel() {
        viewModel.startDate.observe(viewLifecycleOwner) { startDate ->
            viewModel.endDate.observe(viewLifecycleOwner) { endDate ->
                if (startDate != null && endDate != null) {
                    updateDateRange(startDate, endDate)
                }
            }
        }
        
        viewModel.totalExpensesForRange.observe(viewLifecycleOwner) { total ->
            binding.totalAmountTextView.text = CurrencyFormatter.format(total ?: 0.0)
        }
        
        viewModel.categorySummary.observe(viewLifecycleOwner) { categorySummary ->
            if (categorySummary != null && categorySummary.isNotEmpty()) {
                updatePieChart(categorySummary)
                updateBarChart(categorySummary)
                
                binding.noPieChartDataTextView.visibility = View.GONE
                binding.noBarChartDataTextView.visibility = View.GONE
            } else {
                binding.noPieChartDataTextView.visibility = View.VISIBLE
                binding.noBarChartDataTextView.visibility = View.VISIBLE
            }
        }
    }
    
    private fun updateDateRange(startDate: Date, endDate: Date) {
        val startStr = DateUtils.formatDateForDisplay(startDate)
        val endStr = DateUtils.formatDateForDisplay(endDate)
        binding.dateRangeTextView.text = "$startStr - $endStr"
    }
    
    private fun updatePieChart(categorySummary: Map<String, Double>) {
        val entries = ArrayList<PieEntry>()
        
        categorySummary.forEach { (category, amount) ->
            entries.add(PieEntry(amount.toFloat(), category))
        }
        
        val dataSet = PieDataSet(entries, "").apply {
            sliceSpace = 3f
            selectionShift = 5f
            
            val colors = categorySummary.keys.map { category ->
                val colorResId = getCategoryColor(category)
                colorResId
            }
            
            this.colors = colors
        }
        
        val data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter(binding.pieChart))
            setValueTextSize(11f)
            setValueTextColor(Color.WHITE)
        }
        
        binding.pieChart.apply {
            this.data = data
            highlightValues(null)
            invalidate()
        }
    }
    
    private fun updateBarChart(categorySummary: Map<String, Double>) {
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        
        categorySummary.entries.forEachIndexed { index, (category, amount) ->
            entries.add(BarEntry(index.toFloat(), amount.toFloat()))
            labels.add(category)
        }
        
        val dataSet = BarDataSet(entries, "Categories").apply {
            val colors = categorySummary.keys.map { category ->
                val colorResId = getCategoryColor(category)
                colorResId
            }
            
            this.colors = colors
            valueTextSize = 10f
            valueTextColor = Color.BLACK
        }
        
        val data = BarData(dataSet).apply {
            barWidth = 0.9f
        }
        
        binding.barChart.apply {
            this.data = data
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.labelCount = labels.size
            
            invalidate()
        }
    }
    
    private fun getCategoryColor(category: String): Int {
        return when (category) {
            ExpenseCategories.HOUSING -> Color.parseColor("#4285F4")
            ExpenseCategories.FOOD -> Color.parseColor("#EA4335")
            ExpenseCategories.TRANSPORTATION -> Color.parseColor("#FBBC05")
            ExpenseCategories.ENTERTAINMENT -> Color.parseColor("#34A853")
            ExpenseCategories.SHOPPING -> Color.parseColor("#FF6D00")
            ExpenseCategories.UTILITIES -> Color.parseColor("#AA00FF")
            ExpenseCategories.HEALTH -> Color.parseColor("#00BCD4")
            ExpenseCategories.EDUCATION -> Color.parseColor("#3F51B5")
            ExpenseCategories.TRAVEL -> Color.parseColor("#009688")
            else -> Color.parseColor("#9E9E9E")
        }
    }
    
    private fun showDateRangePicker() {
        // For simplicity, we'll just set it to current month when custom is clicked
        // In a real app, you would implement a date range picker dialog
        viewModel.setCurrentMonth()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 