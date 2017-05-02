package apt.tutorial.connectmysqldb;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
ListView lvNews;
    CustomAdapter customAdapter;
    ArrayList<News> newsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        lvNews = (ListView) findViewById(R.id.lvNews);
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
                intent.putExtra("link",newsArrayList.get(position).link);
                startActivity(intent);
            }
        });
        newsArrayList = new ArrayList<News>();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadData().execute("http://vnexpress.net/rss/kinh-doanh.rss");
				//new ReadData().execute("http://nongnghiep.vn/rss/khuyen-nong-7.rss");
            }
        });
    }
    class ReadData extends AsyncTask<String ,Integer,String>{
        @Override
        protected String doInBackground(String... params) {

            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            XMLDOMParser xmldomParser = new XMLDOMParser();
            Document doccument = xmldomParser.getDocument(s);
            NodeList nodeList = doccument.getElementsByTagName("item");
            NodeList nodeListDescription = doccument.getElementsByTagName("description");
            String hinhAnh = "";
            String title = "";
            String link ="";
            for (int i =0;i<nodeList.getLength();i++) {
                String cData = nodeListDescription.item(i + 1).getTextContent();
                Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = pattern.matcher(cData);
                if (matcher.find()){
                    hinhAnh = matcher.group(1);

                }
                Element element = (Element) nodeList.item(i);
                title = xmldomParser.getValue(element, "title");
                link = xmldomParser.getValue(element, "link");
                newsArrayList.add(new News(hinhAnh,link,title));

            }
            customAdapter = new CustomAdapter(NewsActivity.this, android.R.layout.simple_list_item_1, newsArrayList);
            lvNews.setAdapter(customAdapter);

            super.onPostExecute(s);

        }
    }
    private static String docNoiDung_Tu_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }
}
