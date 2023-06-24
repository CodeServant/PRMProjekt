package com.example.prmprojekt

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Notificatons {

    companion object{
        val CHANNEL_ID = "channelId"
        var notifiactionId = 0

        /**
         * Creates notification channel and registers it.
         */
        fun createNotificationChannel(ctx: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name: CharSequence = ctx.getString(R.string.channel_name)
                val description: String = ctx.getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance)
                channel.description = description
                val notificationManager: NotificationManager =
                    ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

        }

        fun createNotification(ctx: Context) {
            val CHANNEL_ID = CHANNEL_ID
            val builder = NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle(ctx.getString(R.string.notify_add_movie))
                .setContentText(ctx.getString(R.string.notify_add_movie_text))
                .setSmallIcon(R.drawable.notofication_image)


            with(NotificationManagerCompat.from(ctx)) {
                if (ActivityCompat.checkSelfPermission(
                        ctx,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notify(notifiactionId, builder.build())
                }
            }
        }
     }

}