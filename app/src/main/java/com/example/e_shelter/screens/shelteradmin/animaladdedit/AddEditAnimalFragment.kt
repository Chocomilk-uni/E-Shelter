package com.example.e_shelter.screens.shelteradmin.animaladdedit

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.e_shelter.MainActivity
import com.example.e_shelter.R
import com.example.e_shelter.convertLongToDateString
import com.example.e_shelter.databinding.FragmentAddEditAnimalBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar


class AddEditAnimalFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentAddEditAnimalBinding? = null
    private val binding get() = _binding!!
    private lateinit var addEditAnimalViewModel: AddEditAnimalViewModel
    private lateinit var species: String
    private lateinit var gender: String
    private lateinit var status: String
    private var admissionDate: Long? = null
    lateinit var arguments: AddEditAnimalFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditAnimalBinding.inflate(inflater, container, false)

        setupTheme()
        setupCustomActionBar()

        val view = binding.root

        arguments = AddEditAnimalFragmentArgs.fromBundle(requireArguments())

        val viewModelFactory = AddEditAnimalViewModelFactory(arguments.animalId)

        addEditAnimalViewModel =
            ViewModelProvider(
                this, viewModelFactory
            )[AddEditAnimalViewModel::class.java]


        binding.addEditAnimalViewModel = addEditAnimalViewModel

        if (arguments.animalId > 0)
            setProfilePic()

        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.saveButton.setOnClickListener {
            val name = binding.petNameEdit.text.toString()
            val age = binding.ageEdit.text.toString()
            val breed = binding.breedEdit.text.toString()
            val description = binding.descriptionEdit.text.toString()
            val isVaccinated = binding.vaccinatedCheckbox.isChecked
            val isSterilised = binding.sterilisedCheckbox.isChecked

            if (checkInput()) {
                addEditAnimalViewModel.onSave(
                    name,
                    age.toInt(),
                    breed,
                    isVaccinated,
                    isSterilised,
                    description,
                    admissionDate!!,
                    species,
                    status,
                    gender
                )
            }
        }

        binding.selectAdmissionDateButton.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Выберите дату")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.show(parentFragmentManager, "tag");

            datePicker.addOnPositiveButtonClickListener {
                binding.admissionDateText.text = convertLongToDateString(datePicker.selection!!)
                admissionDate = datePicker.selection
            }
        }

        addEditAnimalViewModel.saveSuccess.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.save_data_success),
                    Snackbar.LENGTH_SHORT
                ).show()
                addEditAnimalViewModel.doneShowingSnackBar()
            }
        })

        addEditAnimalViewModel.navigateToHome.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController()
                    .navigate(AddEditAnimalFragmentDirections.actionAddEditAnimalFragmentToHomeShelterFragment())
                addEditAnimalViewModel.doneNavigating()
            }
        })

        val speciesAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.species,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.speciesSpinner.adapter = adapter
            binding.speciesSpinner.onItemSelectedListener = this
        }

        val genderAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genders,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.genderSpinner.adapter = adapter
            binding.genderSpinner.onItemSelectedListener = this
        }

        val statusAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.animal_status,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.statusSpinner.adapter = adapter
            binding.statusSpinner.onItemSelectedListener = this
        }

        addEditAnimalViewModel.updateSpinners.observe(viewLifecycleOwner, {
            if (it == true) {
                val speciesPosition = speciesAdapter.getPosition(addEditAnimalViewModel.species)
                binding.speciesSpinner.setSelection(speciesPosition)

                val genderPosition = genderAdapter.getPosition(addEditAnimalViewModel.gender)
                binding.genderSpinner.setSelection(genderPosition)

                val statusPosition = statusAdapter.getPosition(addEditAnimalViewModel.status)
                binding.statusSpinner.setSelection(statusPosition)

                admissionDate = addEditAnimalViewModel.admissionDate

                addEditAnimalViewModel.doneUpdating()
            }
        })

        binding.bottomNavigation.selectedItemId = R.id.add_animal

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.applications -> {
                    view.findNavController()
                        .navigate(AddEditAnimalFragmentDirections.actionAddEditAnimalFragmentToAdoptionApplicationsFragment())
                    true
                }
                R.id.report -> {
                    view.findNavController()
                        .navigate(AddEditAnimalFragmentDirections.actionAddEditAnimalFragmentToReportFragment())
                    true
                }
                R.id.home -> {
                    view.findNavController()
                        .navigate(AddEditAnimalFragmentDirections.actionAddEditAnimalFragmentToHomeShelterFragment())
                    true
                }
                else -> false
            }
        }

        binding.addAnimalPhotoText.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }

        return view
    }

    private fun setProfilePic() {
        if (addEditAnimalViewModel.profilePic != null) {
            binding.animalPhotoImage.setImageBitmap(addEditAnimalViewModel.profilePic)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when (parent.id) {
            binding.speciesSpinner.id -> species = parent.getItemAtPosition(pos).toString()
            binding.genderSpinner.id -> gender = parent.getItemAtPosition(pos).toString()
            binding.statusSpinner.id -> status = parent.getItemAtPosition(pos).toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        when (parent?.id) {
            binding.speciesSpinner.id -> species = parent.getItemAtPosition(0).toString()
            binding.genderSpinner.id -> gender = parent.getItemAtPosition(0).toString()
            binding.statusSpinner.id -> status = parent.getItemAtPosition(0).toString()
        }
    }

    // TODO: add regex
    private fun checkInput(): Boolean {
        if (addEditAnimalViewModel.profilePic == null) {
            Toast.makeText(activity, "Добавьте фото", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.petNameEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите кличку", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.ageEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите возраст", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.breedEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(
                activity,
                "Введите породу. Если порода неизвестна либо отсутвует, впишите вариант \"Беспородный(-ая)\"",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        if (binding.descriptionEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите описание", Toast.LENGTH_LONG).show()
            return false
        }
        if (admissionDate == null && arguments.animalId == 0L) {
            Toast.makeText(activity, "Введите дату", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                try {
                    val inputStream = activity?.contentResolver?.openInputStream(data?.data!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.animalPhotoImage.setImageBitmap(bitmap)
                    addEditAnimalViewModel.onAttachPic(bitmap)
                } catch (e: Exception) {
                }
            }
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

    private fun setupTheme() {
        (requireActivity() as MainActivity).setTheme(R.style.MainTheme)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}