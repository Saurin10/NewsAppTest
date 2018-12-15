package com.example.android.newsapptest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<News> {
    //Constructor with super
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }
        // Get the {@link News} object located at this position in the list
        News currentNews = getItem(position);

        // Find the TextView with view ID News Title
        TextView headlineView = (TextView) listItemView.findViewById(R.id.headline);
        headlineView.setText(currentNews.getNewsHeadline());

        // Find the TextView with view Section Name
        TextView author = (TextView) listItemView.findViewById(R.id.news_section);
        author.setText(currentNews.getNewsSection());

        // Find the TextView with view News Date
        TextView date = (TextView) listItemView.findViewById(R.id.date);
        String stringDate = currentNews.getNewsDate();
        stringDate = formateDate(stringDate);
        date.setText(stringDate);

        return listItemView;
    }

    private String formateDate(String stringDate) {
        //Create Date object
        String dateStyle = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateStyle, Locale.ENGLISH);
        try {
            Date rawDate = dateFormat.parse(stringDate);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM dd, YYYY",
                    Locale.ENGLISH);
            return outputDateFormat.format(rawDate);
        } catch (ParseException e) {
            Log.i("Inside NewsAdapter", "Error while converting the date format");
            return "";
        }
    }
}
