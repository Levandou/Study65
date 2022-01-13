package com.example.libraryy.all.containersDi

import com.example.libraryy.all.presentation.contactDetails.ContactDetailsFragment

interface ContactDetailsContainer {
    fun inject(contactDetailsFragment: ContactDetailsFragment)
}