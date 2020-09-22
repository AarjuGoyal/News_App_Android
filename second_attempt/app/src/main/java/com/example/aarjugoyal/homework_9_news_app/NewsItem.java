package com.example.aarjugoyal.homework_9_news_app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static android.provider.Settings.System.AUTO_TIME_ZONE;
import static android.provider.Settings.System.DATE_FORMAT;

/**
 * Created by aarjugoyal on 4/25/20.
 */

public class NewsItem {
    private String Title;
    private String id;
    String publicationDate;
    private String section;
    private String ImgURL = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
    private String timePassed;

    public void addTitle(String p_title) { Title = p_title;}
    public void addImg(String url) { ImgURL = url;}
    public void addID(String p_id) { id = p_id;}
    public void addPublicationDate(String p_date) { Log.d("NEWS ITEM SETTING","Publiocation date added" + p_date);publicationDate = p_date; }
    public void addSection(String p_section){ section = p_section;}
    public void addTimePassed(String p_t) { timePassed = p_t;}

    public String getTitle()
    {
        return Title;
    }
    public String getId()
    {
        return id;
    }
    public String getPublicationDate()
    {
        return publicationDate;
    }
    public String getSection() { return section;}
    public String getImgURL() {return ImgURL;}
    public String getTimePassed() {return timePassed;}

    public void addFromJSON(JSONObject newsJSON) throws JSONException {
        addTitle(newsJSON.getString("webTitle"));
        addID(newsJSON.getString("id"));
        addSection(newsJSON.getString("sectionName"));
        if(newsJSON.getJSONObject("fields").has("thumbnail"))
        {
            String imgurl = newsJSON.getJSONObject("fields").getString("thumbnail");
            Log.d("GUARDIAN NEWS CARD", "Value of img url is " + imgurl);
            if(imgurl != null)
            {
                addImg(imgurl);
            }
        }

        String timePublished = newsJSON.getString("webPublicationDate");
        addPublicationDate(timePublished);
        ZonedDateTime publishedDate = ZonedDateTime.parse(timePublished);

        ZoneId LA_zone = ZoneId.of( "America/Los_Angeles" );


        ZonedDateTime zdLA_date = publishedDate.withZoneSameInstant(LA_zone);
        ZonedDateTime currTime = ZonedDateTime.now();
        Duration dur = Duration.between(zdLA_date, currTime);
        long seconds = dur.getSeconds();
        int hours = (int)seconds/3600;
        int minutes = (int) seconds/60;
        int second = (int) seconds;
        String timePassed;
        if(hours >= 1)
        {
            timePassed = hours + "h ago";
        }
        else if(hours<1 && minutes >= 1)
        {
            timePassed = minutes + "m ago";
        }
        else
        {
            timePassed = second + "s ago";
        }
        addTimePassed(timePassed);

        Log.d("GUARDIAN NEWS CARD", "Date zone in  LA " + zdLA_date + " current time " + currTime);
        Log.d("GUARDIAN NEWS CARD", "Hours " + hours + " minutes " + minutes + " seconds " + second);

    }

    public void addSectionFromJSON(JSONObject newsJSON) throws JSONException {
        addTitle(newsJSON.getString("webTitle"));
        addID(newsJSON.getString("id"));
        addSection(newsJSON.getString("sectionName"));
        try
        {
            JSONArray assets = newsJSON.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements").getJSONObject(0).getJSONArray("assets");
            if(assets.length() > 0)
            {
                String imgurl = assets.getJSONObject(0).getString("file");
                Log.d("GUARDIAN NEWS CARD", "Value of img url is " + imgurl);
                if(imgurl != null)
                {
                    addImg(imgurl);
                }
            }
        }
        catch(JSONException e)
        {
            Log.d("SECTION NEWS IMAGE LOAD","Attribute missing in path");
            e.printStackTrace();
        }


        String timePublished = newsJSON.getString("webPublicationDate");
        addPublicationDate(timePublished);
        ZonedDateTime publishedDate = ZonedDateTime.parse(timePublished);

        ZoneId LA_zone = ZoneId.of( "America/Los_Angeles" );


        ZonedDateTime zdLA_date = publishedDate.withZoneSameInstant(LA_zone);
        ZonedDateTime currTime = ZonedDateTime.now();
        Duration dur = Duration.between(zdLA_date, currTime);
        long seconds = dur.getSeconds();

        int hours = (int)seconds/3600;
        int days = (int) hours/24;
        int minutes = (int) seconds/60;
        int second = (int) seconds;
        String timePassed;
        if(hours >= 24)
        {
            timePassed = days + "d ago";
        }
        else if(hours >= 1)
        {
            timePassed = hours + "h ago";
        }
        else if(hours<1 && minutes >= 1)
        {
            timePassed = minutes + "m ago";
        }
        else
        {
            timePassed = second + "s ago";
        }
        addTimePassed(timePassed);

        Log.d("GUARDIAN NEWS CARD", "Date zone in  LA " + zdLA_date + " current time " + currTime);
        Log.d("GUARDIAN NEWS CARD", "Hours " + hours + " minutes " + minutes + " seconds " + second);

    }

    public String getArticleID() {
        return id;
    }

    @Override
    public String toString()
    {
        String completeString = "{\"ID\":" + '"' + getArticleID() + '"' +
                ",\"Title\":\"" + getTitle() + "\",\"Date\":\"" + publicationDate + "\",\"Section\":\"" + getSection() + "\",\"img\":\""
                + getImgURL() + "\"}";
        Log.d("NEWS ITEM To STRING", "The String of the news item is " + completeString);
        return completeString;
    }
}
