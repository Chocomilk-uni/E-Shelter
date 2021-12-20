package com.example.e_shelter.screens.shelteradmin.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.example.e_shelter.MainActivity
import com.example.e_shelter.R
import com.example.e_shelter.databinding.FragmentReportBinding


class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val view = binding.root

//        binding.selectReportPeriodButton.setOnClickListener{ v: View ->
//
//        }

//        binding.getReportButton.setOnClickListener { v: View ->
//
//        }

        binding.bottomNavigation.selectedItemId = R.id.report

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.applications -> {
                    view.findNavController().navigate(ReportFragmentDirections.actionReportFragmentToAdoptionApplicationsFragment())
                    true
                }
                R.id.home -> {
                    view.findNavController().navigate(ReportFragmentDirections.actionReportFragmentToHomeShelterFragment())
                    true
                }
                R.id.add_animal -> {
                    view.findNavController().navigate(ReportFragmentDirections.actionReportFragmentToAddEditAnimalFragment(0))
                    true
                }
                else -> false
            }
        }

        setupCustomActionBar()

        return view
    }

    private fun setupCustomActionBar() {
        val toolbar = getView()?.findViewById<Toolbar>(R.id.action_bar)
        if (toolbar != null) {
            (requireActivity() as MainActivity).setupActionBar(toolbar)
        }
        binding.actionBar.backIcon.setOnClickListener { v: View ->
            v.findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}