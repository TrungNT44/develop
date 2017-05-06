package apt.tutorial.connectmysqldb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewsDetailActivity extends Activity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        webView = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());
    }
}
