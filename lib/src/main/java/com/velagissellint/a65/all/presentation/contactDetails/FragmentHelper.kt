package com.velagissellint.a65.all.presentation.contactDetails

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.velagissellint.a65.all.data.BroadcastReceiverForNotify
import com.velagissellint.a65.geoPOJO.featureMember.geocoderMetaData.GeocoderMetaData

class FragmentHelper {
    fun setColorToMarker(marker: Marker, color: Float) {
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(color))
    }

    fun convertStringToLAtLng(geocoderMetaData: GeocoderMetaData): LatLng {
        return geocoderMetaData.location.let { newDouble ->
            LatLng(
                newDouble.substringAfter(',').toDouble(),
                newDouble.substringBefore(',').toDouble()
            )
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun isAlarmSet(context: Context, id: Int?): Boolean {
        val intent = Intent(context, BroadcastReceiverForNotify::class.java)

        val alarmIntent = id?.let {
            PendingIntent.getBroadcast(
                context,
                it,
                intent,
                PendingIntent.FLAG_NO_CREATE
            )
        }
        return alarmIntent != null
    }

    fun latLngToString(latLng: LatLng): String =
        latLng.latitude.toString() + "," + latLng.longitude.toString()
}
