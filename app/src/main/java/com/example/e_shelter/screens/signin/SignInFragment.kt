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
import com.example.e_shelter.App
import com.example.e_shelter.R
import com.example.e_shelter.database.entities.Shelter
import com.example.e_shelter.database.entities.User
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

        binding.logoText.setOnClickListener {
            val newShelter = Shelter(name = "Два пса",
                city = "Москва", address = "ул. Ленина, д. 40", phoneNumber = "89510981264")
            App.database.eShelterDatabaseDao.insert(newShelter)
            val currentShelter = App.database.eShelterDatabaseDao.getLastShelter()
            if (currentShelter != null) {
                App.firebaseDatabase.shelterFirebase.sendShelter(currentShelter)
            }

            val newUser = User(uid = "2WrpM2OHNTOMCVEKW2Gd69HWen53", email = "admin@gmail.com", password = "qwerty", shelterId = currentShelter!!.id)
            App.database.eShelterDatabaseDao.insert(newUser)
            App.firebaseDatabase.userFirebase.sendUser(newUser)
        }

        binding.signInButton.setOnClickListener {
            val login = binding.loginEdit.text.toString()
            val password = binding.passwordEdit.text.toString()

            if (checkInput()) {
                signInViewModel.onSignIn(login, password)
            }
        }

        signInViewModel.loginNotExist.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.login_not_exists),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            signInViewModel.doneShowingSnackBar()
        })

        signInViewModel.firebaseAuth.signInError.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.wrong_password),
                    Snackbar.LENGTH_SHORT
                ).show()
                signInViewModel.firebaseAuth.doneShowingSnackBar()
            }
        })

        signInViewModel.firebaseAuth.navigateToHomeUser.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController()
                    .navigate(SignInFragmentDirections.actionSignInUserFragmentToHomeUserFragment())
                signInViewModel.doneNavigating()
                signInViewModel.firebaseAuth.doneNavigating()
            }
        })

        signInViewModel.firebaseAuth.navigateToHomeShelter.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController()
                    .navigate(SignInFragmentDirections.actionSignInUserFragmentToHomeShelterFragment())
                signInViewModel.doneNavigating()
                signInViewModel.firebaseAuth.doneNavigating()
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