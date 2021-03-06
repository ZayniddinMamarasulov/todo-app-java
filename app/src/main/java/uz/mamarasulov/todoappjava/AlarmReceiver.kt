package uz.mamarasulov.todoappjava

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import uz.mamarasulov.todoappjava.ui.MainActivity
import uz.mamarasulov.todoappjava.util.AppConstants
import uz.mamarasulov.todoappjava.util.toastMessage
import kotlin.time.milliseconds

/**
 *  Created by Zayniddin on Sep 09 - 10:59
 */

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val mNotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val taskId = intent?.extras?.getString(AppConstants.TASK_ID)
        val taskTitle = intent?.extras?.getString(AppConstants.INTENT_TITLE)
        val taskTask = intent?.extras?.getString(AppConstants.INTENT_TASK)
//
        val rowId = taskId.toString()
        val notificationIntent = Intent(context, MainActivity::class.java)
//        notificationIntent.putExtra(Constant.ROW_ID, rowId)
//        notificationIntent.putExtra(Constant.NOTIFICATION, true)
//        notificationIntent.action = "EDIT"
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationFinishIntent = Intent(context, MainActivity::class.java)
//        notificationFinishIntent.putExtra(Constant.ROW_ID, rowId)
//        notificationFinishIntent.putExtra(Constant.NOTIFICATION, true)
//        notificationFinishIntent.action = "FINISH"
        val pendingFinishIntent = PendingIntent.getActivity(context, 0, notificationFinishIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val title = taskTitle//taskTitle//task title
        val message = taskTask//taskTask//task
        val icon = R.mipmap.ic_launcher
        val time = System.currentTimeMillis()

        val notification = NotificationCompat.Builder(context, "notifyMe")
                .setContentTitle(title)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setColor(Color.argb(225, 225, 87, 34))
                .setSmallIcon(icon)
                .setWhen(time)
//                .addAction(R.drawable.ic_edit_black_24dp, "Edit", pendingIntent)
//                .addAction(R.drawable.ic_check_black_24dp, "Finish", pendingFinishIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build()

        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        notification.defaults = notification.defaults or Notification.DEFAULT_SOUND
        notification.defaults = notification.defaults or Notification.DEFAULT_VIBRATE
        mNotificationManager.notify(Integer.parseInt(System.currentTimeMillis().toString().takeLast(4)/*rowId*/), notification)


        var action = intent?.action

        if (action == null) {
            action = intent?.action
        } else {
            if ("EDIT".equals(action)) {
                toastMessage(context, "EDIT")
            } else if ("FINISH".equals(action)) {
                toastMessage(context, "FINISH")
            }
        }
    }

}