package com.example.aarjugoyal.homework_9_news_app;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    String query;
    LinearLayout progressBarLayout;
    RequestQueue SearchQueue;

    RecyclerView recyclerViewSearchNews;
    AdaptorHomeNews adaptorSearchNews;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    Toolbar dToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        progressBarLayout = findViewById(R.id.llProgressBarDetail);

        progressBarLayout.setVisibility(View.VISIBLE);

        Intent previousIntent = getIntent(); // gets the previously created intent
        final String query = previousIntent.getStringExtra("query"); // will return "FirstKeyValue"

        Log.d("SEARCH QUERY","Query recieved as " + query);

        dToolbar= findViewById(R.id.toolbarSearch);
        setSupportActionBar(dToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);



        String Title = "Search results for " + query;
        getSupportActionBar().setTitle(Title);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getSearchResults(query);

        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_items_search);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSearchResults(query);
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

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(adaptorSearchNews != null)
        {
            adaptorSearchNews.notifyDataSetChanged();
        }
    }

    public void getSearchResults(String query)
    {
        SearchQueue = Volley.newRequestQueue(this);

        String URL = "https://homework8backend.wm.r.appspot.com/searchguardianarticles?q=" + query;
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject searchNewJSON = new JSONObject(response);
                    Log.d("GET HOME NEWS", "Values recieved are " + response);
                    JSONArray results = null;
                    searchNewJSON = searchNewJSON.getJSONObject("articles").getJSONObject("response");
                    Log.d("GET HOME NEWS", "Section JSON is " + searchNewJSON);


                    results = searchNewJSON.getJSONArray("results");
                    int pageSize = searchNewJSON.getInt("pageSize");
                    Log.d("GUARDIAN NEWS RENDER", "Results are " + results);
                    List<NewsItem> GuardianSearchNews = new ArrayList<>();
                    for(int i=0; i<results.length(); i++)
                    {
                        NewsItem home_news = new NewsItem();
                        home_news.addSectionFromJSON(results.getJSONObject(i));
                        GuardianSearchNews.add(home_news);
                    }
                    Log.d("SECTION NEWS ITEM CLASS", "List of guardian home news is " + GuardianSearchNews);

                    adaptorSearchNews = new AdaptorHomeNews(SearchActivity.this, GuardianSearchNews, pageSize);

                    recyclerViewSearchNews = findViewById(R.id.recycle_view_search);
                    recyclerViewSearchNews.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerViewSearchNews.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    recyclerViewSearchNews.setAdapter(adaptorSearchNews);


                    progressBarLayout.setVisibility(View.GONE);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Could not parse response recieved", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        });
        SearchQueue.add(request);
    }
}
