package apt.tutorial.connectmysqldb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.entries;

public class ChartActivity extends Activity {
    ArrayList<String> temp = new ArrayList<String>();
    ArrayList<BarEntry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<String>();
    BarChart barChart = null;
    EditText editDateStart, editDateEnd;
    Button btnDoAm;
    BarDataSet barDataSet;
    BarData barData;
    String flag = "temp";
    RadioGroup radioGroup;
    RadioButton radioButtonNhiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chart);
        

        //Toast.makeText(ChartActivity.this,"Chọn ngày bắt đầu và ngày kết thúc để xem biểu đồ dữ liệu",Toast.LENGTH_LONG).show();
        barChart = (BarChart) findViewById(R.id.chart);
        editDateStart = (EditText) findViewById(R.id.editDateStart);
        editDateEnd = (EditText) findViewById(R.id.editDateEnd);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButtonNhiet = (RadioButton) findViewById(R.id.radioNhietDo);
        radioButtonNhiet.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.radioNhietDo:
                        flag = "temp";
                        break;
                    case R.id.radioDoAm:
                        flag = "humid";
                        break;
                }
            }
        });

        btnDoAm = (Button) findViewById(R.id.btnDoAm);
        btnDoAm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entries.clear();
                        labels.clear();

                        new DocJSON2().execute("http://10.11.4.125:80/trung/selectToChart.php");
                        // goi ham gui cac gia tri len trang php
                    }
                });

            }
        });

    }

    public String makePostRequest2(String url) {
        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(url);

        // Các tham số truyền
        List nameValuePair = new ArrayList(2);
        nameValuePair.add(new BasicNameValuePair("start", editDateStart.getText().toString()));
        nameValuePair.add(new BasicNameValuePair("end", editDateEnd.getText().toString()));

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

    public class DocJSON2 extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            return makePostRequest2(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            //MobileNameList.add(s);
            //Toast.makeText(ChartActivity.this,s+" ",Toast.LENGTH_SHORT).show();
            try {
                JSONArray mangJSON = new JSONArray(s);

                //Toast.makeText(MainActivity.this,"so phan tu JSON"+tempList.size(),Toast.LENGTH_LONG).show();
                //editName.setText("so phan tu"+mangJSON.length());
                for (int i = 0; i < mangJSON.length(); i++) {
                    JSONObject objectJSON = mangJSON.getJSONObject(i);
                    if (!objectJSON.get("date").toString().equals("0000-00-00")) {
                        labels.add(objectJSON.getString("date"));
                        double d = 0;
                        if (flag.equals("temp")) {
                            d = objectJSON.getDouble("temp");
                            float f = (float) d;
                            entries.add(new BarEntry(f, i));
                            barDataSet = new BarDataSet(entries, "Nhiệt độ ( độ C )");
                        } else {
                            d = objectJSON.getDouble("humid");
                            float f = (float) d;
                            entries.add(new BarEntry(f, i));
                            barDataSet = new BarDataSet(entries, "Độ ẩm ( % )");
                        }
                        barData = new BarData(labels, barDataSet);
                        barChart.setData(barData);
                        barChart.setDescription("Biểu đồ theo ngày");
                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        barChart.animateY(5000);
                        barChart.invalidate();
                    } else
                        Toast.makeText(ChartActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }

                //Toast.makeText(ChartActivity.this,"so phan tu : "+labels.size(),Toast.LENGTH_SHORT).show();
                //dataset.setDrawFilled(true);
                //barChart.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

}