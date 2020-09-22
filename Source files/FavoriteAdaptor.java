package com.example.aarjugoyal.homework_9_news_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by aarjugoyal on 5/6/20.
 */

public class FavoriteAdaptor extends RecyclerView.Adapter<FavoriteAdaptor.MyViewHolder> {
    private Context mContext;
    private List<String> favoriteList;
    SharedPreferenceNews sharedPreference;

    ItemClickListener mClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTtle, tvDate, tvSection;
        public ImageView ivFavImage, ivBookmark;

        public MyViewHolder (View view) {
            super(view);
            tvTtle = (TextView) view.findViewById(R.id.tvTitleFavorite);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvSection = (TextView) view.findViewById(R.id.tvSection);
            ivFavImage = (ImageView) view.findViewById(R.id.ivFavoriteImage);
            ivBookmark = (ImageView) view.findViewById(R.id.ivBookmark);

        }
    }


    public FavoriteAdaptor(Context mContext, List<String> albumList) {
        this.mContext = mContext;
        this.favoriteList = albumList;

        sharedPreference = new SharedPreferenceNews();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String favoriteItem = favoriteList.get(position);

        try {
            JSONObject favorite = new JSONObject(favoriteItem);
            Log.d("FAVORITE DATA", "JSOn perceived data is " + favorite );

            final String ID = favorite.getString("ID");
            final String title = favorite.getString("Title");
            String Date = favorite.getString("Date");
            String Section = favorite.getString("Section");
            final String imgURL = favorite.getString("img");

            holder.tvTtle.setText(title);
            holder.tvSection.setText(Section);


            Picasso.with(mContext).load(Uri.parse(imgURL))
                    .into(holder.ivFavImage);

            holder.ivBookmark.setImageResource(R.drawable.ic_bookmark_filled_24px);

            holder.ivBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("FAVORITE REMOVE", "bookmark icon clicked");
            sharedPreference.removeFavoriteAsString(mContext, ID);
               removeAt(position);

                }
            });

            String publDate = Date.substring(0,10);

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-mm-dd");

            java.util.Date date_format = sf.parse(publDate);

            SimpleDateFormat sf2 = new SimpleDateFormat("dd MMM");



            holder.tvDate.setText(sf2.format(date_format));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = holder.getAdapterPosition();
                    Log.d("NEWS ITEM CLICK", "Position clicked is " + position);

                    Log.d("NEWS ITEM CLICK ", "Title clicked is " + title);

                    final Intent detailArticleIntent;
                    detailArticleIntent = new Intent(mContext, DetailActivity.class);
                    detailArticleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    detailArticleIntent.putExtra("articleTitle",title);
                    detailArticleIntent.putExtra("articleID",ID);
                    mContext.startActivity(detailArticleIntent);

                    if (mClickListener != null) mClickListener.onItemClick(view, position);
                }
            });


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    try {
                        final Dialog dialog = new Dialog(mContext);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.dialog_box);

                        Log.d("DIALOG", "Value recieved of title " + title);

                        ImageView img = (ImageView) dialog.findViewById(R.id.ivImageDialog);
                        Picasso.with(mContext).load(Uri.parse(imgURL))
                                .into(img);
                        TextView text = (TextView) dialog.findViewById(R.id.tvTitleDialog);
                        text.setText(title);

                        ImageView twitter= dialog.findViewById(R.id.ivTwitterDialog);
                        final ImageView bookmark = dialog.findViewById(R.id.ivBookmarkDialog);


                        if(checkFavoriteItem(ID))
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
                                if(checkFavoriteItem(ID))
                                {
                                    //Remove item here
                                    sharedPreference.removeFavoriteAsString(mContext, ID);
                                    Log.d("Bookmark in adaptor", "Bookmark is UN clicked");
                                    String toast_msg = '"' + title + '"' + " - was removed from Bookmarks";
                                    Toast.makeText(mContext, toast_msg, Toast.LENGTH_SHORT).show();
                                    bookmark.setImageResource(R.drawable.ic_bookmark_border_24px);
                                    removeAt(position);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
//                                else
//                                {
////                    //Add item here
//                                    sharedPreference.addFavorite(mContext, home_new);
//                                    Log.d("Bookmark in adaptor", "Bookmark is clicked");
//                                    String toast_msg = '"' + title + '"' + " - was added to Bookmarks";
//                                    Toast.makeText(mContext, toast_msg, Toast.LENGTH_SHORT).show();
//                                    bookmark.setImageResource(R.drawable.ic_bookmark_filled_24px);
//                                    notifyDataSetChanged();
//                                }
                            }
                        });


                        twitter.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d("TWITTER", "TWITTER BUTTON CLICKED");
                                        String article_url = "https://theguardian.com/" + ID;
                                        String url = "https://twitter.com/intent/tweet?text=Check out this link: "+article_url+"&hashtags=CSCI571NewsSearch";
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        mContext.startActivity(i);
                                    }
                                }
                        );
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(mContext,"Could not open dialog", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });
        } catch (JSONException e) {
            sharedPreference.removeFavoriteArticleString(mContext, favoriteItem);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favoriteList.size());

            e.printStackTrace();
        } catch (ParseException e) {
            sharedPreference.removeFavoriteArticleString(mContext, favoriteItem);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favoriteList.size());
            e.printStackTrace();
        }


    }

    public void removeAt(int position) {
        favoriteList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favoriteList.size());

    }

    public boolean checkFavoriteItem(String checkArticleID) {

        List<String> favorites = sharedPreference.getFavoritesID(mContext);
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
    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    // allows clicks events to be caught
    void setClickListener(Runnable itemClickListener) {
        this.mClickListener = (FavoriteAdaptor.ItemClickListener) itemClickListener;
        Log.d("GUARDIAN NEWS SETUP", "Click listener");
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
