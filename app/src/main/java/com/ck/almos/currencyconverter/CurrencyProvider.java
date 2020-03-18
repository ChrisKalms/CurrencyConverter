package com.ck.almos.currencyconverter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyProvider {
    private final String FIXER_API;

    /**
     * Creates a new provider, to get data from the Fixer API.
     * @param apikey The API Key required to get data from the Fixer API.
     */
    public CurrencyProvider(String apikey){
        FIXER_API = "http://data.fixer.io/api/latest?access_key=" + apikey;
    }

    /**
     * Get the JSON Object returned via the fixer API.
     * @return If successful The JSON object that is returned from the API, else null.
     */
    public JSONObject getJSON(){
        try{
            //try to get the json object from the fixer api
            //if the json object returns "success":false there's an error with privileges
            URL url = new URL(FIXER_API);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer json = new StringBuffer();

            String tmp;
            while((tmp = reader.readLine()) != null){
                json.append(tmp).append("\n");
            }
            reader.close();

            JSONObject data = new JSONObject(json.toString());
            if(!data.getBoolean("success")) {
                return null;
            }
            return data;
        }catch (Exception ex){
            return null;
        }
    }
}
