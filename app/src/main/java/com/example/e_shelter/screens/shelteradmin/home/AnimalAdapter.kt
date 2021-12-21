package com.example.e_shelter.screens.shelteradmin.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_shelter.App
import com.example.e_shelter.R
import com.example.e_shelter.database.entities.Animal
import com.example.e_shelter.databinding.ListItemAnimalShelterAdminBinding
import com.example.e_shelter.formatAge
import com.example.e_shelter.loadImageFromStorage
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AnimalAdapter : ListAdapter<Animal, AnimalAdapter.ViewHolder>(AnimalDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemAnimalShelterAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Animal
        ) {
            binding.nameText.text = item.name
            binding.ageText.text = formatAge(item)
            binding.statusText.text = item.status
            binding.animalImage.setImageBitmap(loadImageFromStorage(item.profilePicPath!!))

            binding.editAnimalButton.setOnClickListener {
                val animalId = item.id
                it.findNavController()
                    .navigate(
                        HomeShelterFragmentDirections.actionHomeShelterFragmentToAddEditAnimalFragment(
                            animalId
                        )
                    )
            }

            binding.deleteButton.setOnClickListener {
                MaterialAlertDialogBuilder(it.context)
                    .setMessage(R.string.delete_confirmation)
                    .setNegativeButton(R.string.no) { dialog, which ->

                    }
                    .setPositiveButton(R.string.yes) { dialog, which ->
                        App.firebaseDatabase.animalFirebase.deleteAnimal(item)
                        App.database.eShelterDatabaseDao.delete(item)
                    }
                    .show()
            }

            binding.animalImage.setOnClickListener {
                val animalId = item.id
                it.findNavController()
                    .navigate(
                        HomeShelterFragmentDirections.actionHomeShelterFragmentToAnimalProfileFragment(
                            animalId
                        )
                    )
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemAnimalShelterAdminBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }


    class AnimalDiffCallback :
        DiffUtil.ItemCallback<Animal>() {

        override fun areItemsTheSame(oldItem: Animal, newItem: Animal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Animal, newItem: Animal): Boolean {
            return oldItem == newItem
        }
    }
}