package com.example.e_shelter.screens.shelteradmin.adoptionapplications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_shelter.App
import com.example.e_shelter.R
import com.example.e_shelter.convertLongToDateString
import com.example.e_shelter.database.entities.AdoptionApplication
import com.example.e_shelter.databinding.ListItemAdoptionApplicationBinding
import com.example.e_shelter.loadImageFromStorage
import com.google.android.material.snackbar.Snackbar

class AdoptionApplicationAdapter :
    ListAdapter<AdoptionApplication, AdoptionApplicationAdapter.ViewHolder>(
        ApplicationDiffCallback()
    ) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemAdoptionApplicationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: AdoptionApplication
        ) {
            binding.nameText.text = item.name
            binding.phoneNumberText.text = item.phoneNumber
            binding.dateText.text = convertLongToDateString(item.date)
            val animal = App.database.eShelterDatabaseDao.getAnimal(item.animalId)
            updateApplicationView(item)

            binding.animalImage.setImageBitmap(loadImageFromStorage(animal!!.profilePicPath!!))

            binding.processApplication.setOnClickListener {
                item.applicationStatus = "Обработана"
                App.database.eShelterDatabaseDao.update(item)

                Snackbar.make(
                    binding.root,
                    "Данные были сохранены",
                    Snackbar.LENGTH_SHORT
                ).show()

                updateApplicationView(item)
            }

            binding.animalImage.setOnClickListener {
                if (item.applicationStatus != "Обработана") {

                    val currentAnimal = App.database.eShelterDatabaseDao.getAnimal(item.animalId)
                    it.findNavController()
                        .navigate(
                            AdoptionApplicationsFragmentDirections.actionAdoptionApplicationsFragmentToAnimalProfileFragment(
                                currentAnimal!!.id
                            )
                        )
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemAdoptionApplicationBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

        private fun updateApplicationView(item: AdoptionApplication) {
            val applicationEntry =
                App.database.eShelterDatabaseDao.getApplication(item.id)

            if (applicationEntry != null) {
                if (applicationEntry.applicationStatus == "Обработана") {
                    binding.processApplication.isGone = true
                    binding.constraintLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.light_gray
                        )
                    )
                    binding.animalImage.isClickable = false
                    binding.animalImage.isFocusable = false
                    binding.line.isGone = false
                } else {
                    binding.processApplication.isGone = false
                    binding.constraintLayout.background = AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.item_background
                    )
                    binding.animalImage.isClickable = true
                    binding.animalImage.isFocusable = true
                    binding.line.isGone = true
                }
            }
        }
    }


    class ApplicationDiffCallback : DiffUtil.ItemCallback<AdoptionApplication>() {

        override fun areItemsTheSame(
            oldItem: AdoptionApplication,
            newItem: AdoptionApplication
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AdoptionApplication,
            newItem: AdoptionApplication
        ): Boolean {
            return oldItem == newItem
        }
    }
}