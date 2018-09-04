package com.example.android.newsappproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    // The user can edit the app settings in the PreferenceFragment.
    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference
            .OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            // The first editable preference is the search keyword, to narrow down the articles.
            Preference searchContent = findPreference(getString(R.string.settings_edit_text_key));
            bindPreferenceSummaryToValue(searchContent);

            // The second editable preference is the news article order.
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);

            // The third editable preference is the news article number displayed.
            Preference articleNumber = findPreference(getString(R.string
                    .settings_article_number_key));
            bindPreferenceSummaryToValue(articleNumber);
        }

        @Override
        /* This method modifies the news article displayed according to the changes made by the
         * user.
         */
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else if (preference instanceof EditTextPreference) {
                if (preference.getKey().equals(getString(R.string.settings_article_number_key))) {
                    if (Integer.parseInt(stringValue) > 0 && Integer.parseInt(stringValue) <= 50)
                        preference.setSummary(stringValue);
                    else if (Integer.parseInt((stringValue)) <= 0) preference.setSummary("1");
                    else preference.setSummary("50");
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                    (preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}