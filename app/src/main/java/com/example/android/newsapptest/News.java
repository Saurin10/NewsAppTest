package com.example.android.newsapptest;

public class News {
    //Check Sample News
    private String newsHeadline;
    //String for section name
    private String newsSection;
    //String for news item date
    private String newsDate;
    //String for Author Name
    private String authorName;
    /**
     * Website URL of the News Item
     */
    private String mUrl;

    public News(String headline, String section, String date, String url, String author) {
        newsHeadline = headline;
        newsSection = section;
        newsDate = date;
        mUrl = url;
        authorName = author;
    }

    public String getNewsHeadline() {
        return newsHeadline;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public String getNewsSection() {
        return newsSection;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getAuthorName() {
        return authorName;
    }
}
