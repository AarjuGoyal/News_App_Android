package com.example.aarjugoyal.homework_9_news_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.location.LocationManager.GPS_PROVIDER;


public class MainActivity extends AppCompatActivity implements LocationListener {

    String city = "Los Angeles";


    TextView Tv_Weather_City;
    TextView Tv_Weather_summary;
    TextView Tv_Current_Temp;
    TextView Tv_Current_State;
    RelativeLayout Weather_card_layout;

    android.support.v7.widget.Toolbar mToolbar;

//    LocationManager locationManager;
    String provider;
    String provider_info;
    Location location;
    double latitude;
    double longitude;

    RecyclerView recyclerViewHomeNews;
    AdaptorHomeNews adapter_home_news;

    BottomNavigationView bottomNavigation;
    android.support.v7.widget.Toolbar searchMenu;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;

    AutoSuggestAdaptor autoSuggestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle("NewsApp");


        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

//        searchMenu = findViewById(R.id.menuActionBar);
//
//
//        // Get ActionBar
//        ActionBar actionBar = setActionBar(searchMenu);
//        // Set below attributes to add logo in ActionBar.



//        actionBar.setTitle("NewsApp");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            openFragment(new HomeFragment());

        }
        else
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        openFragment(new HomeFragment());
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.d("SEARCH MENU", "in Oncreate option menu");
        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search_bar, menu);

        Log.d("SEARCH MENU", "in Oncreate option menu");
        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.action_search);

        // Get SearchView object.
        SearchView searchView = (SearchView) searchMenu.getActionView();

        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setThreshold(3);
//        searchAutoComplete.setBackgroundColor(Color.BLUE);
//        searchAutoComplete.setTextColor(Color.GREEN);
//        searchAutoComplete.setDropDownBackgroundResource(android.R.color.holo_blue_light);

//        // Create a new ArrayAdapter and add data to search auto complete object.
//        String dataArr[] = {"Apple" , "Amazon" , "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
//        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);


        autoSuggestAdapter = new AutoSuggestAdaptor(this,
                android.R.layout.simple_dropdown_item_1line);

        searchAutoComplete.setAdapter(autoSuggestAdapter);


        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                        getAutoSuggestValues(searchAutoComplete.getText().toString());
                    }
                }
                return false;
            }
        });

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
//                Toast.makeText(MainActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();

                final Intent SearchArticleIntent;
                SearchArticleIntent = new Intent(MainActivity.this, SearchActivity.class);
                SearchArticleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                SearchArticleIntent.putExtra("query",queryString);
                startActivity(SearchArticleIntent);
            }
        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage("Search keyword is " + query);
                alertDialog.show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



//
        return super.onCreateOptionsMenu(menu);
    }

    public void getAutoSuggestValues(String keyword) {


        RequestQueue SearchQueue;
        final String BING_API_KEY =
                "645528c5f500464fbe9a9f3af5098971";
        final String BING_AUTOSUGGEST_URL =
                "https://api.cognitive.microsoft.com/bing/v7.0/suggestions?q=" + keyword;
        SearchQueue = Volley.newRequestQueue(this.getApplicationContext());

        Log.d("AUTOSUGGEST", "In autosuggest getting values");
        String URL = BING_AUTOSUGGEST_URL;
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<String> suggestList = new ArrayList<>();
                try {
                    JSONObject suggestResponse = new JSONObject(response);
                    Log.d("AUTOSUGGEST", "Response recieved is " + suggestResponse);

                    JSONArray suggestions = suggestResponse.getJSONArray("suggestionGroups").getJSONObject(0).getJSONArray("searchSuggestions");
                    for (int i = 0; i < suggestions.length(); i++) {
                        JSONObject row = suggestions.getJSONObject(i);
                        suggestList.add(row.getString("displayText"));
                    }



                } catch (JSONException e) {
                    Log.d("AUTOSUGGEST ERROR", "Error trying to connect to API");
                    e.printStackTrace();
                }
                autoSuggestAdapter.setData(suggestList);
                autoSuggestAdapter.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Ocp-Apim-Subscription-Key", "645528c5f500464fbe9a9f3af5098971");
                return headers;
            }
            @Override
            public Map<String, String> getParams() {
                Map<String, String> mParams = new HashMap<>();
                mParams.put("Ocp-Apim-Subscription-Key", "645528c5f500464fbe9a9f3af5098971");
                return mParams;
            }

        };
        SearchQueue.add(request);

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigationHome:
                            openFragment(HomeFragment.newInstance("", ""));
                            return true;
                        case R.id.navigationHeadlines:
                            openFragment(HeadlineFragment.newInstance("", ""));
                            return true;
                        case R.id.navigationTrending:
                            openFragment(TrendFragment.newInstance("", ""));
                            return true;
                        case R.id.navigationFavorites:
                            openFragment(FavoriteFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };


//    private void getHomeNewsData()
//    {
//        Log.d("GET NEWS GUARDIAN", "Inside the function");
//
//        new Thread(){
//
//            @Override
//            public void run(){
//
//
//                final JSONObject newsJSON = RemoteFetch_Guardian_HomeNews.getJSON(getApplicationContext());
//                if(newsJSON == null)
//                {
//                    Log.d("GUARDIAN NEWS DATA", "guardian news is null");
//
//
//                }
//                else
//                {
//                    Log.d("GUARDIAN NEWS DATA", "guardian news JSON Data is " + newsJSON);
////                    renderGuardianHomeNews(newsJSON);
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.d("GUARDIAN NEWS RENDER", "newsJSON is " + newsJSON);
//                            JSONArray results = null;
//                            try
//                            {
//                                results = newsJSON.getJSONArray("results");
//                                int pageSize = newsJSON.getInt("pageSize");
//                                Log.d("GUARDIAN NEWS RENDER", "Results are " + results);
//                                List<NewsItem> GuardianHomeNews = new ArrayList<>();;
//                                for(int i=0; i<pageSize; i++)
//                                {
//                                    NewsItem home_news = new NewsItem();
//                                    home_news.addFromJSON(results.getJSONObject(i));
////                                    home_news
//                                    GuardianHomeNews.add(home_news);
//                                }
//                                Log.d("NEWS ITEM CLASS", "List of guardian home news is " + GuardianHomeNews);
//
//                                adapter_home_news = new AdaptorHomeNews(getApplicationContext(), GuardianHomeNews, pageSize);
//                                recyclerViewHomeNews.setAdapter(adapter_home_news);
//
////                                adapter_home_news = new AdaptorHomeNews(getApplicationContext(), results, pageSize);
////                                adapter_home_news.setClickListener(this);
////                                recyclerViewHomeNews.setAdapter(adapter_home_news);
//                            }
//                            catch (JSONException e1) {
//                                e1.printStackTrace();
//
//                            }
//
//                        }
//                    });
//                }
//            }
//
//
//        }.start();
//    }


//
//
//    private void updateWeatherData(final String city){
//        Log.d("IN WEATHER UPDATION", "Inside the function");
//        new Thread(){
//            public void run(){
//                final JSONObject weatherJSON = RemoteFetch_Weather.getJSON(getApplicationContext(), city);
//                if(weatherJSON == null)
//                {
//                        Log.d("WEATHER DATA", "weather data is null");
//
//
//                }
//                else
//                {
//                    Log.d("WEATHER DATA", "weather JSON Data is " + weatherJSON);
//                    renderWeather(weatherJSON);
//                }
//            }
//        }.start();
//    }
//
//    private void renderWeather(final JSONObject weatherJSON){
//        Log.d("WEATHER SET DATA", "In render weather method");
//        try {
//
//            final String city_weather = weatherJSON.getString("name");
//            Log.d("WEATHER SET VALUES", "Value of city is " + city_weather);
//            final JSONObject sys_weather = weatherJSON.getJSONObject("sys");
//            Log.d("WEATHER SET VALUES", "Value of sys  is " + sys_weather );
//
//            final JSONObject details_weather = weatherJSON.getJSONArray("weather").getJSONObject(0);
//            Log.d("WEATHER SET VALUES ", "Value of details "+ details_weather);
//
//            final JSONObject main_weather = weatherJSON.getJSONObject("main");
//            Log.d("WEATHER SET VALUES ", "Value of details "+ main_weather);
//
//            final String weather_summary = details_weather.getString("main");
//            final int Weather_Image;
//
//            if(Objects.equals(weather_summary, "Clouds"))
//            {
//                Weather_Image = R.drawable.cloudy_weather;
//            }
//            else if(Objects.equals(weather_summary, "Clear"))
//            {
//                Weather_Image = R.drawable.clear_weather;
//            }
//            else if(Objects.equals(weather_summary, "Snow"))
//            {
//                Weather_Image = R.drawable.snowy_weather;
//            }
//            else if(Objects.equals(weather_summary, "Rain") || Objects.equals(weather_summary, "Drizzle"))
//            {
//                Weather_Image = R.drawable.rainy_weather;
//            }
//            else if(Objects.equals(weather_summary, "Thunderstorm"))
//            {
//                Weather_Image = R.drawable.thunder_weather;
//            }
//            else
//            {
//                Weather_Image = R.drawable.sunny_weather;
//            }
//
//            runOnUiThread(new Runnable()
//            {
//
//                @Override
//                public void run() {
//
//                    try {
//                        Tv_Weather_City.setText(city_weather);
//
//                        Log.d("WEATHER SET VALUE", "Value of city set");
//
//                        Tv_Current_State.setText(sys_weather.getString("country"));
//
//                        Tv_Weather_summary.setText( weather_summary);
//
//                        Tv_Current_Temp.setText(String.format("%.2f", main_weather.getDouble("temp"))+ " ℃");
//
//                        Weather_card_layout.setBackground(ContextCompat.getDrawable(getApplicationContext(),Weather_Image ));
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//        }catch(Exception e){
//
//            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
//            Log.e("WEATHER SET ERROR", "Error is " + e);
//        }
//    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    Log.d("LOCATION PERMISSION", "Permission was granted");
//                    // permission was granted, yay! Do the
//                    // location-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
////                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
//
//                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60 * 1, 10, this);
//                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                        Log.d("LOCATION PERMISSIOn", "Value of provider is " + provider_info);
//
//
//                        Log.d("LOCATION", "Value of location is " + location);
//                        updateGPSCoordinates();
//                        Log.d("LOCATION SETTINGS", "Value of field latitude " + latitude);
//                        Log.d("LOCATION SETTINGS", "Value of field longitude " + longitude);
//                        updateWeatherData(city);
//
//                        recyclerViewHomeNews = findViewById(R.id.my_recycler_view);
//                        recyclerViewHomeNews.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//                        getHomeNewsData();
//
//
//                    }
//
//                } else {
//                    Log.d("LOCATION PERMISSION", "Permission was not granted");
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//
//                }
//                return;
//            }
//
//        }
//    }
    /**
     * Update GPSTracker latitude and longitude
     */


//    public void updateGPSCoordinates() {
//        Log.d("LOCATION SETTINGS","Update GPS coordinates");
//        if (location != null) {
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//        }
//        else
//        {
//            Log.d("LOCATION SETTINGS","location was null");
//        }
////    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("LOCATION PERMISSION", "Permission was granted");
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 400, 1, (LocationListener) this);
            Log.d("LOCATION PERMISSIOn", "Value of provider is " + provider);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("LOCATION PERMISSION", "Permission grqant paused");
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.removeUpdates((LocationListener) this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}


