package com.example.aarjugoyal.homework_9_news_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aarjugoyal on 5/7/20.
 */

public class CustomDialog {

    Activity activity;
    SharedPreferenceNews sharedPreference;

    public void showDialog(final Activity activity, final String title, String imgURL, final String ArticleID){
        this.activity = activity;

        sharedPreference = new SharedPreferenceNews();
    final Dialog dialog = new Dialog(activity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(false);
    dialog.setContentView(R.layout.dialog_box);

    Log.d("DIALOG", "Value recieved of title " + title);

        ImageView img = (ImageView) dialog.findViewById(R.id.ivImageDialog);
        Picasso.with(activity).load(Uri.parse(imgURL))
                .into(img);
    TextView text = (TextView) dialog.findViewById(R.id.tvTitleDialog);
    text.setText(title);

    ImageView twitter= dialog.findViewById(R.id.ivTwitterDialog);
    final ImageView bookmark = dialog.findViewById(R.id.ivBookmarkDialog);


        if(checkFavoriteItem(ArticleID))
        {
            bookmark.setImageResource(R.drawable.ic_bookmark_filled_24px);
        }
        else
        {
            bookmark.setImageResource(R.drawable.ic_bookmark_border_24px);
        }


        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFavoriteItem(ArticleID))
                {
                    //Remove item here
                    sharedPreference.removeFavoriteAsString(activity, ArticleID);
                    Log.d("Bookmark in adaptor", "Bookmark is UN clicked");
                    String toast_msg = '"' + title + '"' + " - was removed from Bookmarks";
                    Toast.makeText(activity, toast_msg, Toast.LENGTH_SHORT).show();
                    bookmark.setImageResource(R.drawable.ic_bookmark_border_24px);
                }
                else
                {
//                    //Add item here
//                    sharedPreference.addFavorite(activity, ArticleID);
//                    Log.d("Bookmark in adaptor", "Bookmark is clicked");
//                    String toast_msg = '"' + title + '"' + " - was added to Bookmarks";
//                    Toast.makeText(activity, toast_msg, Toast.LENGTH_SHORT).show();
//                    bookmark.setImageResource(R.drawable.ic_bookmark_filled_24px);
                }
            }
        });


        twitter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TWITTER", "TWITTER BUTTON CLICKED");
                        String article_url = "https://theguardian.com/" + ArticleID;
                        String url = "https://twitter.com/intent/tweet?text=Check out this link: "+article_url+"&hashtags=CSCI571NewsSearch";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        activity.startActivity(i);
                    }
                }
        );
        dialog.setCanceledOnTouchOutside(true);
    dialog.show();

}

    private boolean checkFavoriteItem(String articleID) {


        List<String> favorites = sharedPreference.getFavoritesID(activity.getApplicationContext());
        Log.d("FAVORITES", "List of favorites is " + favorites);
        Log.d("FAVORITES CHECK", "The article string is " + articleID);
        if (favorites != null) {
            if(favorites.contains(articleID))
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

}
