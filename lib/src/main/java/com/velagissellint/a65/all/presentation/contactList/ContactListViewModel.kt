package com.velagissellint.a65.all.presentation.contactList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.velagissellint.a65.DetailedInformationAboutContact
import com.velagissellint.a65.all.data.ContactsRepository
import com.velagissellint.a65.useCase.contactListCase.GetContactListUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ContactListViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
    private val getContactListUseCase: GetContactListUseCase
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

        getContactListUseCase.getContacts()?.let { single ->
            single
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
    }

    fun filter(observable: Observable<String>?) {
        observable?.let {
            it
                .debounce(DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .map { name ->
                    contactsRepository.filter(
                        name,
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

    companion object {
        const val DEBOUNCE_TIME = 1000.toLong()
    }
}
