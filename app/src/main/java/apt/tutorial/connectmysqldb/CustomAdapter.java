package apt.tutorial.connectmysqldb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung1994 on 27-Apr-17.
 */

public class CustomAdapter extends ArrayAdapter<News> {

    public CustomAdapter(Context context, int resource, List<News> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.customlistview,null);
        }
        News news = getItem(position);

        ImageView img = (ImageView) view.findViewById(R.id.img);
        Picasso.with(getContext()).load(news.image).into(img);




        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(news.title);





        return view;
    }
}
