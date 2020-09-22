package com.example.aarjugoyal.homework_9_news_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

/**
 * Created by aarjugoyal on 4/24/20.
 */

class AdaptorHomeNews extends RecyclerView.Adapter<AdaptorHomeNews.ViewHolder>{
    private Context context;
    private JSONArray Responses;
    private int PageSize;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    SharedPreferenceNews sharedPreference;

    private List<NewsItem> NewsResults;

    // data is passed into the constructor
    AdaptorHomeNews(Context p_context, List<NewsItem> responses, int pageSize){
        this.mInflater = LayoutInflater.from(p_context);
        context = p_context;
//        this.Responses = responses;

        NewsResults = responses;
        this.PageSize = pageSize;
        sharedPreference = new SharedPreferenceNews();
        Log.d("RENDER GUARDIAN NEWS", "Number of results are " + PageSize + "Data diplyed JSON is " + Responses);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_rows_news_home, parent, false);

        return new ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Log.d("ADAPTOR ", "in on bind view holder");
        final NewsItem home_new = NewsResults.get(position);
        final String Title = home_new.getTitle();
        final String ArticleID = home_new.getArticleID();
        String Section = home_new.getSection();
        final String imgurl = home_new.getImgURL();
        final String TimePassed = home_new.getTimePassed();
        holder.tvTitle.setText(Title);
        holder.tvSection.setText(Section);

        holder.tvTimePassed.setText(TimePassed);
        Picasso.with(context).load(Uri.parse(imgurl))
                .into(holder.ivThumbnail);

        if(checkFavoriteItem(ArticleID))
        {
            holder.ibBookmark.setImageResource(R.drawable.ic_bookmark_filled_24px);
        }
        else
        {
            holder.ibBookmark.setImageResource(R.drawable.ic_bookmark_border_24px);
        }


        holder.ibBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFavoriteItem(ArticleID))
                {
                    //Remove item here
                    sharedPreference.removeFavorite(context, home_new);
                    Log.d("Bookmark in adaptor", "Bookmark is UN clicked");
                    String toast_msg = '"' + Title + '"' + " - was removed from Bookmarks";
                    Toast.makeText(context, toast_msg, Toast.LENGTH_SHORT).show();
                    holder.ibBookmark.setImageResource(R.drawable.ic_bookmark_border_24px);
                }
                else
                {
                    //Add item here
                    sharedPreference.addFavorite(context, home_new);
                    Log.d("Bookmark in adaptor", "Bookmark is clicked");
                    String toast_msg = '"' + Title + '"' + " - was added to Bookmarks";
                    Toast.makeText(context, toast_msg, Toast.LENGTH_SHORT).show();
                    holder.ibBookmark.setImageResource(R.drawable.ic_bookmark_filled_24px);
                }


            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NEWS ITEM CLICK", "Line number 95");
                int position = holder.getAdapterPosition();
                Log.d("NEWS ITEM CLICK", "Position clicked is " + position);
                String title = getItem(position);
                Log.d("NEWS ITEM CLICK ", "Title clicked is " + title);

                final Intent detailArticleIntent;
                detailArticleIntent = new Intent(context, DetailActivity.class);
                detailArticleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                detailArticleIntent.putExtra("articleTitle",Title);
                detailArticleIntent.putExtra("articleID",ArticleID);
                context.startActivity(detailArticleIntent);

                if (mClickListener != null) mClickListener.onItemClick(view, position);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                try
                {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_box);

                    Log.d("DIALOG", "Value recieved of title " + Title);

                    ImageView img = (ImageView) dialog.findViewById(R.id.ivImageDialog);
                    Picasso.with(context).load(Uri.parse(imgurl))
                            .into(img);
                    TextView text = (TextView) dialog.findViewById(R.id.tvTitleDialog);
                    text.setText(Title);

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
                                sharedPreference.removeFavoriteAsString(context, ArticleID);
                                Log.d("Bookmark in adaptor", "Bookmark is UN clicked");
                                String toast_msg = '"' + Title + '"' + " - was removed from Bookmarks";
                                Toast.makeText(context, toast_msg, Toast.LENGTH_SHORT).show();
                                bookmark.setImageResource(R.drawable.ic_bookmark_border_24px);
                                notifyDataSetChanged();
                            }
                            else
                            {
//                    //Add item here
                    sharedPreference.addFavorite(context, home_new);
                    Log.d("Bookmark in adaptor", "Bookmark is clicked");
                    String toast_msg = '"' + Title + '"' + " - was added to Bookmarks";
                    Toast.makeText(context, toast_msg, Toast.LENGTH_SHORT).show();
                    bookmark.setImageResource(R.drawable.ic_bookmark_filled_24px);
                    notifyDataSetChanged();
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
                                    context.startActivity(i);
                                }
                            }
                    );
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
                catch (Exception e)
                {
                    Toast.makeText(context,"Could not open dialog", Toast.LENGTH_LONG).show();
                }
                return true;
            }


        });


    }



    public boolean checkFavoriteItem(String checkArticleID) {

        List<String> favorites = sharedPreference.getFavoritesID(context);
        Log.d("FAVORITES", "List of favorites is " + favorites);
        Log.d("FAVORITES CHECK", "The article string is " + checkArticleID);
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
    // total number of rows
    @Override
    public int getItemCount() {
        return PageSize;
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {
        CardView CvNewsItem;

        TextView tvTitle;
        TextView tvSection;
        TextView tvTimePassed;
        ImageView ivThumbnail;
        ImageButton ibBookmark;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            CvNewsItem = itemView.findViewById(R.id.cardViewNewItem);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSection = itemView.findViewById(R.id.tvSection);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.ivHomeNews);
            tvTimePassed = itemView.findViewById(R.id.tvTimePassed);
            ibBookmark = itemView.findViewById(R.id.ibBookmark);
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
////
////                    LayoutInflater layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////                    View popup = layoutinflater.inflate(R.layout.dialog_box,null);
////                    try
////                    {
////                        Toast.makeText(context, "long click", Toast.LENGTH_LONG).show();
////                        // set the custom dialog components - text, image and button
////                        TextView text = (TextView) popup.findViewById(R.id.tvTitleDialog);
////                        text.setText("Title//");
////
////                        ImageView dialogImage = (ImageView) popup.findViewById(R.id.ivImageDialog);
////                        Dialog dialog = new Dialog(context);
////                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
////                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////                        dialog.setContentView(popup);
////                        //dialog.setTitle("Title...");
////
//////                    Picasso.with(context).load(Uri.parse(imgurl))
//////                            .into(dialogImage);
////
////
////
////                    dialog.show();
////                    }
////                    catch(Exception e)
////                    {
////                        e.printStackTrace();
////                    }
////
////                    return true;
//
//                    try
//                    {
//                        CustomDialog alert = new CustomDialog();
//                        alert.showDialog((Activity) context, "Error de conexión al servidor");
//                        return true;
//                    }
//                    catch (Exception e)
//                    {
//                        Toast.makeText(context,"Could not open dialog", Toast.LENGTH_LONG).show();
//                    }
//                    return true;
//                }
//            });
        }

//        @Override
//        public void onClick(View view) {
//            Log.d("NEWS ITEM CLICK", "Line number 123");
//            int position = getAdapterPosition();
//            Log.d("NEWS ITEM CLICK", "Position clicked is " + position);
//            String title = getItem(position);
//            Log.d("NEWS ITEM CLICK ", "Title clicked is " + title);
//
//            final Intent detailArticleIntent;
//            detailArticleIntent = new Intent(context, DetailActivity.class);
//            context.startActivity(detailArticleIntent);
//
//            if (mClickListener != null) mClickListener.onItemClick(view, position);
//        }
    }

//    // convenience method for getting data at click position
    String getItem(int position) {
        Log.d("HOME CLICK", "in get Item function for position " + position);
        return NewsResults.get(position).getTitle();
    }

    // allows clicks events to be caught
    void setClickListener(Runnable itemClickListener) {
        this.mClickListener = (ItemClickListener) itemClickListener;
        Log.d("GUARDIAN NEWS SETUP", "Click listener");
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
