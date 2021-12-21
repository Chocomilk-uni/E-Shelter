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

class AnimalSearchResultAdapter : ListAdapter<Animal, AnimalSearchResultAdapter.ViewHolder>(
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
                val database = App.database.eShelterDatabaseDao
                val firebaseAuth = App.firebaseAuth
                val currentUser = firebaseAuth.user

                val currentFavouritesEntry = database.getFavouritesEntry(currentUser!!.uid, item.id)

                if (currentFavouritesEntry == null) {
                    val newFavouritesEntry = Favourites(userUid = currentUser.uid, animalId = item.id)
                    database.insert(newFavouritesEntry)
                    val favouritesEntry = database.getLastFavouritesEntry()

                    App.firebaseDatabase.favouritesFirebase.sendFavouritesEntry(favouritesEntry!!)

                    updateAddToFavButton(item)
                    Snackbar.make(
                        binding.root,
                        "Запись была добавлена в \"Избранное\"",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                else {
                    database.delete(currentFavouritesEntry)
                    App.firebaseDatabase.favouritesFirebase.deleteFavouritesEntry(currentFavouritesEntry)

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
            val database = App.database.eShelterDatabaseDao
            val firebaseAuth = App.firebaseAuth
            val currentUser = firebaseAuth.user

            val favouritesEntry = database.getFavouritesEntry(currentUser!!.uid, item.id)

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