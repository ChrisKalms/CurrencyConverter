package com.ck.almos.currencyconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Handler handler;
    private JSONObject currencies;

    private static CurrencyDb currencyDb;
    private SharedPreferences preferences;
    private CurrencyProvider provider;

    private Button convertButton;
    private NumberPicker numberPickerIn;
    private NumberPicker numberPickerOut;
    private EditText editTextIn;
    private TextView textViewOutput;

    private int numberPickerInValue;
    private int numberPickerOutValue;

    private String[] currencyCodes;
    private long currentTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();

        //instantiate the database
        currencyDb = new CurrencyDb(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //setup view
        setupButtons();
        setupTextViews();
        //setupNumberPickers(); //moved to onResume as they're now dynamically setup via preferences
    }

    @Override
    protected void onResume() {
        super.onResume();
        provider = new CurrencyProvider(preferences.getString("api_key", null));
        setActiveCodes();
        setCurrencies();
        setupNumberPickers();
    }
    private void setCurrencies(){
        try {
            currentTimestamp = Long.parseLong(preferences.getString("historical_list", "not set"));
            //using the selected timestamp in the preferences set the 'active' currencies
            currencies = currencyDb.getCurrencyData(currentTimestamp);
        } catch (NumberFormatException nfe){
            currentTimestamp = -1;
            currencies = null;
        }

    }

    private void setActiveCodes(){
        //get the multi select preferences
        Set<String> strings = preferences.getStringSet("multi_codes", null);

        //to avoid an exception if a user removes all selections in the preferences list
        //set Australian and USA currencies.
        if(strings == null || strings.size() == 0)
            currencyCodes = new String[]{
                    "AUD", "USD"
            };
        else {
            //set the array to the selected currencies in the preferences list, using the size
            //of the set.
            currencyCodes = strings.toArray(new String[strings.size()]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.help:
                    postHelpFile();
                break;
            case R.id.settings:
                    Intent i = new Intent(this, SettingsActivity.class);
                    startActivity(i);
                break;
            case R.id.update:
                    checkValidUpdate();
                break;
        }
        return true;
    }

    private void postHelpFile(){
        Intent i = new Intent(this, HelpWebView.class);
        startActivity(i);
    }

    private void printOutput(){
        try {
            //get necessary values
            Double input = Double.parseDouble(String.valueOf(editTextIn.getText()));
            Double baseCurrency = currencies.getDouble(currencyCodes[numberPickerIn.getValue()]);
            Double toCurrency = currencies.getDouble(currencyCodes[numberPickerOut.getValue()]);

            //get percentage of baseCurrency and toCurrency
            //using percentage against the inputted data
            //get the value that the input will convert to.
            Double x = baseCurrency - toCurrency;
            Double y = x / baseCurrency * 100;
            Double z = input * ((100 - y) * .01);

            //print
            String output = String.format(Locale.US,"%.2f", z);
            textViewOutput.setText(output);
        }catch(JSONException je){
            Toast.makeText(this, "Error with data", Toast.LENGTH_SHORT).show();
        }catch(NumberFormatException e){
            Toast.makeText(this, "Please enter a value to convert", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkValidUpdate(){
        //if currencies isn't populated and no timestamps are in db then need to get data
        if(currencies == null && currencyDb.isEmpty()) {
            updateCurrencyData();
        } else {
            //if it has been more than 30 minutes (60 seconds x 30), then allow update
            if(System.currentTimeMillis() / 1000L - currencyDb.getMostRecentStamp() > 60 * 30){
                updateCurrencyData();
            }
            else{
                Toast.makeText(MainActivity.this, "Currencies are up to date", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateCurrencyData(){
        //start a new thread to try to call the fixer api
        //if json returns null post Toast to user
        new Thread(){
            @Override
            public void run() {
                final JSONObject json = provider.getJSON();
                if(json == null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Cannot Retrieve Data", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //adjust the json object to have the systems current timestamp
                            //so we can use the system time to check if new data will be available
                            //fixer api only has updates every hour on free api
                            long systemTimeStamp = System.currentTimeMillis() / 1000L;
                            try {
                                json.put("timestamp", systemTimeStamp);
                                //add to database
                                currencyDb.addCurrencyData(json);
                            } catch(JSONException e){

                            }
                            Toast.makeText(MainActivity.this,"Updated, select new data in settings", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }.start();
    }

    private void setupNumberPickers(){
        numberPickerIn = findViewById(R.id.convert_from_picker);
        //set the number picker array to null so that we can dynamically allocate
        //the values and size of the array. Exception thrown by onResume() if this
        //is left out.
        numberPickerIn.setDisplayedValues(null);
        numberPickerIn.setMinValue(0);
        numberPickerIn.setMaxValue(currencyCodes.length - 1);
        numberPickerIn.setDisplayedValues(currencyCodes);
        numberPickerInValue = 0;

        numberPickerIn.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                numberPickerInValue = newVal;
            }
        });

        numberPickerOut = findViewById(R.id.convert_to_picker);
        numberPickerOut.setDisplayedValues(null);
        numberPickerOut.setMinValue(0);
        numberPickerOut.setMaxValue(currencyCodes.length - 1);
        numberPickerOut.setDisplayedValues(currencyCodes);
        numberPickerOut.setValue(0);
        numberPickerOutValue = 0;

        numberPickerOut.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                numberPickerOutValue = newVal;
            }
        });
    }

    private void setupButtons(){
        convertButton = findViewById(R.id.convert_currency_button);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currencies != null) {
                    printOutput();
                } else{
                    Toast.makeText(MainActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupTextViews(){
        editTextIn = findViewById(R.id.input_text);
        textViewOutput = findViewById(R.id.output_display);
    }

    /**
     *
     * @return The SQLLite database associated with this application
     */
    public static CurrencyDb getCurrencyDb(){
        return currencyDb;
    }

}
