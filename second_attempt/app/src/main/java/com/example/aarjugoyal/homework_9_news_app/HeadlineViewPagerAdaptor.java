package com.example.aarjugoyal.homework_9_news_app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aarjugoyal on 5/2/20.
 */

public class HeadlineViewPagerAdaptor extends FragmentPagerAdapter {

    List<Fragment> headlineList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();

    public HeadlineViewPagerAdaptor(FragmentManager fm) {
        super(fm);

    }

    public Fragment getItem(int position)
    {
        return headlineList.get(position);
    }

//    @Override
//    public Fragment getItem(int position) {
//        Log.d("SECTION HEADLINE SELECTION", "Value of adaptor is " + position);
//        Bundle bundle = new Bundle();
//        String section;
//        SectionFragment sectionHeadline = new SectionFragment();
//        switch (position) {
//            case 0:
//                section = "World";
//                Log.d("SECTION HEADLINE ADAPTOR", "Section selected as " + section);
//                bundle.putString("section", section );
//                sectionHeadline.setArguments(bundle);
//                return sectionHeadline;
//
//            case 1:
//                section = "Business";
//                Log.d("SECTION HEADLINE ADAPTOR", "Section selected as " + section);
//                bundle.putString("section", section );
//                sectionHeadline.setArguments(bundle);
//                return sectionHeadline;
////            case 2:
////                section = "Politics";
////                Log.d("SECTION HEADLINE ADAPTOR", "Section selected as " + section);
////                bundle.putString("section", section );
////                sectionHeadline.setArguments(bundle);
////                return sectionHeadline;
////            case 3:
////                section = "Technology";
////                Log.d("SECTION HEADLINE ADAPTOR", "Section selected as " + section);
////                bundle.putString("section", section );
////                sectionHeadline.setArguments(bundle);
////                return sectionHeadline;
////            case 4:
////                section = "Sport";
////                Log.d("SECTION HEADLINE ADAPTOR", "Section selected as " + section);
////                bundle.putString("section", section );
////                sectionHeadline.setArguments(bundle);
////                return sectionHeadline;
////            case 5:
////                section = "Science";
////                Log.d("SECTION HEADLINE ADAPTOR", "Section selected as " + section);
////                bundle.putString("section", section );
////                sectionHeadline.setArguments(bundle);
////                return sectionHeadline;
//            default:
//                return null;
//        }
//    }

    @Override
    public int getCount() {
        return headlineList.size();
    }

    @Override
    public int getItemPosition(@Nullable Object object) {
        return POSITION_NONE;
    }

    public void addFragment(Fragment fragment, String title)
    {

        headlineList.add(fragment);
        titleList.add(title);

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
