package com.example.e_shelter.screens.user.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.e_shelter.App
import com.example.e_shelter.MainActivity
import com.example.e_shelter.R
import com.example.e_shelter.databinding.FragmentSearchBinding


class SearchFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var species: String
    private lateinit var gender: String
    private lateinit var city: String
    private lateinit var breed: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        setupTheme()
        setupCustomActionBar()

        val searchViewModel =
            ViewModelProvider(this)[SearchViewModel::class.java]

        binding.citySpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            searchViewModel.cities
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.citySpinner.adapter = adapter
            binding.citySpinner.onItemSelectedListener = this
        }

        binding.breedSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            searchViewModel.breeds
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.breedSpinner.adapter = adapter
            binding.breedSpinner.onItemSelectedListener = this
        }

        binding.genderSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            searchViewModel.genders
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.genderSpinner.adapter = adapter
            binding.genderSpinner.onItemSelectedListener = this
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.species,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.speciesSpinner.adapter = adapter
            binding.speciesSpinner.onItemSelectedListener = this
        }

        binding.searchButton.setOnClickListener {
            var ageFrom: String? = binding.ageFromEdit.text.toString()
            var ageTo: String? = binding.ageFromEdit.text.toString()
            val isSterilised = binding.sterilisedCheckbox.isChecked
            val isVaccinated = binding.vaccinatedCheckbox.isChecked

            if (ageFrom == "") ageFrom = null
            if (ageTo == "") ageTo = null

            searchViewModel.onSearch(
                city,
                species,
                breed,
                ageFrom,
                ageTo,
                gender,
                isSterilised,
                isVaccinated
            )
        }

        searchViewModel.navigateToSearchResult.observe(viewLifecycleOwner, {
            if (it == true) {
                view.findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(
                        searchViewModel.searchModel
                    )
                )
                searchViewModel.doneNavigating()
            }
        })

        binding.bottomNavigation.selectedItemId = R.id.search

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    view.findNavController()
                        .navigate(SearchFragmentDirections.actionSearchFragmentToHomeUserFragment())
                    true
                }
                R.id.favourites -> {
                    view.findNavController()
                        .navigate(SearchFragmentDirections.actionSearchFragmentToFavouritesFragment())
                    true
                }
                else -> false
            }
        }

        return view
    }

    private fun setupCustomActionBar() {
        val toolbar = view?.findViewById<Toolbar>(R.id.action_bar)
        if (toolbar != null) {
            (requireActivity() as MainActivity).setupActionBar(toolbar)
        }
        binding.actionBar.exitIcon.setOnClickListener {
            App.firebaseAuth.signOut()
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSignInUserFragment())
        }
    }

    private fun setupTheme() {
        (requireActivity() as MainActivity).setTheme(R.style.MainTheme)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when (parent.id) {
            binding.speciesSpinner.id -> species = parent.getItemAtPosition(pos).toString()
            binding.genderSpinner.id -> gender = parent.getItemAtPosition(pos).toString()
            binding.citySpinner.id -> city = parent.getItemAtPosition(pos).toString()
            binding.breedSpinner.id -> breed = parent.getItemAtPosition(pos).toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        when (parent?.id) {
            binding.speciesSpinner.id -> species = parent.getItemAtPosition(0).toString()
            binding.genderSpinner.id -> gender = parent.getItemAtPosition(0).toString()
            binding.citySpinner.id -> city = parent.getItemAtPosition(0).toString()
            binding.breedSpinner.id -> breed = parent.getItemAtPosition(0).toString()
        }
    }
}