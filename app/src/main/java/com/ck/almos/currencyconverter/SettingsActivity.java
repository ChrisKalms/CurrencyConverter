package com.ck.almos.currencyconverter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager().beginTransaction().add(android.R.id.content,
                    new SettingsFragment()).commit();
        }

    }

    @SuppressLint("ValidFragment")
    @SuppressWarnings("deprecation")
    public static class SettingsFragment extends PreferenceFragment{
        ListPreference lpSavedDates;
        MultiSelectListPreference mslpCurrencyCodes;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            //get the array of timestamps from the database
            Long[] allDates = MainActivity.getCurrencyDb().getTimestamps();

            //array of the timestamps formatted for display in the Preferences list
            String[] entries = new String[allDates.length];
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm z");
            for(int j = 0; j < allDates.length; j++){
                //multiply by 1000 to convert to milliseconds
                entries[j] = sdf.format(new Date(allDates[j] * 1000L));
            }
            //array of timestamps converted to string
            String[] values = new String[allDates.length];
            for(int i = 0; i < allDates.length; i++){
                values[i] = Long.toString(allDates[i]);
            }

            lpSavedDates = (ListPreference)findPreference("historical_list");
            lpSavedDates.setEntries(entries);
            lpSavedDates.setEntryValues(values);

            mslpCurrencyCodes = (MultiSelectListPreference)findPreference("multi_codes");
            //set the properties from code //note that have to leave setEntries and setValues in the xml or runtime error.
            mslpCurrencyCodes.setEntries(CountryCodes.getCodes());
            mslpCurrencyCodes.setEntryValues(CountryCodes.getCodes());
        }

    }
}
