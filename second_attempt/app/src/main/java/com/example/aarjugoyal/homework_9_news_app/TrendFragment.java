package com.example.aarjugoyal.homework_9_news_app;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
//

//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrendFragment extends android.support.v4.app.Fragment {




    SearchView searchViewTrend;
    TextView tvTemp;
    RequestQueue queue;
    LineChart trendingChart;

    private OnFragmentInteractionListener mListener;

    public TrendFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TrendFragment newInstance(String param1, String param2) {
        TrendFragment fragment = new TrendFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_trend, container, false);;
        searchViewTrend = (SearchView) RootView.findViewById(R.id.searchTrend);



        searchViewTrend.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d("SEARCH TREND", "Search query submitted is " + query);
                FetchTrendResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });
        trendingChart = (LineChart) RootView.findViewById(R.id.linechart);
        FetchTrendResults("CoronaVirus");
        return RootView;
    }

    boolean FetchTrendResults(final String keyword)
    {
        queue = Volley.newRequestQueue(getActivity());
//        setRetryPolicy(new DefaultRetryPolicy(3*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        String URL = "https://homework8backend.wm.r.appspot.com/gettrend?keyword=" + keyword;
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                tvTemp.setText(response.toString());
//                Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                Log.d("TREND GRAPH", "request response recieved " + response);
                try {
                    JSONObject trendResponse = new JSONObject(response);
                    JSONArray timeLine = trendResponse.getJSONObject("trend").getJSONObject("default").getJSONArray("timelineData");
                    List<String> xVals = new ArrayList<String>();
                    List<Entry> yVals = new ArrayList<Entry>();
                    int i;
                    for(i=0; i<timeLine.length(); i++)
                    {
                        float y = timeLine.getJSONObject(i).getJSONArray("value").getInt(0);
                        Log.d("TREND CHART VALUES", "For position of " + i + " value of trend is " + y);
                        xVals.add(String.valueOf(i));
                        Entry trend = new Entry(i,y);
                        yVals.add(trend);

                    }
                    String label = "Trending Chart for " + keyword;
                    LineDataSet trendSet = new LineDataSet(yVals, label);
                    trendSet.setColor(Color.parseColor("#8080ff"));
                    trendSet.setCircleColor(Color.parseColor("#8080ff"));
                    trendSet.setLineWidth(1f);
                    trendSet.setCircleRadius(3f);
                    trendSet.setDrawCircleHole(false);
                    trendSet.setValueTextSize(9f);
                    trendSet.setDrawFilled(false);


                    List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                    dataSets.add(trendSet); // add the datasets


                    // create a data object with the datasets
                    LineData data = new LineData(dataSets);

                    // set data
                    trendingChart.setData(data);
                    trendingChart.invalidate();


                    Legend legend = trendingChart.getLegend();
                    legend.setEnabled(true);
                    legend.setForm(Legend.LegendForm.SQUARE);
                    legend.setTextSize(13f);

//                    Legend.setTextSize(12f);
//                    Legend.setTextColor(Color.BLACK);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        });
        queue.add(request);
        return true;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
