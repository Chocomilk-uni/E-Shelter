package com.example.e_shelter.screens.user.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.e_shelter.R
import com.example.e_shelter.databinding.FragmentSignUpUserBinding
import com.example.e_shelter.isEmailValid
import com.example.e_shelter.isPasswordValid
import com.google.android.material.snackbar.Snackbar


class SignUpUserFragment : Fragment() {
    private var _binding: FragmentSignUpUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpUserBinding.inflate(inflater, container, false)

        val signUpViewModel =
            ViewModelProvider(this).get(SignUpUserViewModel::class.java)

        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.signUpButton.setOnClickListener {
            val name = binding.nameEdit.text.toString()
            val phoneNumber = binding.phoneEdit.text.toString()
            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()

            if (checkInput()) {
                signUpViewModel.onSignUp(name, phoneNumber, email, password)
            }
        }

        signUpViewModel.emailExistsError.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.email_exists),
                    Snackbar.LENGTH_SHORT
                ).show()
                signUpViewModel.doneShowingSnackBar()
            }
        })

        signUpViewModel.phoneExistsError.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.phone_exists),
                    Snackbar.LENGTH_SHORT
                ).show()
                signUpViewModel.doneShowingSnackBar()
            }
        })

        signUpViewModel.firebaseAuth.signUpSuccess.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.sign_up_success),
                    Snackbar.LENGTH_SHORT
                ).show()
                signUpViewModel.firebaseAuth.doneShowingSnackBar()
            }
        })

        signUpViewModel.firebaseAuth.signUpError.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.sign_up_error),
                    Snackbar.LENGTH_SHORT
                ).show()
                signUpViewModel.firebaseAuth.doneShowingSnackBar()
            }
        })

        signUpViewModel.firebaseAuth.navigateToSignIn.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController()
                    .navigate(SignUpUserFragmentDirections.actionSignUpUserFragmentToSignInUserFragment())
                signUpViewModel.firebaseAuth.doneNavigating()
            }
        })

        return binding.root
    }

    private fun checkInput(): Boolean {
        if (binding.nameEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите Имя", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.phoneEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите номер телефона", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.emailEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите email", Toast.LENGTH_LONG).show()
            return false
        }
        if (!isEmailValid(binding.emailEdit.text.toString().trim())) {
            Toast.makeText(activity, "Email введён некорректно", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.passwordEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(activity, "Введите пароль", Toast.LENGTH_LONG).show()
            return false
        }
        if (!isPasswordValid(binding.passwordEdit.text.toString().trim())) {
            Toast.makeText(activity, "Пароль должен состоять минимум из 6 символов и содержать только латинские символы и цифры", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}