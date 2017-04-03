package com.example.moodmobile;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;
import java.util.*;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;


/**
 * The controller used by the application to get moods from ElasticSearch
 *
 * @author Repka
 * @version 1.3
 */

public class ElasticsearchMoodController {

    private static JestDroidClient client;

    /**
     * Given a new mood, send it to the ElasticSearch server to store.
     */
    public static class AddMoodsTask extends AsyncTask<Mood, Void, Void> {

        @Override
        protected Void doInBackground(Mood... moods) {
            verifySettings();

            for (Mood mood : moods) {
                Index index = new Index.Builder(mood).index("cmput301w17t5").type("moods").build();

                try {
                    // Send request to the server to add the mood
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()){
                        mood.setId(result.getId());
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

                // Send request to the server to delete the mood
                try {client.execute(new Delete.Builder(mood.getId())
                        .index("cmput301w17t5")
                        .type("moods")
                        .build());

                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the mood");
                }

                try {
                    Index index = new Index.Builder(mood)
                            .index("cmput301w17t5")
                            .type("moods")
                            .id(mood.getId()) //Added to preserve ID
                            .build();

                    // Send request to the server to add modified mood
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()){
                        // Update the JestID of the mood
                        mood.setId(result.getId());
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

    /**
     * Sends request to the ElasticSearch server to delete a particular mood.
     */

    public static class DeleteMoodsTask extends AsyncTask<Mood, Void, Void> {

        @Override
        protected Void doInBackground(Mood... moods) {
            verifySettings();

            for (Mood mood : moods) {
                // Send request to the server to delete a mood
                try {client.execute(new Delete.Builder(mood.getId())
                        .index("cmput301w17t5")
                        .type("moods")
                        .build());

                } catch (Exception e) {
                    Log.i("Error", "The application failed to delete the mood");
                }


            }
            return null;
        }
    }

    /*
    public static class GetMoodsTask extends AsyncTask<String, Void, ArrayList<Mood>> {
        @Override
        protected ArrayList<Mood> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Mood> moods = new ArrayList<>();
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
    */

    /**
     * Given a particular ID, searches the ElasticSearch server for the particular corresponding mood.
     */
    public static class GetMoodsTaskByID extends AsyncTask<String, Void, ArrayList<Mood>> {
        @Override
        protected ArrayList<Mood> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Mood> moods = new ArrayList<>();
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

    /**
     * Gets moods by username, sorts them from latest first
     */
    public static class GetMoodsTaskByName extends AsyncTask<String, Void, ArrayList<Mood>> {
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
                MoodQuery = "{\"size\": 50, \"query\": {\"term\" : { \"username\" : \"" + search_parameters[0] + "\" }}," +
                        "\"sort\": { \"date\": { \"order\": \"desc\" }}}";
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

    /**
     * Given the users current location, returns the moods within a 5km radius.
     */

    public static class GetNearMoodsTask extends AsyncTask<String, Void, ArrayList<Mood>> {
        @Override
        protected ArrayList<Mood> doInBackground(String... search_parameters) {
            verifySettings();
            ArrayList<Mood> moods = new ArrayList<Mood>();
            String MoodQuery;

            if (search_parameters[0].equals("")){
                MoodQuery = search_parameters[0];
            }
            else{
                MoodQuery = "{\"size\": 50, \"query\":{ \"match_all\":{}}, \"filter\":{ \"geo_distance\":{ \"distance\" : \"5km\",\"location\" : \""
                        + search_parameters[0] + ", " + search_parameters[1]
                        + "\" }}}";
            }

            Search search = new Search.Builder(MoodQuery)
                    .addIndex("cmput301w17t5")
                    .addType("moods")
                    .build();

            try {
                // Send request to the server to get nearby moods.
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

    /**
     * Each of the above methods calls this method to verify that the client is valid and built.
     */

    private static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

}