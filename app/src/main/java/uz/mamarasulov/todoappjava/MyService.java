package uz.mamarasulov.todoappjava;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

/**
 * Created by Zayniddin on Sep 09 - 17:54
 */
public class MyService extends JobIntentService {

    public MyService() {
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, 10000, pendingIntent);

    }
}
