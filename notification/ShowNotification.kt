package com.example.newsfy_rework.notification

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.newsfy_rework.Main
import com.example.newsfy_rework.R

class ShowNotification : BroadcastReceiver() {
    var apiKey = "4ec1ecc7d5c3473da67da5e7c740fd8f"
    var url = "http://newsapi.org/v2/everything?q=general&apiKey=$apiKey"

    private fun myNotification(context: Context){
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, Main::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val notificationBuilder = NotificationCompat.Builder(context)
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentTitle("News you cannot miss")
            .setContentText("Here are some new stories you can check! Click on notification to check news!")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher_round)

        val textStyle: NotificationCompat.BigTextStyle = NotificationCompat.BigTextStyle()
        textStyle.bigText("Here are some new stories you can check! Click on notification to check news!")
        textStyle.setBigContentTitle("News you cannot miss")
        notificationBuilder.setStyle(textStyle)

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onReceive(context: Context, intent: Intent) {
        myNotification(context)
    }
}