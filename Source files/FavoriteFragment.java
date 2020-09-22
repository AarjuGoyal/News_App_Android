package com.example.aarjugoyal.homework_9_news_app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class FavoriteFragment extends android.support.v4.app.Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SharedPreferenceNews sharedPreference;
    TextView favoriteList;
    FavoriteAdaptor adapter;

    List<String> list;
    private RecyclerView recyclerView;

LinearLayout llNoBookmark;
    private OnFragmentInteractionListener mListener;



    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreference = new SharedPreferenceNews();
        list = sharedPreference.getFavorites(getActivity().getApplicationContext());
        adapter = new FavoriteAdaptor(getActivity(), list);

    }

    public static <T> List<T> convertSetToList(Set<T> set)
    {
        // create a list from Set
        List<T> list = new ArrayList<>(set);

        // return the list
        return list;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView =  inflater.inflate(R.layout.fragment_favorite, container, false);

        llNoBookmark = RootView.findViewById(R.id.noBookmarkLayout);
        if(list.isEmpty())
        {
            llNoBookmark.setVisibility(View.VISIBLE);
        }
        else
        {
            llNoBookmark.setVisibility(View.GONE);
        }



        Log.d("FAVORITE FRAGMENT", "The list is "+ list);

        recyclerView = (RecyclerView) RootView.findViewById(R.id.favoriteRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.setAdapter(adapter);

        return RootView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null)
        {
            list = sharedPreference.getFavorites(getActivity().getApplicationContext());

            if(list.isEmpty())
            {
                llNoBookmark.setVisibility(View.VISIBLE);
            }
            else
            {
                llNoBookmark.setVisibility(View.GONE);
            }

            adapter = new FavoriteAdaptor(getActivity(), list);

            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        }
    }

    public interface OnFragmentInteractionListener {


        void onFragmentInteraction(Uri uri);

    }
}
