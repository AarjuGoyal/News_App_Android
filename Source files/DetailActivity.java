package com.example.aarjugoyal.homework_9_news_app;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {

    String Title;
    String ArticleID;

    LinearLayout progressBarLayout;
    TextView tvDetailContent;
    TextView tvTitle;
    TextView tvSection;
    TextView tvDate;
    ImageView ivDetailImage;
    ImageView ivTwitterShare;
    TextView tvFullArticleLink;

    ImageView ivBookmarkDetail;
    Toolbar dToolbar;
    RequestQueue detailArticleQueue;

    SharedPreferenceNews sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressBarLayout = findViewById(R.id.llProgressBarDetail);

        progressBarLayout.setVisibility(View.VISIBLE);

        Intent previousIntent = getIntent(); // gets the previously created intent
        Title = previousIntent.getStringExtra("articleTitle"); // will return "FirstKeyValue"
        ArticleID= previousIntent.getStringExtra("articleID");


        Log.d("DETAIL ARTICLE", "Inside detail article page");
        dToolbar= findViewById(R.id.toolbarDetail);
        setSupportActionBar(dToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        getSupportActionBar().setTitle(Title);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ivBookmarkDetail = findViewById(R.id.ivBookmarkDetail);

        tvDetailContent = findViewById(R.id.tvDetailContent);
        tvTitle = findViewById(R.id.tvTitleDetail);
        tvSection = findViewById(R.id.tvSectionDetail);
        tvDate = findViewById(R.id.tvTimeDetail);
        ivDetailImage = findViewById(R.id.ivDetail);
        tvFullArticleLink = findViewById(R.id.tvFullArticle);

        ivTwitterShare= findViewById(R.id.ivTwitter);
        ivTwitterShare.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TWITTER", "TWITTER BUTTON CLICKED");
                        String article_url = "https://theguardian.com/" + ArticleID;
                        String url = "https://twitter.com/intent/tweet?text=Check out this link: "+article_url+"&hashtags=CSCI571NewsSearch";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
        );


        Log.d("DETAIl ARTICLE", "The title recieved is " + Title + " and Article ID is " + ArticleID);
//        tvTabBarTitle = findViewById(R.id.tvArticleTitle);
//        tvTabBarTitle.setText(Title);

        sharedPreference = new SharedPreferenceNews();

        getDetailArticleValues(ArticleID);



    }


    private void getDetailArticleValues(final String articleID) {
        Log.d("GET SECTION HEADLINES", "Getting detail article values");
        detailArticleQueue = Volley.newRequestQueue(this);

        String URL = "https://homework8backend.wm.r.appspot.com/guardianArticleSearch?weburl=" + ArticleID;
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject detailArticleJSON = new JSONObject(response);
                    Log.d("GET DETAIl ARTICLE ", "Values recieved are " + response);
                    JSONArray results = null;
                    detailArticleJSON = detailArticleJSON.getJSONObject("articles").getJSONObject("response").getJSONObject("content");
                    Log.d("GET DETAIl ARTICLE", "Detail Article Value is " + detailArticleJSON);

//                        tvDetailContent.setText(detailArticleJSON.toString());

                    String imgURL = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                    try
                    {
                        JSONArray assets = detailArticleJSON.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements").getJSONObject(0).getJSONArray("assets");
                        if(assets.length() > 0)
                        {
                            imgURL = assets.getJSONObject(0).getString("file");
                            Log.d("GUARDIAN NEWS CARD", "Value of img url is " + imgURL);
                            if(imgURL == null)
                            {
                                imgURL = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                            }

                        }
                    }
                    catch(JSONException e)
                    {
                        imgURL = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                        Log.d("DETAIL ARTICLE IMAGE LOAD","Attribute missing in path");
                        e.printStackTrace();

                    }
                    Picasso.with(DetailActivity.this).load(Uri.parse(imgURL))
                            .into(ivDetailImage);
                        String title = detailArticleJSON.getString("webTitle");
                        tvTitle.setText(title);
                        tvTitle.setTextSize(20);

                        String section = detailArticleJSON.getString("sectionName");
                        tvSection.setText(section);

                        String date_original = detailArticleJSON.getString("webPublicationDate");
                        String publDate = date_original.substring(0,10);
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-mm-dd");

                        Date date = sf.parse(publDate);

                        SimpleDateFormat sf2 = new SimpleDateFormat("dd MMM YYYY");

                        Log.d("DETAIL", "Value of date is " + sf2.format(date));

                        final String webURL = detailArticleJSON.getString("webUrl");


//                        Log.d("DETAIl","Article published date " + publDate);
                        tvDate.setText(sf2.format(date));

                        JSONArray blocks = detailArticleJSON.getJSONObject("blocks").getJSONArray("body");
                        StringBuilder description = new StringBuilder();
                        for (int i=0; i<blocks.length(); i++)
                        {
                            description.append(blocks.getJSONObject(0).getString("bodyHtml"));

                        }
                        Log.d("DETAIL ARTICLE", "Values set");
                        tvDetailContent.setText(HtmlCompat.fromHtml(description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                    progressBarLayout.setVisibility(View.GONE);
                    final String articleString = "{\"ID\":" + '"' + ArticleID + '"' +
                            ",\"Title\":\"" + Title + "\",\"Date\":\"" + date_original + "\",\"Section\":\"" + section + "\",\"img\":\""
                            + imgURL + "\"}";

                    if(checkFavoriteItem(ArticleID))
                    {
                        ivBookmarkDetail.setImageResource(R.drawable.ic_bookmark_filled_24px);
                    }
                    else
                    {
                        ivBookmarkDetail.setImageResource(R.drawable.ic_bookmark_border_24px);
                    }
                    ivBookmarkDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(checkFavoriteItem(ArticleID))
                            {
                                //Remove item here
                                sharedPreference.removeFavoriteAsString(getApplicationContext(),ArticleID);
                                Log.d("Bookmark in adaptor", "Bookmark is UN clicked");
                                String toast_msg = '"' + Title + '"' + " - was removed from Bookmarks";
                                Toast.makeText(getApplicationContext(), toast_msg, Toast.LENGTH_SHORT).show();
                                ivBookmarkDetail.setImageResource(R.drawable.ic_bookmark_border_24px);

                            }
                            else
                            {
                                //Add item here
                                sharedPreference.addFavoriteAsString(getApplicationContext(), articleString);
                                Log.d("Bookmark in adaptor", "Bookmark is clicked");
                                String toast_msg = '"' + Title + '"' + " - was added to Bookmarks";
                                Toast.makeText(getApplicationContext(), toast_msg, Toast.LENGTH_SHORT).show();
                                ivBookmarkDetail.setImageResource(R.drawable.ic_bookmark_filled_24px);

                            }
                        }
                    });
                    tvFullArticleLink.setPaintFlags(tvFullArticleLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    tvFullArticleLink.setText("View Full Article");
                    tvFullArticleLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String url = webURL;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    });


                } catch (JSONException e) {
                    Toast.makeText(DetailActivity.this, "Could not parse response recieved", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        });
        detailArticleQueue.add(request);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkFavoriteItem(String checkArticleID) {

        List<String> favorites = sharedPreference.getFavoritesID(this);
        Log.d("FAVORITES", "List of favorites is " + favorites);

        if (favorites != null) {
            if(favorites.contains(checkArticleID))
            {
                Log.d("FAVORITES", "Favorites does contain this article ID");
                return true;
            }
            else
            {
                Log.d("FAVORITES", "Favorites does not contain this article ID");
                return false;
            }
        }
        return false;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}
