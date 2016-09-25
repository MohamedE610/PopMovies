package com.example.e610.finalpopmovies.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.e610.finalpopmovies.Models.Movie;
import com.example.e610.finalpopmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by E610 on 21/09/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>  {
    ArrayList<Movie> movies;
    Context context;
    RecyclerViewClickListener ClickListener ;
    public MovieAdapter(){}
    public MovieAdapter(ArrayList<Movie>movies,Context context){
        this.movies=new ArrayList<>();
        this.movies=movies;
        this.context=context;
    }

    public void setClickListener(RecyclerViewClickListener clickListener){
       this.ClickListener= clickListener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_movie,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Picasso.with(context).load(holder.imgString+movies.get(position).getPoster_ImageUrl()).into(holder.PosterImg);
                //holder.studentImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.female));

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final public  String imgString= "http://image.tmdb.org/t/p/w185/";
        ImageView PosterImg;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            PosterImg=(ImageView)itemView.findViewById(R.id.Poster_Image2);
        }

        @Override
        public void onClick(View view) {
            if(ClickListener!=null)
            ClickListener.ItemClicked(view ,getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener
    {

        public void ItemClicked(View v, int position);
    }
}

