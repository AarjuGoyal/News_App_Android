package com.example.aarjugoyal.homework_9_news_app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;


public class SectionFragment extends android.support.v4.app.Fragment {

    private static final String ARG_PARAM1 = "section";

    private String Section;

    RecyclerView recyclerViewSection;
    AdaptorHomeNews adapter_section_news;
    RequestQueue sectionHeadlineQueue;
    List<NewsItem> SectionHeadline = new ArrayList<>();
    AdaptorHomeNews adaptor_section_news;
    int sectionNewsPageSize = 0;
    ProgressBar spinner;
    LinearLayout progressBarLayout;

    private OnFragmentInteractionListener mListener;
    String section;

    private SwipeRefreshLayout mSwipeRefreshLayoutSection;
    public SectionFragment() {
        // Required empty public constructor
    }


    public static SectionFragment newInstance(String param1, String param2) {
        SectionFragment fragment = new SectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Section = getArguments().getString(ARG_PARAM1);
            Log.d("SECTION HEADLINE", "Argument recieved is " + Section);


        }
       ;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String mySection = this.getArguments().getString("section");
        Log.d("SECTION HEADLINE VIEW ", "Argument recieved is ");
        View RootView =  inflater.inflate(R.layout.fragment_section, container, false);

        progressBarLayout = RootView.findViewById(R.id.llProgressBarSection);

        progressBarLayout.setVisibility(View.VISIBLE);
        getSectionHeadlines(Section);


        recyclerViewSection = RootView.findViewById(R.id.rv_section_news);
        recyclerViewSection.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        Log.d("SECTION HEADLINES VIEW ", "Before setting adaptor for section headlines ");

        adaptor_section_news = new AdaptorHomeNews(getActivity(), SectionHeadline, sectionNewsPageSize);
        recyclerViewSection.setAdapter(adaptor_section_news);

        mSwipeRefreshLayoutSection = RootView.findViewById(R.id.swiperefresh_items_section);

        return RootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayoutSection.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSectionHeadlines(Section);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mSwipeRefreshLayoutSection.isRefreshing()) {
                            mSwipeRefreshLayoutSection.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });
    }

    private void getSectionHeadlines(final String section)
    {

        Log.d("GET SECTION HEADLINES", "Getting the news headlines for section " + section);
        sectionHeadlineQueue = Volley.newRequestQueue(getActivity());

        String URL = "https://homework8backend.wm.r.appspot.com/guardiantimessection?section=" + section;
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject sectionNewsJSON = new JSONObject(response);
                    Log.d("GET SECTION HEADLINES " + section, "Values recieved are " + response);
                    JSONArray results = null;
                    sectionNewsJSON = sectionNewsJSON.getJSONObject("articles").getJSONObject("response");
                    Log.d("GET SECTION HEADLINES " + section, "Section JSON is " + sectionNewsJSON);
                    results = sectionNewsJSON.getJSONArray("results");
                    int pageSize = sectionNewsJSON.getInt("pageSize");
                    sectionNewsPageSize = pageSize;
                    Log.d("GET SECTION HEADLINES " + section, "Results are " + results);
                    for(int i=0; i<pageSize; i++)
                    {
                        NewsItem home_news = new NewsItem();
                        home_news.addSectionFromJSON(results.getJSONObject(i));
//                                    home_news
                        SectionHeadline.add(home_news);
                    }
                    Log.d("SECTION NEWS ITEM CLASS", "List of guardian home news is " + SectionHeadline);

                    adapter_section_news = new AdaptorHomeNews(getActivity(), SectionHeadline, sectionNewsPageSize);
                    recyclerViewSection.setAdapter(adapter_section_news);
                    recyclerViewSection.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                    progressBarLayout.setVisibility(View.GONE);

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
        sectionHeadlineQueue.add(request);

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {

        super.onResume();
        if(adapter_section_news != null)
        {
            getSectionHeadlines(Section);
        }
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
