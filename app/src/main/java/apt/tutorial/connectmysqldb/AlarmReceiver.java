package apt.tutorial.connectmysqldb;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung1994 on 31-May-17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final String alaramIntent = "apt.tutorial.connectmysqldb.alarmIntent";
    public static final int alarmIntentCode = 000054310;
    public static boolean flag = false;

    private String TAG = AlarmReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {

        new DocJSON().execute("http://192.168.1.103:80/trung/display.php");
        if(flag == true) {
            Log.d("trung","onReceive");
            Notification.Builder mBuilder =
                    new Notification.Builder(context)
                            .setSmallIcon(R.drawable.ball_green)
                            .setContentTitle("Nhà kính")
                            .setContentText("Nhiệt độ đang nằm ngoài giá trị thiết lập cảnh báo!")
                            .setAutoCancel(true);
            mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            Intent resultIntent = new Intent(context, HelpActivity.class);
// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);

// Gets an instance of the NotificationManager service//

            NotificationManager mNotificationManager =

                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Log.d("trung","After Receive");
//When you issue multiple notifications about the same type of event, it’s best practice for your app to try to update an existing notification with this new information, rather than immediately creating a new notification. If you want to update this notification at a later date, you need to assign it an ID. You can then use this ID whenever you issue a subsequent notification. If the previous notification is still visible, the system will update this existing notification, rather than create a new one. In this example, the notification’s ID is 001//

            //NotificationManager.notify().

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mNotificationManager.notify(001, mBuilder.build());
            }
            flag = false;
        }
    }
    public String makePostRequest(String url) {
        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(url);



        String kq = "";
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            kq = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return kq;
    }
    public class DocJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            return makePostRequest(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            //MobileNameList.add(s);
            if (!s.isEmpty()) {
                //Log.d("trung", s);
                try {
                    JSONArray mangJSON = new JSONArray(s);

                    for (int i = 0; i < mangJSON.length(); i++) {
                        JSONObject objectJSON = mangJSON.getJSONObject(i);
                        if (!objectJSON.get("date").toString().equals("0000-00-00")) {

                            Double  tempDouble = objectJSON.getDouble("temp");
                            if ( (tempDouble > 30.0f) ||  (tempDouble < 20.0f)){

                                flag = true;
                                    Log.d("trung", tempDouble.toString());
                            }

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                //Toast.makeText(MainActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();


        }
    }

}
