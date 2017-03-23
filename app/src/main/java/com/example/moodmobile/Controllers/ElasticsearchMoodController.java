package com.example.moodmobile.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.moodmobile.Classes.Mood;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.*;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Modified by Jia on 2017-03-12.
 * Created by Derek.R on 2017-03-07.
 */

public class ElasticsearchMoodController {
    private static JestDroidClient client;

    // TODO we need a function which adds mood to elastic search
    public static class AddMoodsTask extends AsyncTask<Mood, Void, Void> {

        @Override
        protected Void doInBackground(Mood... moods) {
            verifySettings();

            for (Mood mood : moods) {
                Index index = new Index.Builder(mood).index("cmput301w17t5").type("moods").build();

                try {
                    // where is the client?
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()){
                        mood.setId(result.getId());
                    }
                    else{
                        Log.i("Error", "ElasticSearch was not able to add the mood.");
                        //TODO add mood to "addmood.sav"
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the mood");
                }

            }
            return null;
        }
    }

    /**
     * Updates a mood by deleting it, and then completely rebuilding it.
     * using Update.Builder() didn't work.
     *
     */
    public static class UpdateMoodsTask extends AsyncTask<Mood, Void, Void> {

        @Override
        protected Void doInBackground(Mood... moods) {
            verifySettings();

            for (Mood mood : moods) {

                try {client.execute(new Delete.Builder(mood.getId())
                        .index("cmput301w17t5")
                        .type("moods")
                        .build());

                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the mood");
                    //TODO add mood to "updatemood.sav"
                }

                try {
                    // where is the client?
                    Index index = new Index.Builder(mood)
                            .index("cmput301w17t5")
                            .type("moods")
                            .id(mood.getId()) //Added to preserve ID
                            .build();


                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()){
                        //mood.setId(result.getId());
                    }
                    else{
                        Log.i("Error", "ElasticSearch was not able to add the mood.");
                        //TODO add mood to "addmood.sav"
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the mood");
                    //TODO add mood to file?
                }

            }
            return null;
        }
    }

    public static class DeleteMoodsTask extends AsyncTask<Mood, Void, Void> {

        @Override
        protected Void doInBackground(Mood... moods) {
            verifySettings();

            for (Mood mood : moods) {

                try {client.execute(new Delete.Builder(mood.getId())
                        .index("cmput301w17t5")
                        .type("moods")
                        .build());

                } catch (Exception e) {
                    Log.i("Error", "The application failed to delete the mood");
                    //TODO add mood to "deletemood.sav"
                }


            }
            return null;
        }
    }

    // TODO we need a function which gets mood from elastic search
    public static class GetMoodsTask extends AsyncTask<String, Void, ArrayList<Mood>> {
        @Override
        protected ArrayList<Mood> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Mood> moods = new ArrayList<Mood>();
            //Search string here
            String MoodQuery;
            if (search_parameters[0].equals("")){
                MoodQuery = search_parameters[0];
            }
            else{
                MoodQuery = "{\"query\": {\"term\" : { \"username\" : \"" + search_parameters[0] + "\" }}}";
            }


            // TODO Build the query
            Search search = new Search.Builder(MoodQuery)
                    .addIndex("cmput301w17t5")
                    .addType("moods")
                    .build();

            try {
                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()){
                    List<Mood> foundMoods = result.getSourceAsObjectList(Mood.class);
                    moods.addAll(foundMoods);
                    Collections.sort(moods, new Comparator<Mood>() {
                        @Override
                        public int compare(Mood mood1, Mood mood2) {
                            return mood2.getDate().compareTo(mood1.getDate());
                        }
                    });
                }
                else{
                    Log.i("Error", "The search query failed to find any mood that matched");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return moods;
        }
    }

    public static class GetMoodsTaskByID extends AsyncTask<String, Void, ArrayList<Mood>> {
        @Override
        protected ArrayList<Mood> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Mood> moods = new ArrayList<Mood>();
            //Search string here
            String MoodQuery;
            if (search_parameters[0].equals("")){
                MoodQuery = search_parameters[0];
            }
            else{
                MoodQuery = "{\"query\": {\"term\" : { \"_id\" : \"" + search_parameters[0] + "\" }}}";
            }


            // TODO Build the query
            Search search = new Search.Builder(MoodQuery)
                    .addIndex("cmput301w17t5")
                    .addType("moods")
                    .build();

            try {
                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()){
                    List<Mood> foundMoods = result.getSourceAsObjectList(Mood.class);
                    moods.addAll(foundMoods);
                    Collections.sort(moods, new Comparator<Mood>() {
                        @Override
                        public int compare(Mood mood1, Mood mood2) {
                            return mood2.getDate().compareTo(mood1.getDate());
                        }
                    });
                }
                else{
                    Log.i("Error", "The search query failed to find any mood that matched");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return moods;
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