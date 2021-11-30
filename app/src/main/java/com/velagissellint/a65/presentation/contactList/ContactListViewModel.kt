package com.velagissellint.a65.presentation.contactList

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.velagissellint.a65.R
import com.velagissellint.a65.data.ContactsRepository
import com.velagissellint.a65.domain.DetailedInformationAboutContact
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {
    private val mutableContactList = MutableLiveData<List<DetailedInformationAboutContact>>()
    var contactList = mutableContactList as LiveData<List<DetailedInformationAboutContact>>

    private fun getContacts() {
        val listDetailedInformationAboutContact = contactsRepository.getContacts()
        mutableContactList.value = listDetailedInformationAboutContact
    }

    init {
        getContacts()
    }

     fun filter(text: String,adapter:ContactListAdapter,requireContext:Context) {
        val filteredlist: MutableList<DetailedInformationAboutContact> = mutableListOf()
        for (item in contactList.value!!) {
            if (item.fullName.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(requireContext, requireContext.getString(R.string.contact_not_found), Toast.LENGTH_SHORT).show()
        } else {
            adapter.submitList(filteredlist)
        }
    }
}