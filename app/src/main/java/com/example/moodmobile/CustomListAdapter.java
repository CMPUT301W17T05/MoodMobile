package com.example.moodmobile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Derek.R on 2017-03-20.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    /*private final Integer[] imgid;*/

    public CustomListAdapter(Activity context, String[] itemname/*, Integer[] imgid*/) {
        super(context, R.layout.list_item, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        /*this.imgid=imgid;*/
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item, null,true);

        ImageView emoteView = (ImageView) rowView.findViewById(R.id.emoticon);
        TextView reasontxt = (TextView) rowView.findViewById(R.id.reason);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.picture);

        reasontxt.setText("Description "+itemname[position]);
        return rowView;

    };
}

