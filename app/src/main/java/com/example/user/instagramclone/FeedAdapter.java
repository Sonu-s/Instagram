package com.example.user.instagramclone;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedAdapter extends BaseAdapter {

    Context mContext;
    List<AllImages> images;


    public FeedAdapter(Context mContext, List<AllImages> images) {

        this.mContext = mContext;
        this.images = images;

    }


    @Override
    public int getCount() {

        return images.size();
    }

    @Override
    public Object getItem(int i) {

        return null;
    }

    @Override
    public long getItemId(int i) {

        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder = new Holder();


        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.image_item, viewGroup, false);
            holder.imageFeed = view.findViewById(R.id.imageFeed);
            holder.userName = view.findViewById(R.id.userName);
            holder.likeButton = view.findViewById(R.id.likeButton);
            holder.countText = view.findViewById(R.id.countText);
            holder.dislikeButton = view.findViewById(R.id.dislikeButton);
            holder.userProfile = view.findViewById(R.id.userProfile);

            final Holder finalHolder1 = holder;
            final Holder finalHolder = holder;

            final Holder finalHolder2 = holder;
            final Holder finalHolder3 = holder;

            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    finalHolder1.dislikeButton.setVisibility(View.VISIBLE);
                    finalHolder.likeButton.setVisibility(View.INVISIBLE);

                    finalHolder2.count1 -= 1;
                    // Log.i("_Countd",String.valueOf(finalHolder2.count1));
                    finalHolder3.countText.setText(String.valueOf(finalHolder2.count1));

                }
            });


            holder.dislikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    finalHolder.likeButton.setVisibility(View.VISIBLE);
                    finalHolder1.dislikeButton.setVisibility(View.INVISIBLE);

                    finalHolder2.count1 = 1;

                    // Log.i("_Counti",String.valueOf(finalHolder2.count1));

                    finalHolder3.countText.setText(String.valueOf(finalHolder2.count1));
                }
            });

            // Log.i("_Countn",String.valueOf(finalHolder2.count1));

            view.setTag(holder);
        } else {

            holder = (Holder) view.getTag();
        }

        AllImages allImages = images.get(i);

        holder.userName.setText(allImages.getName());

        Picasso.with(mContext)
                .load(allImages.getmImageUri())
                .placeholder(R.drawable.ic_launcher_background)
                .fit()
                .into(holder.imageFeed);

        return view;
    }


    public class Holder {

        ImageView imageFeed;
        TextView userName;
        ImageView likeButton;
        ImageView dislikeButton;
        TextView countText;
        ImageView userProfile;
        int count1 = 0;
    }

}
