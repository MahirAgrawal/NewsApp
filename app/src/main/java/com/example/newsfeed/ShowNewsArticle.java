package com.example.newsfeed;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ShowNewsArticle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news_article);
        //hides the toolbar of this activity
        this.getSupportActionBar().hide();

        //get intent and put values into view
        setupArticle();
    }

    private void setupArticle() {
        Intent in = getIntent();

        //content filling in different views
        Glide.with(this).load(in.getStringExtra("imageUrl")).into((ImageView)findViewById(R.id.newsImage_show_article));
        ((TextView)findViewById(R.id.newsAuthor)).setText("Author: " + in.getStringExtra("author"));
        ((TextView)findViewById(R.id.newsTimestamp)).setText("Date: " + in.getStringExtra("timestamp"));
        ((TextView)findViewById(R.id.newsContent)).setText(in.getStringExtra("content") + "\n\n");



        if(URLUtil.isValidUrl(in.getStringExtra("readMoreUrl"))){
//            Log.d("onShare", "setupArticle: " + in.getStringExtra("readMoreUrl"));
            //filling textview for Url and intent for opening the url in browser
            ((TextView)findViewById(R.id.newsUrl)).setText("Read Full Article");

            ((TextView)findViewById(R.id.newsUrl)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(in.getStringExtra("readMoreUrl")));
                    startActivity(intent);
                }
            });
        }
    }
}