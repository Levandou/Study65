package com.velagissellint.a65.all.presentation.contactDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.velagissellint.a65.DetailedInformationAboutContact
import com.velagissellint.a65.directionPOJO.ResultDirection
import com.velagissellint.a65.geoPOJO.featureMember.geocoderMetaData.GeocoderMetaData
import com.velagissellint.a65.useCase.broadcast.OffReminderUseCase
import com.velagissellint.a65.useCase.broadcast.OnReminderUseCase
import com.velagissellint.a65.useCase.contactDetails.GetContactDetailsUseCase
import com.velagissellint.a65.useCase.db.GetFromDbUseCase
import com.velagissellint.a65.useCase.geo.GetDirectionUseCase
import com.velagissellint.a65.useCase.geo.GetGeoUseCase
import com.velagissellint.a655.BuildConfig
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ContactDetailsViewModel @Inject constructor(
    private val getContactDetailsUseCase: GetContactDetailsUseCase,
    private val offReminderUseCase: OffReminderUseCase,
    private val onReminderUseCase: OnReminderUseCase,
    private val geoUseCase: GetGeoUseCase,
    private val getFromDbUseCase: GetFromDbUseCase,
    private val getDirectionUseCase: GetDirectionUseCase
) : ViewModel() {
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val mutableContact = MutableLiveData<DetailedInformationAboutContact?>()
    val contact = mutableContact as LiveData<DetailedInformationAboutContact?>
    private val isLoading = MutableLiveData<Boolean>()
    val isLoadingPublic = isLoading as LiveData<Boolean>
    private val geoCoderMetaDataMutableLiveData = MutableLiveData<GeocoderMetaData>()
    val geoCoderMetaDataLiveData = geoCoderMetaDataMutableLiveData as LiveData<GeocoderMetaData>
    private val mutableGeoFromDb = MutableLiveData<GeocoderMetaData>()
    val geoFromDb = mutableGeoFromDb as LiveData<GeocoderMetaData>
    private val mutableListGeoFromDb = MutableLiveData<List<GeocoderMetaData>>()
    val listGeoFromDb = mutableListGeoFromDb as LiveData<List<GeocoderMetaData>>
    private val resultDirectionMutableLiveData = MutableLiveData<ResultDirection>()
    val resultDirectionLiveData = resultDirectionMutableLiveData as LiveData<ResultDirection>
    private var url: String = "https://maps.googleapis.com/maps/api/directions/json?"
    private var origin: String = "origin="
    private var destination: String = "&destination="
    private var apiKey: String = "&key="
    var isAllTouch = false
    var isModeDirection = false
    var howMarkersClickInModeDirections = 0

    fun loadDirection(point1: String, point2: String) {
        val finalUrl = url + origin + point1 + destination + point2 + apiKey
        getDirectionUseCase.getDirection(finalUrl + BuildConfig.API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resultDirectionMutableLiveData.value = it
            }, {
                Log.d("ERROR", it.message.toString())
            })
    }

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
                    mutableGeoFromDb.value = getFromDbUseCase.getFromDb(it.phoneNumber)
                    mutableListGeoFromDb.value = getFromDbUseCase.getAllFromDb(it.phoneNumber)
                }, {
                    Log.d("ERRORO", it.message.toString())
                })
                ?.addTo(disposable)
        }
    }

    fun loadGeo(geo: String) {
        geoUseCase.getGeo(geo = geo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val geocoderMetaData: GeocoderMetaData =
                    it.response?.geoObjectCollection?.featureMember?.get(0)?.geoObject
                        ?.metaDataProperty?.geocoderMetaData!!
                geocoderMetaData.idOfContact = mutableContact.value!!.phoneNumber
                geocoderMetaData.location = geo
                getFromDbUseCase.addToDb(geocoderMetaData)
                geoCoderMetaDataMutableLiveData.value = geocoderMetaData
            }, {
                Log.d("geoCoder", it.message.toString())
            })
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
