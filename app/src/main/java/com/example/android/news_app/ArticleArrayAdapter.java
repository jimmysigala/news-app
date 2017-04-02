package com.example.android.news_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

        // Inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get the object located at this position in the list
        articles = getItem(position);

        // Set the info provided by the web api to the TextViews and ImageView found in list_item.xml
        TextView sectionNameTextView = (TextView) listItemView.findViewById(R.id.sectionName);
        sectionNameTextView.setText(articles.getmSectionName());

        TextView webTitleTextView = (TextView) listItemView.findViewById(R.id.webTitle);
        webTitleTextView.setText(articles.getmWebTitle());

        TextView webPubDateTextView = (TextView) listItemView.findViewById(R.id.webPubDate);
        webPubDateTextView.setText(formatDate(articles.getmWebPubDate()));

        // Use the thumbnail provided by the web api, if null use the image located in drawable folder
        ImageView thumbnail = (ImageView) listItemView.findViewById(R.id.thumbnail);
        if (articles.getmThumbnail() != null) {
            thumbnail.setImageBitmap(articles.getmThumbnail());
        } else {
            thumbnail.setImageResource(R.drawable.the_guardian);
        }

        return listItemView;
    }

    // This method will format the date
    private String formatDate(String date) {
        String formattedDate = "";
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try {
            Date date2 = oldFormat.parse(date);
            SimpleDateFormat newFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");
            formattedDate = newFormat.format(date2);
        } catch (ParseException e) {
            Log.e(ArticleArrayAdapter.class.getName(), "Error parsing date", e);
        }
        return formattedDate;
    }
}
