package com.ck.almos.currencyconverter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CurrencyDb {
    private static final String DB_NAME = "currency";
    private static final String TABLE_NAME = "currency";
    private static final int DB_VERSION = 1;
    private static final String DB_CREATE_QUERY =
            "create table " +
            TABLE_NAME +
            "(" +
            "TS integer, " +
            "AED FLOAT, " +
            "AFN FLOAT, " +
            "AMD FLOAT, " +
            "ANG FLOAT, " +
            "AOA FLOAT, " +
            "ARS FLOAT, " +
            "AUD FLOAT, " +
            "AWG FLOAT, " +
            "AZN FLOAT, " +
            "BAM FLOAT, " +
            "BBD FLOAT, " +
            "BDT FLOAT, " +
            "BGN FLOAT, " +
            "BHD FLOAT, " +
            "BIF FLOAT, " +
            "BMD FLOAT, " +
            "BND FLOAT, " +
            "BOB FLOAT, " +
            "BRL FLOAT, " +
            "BSD FLOAT, " +
            "BTC FLOAT, " +
            "BTN FLOAT, " +
            "BWP FLOAT, " +
            "BYN FLOAT, " +
            "BYR FLOAT, " +
            "BZD FLOAT, " +
            "CAD FLOAT, " +
            "CDF FLOAT, " +
            "CHF FLOAT, " +
            "CLF FLOAT, " +
            "CLP FLOAT, " +
            "CNY FLOAT, " +
            "COP FLOAT, " +
            "CRC FLOAT, " +
            "CUC FLOAT, " +
            "CUP FLOAT, " +
            "CVE FLOAT, " +
            "CZK FLOAT, " +
            "DJF FLOAT, " +
            "DKK FLOAT, " +
            "DOP FLOAT, " +
            "DZD FLOAT, " +
            "EGP FLOAT, " +
            "ERN FLOAT, " +
            "ETB FLOAT, " +
            "EUR FLOAT, " +
            "FJD FLOAT, " +
            "FKP FLOAT, " +
            "GBP FLOAT, " +
            "GEL FLOAT, " +
            "GGP FLOAT, " +
            "GHS FLOAT, " +
            "GIP FLOAT, " +
            "GMD FLOAT, " +
            "GNF FLOAT, " +
            "GTQ FLOAT, " +
            "GYD FLOAT, " +
            "HKD FLOAT, " +
            "HNL FLOAT, " +
            "HRK FLOAT, " +
            "HTG FLOAT, " +
            "HUF FLOAT, " +
            "IDR FLOAT, " +
            "ILS FLOAT, " +
            "IMP FLOAT, " +
            "INR FLOAT, " +
            "IQD FLOAT, " +
            "IRR FLOAT, " +
            "ISK FLOAT, " +
            "JEP FLOAT, " +
            "JMD FLOAT, " +
            "JOD FLOAT, " +
            "JPY FLOAT, " +
            "KES FLOAT, " +
            "KGS FLOAT, " +
            "KHR FLOAT, " +
            "KMF FLOAT, " +
            "KPW FLOAT, " +
            "KRW FLOAT, " +
            "KWD FLOAT, " +
            "KYD FLOAT, " +
            "KZT FLOAT, " +
            "LAK FLOAT, " +
            "LBP FLOAT, " +
            "LKR FLOAT, " +
            "LRD FLOAT, " +
            "LSL FLOAT, " +
            "LTL FLOAT, " +
            "LVL FLOAT, " +
            "LYD FLOAT, " +
            "MAD FLOAT, " +
            "MDL FLOAT, " +
            "MGA FLOAT, " +
            "MKD FLOAT, " +
            "MMK FLOAT, " +
            "MNT FLOAT, " +
            "MOP FLOAT, " +
            "MRO FLOAT, " +
            "MUR FLOAT, " +
            "MVR FLOAT, " +
            "MWK FLOAT, " +
            "MXN FLOAT, " +
            "MYR FLOAT, " +
            "MZN FLOAT, " +
            "NAD FLOAT, " +
            "NGN FLOAT, " +
            "NIO FLOAT, " +
            "NOK FLOAT, " +
            "NPR FLOAT, " +
            "NZD FLOAT, " +
            "OMR FLOAT, " +
            "PAB FLOAT, " +
            "PEN FLOAT, " +
            "PGK FLOAT, " +
            "PHP FLOAT, " +
            "PKR FLOAT, " +
            "PLN FLOAT, " +
            "PYG FLOAT, " +
            "QAR FLOAT, " +
            "RON FLOAT, " +
            "RSD FLOAT, " +
            "RUB FLOAT, " +
            "RWF FLOAT, " +
            "SAR FLOAT, " +
            "SBD FLOAT, " +
            "SCR FLOAT, " +
            "SDG FLOAT, " +
            "SEK FLOAT, " +
            "SGD FLOAT, " +
            "SHP FLOAT, " +
            "SLL FLOAT, " +
            "SOS FLOAT, " +
            "SRD FLOAT, " +
            "STD FLOAT, " +
            "SVC FLOAT, " +
            "SYP FLOAT, " +
            "SZL FLOAT, " +
            "THB FLOAT, " +
            "TJS FLOAT, " +
            "TMT FLOAT, " +
            "TND FLOAT, " +
            "TOP FLOAT, " +
            "TRY FLOAT, " +
            "TTD FLOAT, " +
            "TWD FLOAT, " +
            "TZS FLOAT, " +
            "UAH FLOAT, " +
            "UGX FLOAT, " +
            "USD FLOAT, " +
            "UYU FLOAT, " +
            "UZS FLOAT, " +
            "VEF FLOAT, " +
            "VND FLOAT, " +
            "VUV FLOAT, " +
            "WST FLOAT, " +
            "XAF FLOAT, " +
            "XAG FLOAT, " +
            "XAU FLOAT, " +
            "XCD FLOAT, " +
            "XDR FLOAT, " +
            "XOF FLOAT, " +
            "XPF FLOAT, " +
            "YER FLOAT, " +
            "ZAR FLOAT, " +
            "ZMK FLOAT, " +
            "ZMW FLOAT, " +
            "ZWL FLOAT " +
            ");";



    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;

    /**
     * Creates a new database to add and retrieve data from.
     * @param ctx The application environment context.
     */
    public CurrencyDb(Context ctx){
        dbHelper = new SQLiteOpenHelper(ctx, DB_NAME, null, DB_VERSION ) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                sqLiteDatabase.execSQL(DB_CREATE_QUERY);
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(sqLiteDatabase);
            }
        };
        db = dbHelper.getWritableDatabase();
    }

    /**
     * Check database for entries.
     * @return True if the database is empty, False if the database has rows.
     */
    public boolean isEmpty(){
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + ";", null);
        if(c.getCount() == 0)
            return true;
        else
            return false;
    }

    /**
     * Get the most recent timestamp.
     * @return The value of the most recent timestamp. If no timestamps are available, -1 returned.
     */
    public Long getMostRecentStamp(){
        Cursor c = db.query(TABLE_NAME, new String[]{"TS"}, null, null,null,null,"TS DESC", "1");
        if(c.getCount() > 0) {
            c.moveToFirst();
            return c.getLong(0);
        }
        else
            return -1L;

    }

    /**
     * Get all timestamps from the database.
     * @return All timestamps within the database.
     */
    public Long[] getTimestamps(){
        //get all results from TS column, call descending so that latest added will be first
        Cursor c = db.query(TABLE_NAME, new String[]{"TS"}, null, null,null,null, "TS DESC" );
        ArrayList<Long> timestamps = new ArrayList<Long>();

        if(c != null){
            c.moveToFirst();
            while(!c.isAfterLast()){
                timestamps.add(c.getLong(0));
                c.moveToNext();
            }
            c.close();
        }
        return timestamps.toArray(new Long[timestamps.size()]);
    }

    /**
     * Add currency data into the database.
     * @param json JSON object containing all values from the API
     */
    public void addCurrencyData(JSONObject json){
        ContentValues data = new ContentValues();
        try {
            data.put("TS", json.getInt("timestamp"));
            //get the rates json object which is the value associated with each code
            JSONObject rates = json.getJSONObject("rates");
            //foreach code, add to the ContentValue that code for the ColumnName and the value to be saved
            for(String s : CountryCodes.getCodes()){
                data.put(s, rates.getDouble(s));
            }
            db.insert(TABLE_NAME, null, data);
        } catch (JSONException e){

        }
    }

    /**
     * Get Currency JSON Object from the database.
     * @param timestamp The value to get the specific row from the database.
     * @return JSON object containing all the columns pertaining to the inputted timestamp.
     */
    public JSONObject getCurrencyData(long timestamp){
        JSONObject j = new JSONObject();
        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE TS = " + timestamp + ";", null);
        c.moveToFirst();
        for (int i = 0; i < CountryCodes.getCodes().length; i++) {
            try {
                j.put(CountryCodes.getCodes()[i], c.getDouble(c.getColumnIndex(CountryCodes.getCodes()[i])));
            } catch (JSONException e) {

            }
        }

        return j;
    }
}
