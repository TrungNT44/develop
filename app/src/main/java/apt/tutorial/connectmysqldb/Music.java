package apt.tutorial.connectmysqldb;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Trung1994 on 05-Jun-17.
 */

public class Music extends Service {
    MediaPlayer mediaPlayer;
    int id;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("trung", "sang music dc k");
        String alarmMusic = intent.getExtras().getString("alarm");
        if (alarmMusic.equals("on")){
            id = 1;
        }
        else if (alarmMusic.equals("off")){
            id=0;
        }
        if ( id == 1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.siro);
            mediaPlayer.start();
            id = 0;
        }
        else if (id == 0){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
         return super.onStartCommand(intent, flags, startId);
    }
}
