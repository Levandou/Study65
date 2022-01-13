package com.example.libraryy.all.presentation.contactList

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.DetailedInformationAboutContact

class DetailedInformationDiffCallback : DiffUtil.ItemCallback<DetailedInformationAboutContact>() {
    override fun areItemsTheSame(
        oldItem: DetailedInformationAboutContact,
        newItem: DetailedInformationAboutContact
    ): Boolean = oldItem.fullName == newItem.fullName

    override fun areContentsTheSame(
        oldItem: DetailedInformationAboutContact,
        newItem: DetailedInformationAboutContact
    ): Boolean = oldItem == newItem
}