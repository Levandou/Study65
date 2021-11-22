package com.velagissellint.a65.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.velagissellint.a65.data.ContactsRepository
import com.velagissellint.a65.domain.DetailedInformationAboutContact
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {
    private val mutableContactList  = MutableLiveData<List<DetailedInformationAboutContact>>()
    var contactList = mutableContactList as LiveData<List<DetailedInformationAboutContact>>

   private fun getContacts() {
        val listDetailedInformationAboutContact = contactsRepository.getContacts()
        mutableContactList.value = listDetailedInformationAboutContact
        contactList=mutableContactList
    }

    init {
        getContacts()
    }
}