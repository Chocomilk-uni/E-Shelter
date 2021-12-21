package com.example.e_shelter.screens.animalprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.e_shelter.App
import com.example.e_shelter.MainActivity
import com.example.e_shelter.R
import com.example.e_shelter.databinding.FragmentAnimalProfileBinding
import com.example.e_shelter.screens.shelteradmin.home.HomeShelterFragmentDirections
import com.google.android.material.snackbar.Snackbar


class AnimalProfileFragment : Fragment() {
    private var _binding: FragmentAnimalProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var animalProfileViewModel: AnimalProfileViewModel
    lateinit var arguments: AnimalProfileFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimalProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        setupTheme()
        setupCustomActionBar()

        arguments = AnimalProfileFragmentArgs.fromBundle(requireArguments())

        val viewModelFactory = AnimalProfileViewModelFactory(arguments.animalId)

        animalProfileViewModel =
            ViewModelProvider(
                this, viewModelFactory
            )[AnimalProfileViewModel::class.java]

        binding.animalProfileViewModel = animalProfileViewModel

        setupBottomNavigation(view)
        if (animalProfileViewModel.isAdmin) setupAdminLayoutOptions()
        updateAddToFavButton()
        binding.animalPhoto.setImageBitmap(animalProfileViewModel.profilePic)

        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.addToFavouritesButton.setOnClickListener{
            animalProfileViewModel.onAddToFavourites()
        }

        binding.applyForAdoptionButton.setOnClickListener {
            animalProfileViewModel.onApplyForAdoption()
        }

        animalProfileViewModel.addToFavouritesSuccess.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.add_to_favourites_success),
                    Snackbar.LENGTH_SHORT
                ).show()
                animalProfileViewModel.doneShowingSnackBar()
                updateAddToFavButton()
            }
        })

        animalProfileViewModel.deleteFromFavouritesSuccess.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.delete_from_favourites_success),
                    Snackbar.LENGTH_SHORT
                ).show()
                animalProfileViewModel.doneShowingSnackBar()
                updateAddToFavButton()
            }
        })

        animalProfileViewModel.applicationCreateSuccess.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.adoption_application_created),
                    Snackbar.LENGTH_SHORT
                ).show()
                animalProfileViewModel.doneShowingSnackBar()
            }
        })

        animalProfileViewModel.applicationAlreadyExistError.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.application_already_exist),
                    Snackbar.LENGTH_SHORT
                ).show()
                animalProfileViewModel.doneShowingSnackBar()
            }
        })

        return view
    }

    private fun setupCustomActionBar() {
        val toolbar = view?.findViewById<Toolbar>(R.id.action_bar)
        if (toolbar != null) {
            (requireActivity() as MainActivity).setupActionBar(toolbar)
        }
        binding.actionBar.exitIcon.setOnClickListener {
            App.firebaseAuth.signOut()
            findNavController().navigate(AnimalProfileFragmentDirections.actionAnimalProfileFragmentToSignInUserFragment())
        }
    }

    private fun setupAdminLayoutOptions() {
        binding.addToFavouritesButton.isGone = true
        binding.applyForAdoptionButton.isGone = true
    }

    private fun updateAddToFavButton() {
        if (animalProfileViewModel.favouritesEntryExist) {
            binding.addToFavouritesButton.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.button_favourites_added
                )
            )
        }
        else binding.addToFavouritesButton.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.button_add_to_favourites
            )
        )
    }

    private fun setupBottomNavigation(v: View) {
        if (animalProfileViewModel.isAdmin) {

            binding.bottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.applications -> {
                        v.findNavController()
                            .navigate(HomeShelterFragmentDirections.actionHomeShelterFragmentToAdoptionApplicationsFragment())
                        true
                    }
                    R.id.report -> {
                        v.findNavController()
                            .navigate(HomeShelterFragmentDirections.actionHomeShelterFragmentToReportFragment())
                        true
                    }
                    R.id.add_animal -> {
                        v.findNavController().navigate(
                            HomeShelterFragmentDirections.actionHomeShelterFragmentToAddEditAnimalFragment(
                                0L
                            )
                        )
                        true
                    }
                    else -> false
                }
            }
        }
        else {
            binding.bottomNavigation.menu.clear()
            binding.bottomNavigation.inflateMenu(R.menu.bottom_navigation_user)

            binding.bottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.search -> {
                        v.findNavController()
                            .navigate(AnimalProfileFragmentDirections.actionAnimalProfileFragmentToSearchFragment())
                        true
                    }
                    R.id.home -> {
                        v.findNavController()
                            .navigate(AnimalProfileFragmentDirections.actionAnimalProfileFragmentToHomeUserFragment())
                        true
                    }
                    R.id.favourites -> {
                        v.findNavController().navigate(
                            AnimalProfileFragmentDirections.actionAnimalProfileFragmentToFavouritesFragment()
                        )
                        true
                    }
                    else -> false
                }
            }
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