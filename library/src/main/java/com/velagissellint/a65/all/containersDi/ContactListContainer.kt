package com.velagissellint.a65.all.containersDi

import com.velagissellint.a65.all.presentation.contactList.ContactListFragment

interface ContactListContainer {
    fun inject(contactListFragment: ContactListFragment)
}
