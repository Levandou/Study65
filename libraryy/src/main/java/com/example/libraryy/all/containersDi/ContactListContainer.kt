package com.example.libraryy.all.containersDi

import com.example.libraryy.all.presentation.contactList.ContactListFragment

interface ContactListContainer {
    fun inject(contactListFragment: ContactListFragment)
}