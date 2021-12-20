package com.example.e_shelter.screens.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.e_shelter.R
import com.example.e_shelter.databinding.FragmentSignInBinding
import com.google.android.material.snackbar.Snackbar


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        val signInViewModel =
            ViewModelProvider(this)[SignInViewModel::class.java]

        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.signInButton.setOnClickListener {
            val login = binding.loginEdit.text.toString()
            val password = binding.passwordEdit.text.toString()

            if (checkInput()) {
                signInViewModel.onSignIn(login, password)
            }

            signInViewModel.doneShowingSnackBar()
        }

        signInViewModel.loginNotExist.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.login_not_exists),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        signInViewModel.wrongPassword.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.wrong_password),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        signInViewModel.navigateToHomeUser.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController()
                    .navigate(SignInFragmentDirections.actionSignInUserFragmentToHomeUserFragment())
                signInViewModel.doneNavigating()
            }
        })

        signInViewModel.navigateToHomeShelter.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController()
                    .navigate(SignInFragmentDirections.actionSignInUserFragmentToHomeShelterFragment())
                signInViewModel.doneNavigating()
            }
        })

        binding.signUpButton.setOnClickListener { v: View ->
            v.findNavController()
                .navigate(SignInFragmentDirections.actionSignInUserFragmentToSignUpUserFragment())
        }

        binding.applyForShelterAdminButton.setOnClickListener { v: View ->
            v.findNavController()
                .navigate(SignInFragmentDirections.actionSignInUserFragmentToSignUpShelterAdminFragment())
        }

        return binding.root
    }


    // TODO: add regex
    private fun checkInput(): Boolean {
        if (binding.loginEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите логин", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.passwordEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите пароль", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}