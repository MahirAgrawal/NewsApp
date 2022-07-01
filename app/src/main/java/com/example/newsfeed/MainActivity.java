package com.example.newsfeed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{
    private static String topic = "national";
    private static String url = "https://inshorts.deta.dev/news?category=";
    private ArrayList<NewsModel> newsList = null;

    //stores the id to highlight and disable the current news topic
    MenuItem disableIteminMenu = null;
    MenuItem enableIteminMenu = null;

    //stores the max number of attempts can be made by volley to api call for single request
    private final int maxAttempts = 4;

    //current request attempts is stored in this variable
    private int leftAttempts = maxAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting color of actionBar
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6c25be")));

        //views the loading screen
        viewProgressBar();

        //load news to be populated into views
        getNews();
    }

    //will set the toolbar layout as mentioned in the res/menu/activity_main_menu.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.activity_main_menu,menu);
        return true;
    }


    //to respond on menu item selection
    //to recognize particular item in menu we use item.getItemId() method and match with desired item's id
    //to handle the click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()){
            case R.id.all:
            case R.id.national:
            case R.id.entertainment:
            case R.id.business:
            case R.id.politics:
            case R.id.sports:
            case R.id.hatke:
            case R.id.startup:
            case R.id.world:
                topic = item.getTitle().toString().toLowerCase();

                //below code to enable the previously disabled item and disable the item which is currently loading
                enableIteminMenu = disableIteminMenu;
                disableIteminMenu = item;
                updateActionToolbar();


                //starts the loader
                viewProgressBar();

                //gets the news in background
                getNews();
                break;
        }
        return true;
    }

    private void updateActionToolbar() {
        if(disableIteminMenu != null){
            disableIteminMenu.setEnabled(false);
        }
        if(enableIteminMenu != null){
            enableIteminMenu.setEnabled(true);
        }
    }

    //sets the layout to loading screen layout
    private void viewProgressBar() {
        //hides the recycler view and shows the progressBar
        this.findViewById(R.id.newsRecyclerView).setVisibility(View.INVISIBLE);
        this.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    //toggles all the action performed by viewProgressBar
    private void removeProgressBar() {
        //shows recycler view and hides the progressBar
        this.findViewById(R.id.newsRecyclerView).setVisibility(View.VISIBLE);
        this.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    }

    private void getNews() {

        //error message textview should get removed when requesting
        ((TextView)findViewById(R.id.errorMessage)).setVisibility(View.GONE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+topic,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //reset the leftAttempts for future api calls after successful fetching of data
                        leftAttempts = maxAttempts;
                        try {
                            MainActivity.this.setUpNewsList(response.getJSONArray("data"));
                            //setupnewslist is the bottleneck method which takes all the time so after that only we can remove loader
                            MainActivity.this.removeProgressBar();

                            //setup recycler view after getting response only or else data will not be present in recycler view
                            MainActivity.this.setupRecyclerView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        leftAttempts--;
                        if (leftAttempts > 0) {
                            getNews();
                        }
                        else {
                            MainActivity.this.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                            ((TextView)MainActivity.this.findViewById(R.id.errorMessage)).setVisibility(View.VISIBLE);
                            leftAttempts = maxAttempts;
                        }
                    }
                }
        );

        SingletonVolley.getInstance(this).addToRequestQueue(request);
    }


    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new NewsAdapter(this,newsList));
    }

    private void setUpNewsList(JSONArray data) {
//        Log.d("onCreate", "makeNewsObjects: " + data);
        ArrayList<NewsModel> newsList = new ArrayList<NewsModel>();
        for(int i = 0;i < data.length();i++){
            try {
                newsList.add(new NewsModel(data.getJSONObject(i).getString("author"),
                                 data.getJSONObject(i).getString("title"),
                                 data.getJSONObject(i).getString("content"),
                                 data.getJSONObject(i).getString("date"),
                                 data.getJSONObject(i).getString("imageUrl"),
                                 data.getJSONObject(i).getString("readMoreUrl"),
                                 data.getJSONObject(i).getString("url"))
                            );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.newsList = newsList;
    }
}