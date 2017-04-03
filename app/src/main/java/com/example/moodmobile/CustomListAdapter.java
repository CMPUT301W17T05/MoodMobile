package com.example.moodmobile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The adapter to the ArrayList that contains the moods belonging to a user.
 * This class takes the parameters from each individual mood and applies
 * it to the custom list layout.
 * The main parameters used for this class are as follows:
 * <ul>
 *     <li>context: The activity that holds the adapter</li>
 *     <li>reasons: The reason why text attached to each mood/li>
 *     <li>emoteids: Emoticons</li>
 *     <li>usernames: Username and the social situation they are in</li>
 *     <li>locations: Location and time of posting</li>
 *     <li>images: Images attached to moods</li>
 * </ul>
 *
 * @author Repka
 * @version 1.0
 */


public class CustomListAdapter extends ArrayAdapter<Mood> {

    public static final int size = 100;
    private final Activity context;
    private final String[] reasons = new String[size];
    private final Integer[] emoteids = new Integer[size];
    private final String[] usernames = new String[size];
    private final String[] locations = new String[size];
    private final Bitmap[] images = new Bitmap[size];
    private List<Address> addresses;
    private Integer position;
    private byte[] decodedString;


    /**
     * The constructor for the CustomListAdapter class.
     * By taking an ArrayList of moods, it formats them and stores them in lists to be
     * used by the getter when the moods are to be displayed.
     * @param activityContext The activity where the adapter is.
     * @param MoodsList The ArrayList of moods.
     */

    public CustomListAdapter(Activity activityContext, ArrayList<Mood> MoodsList) {
        super(activityContext, R.layout.list_item, MoodsList);
        // TODO Auto-generated constructor stub
        context = activityContext;
        Integer position = 0;
        // Setup the GeoCoder
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        for (Mood mood : MoodsList) {
            reasons[position] = mood.getMessage();
            emoteids[position] = getEmoticon(mood.getFeeling());
            usernames[position] = mood.getUsername() + " " + mood.getSituation();
            // Get the moodImage stored as a string, and convert it to a Bitmap Picture.
            if (mood.getMoodImage() != null) {
                byte[] decodedString = Base64.decode(mood.getMoodImage(), Base64.NO_WRAP);
                images[position] = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }

            if (mood.getLocation() != null){
                // Try to get the location form the mood, and add it to the date.
                try {
                    List<Address> addresses= gcd.getFromLocation(mood.getLatitude(), mood.getLongitude(), 1);
                    locations[position] = addresses.get(0).getLocality() + " @ " + mood.getDate();
                } catch (Exception e) {
                    locations[position] = mood.getDate().toString();
                }
            }
            else locations[position] = mood.getDate().toString();

            position++;
        }
    }

    /**
     * The getter for the individual rows of the custom list.
     * The individual elements were setup is lists by the setter.
     * @see CustomListAdapter
     * @param position The index in the list that the mood is in.
     * @param view
     * @param parent
     * @return The view for the row of the custom list.
     */

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        //Get the view from the layout, and inflate it.
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null,true);

        //Get the individual elements of the view
        ImageView emoteView = (ImageView) rowView.findViewById(R.id.emoticon);
        TextView reasontxt = (TextView) rowView.findViewById(R.id.reason);
        TextView usernametxt = (TextView) rowView.findViewById(R.id.username);
        TextView locationtxt = (TextView) rowView.findViewById(R.id.location);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.picture);

        //Set the individual elements of the view and return the completed View
        reasontxt.setText(reasons[position]);
        emoteView.setImageResource(emoteids[position]);
        usernametxt.setText(usernames[position]);
        locationtxt.setText(locations[position]);
        imageView.setImageBitmap(images[position]);
        return rowView;

    }

    /**
     * Given an input string that represents the emotion of a mood, this class
     * returns the drawable image to be used by the view.
     * @param emotion The raw emotion from the mood class.
     * @return The emoticon used by the view.
     */

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

