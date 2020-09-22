package com.example.aarjugoyal.homework_9_news_app;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by aarjugoyal on 4/22/20.
 */

public class RemoteFetch_Weather {

    private static final String OPEN_WEATHER_MAP_API =
            "58fe1e8be7907f532d7ecce596a4ceca";
    private static final String OPEN_WEATHER_MAP_URL =
            "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=" + OPEN_WEATHER_MAP_API;

    public static JSONObject getJSON(Context context, String city){
        Log.d("WEATHER UPDATION", "Inside Remote Fetch function getJSON");
        try {
            Log.d("WEATHER API KEY", "API key is " + R.string.open_weather_maps_app_id);
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, city));
            Log.d("WEATHER GET JSON DATA 1", "URL for weather" + url);
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

//            connection.addRequestProperty("x-api-key",
//                    context.getString(R.string.open_weather_maps_app_id));
            Log.d("WEATHER GET JSON DATA 2", "URL for weather" + url);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            Log.d("WEATHER GET JSON DATA 3", "reader assigned");
            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();
            Log.d("WEATHER UPDATION", "JSON data recieved is " + json);
            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            Log.d("WEATHER ERROR", "error is ", e);
            return null;
        }
    }
}
