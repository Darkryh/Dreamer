package com.ead.project.dreamer.app.data.notifications

import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.ead.project.dreamer.R
import com.ead.project.dreamer.app.data.util.system.loadImage
import com.ead.project.dreamer.app.data.util.system.notificationBuilder
import com.ead.project.dreamer.app.data.util.system.notificationManager
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

class NotificationManager @Inject constructor(private val context: Context) {

    private val marketingNotificationBuilder by lazy {
        context.notificationBuilder(NotificationChannels.CHANNEL_MARKETING) {
            setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            setAutoCancel(true)
            setOnlyAlertOnce(true)
        }
    }

    private val marketingSummaryBuilder by lazy {
        context.notificationBuilder(NotificationChannels.CHANNEL_MARKETING) {
            setContentTitle(context.getString(R.string.marketing))
            setSmallIcon(R.drawable.ic_play)
            setGroup(GROUP_SUMMARY_MARKETING)
            setGroupSummary(true)
        }
    }

    private fun NotificationCompat.Builder.show(id : Int) {
        context.notificationManager.notify(id,build())
    }

    fun cancelNotification(id: Int) {
        context.notificationManager.cancel(id)
    }

    fun onMarketing(notification : RemoteMessage.Notification) {
        with(marketingNotificationBuilder) {
            clearActions()
            setContentTitle(notification.title)
            setContentText(notification.body)

            if (notification.imageUrl != null) {
                loadImage(notification.imageUrl.toString())
            }
            show(Notifications.MARKETING_ID)
        }
    }

    fun createGroupMarketingSummaryNotification() {
        marketingSummaryBuilder.show(MARKETING_ID)
    }

    companion object {

        const val ALL = 2
        const val FAVORITES = 1
        const val NONE = 0

        private const val GROUP_SUMMARY_MARKETING = "GROUP_SUMMARY_MARKETING"
        private const val MARKETING_ID = 1500

        private const val CHANNEL_SETTINGS_ID = 1500

        fun showSettingNotification(notifier : NotificationManager, context: Context) {
            /*val notification = notifier.create(
                title = context.getString(R.string.first_time_notification_title),
                content = context.getString(R.string.first_time_notification_description),
                idDrawable = R.drawable.ic_launcher_foreground,
                channelKey = HomeWorker.CHANNEL_APP_KEY_SERIES,
                imageUrl = AppInfo.LOGO_URL
            ).apply {
                setOnlyAlertOnce(true)
                addAction(0,"Todos",notifier.getPendingIntent(context, NotificationReceiver.PREFERENCE_ACTIVATION_ALL))
                addAction(0,"Solo favoritos",notifier.getPendingIntent(context, NotificationReceiver.PREFERENCE_ACTIVATION_FAVORITES))
                addAction(0,"Ninguno",notifier.getPendingIntent(context, NotificationReceiver.PREFERENCE_DEACTIVATION))
            }
            notifier.notify(CHANNEL_SETTINGS_ID,notification.build())*/
            //Constants.disableFirstTimeShowingNotifications()
        }
    }
}