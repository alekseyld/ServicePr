package com.alekseyld.servicepr;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.concurrent.TimeUnit;

/**
 * Created by Alekseyld on 30.06.2016.
 */
public class DataService extends IntentService {
    private final String LOG_TAG = "ServiceLog";
    public static boolean isRunning = false;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * DataService Used to name the worker thread, important only for debugging.
     */
    public DataService() {
        super("DataService");
    }

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
        isRunning = true;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
        isRunning = false;
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "onHandleIntent");

        //mock data
//        putToDataBase(
//                getData(intent.getStringExtra("name"),
//                        intent.getIntExtra("number", 0)));
        startForeground(1, getNotif("Getting data..."));
        int time = 20;

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        doIt("First", 1, time, v);
        doIt("Second", 2, time, v);
        doIt("Third", 3, time, v);
        doIt("Fourth", 4, time, v);

        v.vibrate(500);
        NotificationManager n = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = getNotif("Data is get");
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        n.notify("end", 5, notification);

        stopSelf();
    }

    private void doIt(String s, int i, int time, Vibrator v){
        pause(time);
        putToDataBase(getData(s, i));
        v.vibrate(200);
    }

    //get data from server
    private WrapData getData(String s, int i) {
        return new WrapData(s, i);
    }

    // put data to database
    private void putToDataBase(WrapData data){
        SQLite.insert(DataTable.class)
                .columns(DataTable_Table.name, DataTable_Table.number)
                .values(data.getName(), data.getNumber())
                .execute();
    }

    //do a pause
    private void pause(int p){
        try {
            TimeUnit.SECONDS.sleep(p);
        } catch (InterruptedException e) {e.printStackTrace();}
    }

    private Notification getNotif(String s) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(s)
                        .setContentText("Data");
//                        .setContentText("name = "+intent.getStringExtra("name")+"; "+
//                                        "number = "+intent.getIntExtra("number", 0));
        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(1, mBuilder.build());
        return mBuilder.build();
    }

}
