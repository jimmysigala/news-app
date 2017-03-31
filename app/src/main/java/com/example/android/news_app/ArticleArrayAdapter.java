package com.example.android.news_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        // Article articles = getItem(position);
        articles = getItem(position);


        TextView sectionNameTextView = (TextView) listItemView.findViewById(R.id.sectionName);
        sectionNameTextView.setText(articles.getmSectionName());

        TextView webTitleTextView = (TextView) listItemView.findViewById(R.id.webTitle);
        webTitleTextView.setText(articles.getmWebTitle());

        TextView webPubDateTextView = (TextView) listItemView.findViewById(R.id.webPubDate);
        webPubDateTextView.setText(articles.getmWebPubDate());

        return listItemView;
    }

    public Article getArticlePosition() {
        return articles;
    }


}
