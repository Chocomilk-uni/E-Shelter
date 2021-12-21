package com.example.e_shelter.screens.user.searchresult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.e_shelter.App
import com.example.e_shelter.MainActivity
import com.example.e_shelter.R
import com.example.e_shelter.databinding.FragmentSearchResultBinding


class SearchResultFragment : Fragment() {
    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchResultViewModel: SearchResultViewModel
    lateinit var arguments: SearchResultFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        val view = binding.root

        setupTheme()
        setupCustomActionBar()

        arguments = SearchResultFragmentArgs.fromBundle(requireArguments())

        val viewModelFactory = SearchResultViewModelFactory(arguments.searchModel)

        searchResultViewModel =
            ViewModelProvider(
                this, viewModelFactory
            )[SearchResultViewModel::class.java]

        val adapter = AnimalSearchResultAdapter()
        binding.searchResultList.adapter = adapter
        adapter.submitList(searchResultViewModel.resultedAnimals)

        if (!searchResultViewModel.zeroResults) {
            binding.infoText.isGone = true
        } else binding.searchResultList.isGone = true

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    view.findNavController()
                        .navigate(SearchResultFragmentDirections.actionSearchResultFragmentToHomeUserFragment())
                    true
                }
                R.id.favourites -> {
                    view.findNavController()
                        .navigate(SearchResultFragmentDirections.actionSearchResultFragmentToFavouritesFragment())
                    true
                }
                R.id.search -> {
                    view.findNavController()
                        .navigate(SearchResultFragmentDirections.actionSearchResultFragmentToSearchFragment())
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
            findNavController().navigate(SearchResultFragmentDirections.actionSearchResultFragmentToSignInUserFragment())
        }
    }

    private fun setupTheme() {
        (requireActivity() as MainActivity).setTheme(R.style.MainTheme)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}