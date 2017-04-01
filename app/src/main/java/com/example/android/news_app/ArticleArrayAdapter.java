package com.example.android.news_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jimmy on 3/30/2017.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    private Article articles;

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        articles = getItem(position);

        TextView sectionNameTextView = (TextView) listItemView.findViewById(R.id.sectionName);
        sectionNameTextView.setText(articles.getmSectionName());

        TextView webTitleTextView = (TextView) listItemView.findViewById(R.id.webTitle);
        webTitleTextView.setText(articles.getmWebTitle());

        TextView webPubDateTextView = (TextView) listItemView.findViewById(R.id.webPubDate);
        webPubDateTextView.setText(formatDate(articles.getmWebPubDate()));

        return listItemView;
    }

    private String formatDate(String date) {

        String formattedDate = "";

        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try {
            Date date2 = oldFormat.parse(date);
            SimpleDateFormat newFormat = new SimpleDateFormat("d MMM yyyy h:mm aaa");
            formattedDate = newFormat.format(date2);
        } catch (ParseException e) {
            Log.e(ArticleArrayAdapter.class.getName(), "Error parsing date", e);
        }

        return formattedDate;
    }

}
