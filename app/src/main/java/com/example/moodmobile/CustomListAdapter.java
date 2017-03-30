package com.example.moodmobile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Derek.R on 2017-03-20.
 */

public class CustomListAdapter extends ArrayAdapter<Mood> {

    private Activity context;
    private String[] reasons = new String[10];
    private Integer[] emoteids = new Integer[10];
    private String[] usernames = new String[10];
    private String[] locations = new String[10];
    private Bitmap[] images = new Bitmap[10];
    private List<Address> addresses;
    private Integer position;
    private byte[] decodedString;

    public CustomListAdapter(Activity activityContext, ArrayList<Mood> MoodsList) {
        super(activityContext, R.layout.list_item, MoodsList);
        // TODO Auto-generated constructor stub
        context = activityContext;
        position = 0;
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        for (Mood mood : MoodsList) {
            reasons[position] = mood.getMessage();
            emoteids[position] = getEmoticon(mood.getFeeling());
            usernames[position] = mood.getUsername() + " " + mood.getSituation();
            if (mood.getMoodImage() != null) {
                decodedString = Base64.decode(mood.getMoodImage(), Base64.NO_WRAP);
                images[position] = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }

            if (mood.getLocation() != null){
                try {
                    addresses = gcd.getFromLocation(mood.getLocation().getLatitude(), mood.getLocation().getLongitude(), 1);
                    locations[position] = addresses.get(0).getLocality() + " @ " + mood.getDate();
                } catch (IOException e) {
                    locations[position] = mood.getDate().toString();
                }
            }
            else locations[position] = mood.getDate().toString();

            position++;
        }
    }

    @Override
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

    }

    private Integer getEmoticon(String emotion){
        Integer emoticon;
        switch (emotion) {
            case "Anger":
                emoticon = R.drawable.anger;
                break;
            case "Confusion":
                emoticon = R.drawable.confusion;
                break;
            case "Disgust":
                emoticon = R.drawable.disgust;
                break;
            case "Fear":
                emoticon = R.drawable.fear;
                break;
            case "Happiness":
                emoticon = R.drawable.happiness;
                break;
            case "Sadness":
                emoticon = R.drawable.sadness;
                break;
            case "Shame":
                emoticon = R.drawable.shame;
                break;
            case "Surprise":
                emoticon = R.drawable.surprise;
                break;
            default:
                throw new IllegalArgumentException("Invalid emotion: " + emotion);
        }
        return emoticon;
    }
}

