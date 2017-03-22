package com.example.moodmobile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Derek.R on 2017-03-20.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] reasons;
    private final Integer[] emoteids;
    private final String[] usernames;
    private final String[] locations;
    private final Bitmap[] images;

    public CustomListAdapter(Activity context, String[] reason, Integer[] emoteid, String[] username, String[] locations, Bitmap[] images) {
        super(context, R.layout.list_item, reason);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.reasons = reason;
        this.emoteids = emoteid;
        this.usernames = username;
        this.locations = locations;
        this.images = images;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null,true);

        ImageView emoteView = (ImageView) rowView.findViewById(R.id.emoticon);
        TextView reasontxt = (TextView) rowView.findViewById(R.id.reason);
        TextView usernametxt = (TextView) rowView.findViewById(R.id.username);
        TextView locationtxt = (TextView) rowView.findViewById(R.id.location);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.picture);

        reasontxt.setText(reasons[position]);
        emoteView.setImageResource(emoteids[position]);
        usernametxt.setText(usernames[position]);
        locationtxt.setText(locations[position]);
        imageView.setImageBitmap(images[position]);
        return rowView;

    };
}

