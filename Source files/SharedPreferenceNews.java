package com.example.aarjugoyal.homework_9_news_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by aarjugoyal on 5/4/20.
 */

public class SharedPreferenceNews {

    public static final String PREFS_NAME = "NEWS APP";
    public static final String FAVORITES = "Favorite News";
    public SharedPreferenceNews()
    {
        super();
    }
//    public void saveFavorites(Context context, Set<String> favoritesID) {
//        SharedPreferences settings;
//        SharedPreferences.Editor editor;
//
//        settings = context.getSharedPreferences(PREFS_NAME,
//                Context.MODE_PRIVATE);
//        editor = settings.edit();
//
//
//        editor.putStringSet(FAVORITES, favoritesID);
//
//        editor.commit();
//    }
//
//    public void addFavorite(Context context, NewsItem newsItem) {
//        Set<String> favorites = getFavorites(context);
//        Log.d("FAVORITES SETTING ADD", "Current value of favorite is " + favorites);
//        if (favorites == null) {
//            favorites = new HashSet<String>();
//        }
//        favorites.add(newsItem.toString());
//        Log.d("FAVORITES SETTING ADD", "New value of favorite is " + favorites);
//        saveFavorites(context, favorites);
//    }
//
//    public void removeFavorite(Context context, NewsItem newsItem) {
//        Set<String> favorites = getFavorites(context);
//        if (favorites != null) {
//            Log.d("FAVORITE SETTING REMOVE", "Current value of favorite is " + favorites);
//            favorites.remove(newsItem.toString());
//            Log.d("FAVORITES SETTING REMOVE", "New value of favorite is " + favorites);
//            saveFavorites(context, favorites);
//        }
//    }
//
//    public Set<String> getFavorites(Context context) {
//        SharedPreferences settings;
//        Set<String> favorites;
//
//        settings = context.getSharedPreferences(PREFS_NAME,
//                Context.MODE_PRIVATE);
//
//        if (settings.contains(FAVORITES)) {
//            favorites = settings.getStringSet(FAVORITES, null);
//            Log.d("FAVORITE SETTINGS GET", "The value of favorites recieved and is " + favorites);
//
////            favorites = Arrays.asList(favoriteItems);
////            favorites = new ArrayList<NewsItem>(favorites);
//        } else
//            return null;
//
//        return favorites;
//    }

    public void saveFavorites(Context context, List<String> favoritesID) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Set<String> hSetFav = new HashSet<String>();
        for (String x : favoritesID)
            hSetFav.add(x);
        editor.putStringSet(FAVORITES, hSetFav);

        editor.commit();
    }

    public void addFavorite(Context context, NewsItem newsItem) {
        List<String> favorites = getFavorites(context);
        Log.d("FAVORITES SETTING ADD", "Current value of favorite is " + favorites);
        if (favorites == null) {
            favorites = new ArrayList<String>();
        }
        favorites.add(newsItem.toString());
        Log.d("FAVORITES SETTING ADD", "New value of favorite is " + favorites);
        saveFavorites(context, favorites);
    }

    public void addFavoriteAsString(Context context, String newsItemString) {
        List<String> favorites = getFavorites(context);
        Log.d("FAVORITES SETTING ADD", "Current value of favorite is " + favorites);
        if (favorites == null) {
            favorites = new ArrayList<String>();
        }
        favorites.add(newsItemString);
        Log.d("FAVORITES SETTING ADD", "New value of favorite is " + favorites);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, NewsItem newsItem) {
        List<String> favorites = getFavorites(context);
        if (favorites != null) {
            Log.d("FAVORITE SETTING REMOVE", "Current value of favorite is " + favorites);

            favorites.remove(newsItem.toString());
            Log.d("FAVORITES SETTING REMOVE", "New value of favorite is " + favorites);
            saveFavorites(context, favorites);
        }
    }

    public void removeFavoriteAsString(Context context, String ID) {
        List<String> favorites = getFavorites(context);
        if (favorites != null) {
            Log.d("FAVORITE SETTING REMOVE", "Current value of favorite is " + favorites);
            Log.d("FAVORITE SETTING REMOVE", "The value of ID string returned is " + ID);
            List<String> favlistID = getFavoritesID(context);
            int pos = favlistID.indexOf(ID);
            Log.d("FAVORITE SETTING REMOVE","Index of article removed from list is " + pos);
            if(pos >= 0 )
            {
                favorites.remove(pos);

            }

            Log.d("FAVORITES SETTING REMOVE", "New value of favorite is " + favorites);
            saveFavorites(context, favorites);
        }
    }

    public void removeFavoriteArticleString(Context context, String articleString) {
        List<String> favorites = getFavorites(context);
        if (favorites != null) {
            Log.d("FAVORITE SETTING REMOVE", "Current value of favorite is " + favorites);
            Log.d("FAVORITE SETTING REMOVE", "The value of article string removed is " + articleString);

            favorites.remove(articleString);
            Log.d("FAVORITES SETTING REMOVE", "New value of favorite is " + favorites);
            saveFavorites(context, favorites);
        }
    }
    public List<String> getFavorites(Context context) {
        SharedPreferences settings;
        Set<String> favorites;
        List<String> favList = new ArrayList<String>();
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            favorites = settings.getStringSet(FAVORITES, null);
            for (String e : favorites)
                favList.add(e);

            Log.d("FAVORITE SETTINGS GET", "The value of favorites recieved and is " + favorites);

//            favorites = Arrays.asList(favoriteItems);
//            favorites = new ArrayList<NewsItem>(favorites);
        } else
            return null;

        return favList;
    }

    public List<String> getFavoritesID(Context context)
    {
        SharedPreferences settings;
        Set<String> favorites;
        List<String> favList = new ArrayList<String>();
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            favorites = settings.getStringSet(FAVORITES, null);
            for (String e : favorites)
            {
                try {
                    JSONObject favJSON = new JSONObject(e);
                    String id = favJSON.getString("ID");
                    Log.d("FAVORITE GET ID", "The ID is " + id);
                    favList.add(id);
                } catch (JSONException e1) {
                    Log.d("FAVORITE ID SETTING", "Error retrieving ID");
                    e1.printStackTrace();
                }

            }


            Log.d("FAVORITE SETTINGS GET", "The value of favorites recieved and is " + favList);

//            favorites = Arrays.asList(favoriteItems);
//            favorites = new ArrayList<NewsItem>(favorites);
        } else
            return null;

        return favList;

    }
}
