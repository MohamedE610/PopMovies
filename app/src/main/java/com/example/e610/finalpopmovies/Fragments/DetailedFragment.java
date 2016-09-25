package com.example.e610.finalpopmovies.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.e610.finalpopmovies.Activities.MainActivity;
import com.example.e610.finalpopmovies.Models.Movie;
import com.example.e610.finalpopmovies.Models.Review;
import com.example.e610.finalpopmovies.Models.Trailer;
import com.example.e610.finalpopmovies.R;
import com.example.e610.finalpopmovies.Utils.FetchData;
import com.example.e610.finalpopmovies.Utils.MySharedPreferences;
import com.example.e610.finalpopmovies.Utils.NetworkResponse;
import com.squareup.picasso.Picasso;


/**
 * Created by E610 on 21/09/2016.
 */
public class DetailedFragment extends Fragment {

    final public  String imgString= "http://image.tmdb.org/t/p/w185/";

    static boolean FavouriteSelected=false;

    ImageView Poster_Img;
    TextView Title;
    TextView Overview;
    TextView ReleaseDate;
    TextView Vote_Rating;
    TextView ReviewAuthor;
    TextView ReviewContent;
    TextView TrailerName;
    ImageView Favourite;
    ImageView button;

    MySharedPreferences mySharedPreferences;
    Bundle MoviesInfo ;
    Movie movie ;

    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=  inflater.inflate(R.layout.detailed_fragment,container,false);
        view=v;
        MoviesInfo=new Bundle();
        movie=new Movie();
        mySharedPreferences=new MySharedPreferences(getActivity(),"FavouriteMovies");


        Poster_Img=(ImageView)v.findViewById(R.id.Poster_Image);
        Title=(TextView)v.findViewById(R.id.Title);
        ReleaseDate=(TextView)v.findViewById(R.id.Release_Date);
        Overview=(TextView)v.findViewById(R.id.Overview);
        Vote_Rating=(TextView)v.findViewById(R.id.Vote_Rating);
        ReviewAuthor=(TextView)v.findViewById(R.id.ReviewAuthor);
        ReviewContent=(TextView)v.findViewById(R.id.ReviewContent);
        TrailerName=(TextView)v.findViewById(R.id.TrailerName);
        Favourite=(ImageView)v.findViewById(R.id.Favourite);
        button=(ImageView)v.findViewById(R.id.button);

        MoviesInfo=this.getArguments();

        if(savedInstanceState!=null){
            movie= savedInstanceState.getParcelable("Movie");
            setReviewDetails();
            setTrailerDetails();
        }
        else{
            movie=MoviesInfo.getParcelable("Movie");

            if(!FavouriteSelected) {
                FetchReview();
                FetchTrailer();
            }
            else {
               setReviewDetails();
                setTrailerDetails();
            }
        }

        setMovieDetails();

        movie.Favourite=mySharedPreferences.CheckFavourite(movie.getId());

        if(movie.Favourite)
            Favourite.setImageResource(R.drawable.staron);
        else
            Favourite.setImageResource(R.drawable.staroff);


        Favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movie.Favourite){
                    movie.Favourite=true;
                    mySharedPreferences.SaveData(movie);
                    Favourite.setImageResource(R.drawable.staron);
                    Toast.makeText(getActivity(),"Save in Favourite Movies", Toast.LENGTH_SHORT).show();
                }
                else {
                    movie.Favourite=false;
                    mySharedPreferences.RemoveMovie(movie.getId());
                    Toast.makeText(getActivity(),"Remove From Favourite Movies", Toast.LENGTH_SHORT).show();
                    Favourite.setImageResource(R.drawable.staroff);
                }

            }
        });

        return v;
    }

public void FetchReview() {

    if(MainActivity.NetworkState()){
       FetchData fetchData = new FetchData("Review",movie.getId());
       fetchData.execute();

       fetchData.setNetworkResponse(new NetworkResponse() {
        @Override
        public void OnSuccess(String JsonData) {
            Review review = new Review();
            review = Review.ParsingReviewData(JsonData);
            movie.setReview(review);
           setReviewDetails();


        }

        @Override
        public void OnUpdate(boolean IsDataReceived) {

        }
    });
    }
    else {
        Review review = new Review();
        movie.setReview(review);
        setReviewDetails();
        Toast.makeText(getActivity()," No Internet Connection", Toast.LENGTH_SHORT).show();
    }
}
    public void FetchTrailer() {

        if(MainActivity.NetworkState()) {
            FetchData fetchData = new FetchData("Trailer", movie.getId());
            fetchData.execute();

            fetchData.setNetworkResponse(new NetworkResponse() {
                @Override
                public void OnSuccess(String JsonData) {
                    Trailer trailer = new Trailer();
                    trailer = Trailer.ParsingTrailerData(JsonData);
                    movie.setTrailer(trailer);

                    setTrailerDetails();
                }

                @Override
                public void OnUpdate(boolean IsDataReceived) {

                }
            });
        }
        else{
            Trailer trailer = new Trailer();
            movie.setTrailer(trailer);
            setTrailerDetails();
            Toast.makeText(getActivity()," No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void setMovieDetails(){

        Picasso.with(view.getContext()).load(imgString+ movie.getPoster_ImageUrl())
                .placeholder(R.drawable.loadingicon)
                .error(R.drawable.error).into(Poster_Img);

        Title.setText( movie.getTitle());
        Overview.setText(movie.getOverview());
        ReleaseDate.setText(movie.getRelease_Date());
        Vote_Rating.setText(movie.getVote_average()+"/10");
    }
    public void setTrailerDetails(){
        TrailerName.setText(movie.getTrailerName());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+ movie.getTrailerKey())));
            }
        });
    }
    public void setReviewDetails(){
        ReviewAuthor.setText(movie.getReviewAuthor());
        ReviewContent.setText(movie.getReviewContent());
    }

    public static void IsFavouriteSelected(boolean isSelected)
    {
        FavouriteSelected=isSelected;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("Movie",movie);
        super.onSaveInstanceState(outState);
    }

}
