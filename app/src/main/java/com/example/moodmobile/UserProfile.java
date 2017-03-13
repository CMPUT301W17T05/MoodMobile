/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.moodmobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Spinner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class UserProfile extends AppCompatActivity {

    public static final int IMAGE_REQUEST = 20;
    private ImageView imageView;
    private Spinner genderSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        imageView = (ImageView) findViewById(R.id.profileImage);
        genderSpinner = (Spinner) findViewById(R.id.gender);


        // Create an ArrayAdapter using the mood_array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);
    }


    public void photo(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);

        File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        String path = pictureDir.getPath();

        Uri data = Uri.parse(path);

        intent.setDataAndType(data,"image/*");

        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_REQUEST) {
                Uri imageUri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    Bitmap image =  BitmapFactory.decodeStream(inputStream);

                    imageView.setImageBitmap(image);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }

    }


}
