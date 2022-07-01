package com.example.newsfeed;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NewsModel> newsList;

    public NewsAdapter(Context context, ArrayList<NewsModel> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.news_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);

        //showNewsArticle listener
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, ((TextView)view).getText() + " " + viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                viewHolder.itemClicked(viewHolder.getAdapterPosition());
            }
        });

        //share news listener
        viewHolder.newsShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.shareNews(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.newsTitle.setText(newsList.get(position).getTitle());
        Glide.with(context).load(newsList.get(position).getImageUrl()).into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return newsList!=null?newsList.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView newsImage;
        TextView newsTitle;
        CardView cardView;
        ImageButton newsShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.newsImage = itemView.findViewById(R.id.newsImage_show_article);
            this.newsTitle = itemView.findViewById(R.id.newsTitle);
            this.cardView = itemView.findViewById(R.id.card_view);
            this.newsShare = itemView.findViewById(R.id.newsShare);
        }

        public void itemClicked(int position) {
            Intent in = new Intent(context,ShowNewsArticle.class);

            //pass data to open new activity
            in.putExtra("author",newsList.get(position).getAuthor());
            in.putExtra("timestamp",newsList.get(position).getDate());
            in.putExtra("imageUrl",newsList.get(position).getImageUrl());
            in.putExtra("content",newsList.get(position).getContent());
            in.putExtra("readMoreUrl",newsList.get(position).getReadMoreUrl());
            context.startActivity(in);
        }

        public void shareNews(int position) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, newsList.get(position).getInShortsUrl());
            context.startActivity(Intent.createChooser(shareIntent,"Share via"));
        }
    }
}
