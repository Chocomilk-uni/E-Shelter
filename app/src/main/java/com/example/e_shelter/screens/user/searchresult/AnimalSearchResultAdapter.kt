package com.example.e_shelter.screens.user.searchresult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_shelter.App
import com.example.e_shelter.R
import com.example.e_shelter.database.entities.Animal
import com.example.e_shelter.database.entities.Favourites
import com.example.e_shelter.databinding.ListItemAnimalSearchResultUserBinding
import com.example.e_shelter.formatAge
import com.example.e_shelter.loadImageFromStorage
import com.google.android.material.snackbar.Snackbar

class AnimalSearchResultAdapter() : ListAdapter<Animal, AnimalSearchResultAdapter.ViewHolder>(
    AnimalDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemAnimalSearchResultUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Animal
        ) {
            updateAddToFavButton(item)

            binding.nameText.text = item.name
            binding.ageText.text = formatAge(item)
            binding.descriptionText.text = item.description
            binding.animalImage.setImageBitmap(loadImageFromStorage(item.profilePicPath!!))

            binding.addToFavouritesButton.setOnClickListener {
                val database = App.database

                val currentFavouritesEntry = App.database.eShelterDatabaseDao.getFavouritesEntry(App.userId, item.id)

                if (currentFavouritesEntry == null) {
                    val newFavouritesEntry = Favourites(userId = App.userId, animalId = item.id)
                    database.eShelterDatabaseDao.insert(newFavouritesEntry)
                    updateAddToFavButton(item)
                    Snackbar.make(
                        binding.root,
                        "Запись была добавлена в \"Избранное\"",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                else {
                    database.eShelterDatabaseDao.delete(currentFavouritesEntry)
                    updateAddToFavButton(item)
                    Snackbar.make(
                        binding.root,
                        "Запись была удалена из \"Избранного\"",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            binding.animalImage.setOnClickListener {
                val animalId = item.id
                it.findNavController()
                    .navigate(
                        SearchResultFragmentDirections.actionSearchResultFragmentToAnimalProfileFragment(
                            animalId
                        )
                    )
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemAnimalSearchResultUserBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

        private fun updateAddToFavButton(item: Animal) {
            val favouritesEntry = App.database.eShelterDatabaseDao.getFavouritesEntry(App.userId, item.id)

            if (favouritesEntry != null) binding.addToFavouritesButton.setImageDrawable(
                AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.button_favourites_added
                )
            )
            else binding.addToFavouritesButton.setImageDrawable(
                AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.button_add_to_favourites
                )
            )
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