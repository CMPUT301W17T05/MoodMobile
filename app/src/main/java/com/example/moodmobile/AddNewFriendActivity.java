package com.example.moodmobile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This class is to handle the sending of friend requests,
 * receiving of friend requests
 *
 */

public class AddNewFriendActivity extends AppCompatActivity {



    /**
     * This method logs an error and notifies the user
     * if his search for a user does not return anything.
     */
    public void displayUserNotFoundMessage(){
        Log.i("Error", "User does not exist.");

        Context context = getApplicationContext();

        CharSequence text = "User does not exist!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Called upon creation of the class.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_friend);

        final ArrayList<Account> searchResults = new ArrayList<Account>();

        final String username = getIntent().getStringExtra("username"); //User currently logged in
        final EditText usernameSearch = (EditText) findViewById(R.id.usernameSearch);

        Button sendRequest =        (Button) findViewById(R.id.SendRequest);
        Button getFriendRequests =  (Button) findViewById(R.id.GetFriendRequests);


        getFriendRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AddNewFriendActivity.this, ViewFriendRequests.class);
                intent.putExtra("username", username);

                startActivityForResult(intent, 1);
            }
        });

        /**WORKS!**/
        sendRequest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Account friendRequestDest;

                /**
                 * Download account from server, then add friend request locally, then save back to server
                 */
                ElasticsearchAccountController.GetUser searchUserTask = new ElasticsearchAccountController.GetUser();
                searchUserTask.execute(usernameSearch.getText().toString());

                try {
                    searchResults.clear();
                    searchResults.addAll(searchUserTask.get());

                } catch (Exception e) {
                    Log.i("Error", "Could not download account details for logged in user..");

                }

                if (searchResults.size() != 0){
                    friendRequestDest = searchResults.get(0);
                    friendRequestDest.addFollowRequest(username);

                    ElasticsearchAccountController.UpdateAccountTask updateAccount = new ElasticsearchAccountController.UpdateAccountTask();
                    updateAccount.execute(friendRequestDest);

                    Context context = getApplicationContext();

                    CharSequence text = "Following request sent to " + usernameSearch.getText().toString() + ".";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    displayUserNotFoundMessage();
                }
            }
        });

    }
}
