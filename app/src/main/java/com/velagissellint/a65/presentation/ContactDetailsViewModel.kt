package com.velagissellint.a65.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.velagissellint.a65.data.ContactsRepository
import com.velagissellint.a65.domain.DetailedInformationAboutContact
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactDetailsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
): ViewModel() {
    private val mutableContact  = MutableLiveData<DetailedInformationAboutContact>()
    val contact = mutableContact as LiveData<DetailedInformationAboutContact>

    private fun getContact(){
        val detailedInformationAboutContact=contactsRepository.getContact(1)
        mutableContact.value= detailedInformationAboutContact
    }

    init {
     getContact()
    }
}