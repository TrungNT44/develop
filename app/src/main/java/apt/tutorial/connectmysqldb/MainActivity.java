package apt.tutorial.connectmysqldb;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.lang.Float;

import static android.R.attr.data;
import static android.R.attr.entries;

public class MainActivity extends TabActivity {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    Button btnPast,btnPickDate;
    WebView webViewTinKhuyenNong;
    TextView tvDate;
    EditText editTmp, editHum, editStt;
    ArrayList<String> labels = new ArrayList<String>();

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(MainActivity.this,"Chọn ngày hiện tại hoặc ngày vừa chọn để xem thông tin thời tiết",Toast.LENGTH_LONG).show();
        TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
        spec.setContent(R.id.thongtin);
        spec.setIndicator("Thời tiết");
        getTabHost().addTab(spec);

        spec=getTabHost().newTabSpec("tag2");
        spec.setContent(new Intent(this,ChartActivity.class));
        spec.setIndicator("Biểu đồ");
        getTabHost().addTab(spec);

        spec=getTabHost().newTabSpec("tag3");
        spec.setContent(new Intent(this,NewsActivity.class));
        spec.setIndicator("Tin tức");
        getTabHost().addTab(spec);

        getTabHost().setCurrentTab(0);


        tvDate = (TextView) findViewById(R.id.tvDate);
        editTmp = (EditText) findViewById(R.id.editTmp);
        editHum = (EditText) findViewById(R.id.editHum);
        editStt = (EditText) findViewById(R.id.editStt);
        btnPast = (Button) findViewById(R.id.btnGetData);
        btnPast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new DocJSON().execute("http://192.168.1.104:80/trung/select.php");
                        // goi ham gui cac gia tri len trang php
                    }
                });
            }
        });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new DocJSON().execute("http://192.168.1.104:80/trung/display.php");
                        // goi ham gui cac gia tri len trang php
                    }
                });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        btnPickDate = (Button) findViewById(R.id.btnPickDate);
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });
        webViewTinKhuyenNong = (WebView)findViewById(R.id.webViewTinNongNghiep);

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            webViewTinKhuyenNong.loadUrl("http://www.khuyennongvn.gov.vn/tu-van-hoi-dap_t113c48");
            webViewTinKhuyenNong.setWebViewClient(new WebViewClient());
        }
        else{
            //setContentView(R.layout.no_internet_access);
            Toast.makeText(MainActivity.this,"Kiểm tra lại kết nối với 3G hoặc wifi",Toast.LENGTH_LONG).show();

        }



    }
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                     // year = arg1;
                     //month= arg2;
                     //day= arg3;
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        if ( month >= 10) {
            tvDate.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
        else{
            tvDate.setText(new StringBuilder().append(year).append("-0")
                    .append(month).append("-").append(day));
        }
    }

    public String makePostRequest(String url) {
        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(url);

        // Các tham số truyền
        List nameValuePair = new ArrayList(1);
        nameValuePair.add(new BasicNameValuePair("so1", tvDate.getText().toString()));

        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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

    // viet 1 ham gui thong tin len trang php

    public class DocJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            return makePostRequest(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            //MobileNameList.add(s);
            if (!s.isEmpty()) {
                try {
                    JSONArray mangJSON = new JSONArray(s);
                    tvDate.setText("");
                    editTmp.setText("");
                    editHum.setText("");
                    editStt.setText("");
                    for (int i = 0; i < mangJSON.length(); i++) {
                        JSONObject objectJSON = mangJSON.getJSONObject(i);
                        if (!objectJSON.get("date").toString().equals("0000-00-00")) {
                            tvDate.setText(objectJSON.get("date").toString());
                            editTmp.setText(objectJSON.get("temp").toString()+" độ C");
                            editHum.setText(objectJSON.get("humid").toString());
                            editStt.setText(objectJSON.get("stt").toString());
                        } else
                            Toast.makeText(MainActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
                Toast.makeText(MainActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();


        }
    }


}
