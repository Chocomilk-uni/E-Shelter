package com.example.e_shelter.screens.shelteradmin.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.e_shelter.R
import com.example.e_shelter.databinding.FragmentSignUpShelterAdminBinding
import com.google.android.material.snackbar.Snackbar

class SignUpShelterAdminFragment : Fragment() {
    private var _binding: FragmentSignUpShelterAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpShelterAdminBinding.inflate(inflater, container, false)

        val signUpViewModel =
            ViewModelProvider(this).get(SignUpShelterAdminViewModel::class.java)

        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.sendApplicationButton.setOnClickListener {
            val city = binding.cityEdit.text.toString()
            val address = binding.addressEdit.text.toString()
            val name = binding.shelterNameEdit.text.toString()
            val socialLinks = binding.socialLinksEdit.text.toString()
            val email = binding.emailEdit.text.toString()

            if (checkInput()) {
                signUpViewModel.onSendApplication(city, address, name, socialLinks, email)
            }
        }

        signUpViewModel.applySuccess.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.shelter_admin_apply_success),
                    Snackbar.LENGTH_LONG
                ).show()
                signUpViewModel.doneShowingSnackBar()
            }
        })

        signUpViewModel.navigateToSignIn.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController()
                    .navigate(SignUpShelterAdminFragmentDirections.actionSignUpShelterAdminFragmentToSignInUserFragment())
                signUpViewModel.doneNavigating()
            }
        })

        return binding.root
    }

    // TODO: add regex
    private fun checkInput(): Boolean {
        if (binding.cityEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите город", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.addressEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите адрес", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.shelterNameEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите название приюта", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.emailEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите email", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}