package com.example.cemo.testproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    EditText itemSearch;
    TextView responseView;
    ProgressBar progressBar;

    //Search items
    String searchString;

    //Walmart related things
    //static final String WALMART_API_KEY = "bue3xb9zsg69nawfue55zxd7";
    static final String WALMART_API_URL = "http://api.walmartlabs.com/v1/search?query=";
    static final String WALMART_API_REPSONSE = "&format=json&apiKey=bue3xb9zsg69nawfue55zxd7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseView = (TextView) findViewById(R.id.responseView);
        itemSearch = (EditText) findViewById(R.id.itemSearch);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button queryButton = (Button) findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchString = itemSearch.getText().toString();
                System.out.println(searchString);
                new RetrieveFeedTask().execute();
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here
            String walmartURL = WALMART_API_URL + searchString + WALMART_API_REPSONSE;
            System.out.println(walmartURL);
            try {
                URL url = new URL(walmartURL);
                //URL url = new URL("http://api.macys.com/v4/catalog/search?searchphrase=red%20dress&sortorderby=PRICE_ASCENDING&perpage=10&page=2&imagewidth=100&upcimagewidth=100&swatchimagewidth=100");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            responseView.setText(response);
            // TODO: check this.exception
            // TODO: do something with the feed

//            try {
//                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
//                String requestID = object.getString("requestId");
//                int likelihood = object.getInt("likelihood");
//                JSONArray photos = object.getJSONArray("photos");
//                .
//                .
//                .
//                .
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }
}