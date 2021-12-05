package com.velagissellint.a65.data

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.velagissellint.a65.ContactDetailsFragment
import com.velagissellint.a65.R
import com.velagissellint.a65.putNextBirthday
import java.util.Calendar

class BroadcastReceiverForNotify : BroadcastReceiver() {
    @SuppressLint("StringFormatInvalid")
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                context.getString(R.string.id_for_channel),
                context.getString(R.string.name_for_channel),
                importance
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val id = intent.getIntExtra(CONTACT_ID, 0)
        val resultIntent = Intent(context, ContactDetailsFragment::class.java)
        val resultPendingIntent =
            PendingIntent.getActivity(context,
                id,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val builder =
            NotificationCompat.Builder(context, context.getString(R.string.id_for_channel))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(context.getString(R.string.title_for_notify))
                .setContentText(
                    context.getString(
                        R.string.content_text,
                        intent.getStringExtra(FULL_NAME)
                    )
                )
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)

        NotificationManagerCompat.from(context).notify(id, builder.build())

        val alarmIntent =
            PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.set(
            AlarmManager.RTC_WAKEUP,
            putNextBirthday(intent.getSerializableExtra(CONTACT_BIRTHDAY) as Calendar),
            alarmIntent
        )
    }

    companion object {
        const val CONTACT_ID = "id"
        const val CONTACT_BIRTHDAY = "contactBirthday"
        const val FULL_NAME = "FULL_NAME"
    }
}