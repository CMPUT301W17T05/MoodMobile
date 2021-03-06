/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.moodmobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This is the activity that allows users to edit their profile
 */
//TODO r.id.SaveBot is never used
public class UserProfile extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 20;
    private Intent getUsernameIntent;
    private String username;
    private TextView usernameTxt;
    private EditText nicknameTxt;
    private EditText regionTxt;
    private Spinner genderSpinner;
    private String encodeImage;
    private ImageView userProfile;

    private final ArrayList<String> genderArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        getUsernameIntent = getIntent();

        //TODO remove doulbe reference to R.id.profileImage
        usernameTxt = (TextView) findViewById(R.id.username);
        nicknameTxt = (EditText) findViewById(R.id.nickname);
        regionTxt = (EditText) findViewById(R.id.region);
        genderSpinner = (Spinner) findViewById(R.id.gender);
        userProfile = (ImageView) findViewById(R.id.profileImage);

        // Create an ArrayAdapter using the mood_array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);
    }


    /**
     * When activity starts, ElasticsearchAccountController.GetUser(username) is used to download
     * the account information of the user, and the view "user_profile" is filled with the account
     * info.
     */
    @Override
    protected void onStart() {
        super.onStart();
        username = getUsernameIntent.getStringExtra("username");
        Log.d("JBCNM: ", String.valueOf(username));

        final ArrayList<Account> accountList = new ArrayList<>();

        ElasticsearchAccountController.GetUser getUser = new ElasticsearchAccountController.GetUser();
        getUser.execute(username);

        try {
            accountList.clear();
            accountList.addAll(getUser.get());
        } catch (Exception e) {
            Log.i("Error", "Failed to get the tweets out of asyc object");
        }

        Log.d("LALALA: ", String.valueOf(accountList.size()));
        genderArray.add("Male");
        genderArray.add("Female");
        if (accountList.get(0).getProfileImage() != null) {
            byte[] decodedString = Base64.decode(accountList.get(0).getProfileImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            userProfile.setImageBitmap(decodedByte);

        }

        usernameTxt.setText(username);
        nicknameTxt.setText(accountList.get(0).getNickname());
        regionTxt.setText(accountList.get(0).getRegion());

        int position = genderArray.indexOf(accountList.get(0).getGender());
        genderSpinner.setSelection(position);

    }


    /**
     * Photo.
     *
     * this is the onClickListener for Edit Image button, it creates an intent to Gallery, and it
     * allows users to select a image from Gallery.
     *
     * @param v the v
     */
    public void photo(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);

        File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        String path = pictureDir.getPath();

        Uri data = Uri.parse(path);

        intent.setDataAndType(data,"image/*");

        startActivityForResult(intent, IMAGE_REQUEST);
    }

    /**
     * onActivityResult receive the image that selected from Gallery, and it set the user profile photo
     * to be the image selected.
     */
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_REQUEST) {
                Uri imageUri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    Bitmap image =  BitmapFactory.decodeStream(inputStream);

                    //Resize the image to addressed format.
                    Bitmap resized = Bitmap.createScaledBitmap(image, 320, 240,true);

                    //Compress the image.
                    ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                    resized.compress(Bitmap.CompressFormat.JPEG, 5 , bmpStream);
                    byte[] bitmapdata = bmpStream.toByteArray();
                    resized = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length);

                    ImageView profileImage = (ImageView) findViewById(R.id.profileImage);
                    profileImage.setImageBitmap(resized);
                    encodeImage = getEncoded64ImageStringFromBitmap(resized);


                    userProfile.setImageBitmap(resized);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }

    }


    /**
     * Save profile.
     *  This is the onClickListener for the "save" button, ElasticsearchAccountController.UpdateAccountTask()
     *  is used to update the account information of current user. Note that the image is encoded to
     *  a Base64 form and stored in database as a String type.
     * @param view the view
     */
    public void saveProfile(View view) {
        final ArrayList<Account> accountList = new ArrayList<>();
        ElasticsearchAccountController.GetUser getUser = new ElasticsearchAccountController.GetUser();
        getUser.execute(username);
        try {
            accountList.clear();
            accountList.addAll(getUser.get());
        } catch (Exception e) {
            Log.i("Error", "Failed to get the tweets out of asyc object");
        }

        Account account = accountList.get(0);

        ElasticsearchAccountController.UpdateAccountTask updateAccountTask =
                new ElasticsearchAccountController.UpdateAccountTask();


        try {
            if (encodeImage != null) {
                account.setProfileImage(encodeImage);
            }
            account.setNickname(nicknameTxt.getText().toString());
            account.setGender(genderSpinner.getSelectedItem().toString());
            account.setRegion(regionTxt.getText().toString());
        }
        catch (Exception e) {

            Log.i("Error", "Failed to edit user profile.");

        }

        updateAccountTask.execute(account);

        Context context = getApplicationContext();
        Toast.makeText(context, "Edit profile successfully", Toast.LENGTH_SHORT).show();


        finish();
    }


    /**
     * This method takes a Bitmap type image, to use Base64.encodeToString() to convert the image
     * to a Base64 String.
     */
    private String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        Log.i("HAHAHA", imgString);
        return imgString;
    }


}
