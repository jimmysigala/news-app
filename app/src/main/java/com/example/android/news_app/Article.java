package com.example.android.news_app;

import android.graphics.Bitmap;

/**
 * Created by Jimmy on 3/30/2017.
 */

public class Article {
    private String mSectionName;
    private String mWebTitle;
    private String mWebPubDate;
    private String mWebURL;
    private Bitmap mThumbnail;

    public Article(String sectionName, String webTitle, String webPubDate, String webURL, Bitmap thumbnail) {

        mSectionName = sectionName;
        mWebTitle = webTitle;
        mWebPubDate = webPubDate;
        mWebURL = webURL;
        mThumbnail = thumbnail;
    }

    public String getmSectionName() {
        return mSectionName;
    }

    public String getmWebTitle() {
        return mWebTitle;
    }

    public String getmWebPubDate() {
        return mWebPubDate;
    }

    public String getmWebURL() {
        return mWebURL;
    }

    public Bitmap getmThumbnail() {
        return mThumbnail;
    }
}
