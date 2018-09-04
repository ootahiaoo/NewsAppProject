package com.example.android.newsappproject;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * The {@link NewsAdapter} creates a list item layout for each news article
 * in the data source (a list of {@link News} objects).
 * These list item layouts will be provided to the ListView to be displayed to the user.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news    is the list of news articles, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of news article.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item,
                    parent, false);
        }
        // Find the current position in the list.
        News currentNewsItem = getItem(position);

        /* Find the TextView for the section information. Update it to display the section data
         * for the current position in the list.
         */
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.news_item_section);
        sectionTextView.setText(currentNewsItem.getSection());

        /* Find the TextView for the title information. Update it to display the title data for the
         * current position in the list.
         */
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.news_item_title);
        titleTextView.setText(currentNewsItem.getTitle());

        /* Find the TextView for the author/contributor information. Update it to display the
         * author data for the current position in the list.
         */
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.textview_author);
        authorTextView.setText(currentNewsItem.getAuthor());

        // Find the TextViews for the data and time information.
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.textview_date);

        // Get the day and time information for the current position.
        String originalTime = currentNewsItem.getTime();

        /* This is the day and time information format used on the guardian open platform. The
         * data received will be in this format, so we need to convert them to another format.
         */
        SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",
                Locale.US);
        Date myDate = null;
        try {
            /* SimpleDateFormat can't parse the original format if we don't change the "Z" in the
             * end, so we manually replace it.
             */
            myDate = originalDateFormat.parse(originalTime.replaceAll("Z$", "+0000"));
        } catch (ParseException e) {
            Log.e("NewsAdapter", "Problem parsing the date string");
        }

        // Format the date and update the TextView to display the date information.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(myDate);
        dateTextView.setText(formattedDate);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
