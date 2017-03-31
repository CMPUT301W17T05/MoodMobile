package com.example.moodmobile;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by Derek.R on 2017-03-07.
 */

public class ElasticsearchAccountController {
    private static JestDroidClient client;


    // TODO we need a function which adds Account to elastic search
    public static class AddUser extends AsyncTask<Account, Void, Void> {

        @Override
        protected Void doInBackground(Account... accounts) {
            verifySettings();

            for (Account account : accounts) {
                Index index = new Index.Builder(account).index("cmput301w17t5").type("users").build();

                try {
                    // where is the client?
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()){
                        account.setId(result.getId());
                    }
                    else{
                        Log.i("Error", "ElasticSearch was not able to add the tweet.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the tweets");
                }

            }
            return null;
        }
    }


    public static class GetUser extends AsyncTask<String, Void, ArrayList<Account>> {
        @Override
        protected ArrayList<Account> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Account> accounts = new ArrayList<>();
            //Search string here
            String UserQuery;
            if (search_parameters[0].equals("")){
                UserQuery = search_parameters[0];
            }
            else{
                UserQuery = "{\"query\": {\"match\" : { \"username\" : \"" + search_parameters[0] + "\" }}}";
            }


            // TODO Build the query
            Search search = new Search.Builder(UserQuery)
                    .addIndex("cmput301w17t5")
                    .addType("users")
                    .build();

            try {
                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()){
                    List<Account> foundUsers = result.getSourceAsObjectList(Account.class);
                    accounts.addAll(foundUsers);
                }
                else{
                    Log.i("Error", "The search query failed to find any tweets that matched");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return accounts;
        }
    }


    public static class UpdateAccountTask extends AsyncTask<Account, Void, Void> {

        @Override
        protected Void doInBackground(Account... accounts) {
            verifySettings();

            for (Account account : accounts) {

                try {client.execute(new Delete.Builder(account.getId())
                        .index("cmput301w17t5")
                        .type("users")
                        .build());

                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the mood");
                }

                try {
                    // where is the client?
                    Index index = new Index.Builder(account)
                            .index("cmput301w17t5")
                            .type("users")
                            .id(account.getId()) //Added to preserve ID
                            .build();


                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()){
                        //TODO add statement
                        //mood.setId(result.getId());
                    }
                    else{
                        Log.i("Error", "ElasticSearch was not able to add the mood.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the mood");
                }

            }
            return null;
        }
    }





    public static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}