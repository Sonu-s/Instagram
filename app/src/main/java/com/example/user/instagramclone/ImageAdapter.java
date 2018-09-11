package com.example.user.instagramclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    Context mContext;
    List<FriendsName> names;


    public ImageAdapter(Context mContext, List<FriendsName> names){

     this.mContext = mContext;
     this.names = names;

    }
    @Override
    public int getCount() {
        return names.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();

        if (convertView == null){

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = inflater.inflate(R.layout.row_item,parent,false);
           holder.nameTextView = convertView.findViewById(R.id.nameTextView);
           convertView.setTag(holder);


        }else{

            holder = (Holder) convertView.getTag();
        }

        FriendsName friendsName = names.get(position);

        holder.nameTextView.setText(friendsName.getName());


        return convertView;
    }

    public class Holder{

       TextView nameTextView;
    }
}
