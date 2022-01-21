package com.velagissellint.a65.all.presentation.contactDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.velagissellint.a65.DetailedInformationAboutContact
import com.velagissellint.a65.useCase.broadcast.OffReminderUseCase
import com.velagissellint.a65.useCase.broadcast.OnReminderUseCase
import com.velagissellint.a65.useCase.contactDetails.GetContactDetailsUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ContactDetailsViewModel @Inject constructor(
    private val getContactDetailsUseCase: GetContactDetailsUseCase,
    private val offReminderUseCase: OffReminderUseCase,
    private val onReminderUseCase: OnReminderUseCase
) : ViewModel() {
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val mutableContact = MutableLiveData<DetailedInformationAboutContact?>()
    val contact = mutableContact as LiveData<DetailedInformationAboutContact?>
    private val isLoading = MutableLiveData<Boolean>()
    val isLoadingPublic = isLoading as LiveData<Boolean>
    var isAlarmSet = false
    fun getContact(id: Int?) {

        id?.let { idAfterLet ->
            getContactDetailsUseCase.idOfContact(idAfterLet)
            getContactDetailsUseCase.getContact()
                ?.subscribeOn(Schedulers.io())
                ?.doOnSubscribe { isLoading.postValue(true) }
                ?.doFinally { isLoading.postValue(false) }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    mutableContact.value = it
                }, {
                    Log.d("ERRORO", it.message.toString())
                })
                ?.addTo(disposable)
        }
    }

    fun offReciver(id: Int) {
        isAlarmSet = false
        offReminderUseCase.offReminder(id)
    }

    fun onReciver(id: Int, detailedInformationAboutContact: DetailedInformationAboutContact) {
        isAlarmSet = true
        onReminderUseCase.onReminder(id, detailedInformationAboutContact)
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}
