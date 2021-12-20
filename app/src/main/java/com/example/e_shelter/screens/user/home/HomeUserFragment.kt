package com.example.e_shelter.screens.user.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.e_shelter.MainActivity
import com.example.e_shelter.R
import com.example.e_shelter.databinding.FragmentHomeUserBinding


class HomeUserFragment : Fragment() {
    private var _binding: FragmentHomeUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.goToSearchButton.setOnClickListener{ v: View ->
            v.findNavController().navigate(HomeUserFragmentDirections.actionHomeUserFragmentToSearchFragment())
        }

        binding.bottomNavigation.selectedItemId = R.id.home

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.search -> {
                    view.findNavController().navigate(HomeUserFragmentDirections.actionHomeUserFragmentToSearchFragment())
                    true
                }
                R.id.favourites -> {
                    view.findNavController().navigate(HomeUserFragmentDirections.actionHomeUserFragmentToFavouritesFragment())
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
        binding.actionBar.backIcon.isGone = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}