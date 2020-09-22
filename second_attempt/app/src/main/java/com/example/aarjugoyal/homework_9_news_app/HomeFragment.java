package com.example.aarjugoyal.homework_9_news_app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.support.v4.content.ContextCompat.getSystemService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends android.support.v4.app.Fragment {

    String city = "Los Angeles";


    TextView Tv_Weather_City;
    TextView Tv_Weather_summary;
    TextView Tv_Current_Temp;
    TextView Tv_Current_State;
    RelativeLayout Weather_card_layout;

    String provider;
    String provider_info;
    Location location;
    double latitude;
    double longitude;

    RecyclerView recyclerViewHomeNews;
    AdaptorHomeNews adapter_home_news;

    LinearLayout llProgressBarHome;

    RequestQueue HomeNewsQueue;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        checkLocationPermission();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View RootView =  inflater.inflate(R.layout.fragment_home, container, false);

        llProgressBarHome = RootView.findViewById(R.id.llProgressBarHome);
        llProgressBarHome.setVisibility(View.VISIBLE);

        recyclerViewHomeNews =(RecyclerView) RootView.findViewById(R.id.my_recycler_view);
        recyclerViewHomeNews.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        mSwipeRefreshLayout = RootView.findViewById(R.id.swiperefresh_items);
        return RootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomeNews();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onItemClick(View view, int position) {
//        Log.d("NEWS CLICK", "onItemClick function position " + position);
//        Toast.makeText(getActivity(), "You clicked " + adapter_home_news.getItem(position) + " on row number " + position, Toast.LENGTH_LONG).show();
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Tv_Weather_City = (TextView) getView().findViewById(R.id.Weather_city);
        Tv_Weather_summary = (TextView) getView().findViewById(R.id.Weather_summary);
        Tv_Current_Temp = (TextView) getView().findViewById(R.id.Weather_temperature);
        Tv_Current_State = (TextView) getView().findViewById(R.id.Weather_state);
        Weather_card_layout = (RelativeLayout) getView().findViewById(R.id.Weather_Card_layout);
    }
    private void updateWeatherData(final String city, final String state){
        Log.d("IN WEATHER UPDATION", "Inside the function");
        new Thread(){
            public void run(){
                final JSONObject weatherJSON = RemoteFetch_Weather.getJSON(getContext(), city);
                if(weatherJSON == null)
                {
                    Log.d("WEATHER DATA", "weather data is null");


                }
                else
                {
                    Log.d("WEATHER DATA", "weather JSON Data is " + weatherJSON);
                    renderWeather(weatherJSON, state);
                }
            }
        }.start();
    }

    private void renderWeather(final JSONObject weatherJSON, final String state){
        Log.d("WEATHER SET DATA", "In render weather method");
        try {

            final String city_weather = weatherJSON.getString("name");
            Log.d("WEATHER SET VALUES", "Value of city is " + city_weather);
            final JSONObject sys_weather = weatherJSON.getJSONObject("sys");
            Log.d("WEATHER SET VALUES", "Value of sys  is " + sys_weather );

            final JSONObject details_weather = weatherJSON.getJSONArray("weather").getJSONObject(0);
            Log.d("WEATHER SET VALUES ", "Value of details "+ details_weather);

            final JSONObject main_weather = weatherJSON.getJSONObject("main");
            Log.d("WEATHER SET VALUES ", "Value of details "+ main_weather);

            final String weather_summary = details_weather.getString("main");
            final int Weather_Image;

            if(Objects.equals(weather_summary, "Clouds"))
            {
                Weather_Image = R.drawable.cloudy_weather;
            }
            else if(Objects.equals(weather_summary, "Clear"))
            {
                Weather_Image = R.drawable.clear_weather;
            }
            else if(Objects.equals(weather_summary, "Snow"))
            {
                Weather_Image = R.drawable.snowy_weather;
            }
            else if(Objects.equals(weather_summary, "Rain") || Objects.equals(weather_summary, "Drizzle"))
            {
                Weather_Image = R.drawable.rainy_weather;
            }
            else if(Objects.equals(weather_summary, "Thunderstorm"))
            {
                Weather_Image = R.drawable.thunder_weather;
            }
            else
            {
                Weather_Image = R.drawable.sunny_weather;
            }

            getActivity().runOnUiThread(new Runnable()
            {

                @Override
                public void run() {

                    try {
                        Tv_Weather_City.setText(city_weather);

                        Log.d("WEATHER SET VALUE", "Value of city set");

                        Tv_Current_State.setText(state);

                        Tv_Weather_summary.setText( weather_summary);

                        Tv_Current_Temp.setText(String.format("%.2f", main_weather.getDouble("temp"))+ " ℃");

                        Weather_card_layout.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(),Weather_Image ));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }catch(Exception e){

            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            Log.e("WEATHER SET ERROR", "Error is " + e);
        }
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {

//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
////                new AlertDialog.Builder(getActivity())
////                        .setTitle("Title for News App permission")
////                        .setMessage("Message for News App permission")
////                        .setPositiveButton("Positive button", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialogInterface, int i) {
////                                //Prompt the user once explanation has been shown
////                                ActivityCompat.requestPermissions(getActivity(),
////                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
////                                        MY_PERMISSIONS_REQUEST_LOCATION);
////                            }
////                        })
////                        .create()
////                        .show();
//
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;


                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                return true;

        }
        else
        {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60 * 1, 10, (LocationListener) getActivity());
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.d("LOCATION PERMISSIOn", "Value of provider is " + provider_info);


            Log.d("LOCATION", "Value of location is " + location);
            updateGPSCoordinates();
            Log.d("LOCATION SETTINGS", "Value of field latitude " + latitude);
            Log.d("LOCATION SETTINGS", "Value of field longitude " + longitude);

            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String cityName = addresses.get(0).getLocality();

                String state = addresses.get(0).getAdminArea();
                Log.d("LOCATION VALUES", "Value of city is " + cityName + " state " + state );

                updateWeatherData(cityName, state);

            } catch (IOException e) {

                e.printStackTrace();
            }

            getHomeNews();
            return true;
        }
    }
    private void getHomeNews()
    {

        final String GUARDIAN_NEWS_API =
                "360d55ef-f616-4733-b330-a5bbebd68d61";
        final String GUARDIAN_HOME_NEWS_URL =
                "https://homework8backend.wm.r.appspot.com/guardiantimesHomeNews";
        HomeNewsQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String URL = GUARDIAN_HOME_NEWS_URL;
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject homeNewsJSON = new JSONObject(response);
                    Log.d("GET HOME NEWS", "Values recieved are " + response);
                    JSONArray results = null;
                    homeNewsJSON = homeNewsJSON.getJSONObject("articles").getJSONObject("response");
                    Log.d("GET HOME NEWS", "Section JSON is " + homeNewsJSON);


                    results = homeNewsJSON.getJSONArray("results");
                    int pageSize = homeNewsJSON.getInt("pageSize");
                    Log.d("GUARDIAN NEWS RENDER", "Results are " + results);
                    List<NewsItem> GuardianHomeNews = new ArrayList<>();
                    for(int i=0; i<pageSize; i++)
                    {
                        NewsItem home_news = new NewsItem();
                        home_news.addFromJSON(results.getJSONObject(i));
                        GuardianHomeNews.add(home_news);
                    }
                    Log.d("SECTION NEWS ITEM CLASS", "List of guardian home news is " + GuardianHomeNews);

                    adapter_home_news = new AdaptorHomeNews(getActivity(), GuardianHomeNews, pageSize);
                    recyclerViewHomeNews.setAdapter(adapter_home_news);
                    recyclerViewHomeNews.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                    llProgressBarHome.setVisibility(View.GONE);

                } catch (JSONException e) {
                    Toast.makeText(getActivity(),"Could not parse response recieved", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        });
        HomeNewsQueue.add(request);

    }

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
//                final JSONObject newsJSON = RemoteFetch_Guardian_HomeNews.getJSON(getActivity().getApplicationContext());
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
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.d("GUARDIAN NEWS RENDER", "newsJSON is " + newsJSON);
//                            JSONArray results = null;
//                            try
//                            {
//                                results = newsJSON.getJSONArray("results");
//                                int pageSize = newsJSON.getInt("pageSize");
//                                Log.d("GUARDIAN NEWS RENDER", "Results are " + results);
//                                List<NewsItem> GuardianHomeNews = new ArrayList<>();
//                                for(int i=0; i<pageSize; i++)
//                                {
//                                    NewsItem home_news = new NewsItem();
//                                    home_news.addFromJSON(results.getJSONObject(i));
//                                    GuardianHomeNews.add(home_news);
//                                }
//                                Log.d("NEWS ITEM CLASS", "List of guardian home news is " + GuardianHomeNews);
//
//                                adapter_home_news = new AdaptorHomeNews(getActivity().getApplicationContext(), GuardianHomeNews, pageSize);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d("LOCATION PERMISSION", "Permission was granted");
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
//                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);

                        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60 * 1, 10, (LocationListener) getActivity());
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Log.d("LOCATION PERMISSIOn", "Value of provider is " + provider_info);


                        Log.d("LOCATION", "Value of location is " + location);
                        updateGPSCoordinates();
                        Log.d("LOCATION SETTINGS", "Value of field latitude " + latitude);
                        Log.d("LOCATION SETTINGS", "Value of field longitude " + longitude);

                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            String cityName = addresses.get(0).getLocality();

                            String state = addresses.get(0).getAdminArea();
                            Log.d("LOCATION VALUES", "Value of city is " + cityName + " state " + state );

                            updateWeatherData(cityName, state);

                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                        recyclerViewHomeNews = getView().findViewById(R.id.my_recycler_view);
//                        recyclerViewHomeNews.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        recyclerViewHomeNews.setLayoutManager(new LinearLayoutManager(getActivity()));

                        getHomeNews();


                    }

                } else {
                    Log.d("LOCATION PERMISSION", "Permission was not granted");

                }
                return;
            }

        }
    }
    /**
     * Update GPSTracker latitude and longitude
     */
    public void updateGPSCoordinates() {
        Log.d("LOCATION SETTINGS","Update GPS coordinates");
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        else
        {
            Log.d("LOCATION SETTINGS","location was null");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("LOCATION PERMISSION", "Permission was granted");
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 400, 1, (LocationListener) getActivity());
            Log.d("LOCATION PERMISSIOn", "Value of provider is " + provider);
        }

        if(adapter_home_news != null)
        {
            adapter_home_news.notifyDataSetChanged();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("LOCATION PERMISSION", "Permission grqant paused");
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.removeUpdates((LocationListener) getActivity());
        }
    }




}

