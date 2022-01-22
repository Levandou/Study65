package com.velagissellint.a65.all.containersDi

import com.velagissellint.a65.all.presentation.contactDetails.ContactDetailsFragment

interface ContactDetailsContainer {
    fun inject(contactDetailsFragment: ContactDetailsFragment)
}