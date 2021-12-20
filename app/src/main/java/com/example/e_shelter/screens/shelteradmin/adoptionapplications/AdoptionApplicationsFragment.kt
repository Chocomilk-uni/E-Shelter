package com.example.e_shelter.screens.shelteradmin.adoptionapplications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.e_shelter.MainActivity
import com.example.e_shelter.R
import com.example.e_shelter.databinding.FragmentAdoptionApplicationsBinding


class AdoptionApplicationsFragment : Fragment() {
    private var _binding: FragmentAdoptionApplicationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adoptionApplicationsViewModel: AdoptionApplicationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdoptionApplicationsBinding.inflate(inflater, container, false)
        val view = binding.root

        setupTheme()
        setupCustomActionBar()

        adoptionApplicationsViewModel =
            ViewModelProvider(this)[AdoptionApplicationsViewModel::class.java]

        val adapter = AdoptionApplicationAdapter()
        binding.adoptionApplicationsList.adapter = adapter

        adoptionApplicationsViewModel.applications.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.bottomNavigation.selectedItemId = R.id.report

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.report -> {
                    view.findNavController().navigate(AdoptionApplicationsFragmentDirections.actionAdoptionApplicationsFragmentToReportFragment())
                    true
                }
                R.id.home -> {
                    view.findNavController().navigate(AdoptionApplicationsFragmentDirections.actionAdoptionApplicationsFragmentToHomeShelterFragment())
                    true
                }
                R.id.add_animal -> {
                    view.findNavController().navigate(AdoptionApplicationsFragmentDirections.actionAdoptionApplicationsFragmentToAddEditAnimalFragment(0))
                    true
                }
                else -> false
            }
        }

        setupCustomActionBar()

        return view
    }

    private fun setupTheme() {
        (requireActivity() as MainActivity).setTheme(R.style.MainTheme)
    }

    private fun setupCustomActionBar() {
        val toolbar = view?.findViewById<Toolbar>(R.id.action_bar)
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