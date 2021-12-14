package com.velagissellint.a65.presentation.contactList

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.velagissellint.a65.R
import com.velagissellint.a65.data.ContactsRepository
import com.velagissellint.a65.domain.DetailedInformationAboutContact
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.*
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val mutableContactList = MutableLiveData<List<DetailedInformationAboutContact>>()
    var contactList = mutableContactList as LiveData<List<DetailedInformationAboutContact>>
    private val isLoading = MutableLiveData<Boolean>()
    var isLoadingPublic = isLoading as LiveData<Boolean>
    private val mutableContactListFilter = MutableLiveData<List<DetailedInformationAboutContact>>()
    var contactListFilter =
        mutableContactListFilter as LiveData<List<DetailedInformationAboutContact>>

    private fun getContacts() {
        contactsRepository.getContactsSingle()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { isLoading.postValue(true) }
            .doFinally { isLoading.postValue(false) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mutableContactList.value = it
            }, {
                Log.d("ERRORO", it.message.toString())
            })
            .addTo(disposable)
    }

    fun filter(text: String, requireContext: Context) {
        Observable.fromIterable(mutableContactList.value)
            .delay(1000, TimeUnit.MILLISECONDS)
            .filter { detail: DetailedInformationAboutContact? ->
                (detail?.fullName?.toLowerCase()?.contains(text.toLowerCase()) == true)
            }
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mutableContactListFilter.value = it
            }, {
                Log.d("ERRORO", it.message.toString())
            })
            .addTo(disposable)
        if (mutableContactList.value?.isEmpty() == true)
            Toast.makeText(
                requireContext,
                requireContext.getString(R.string.contact_not_found),
                Toast.LENGTH_SHORT
            ).show()
    }

    init {
        getContacts()
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}