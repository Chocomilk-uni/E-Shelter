package com.example.e_shelter.screens.user.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.e_shelter.MainActivity
import com.example.e_shelter.R
import com.example.e_shelter.databinding.FragmentFavouritesBinding


class FavouritesFragment : Fragment() {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favouritesViewModel: FavouritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val view = binding.root

        setupTheme()
        setupCustomActionBar()

        favouritesViewModel =
            ViewModelProvider(this)[FavouritesViewModel::class.java]

        if (favouritesViewModel.animals.value != null) binding.infoText.isGone = true
        else binding.favouritesList.isGone = true

        val adapter = FavouritesAdapter()
        binding.favouritesList.adapter = adapter

        favouritesViewModel.animals.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.bottomNavigation.selectedItemId = R.id.favourites

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.search -> {
                    view.findNavController().navigate(FavouritesFragmentDirections.actionFavouritesFragmentToSearchFragment())
                    true
                }
                R.id.home -> {
                    view.findNavController().navigate(FavouritesFragmentDirections.actionFavouritesFragmentToHomeUserFragment())
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
        binding.actionBar.backIcon.setOnClickListener { v: View ->
            v.findNavController().navigateUp()
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