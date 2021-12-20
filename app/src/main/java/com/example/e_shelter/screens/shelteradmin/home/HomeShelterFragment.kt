package com.example.e_shelter.screens.shelteradmin.home

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.e_shelter.MainActivity
import com.example.e_shelter.R
import com.example.e_shelter.databinding.FragmentHomeShelterBinding
import com.google.android.material.snackbar.Snackbar


class HomeShelterFragment : Fragment() {
    private var _binding: FragmentHomeShelterBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeShelterViewModel: HomeShelterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeShelterBinding.inflate(inflater, container, false)

        val view = binding.root

        setupTheme()
        setupCustomActionBar()

        homeShelterViewModel =
            ViewModelProvider(this)[HomeShelterViewModel::class.java]

        binding.homeShelterViewModel = homeShelterViewModel
        setLogo()

        binding.lifecycleOwner = this.viewLifecycleOwner

        val adapter = AnimalAdapter()
        binding.animalList.adapter = adapter

        val dogsAdapter = AnimalAdapter()
        val catsAdapter = AnimalAdapter()
        val exoticAdapter = AnimalAdapter()

        homeShelterViewModel.animals.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        homeShelterViewModel.dogs.observe(viewLifecycleOwner, {
            it?.let {
                dogsAdapter.submitList(it)
            }
        })

        homeShelterViewModel.cats.observe(viewLifecycleOwner, {
            it?.let {
                catsAdapter.submitList(it)
            }
        })

        homeShelterViewModel.exotic.observe(viewLifecycleOwner, {
            it?.let {
                exoticAdapter.submitList(it)
            }
        })

        binding.editButton.setOnClickListener {
            unlockEditingOptions()
        }

        binding.filtersChipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.all_chip -> binding.animalList.swapAdapter(adapter, false)
                R.id.dogs_chip -> binding.animalList.swapAdapter(dogsAdapter, false)
                R.id.cats_chip -> binding.animalList.swapAdapter(catsAdapter, false)
                R.id.exotic_chip -> binding.animalList.swapAdapter(exoticAdapter, false)
            }
        }

        binding.saveButton.setOnClickListener {
            val name = binding.shelterNameEdit.text.toString()
            val phoneNumber = binding.phoneEdit.text.toString()
            val city = binding.cityEdit.text.toString()
            val address = binding.addressEdit.text.toString()

            if (checkInput()) {
                homeShelterViewModel.onEdit(name, phoneNumber, city, address)
            }
        }

        homeShelterViewModel.editSuccess.observe(viewLifecycleOwner, {
            if (it == true) {
                hideEditingOptions()
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.save_data_success),
                    Snackbar.LENGTH_SHORT
                ).show()
                homeShelterViewModel.doneShowingSnackBar()
            }
        })


        binding.bottomNavigation.selectedItemId = R.id.home
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.applications -> {
                    view.findNavController()
                        .navigate(HomeShelterFragmentDirections.actionHomeShelterFragmentToAdoptionApplicationsFragment())
                    true
                }
                R.id.report -> {
                    view.findNavController()
                        .navigate(HomeShelterFragmentDirections.actionHomeShelterFragmentToReportFragment())
                    true
                }
                R.id.add_animal -> {
                    view.findNavController().navigate(
                        HomeShelterFragmentDirections.actionHomeShelterFragmentToAddEditAnimalFragment(
                            0L
                        )
                    )
                    true
                }
                else -> false
            }
        }

        binding.addShelterLogoText.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }

        return binding.root
    }

    private fun setLogo() {
        if (homeShelterViewModel.logoPic != null) {
            binding.shelterLogoImage.setImageBitmap(homeShelterViewModel.logoPic)
            binding.addShelterLogoText.isGone = true
        } else binding.addShelterLogoText.isGone = false
    }

    // TODO: add regex
    private fun checkInput(): Boolean {
        if (binding.shelterNameEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите название приюта", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.phoneEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите номер телефона", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.cityEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите город", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.addressEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите адрес", Toast.LENGTH_LONG).show()
            return false
        }
        if (homeShelterViewModel.logoPic == null) {
            Toast.makeText(activity, "Добавьте логотип", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun unlockEditingOptions() {
        binding.shelterNameText.isGone = true
        binding.addressText.isGone = true
        binding.phoneText.isGone = true
        binding.editButton.isGone = true
        binding.shelterLogoImage.setImageResource(R.color.complementing_light_green)

        binding.addShelterLogoText.isGone = false
        binding.shelterNameEdit.isGone = false
        binding.cityEdit.isGone = false
        binding.addressEdit.isGone = false
        binding.phoneEdit.isGone = false
        binding.saveButton.isGone = false
        binding.addShelterLogoText.isGone = false
    }

    private fun hideEditingOptions() {
        binding.shelterNameText.isGone = false
        binding.addressText.isGone = false
        binding.phoneText.isGone = false
        binding.editButton.isGone = false
        binding.addShelterLogoText.isGone = false
        binding.shelterLogoImage.setImageBitmap(homeShelterViewModel.logoPic)

        binding.shelterNameEdit.isGone = true
        binding.cityEdit.isGone = true
        binding.addressEdit.isGone = true
        binding.phoneEdit.isGone = true
        binding.saveButton.isGone = true
        binding.addShelterLogoText.isGone = true
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                try {
                    val inputStream = activity?.contentResolver?.openInputStream(data?.data!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.shelterLogoImage.setImageBitmap(bitmap)
                    homeShelterViewModel.onAttachPic(bitmap)
                } catch (e: Exception) {
                }
            }
        }

    private fun setupCustomActionBar() {
        val toolbar = view?.findViewById<Toolbar>(R.id.action_bar)
        if (toolbar != null) {
            (requireActivity() as MainActivity).setupActionBar(toolbar)
        }
        binding.actionBar.backIcon.isGone = true
    }

    private fun setupTheme() {
        (requireActivity() as MainActivity).setTheme(R.style.MainTheme)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}