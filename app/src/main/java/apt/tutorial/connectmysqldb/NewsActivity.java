package apt.tutorial.connectmysqldb;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsActivity extends Activity {

    Button btnAlarm,btnCancelAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        btnAlarm = (Button) findViewById(R.id.btnAlarm);
        btnCancelAlarm= (Button) findViewById(R.id.btnCancelAlarm);

        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSchedulerAlarm();
            }
        });
        btnCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSchedulerAlarm();
            }
        });
    }
    void startSchedulerAlarm() {
        int oneMinute = 1 * (5 * 1000); //Trigger Every 1 Minute
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("alarm", "on");
        intent.setAction(AlarmReceiver.alaramIntent);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, AlarmReceiver.alarmIntentCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), oneMinute, alarmIntent);

    }
    void stopSchedulerAlarm() {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("alarm", "off");
        sendBroadcast(intent);
        intent.setAction(AlarmReceiver.alaramIntent);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, AlarmReceiver.alarmIntentCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(alarmIntent);
    }
}
