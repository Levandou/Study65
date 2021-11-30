package com.velagissellint.a65.presentation.contactDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.velagissellint.a65.data.ContactsRepository
import com.velagissellint.a65.domain.DetailedInformationAboutContact
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactDetailsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
) : ViewModel() {
    private val mutableContact = MutableLiveData<DetailedInformationAboutContact?>()
    var contact = mutableContact as LiveData<DetailedInformationAboutContact?>

    fun getContact(id: Int?) {
        val detailedInformationAboutContact = id?.let { contactsRepository.getContact(it) }
        mutableContact.value = detailedInformationAboutContact
    }
}