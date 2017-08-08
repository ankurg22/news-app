package com.example.android.newsapp;

/**
 * Created by Ankur Gupta on 7/8/17.
 * guptaankur.gupta0@gmail.com
 */

public class News {
    private String mTitle;
    private String mPublicationDate;
    private String mUrl;
    private String mSection;

    public News(String section, String date, String title, String url) {
        mTitle = title;
        mPublicationDate = date;
        mUrl = url;
        this.mSection = section;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getSection() {
        return mSection;
    }

    public void setPublicationDate(String publicationDate) {
        mPublicationDate = publicationDate;
    }
}
