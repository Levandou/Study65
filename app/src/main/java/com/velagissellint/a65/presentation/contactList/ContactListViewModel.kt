package com.velagissellint.a65.presentation.contactList

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.velagissellint.a65.data.ContactsRepository
import com.velagissellint.a65.domain.DetailedInformationAboutContact
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class ContactListViewModelFactory @Inject constructor(private val myViewModelProvider: Provider<ContactListViewModel>) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return myViewModelProvider.get() as T
    }
}

class ContactListViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val mutableContactList = MutableLiveData<List<DetailedInformationAboutContact>>()
    val contactList = mutableContactList as LiveData<List<DetailedInformationAboutContact>>
    private val isLoading = MutableLiveData<Boolean>()
    val isLoadingPublic = isLoading as LiveData<Boolean>
    private val mutableContactListFilter = MutableLiveData<List<DetailedInformationAboutContact>>()
    val contactListFilter =
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

    fun filter(requireContext: Context, observable: @NonNull Observable<String?>?) {
        observable?.let {
            it
                .debounce(1000, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .map { name ->
                    contactsRepository.filter(
                        name,
                        requireContext,
                        mutableContactList.value
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableContactListFilter.value = it
                }, {
                    Log.d("ERRORO", it.message.toString())
                })
                .addTo(disposable)
        }
    }

    init {
        getContacts()
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()

    }
}