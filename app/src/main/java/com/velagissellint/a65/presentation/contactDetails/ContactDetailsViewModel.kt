package com.velagissellint.a65.presentation.contactDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.velagissellint.a65.data.ContactsRepository
import com.velagissellint.a65.domain.DetailedInformationAboutContact
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

@HiltViewModel
class ContactDetailsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
) : ViewModel() {
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val mutableContact = MutableLiveData<DetailedInformationAboutContact?>()
    var contact = mutableContact as LiveData<DetailedInformationAboutContact?>
    private val isLoading = MutableLiveData<Boolean>()
    var isLoadingPublic=isLoading as LiveData<Boolean>

    fun getContact(id: Int?) {
        id?.let { idAfterLet ->
            contactsRepository.getContact(idAfterLet)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { isLoading.postValue(true) }
                .doFinally{isLoading.postValue(false)}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mutableContact.value = it
                }, {
                    Log.d("ERRORO", it.message.toString())
                })
                .addTo(disposable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}