package com.example.e_shelter.screens.shelteradmin.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.e_shelter.App
import com.example.e_shelter.MainActivity
import com.example.e_shelter.R
import com.example.e_shelter.convertLongToPeriodDateString
import com.example.e_shelter.databinding.FragmentReportBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar


class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private var dateFrom: Long? = null
    private var dateTo: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val view = binding.root

        val reportViewModel =
            ViewModelProvider(this)[ReportViewModel::class.java]

        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Выберите отчётный период")
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

        dateRangePicker.addOnPositiveButtonClickListener {
            binding.reportPeriodText.text = convertLongToPeriodDateString(
                dateRangePicker.selection!!.first,
                dateRangePicker.selection!!.second
            )
            dateFrom = dateRangePicker.selection!!.first
            dateTo = dateRangePicker.selection!!.second
        }

        binding.selectReportPeriodButton.setOnClickListener {
            dateRangePicker.show(parentFragmentManager, "tag");
        }

        binding.getReportButton.setOnClickListener {
            if (reportViewModel.onGetReport(dateFrom!!, dateTo!!))
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.get_report_success),
                    Snackbar.LENGTH_SHORT
                ).show()
            else
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.get_report_error),
                    Snackbar.LENGTH_SHORT
                ).show()
        }

        binding.bottomNavigation.selectedItemId = R.id.report

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.applications -> {
                    view.findNavController()
                        .navigate(ReportFragmentDirections.actionReportFragmentToAdoptionApplicationsFragment())
                    true
                }
                R.id.home -> {
                    view.findNavController()
                        .navigate(ReportFragmentDirections.actionReportFragmentToHomeShelterFragment())
                    true
                }
                R.id.add_animal -> {
                    view.findNavController().navigate(
                        ReportFragmentDirections.actionReportFragmentToAddEditAnimalFragment(0)
                    )
                    true
                }
                else -> false
            }
        }

        setupCustomActionBar()

        return view
    }

    private fun setupCustomActionBar() {
        val toolbar = view?.findViewById<Toolbar>(R.id.action_bar)
        if (toolbar != null) {
            (requireActivity() as MainActivity).setupActionBar(toolbar)
        }
        binding.actionBar.exitIcon.setOnClickListener {
            App.firebaseAuth.signOut()
            findNavController().navigate(ReportFragmentDirections.actionReportFragmentToSignInUserFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}