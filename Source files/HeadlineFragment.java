package com.example.aarjugoyal.homework_9_news_app;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeadlineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HeadlineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeadlineFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    HeadlineViewPagerAdaptor pageAdapter;
    TabItem tabWorld;
    TabItem tabBusiness;
    TabItem tabPolitics;
    TabItem tabTechnology;
    TabItem tabSports;
    TabItem tabScience;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public HeadlineFragment() {
        // Required empty public constructor
    }


    public static HeadlineFragment newInstance(String param1, String param2) {
        HeadlineFragment fragment = new HeadlineFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView =  inflater.inflate(R.layout.fragment_headline, container, false);
        toolbar = RootView.findViewById(R.id.toolbar);
//        toolbar.setTitle(getResources().getString(R.string.app_name));
//        setSupportActionBar(toolbar);



        tabLayout = RootView.findViewById(R.id.tablayout);
        tabWorld = RootView.findViewById(R.id.tabWorld);
        tabBusiness = RootView.findViewById(R.id.tabBusiness);
        tabPolitics = RootView.findViewById(R.id.tabPolitics);
        tabTechnology = RootView.findViewById(R.id.tabTechnology);
        tabSports = RootView.findViewById(R.id.tabSports);
        tabScience = RootView.findViewById(R.id.tabScience);
        viewPager = RootView.findViewById(R.id.viewPager);

////        HeadlineViewPagerAdaptor headlineAdaptor = new HeadlineViewPagerAdaptor(getFragmentManager(), tabLayout.getTabCount());
//        HeadlineViewPagerAdaptor headlineAdaptor = new HeadlineViewPagerAdaptor(getFragmentManager());
//        viewPager.setAdapter(headlineAdaptor);
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int tabPosition = tab.getPosition();
//                Log.d("SECTION HEADLINE" , "Value of position on Tab selected listener " + tabPosition);
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        return RootView;
    }

    public void onActivityCreated(@Nullable Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setUpViewPager(ViewPager viewPager) {
        HeadlineViewPagerAdaptor sectionAdaptor = new HeadlineViewPagerAdaptor(getChildFragmentManager());

        SectionFragment worldFragment = new SectionFragment();
        Bundle worldBundle = new Bundle();
        worldBundle.putString("section", "world");
        worldFragment.setArguments(worldBundle);
        sectionAdaptor.addFragment(worldFragment, "world");

        SectionFragment businessFragment = new SectionFragment();
        Bundle businessBundle = new Bundle();
        businessBundle.putString("section", "business");
        businessFragment.setArguments(businessBundle);
        sectionAdaptor.addFragment(businessFragment, "business");

        SectionFragment politicsFragment = new SectionFragment();
        Bundle politicsBundle = new Bundle();
        politicsBundle.putString("section", "politics");
        politicsFragment.setArguments(politicsBundle);
        sectionAdaptor.addFragment(politicsFragment, "politics");

        SectionFragment technologyFragment = new SectionFragment();
        Bundle technologyBundle = new Bundle();
        technologyBundle.putString("section", "technology");
        technologyFragment.setArguments(technologyBundle);
        sectionAdaptor.addFragment(technologyFragment, "technology");

        SectionFragment sportsFragment = new SectionFragment();
        Bundle sportsBundle = new Bundle();
        sportsBundle.putString("section", "sports");
        sportsFragment.setArguments(sportsBundle);
        sectionAdaptor.addFragment(sportsFragment, "sports");

        SectionFragment scienceFragment = new SectionFragment();
        Bundle scienceBundle = new Bundle();
        scienceBundle.putString("section", "science");
        scienceFragment.setArguments(scienceBundle);
        sectionAdaptor.addFragment(scienceFragment, "science");

        viewPager.setAdapter(sectionAdaptor);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
