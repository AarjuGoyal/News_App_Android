package com.example.aarjugoyal.homework_9_news_app;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * Created by aarjugoyal on 4/24/20.
 */

public class RemoteFetch_Guardian_HomeNews {

    private static final String GUARDIAN_NEWS_API =
            "360d55ef-f616-4733-b330-a5bbebd68d61";
    private static final String GUARDIAN_HOME_NEWS_URL =
            "https://content.guardianapis.com/search?order-by=newest&show-fields=starRating,headline,thumbnail,short-url&api-key=" + GUARDIAN_NEWS_API;

    public static JSONObject getJSON(Context context){
        Log.d("GUARDIAN NEWS HOME ", "Inside Remote Fetch function getJSON");
        try {

            URL url = new URL(GUARDIAN_HOME_NEWS_URL);
            Log.d("GUARDIAN NEWS HOME 1", "URL for getting news " + url);
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();


            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            Log.d("GUARDIAN NEWS GET JSON DATA 3", "reader assigned");
            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();


            Log.d("GUARDIAN NEWS UPDATION", "JSON data received is " + json);
            JSONObject data = new JSONObject(json.toString());


            JSONObject response = data.getJSONObject("response");

            Log.d("GUARDIAN NEWS UPDATION", "Response received is " + response);

            String status = response.getString("status");

            if(!Objects.equals("ok", status)){
                return null;
            }

            return response;
        }catch(Exception e){
            Log.d("GUARDIAN NEWS HOME RECEIVING ERROR", "error is " + e);
            return null;
        }
    }

}
