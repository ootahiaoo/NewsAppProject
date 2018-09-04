package com.example.android.newsappproject;

public class News {

    private String section;
    private String title;
    private String time;
    private String author;
    private String url;

    /**
     * Constructs a new {@link News} object.
     *
     * @param section is the section/genre in which the news article is classified.
     * @param title   is the title of the news article.
     * @param time    is the time and date when the article was published.
     * @param author  is the contributor who wrote the news article.
     * @param url     is the url linking to the full news article page.
     */
    public News(String section, String title, String time, String author, String url) {
        this.section = section;
        this.title = title;
        this.time = time;
        this.author = author;
        this.url = url;
    }

    public String getSection() {
        return section;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }
}

