package com.example.e610.finalpopmovies.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.e610.finalpopmovies.Adapters.MovieAdapter;
import com.example.e610.finalpopmovies.Fragments.DetailedFragment;
import com.example.e610.finalpopmovies.Fragments.MainFragment;
import com.example.e610.finalpopmovies.Models.Movie;
import com.example.e610.finalpopmovies.R;
import com.example.e610.finalpopmovies.Utils.FetchData;
import com.example.e610.finalpopmovies.Utils.MySharedPreferences;
import com.example.e610.finalpopmovies.Utils.NetworkResponse;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainFragment.SendToMainActivity {

    boolean InstanceState;

    MainFragment mainFragment;
    Spinner spinner;
    Activity CurrentActivity;
    ArrayList<Movie> Movies;
    MySharedPreferences mySharedPreferences;
    static boolean checkFrag=false;
    MovieAdapter movieAdapter;
    Bundle MoviesInfo;
    Context context;
    boolean IsTablet;
    RecyclerView MoviesRecyclerView;
    View view;
    public static Activity ctx; // for using it inside NetworkState()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CurrentActivity=this;
        ctx =this;
        movieAdapter=new MovieAdapter();
        mainFragment=  new MainFragment();
        IsTablet = getResources().getBoolean(R.bool.isTablet);
        MoviesInfo = new Bundle();
        mySharedPreferences=new MySharedPreferences(this,"FavouriteMovies");


    if(savedInstanceState==null){
        getFragmentManager().beginTransaction().add(R.id.MainFragment, mainFragment).commit();
        InstanceState = false;
       }
     else{
          mainFragment=(MainFragment)getFragmentManager().findFragmentById(R.id.MainFragment);
          InstanceState = true;
          Movies=savedInstanceState.getParcelableArrayList("Movies");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Movies",Movies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void send(View v) {
        view=v;
        context=view.getContext();
        MoviesRecyclerView=(RecyclerView) view.findViewById(R.id.MoviesRecyclerView);

        if(IsTablet)
            MoviesRecyclerView.setLayoutManager(new GridLayoutManager(context,3));
        else
            MoviesRecyclerView.setLayoutManager(new GridLayoutManager(context,2));

        spinner=(Spinner) view.findViewById(R.id.spinner);
        String [] SpinnerData= {"Popular Movies","Top Rated Movies","Favourite Movies"};
        ArrayAdapter<String>SpinnerAdapter=new ArrayAdapter<String>(context,R.layout.spinner_item,SpinnerData);
        spinner.setAdapter(SpinnerAdapter);


        DisplayMovies();
    }


    public void DisplayMovies(){

        if(mySharedPreferences.IsFirstTime()){
            mySharedPreferences.Clear();
            mySharedPreferences.FirstTime();
            collectData("Popular Movies");
        }
        else {
            String key=mySharedPreferences.getUserSetting();
            if(key.equals("Popular Movies")||key.equals("")) {
                     collectData("Popular Movies");
                spinner.setSelection(0); // 0 index of "Popular Movies" in a Spinner also I can get it by  SpinnerAdapter.getPosition("Popular Movies");
            }
            else if(key.equals("Top Rated Movies")) {
                collectData("Top Rated Movies");
                 spinner.setSelection(1);  // 1 index of "Top Rated Movies" in a Spinner
            }
            else if(key.equals("Favourite Movies")){
                    DisplayFavouriteMovies();
                spinner.setSelection(2); // 2 index of "Favourite Movies" in a Spinner
            }
        }
    }

    public void collectData(String Key){

        if(MainActivity.NetworkState()) {
            FetchData fetchData = new FetchData(Key, "");
            fetchData.execute();

            fetchData.setNetworkResponse(new NetworkResponse() {

                @Override
                public void OnSuccess(String JsonData) {

                    Movies = Movie.ParsingTrailerData(JsonData);
                    movieAdapter = new MovieAdapter(Movies, context);
                    MoviesRecyclerView.setAdapter(movieAdapter);

                    if(JsonData==null)
                        Toast.makeText(MainActivity.ctx," No Internet Connection", Toast.LENGTH_SHORT).show();

                    CheckTablet();
                    ClickEvent();
                }

                @Override
                public void OnUpdate(boolean IsDataReceived) {

                }
            });
        }
        else{
            Movies = new ArrayList<>();
            movieAdapter = new MovieAdapter(Movies, context);
            MoviesRecyclerView.setAdapter(movieAdapter);
            Toast.makeText(this," No Internet Connection", Toast.LENGTH_SHORT).show();
            CheckTablet();
            ClickEvent();
        }
    }


    public void CheckTablet(){
        Movie movie = new Movie();
        if (IsTablet ) {
            if (Movies.size() != 0)
                movie = Movies.get(0);
                MoviesInfo.putParcelable("Movie", movie);
                if (!InstanceState && !checkFrag ) {
                     DetailedFragment detailedFragment1 = new DetailedFragment();
                     detailedFragment1.setArguments(MoviesInfo);
                     getFragmentManager().beginTransaction().replace(R.id.DetailedFragment, detailedFragment1).commit();
                     checkFrag = true;
              }
        }
    }


    public  void DisplayFavouriteMovies(){
        Movies=getFavouriteMovies();
        movieAdapter=new MovieAdapter(Movies,context);
        MoviesRecyclerView.setAdapter(movieAdapter);
        CheckTablet();
        ClickEvent();
        DetailedFragment.IsFavouriteSelected(true);
    }

    public ArrayList<Movie> getFavouriteMovies(){
        ArrayList<String> list=new ArrayList<>();
        ArrayList<Movie>FavouriteList=new ArrayList<>();
         list=mySharedPreferences.getFavouriteMovielist();
        if(list==null)
            return FavouriteList;
        for(String id: list){
            Movie movie=mySharedPreferences.RetriveData(id);
            if(movie!=null)
               FavouriteList.add(movie);
        }


        return FavouriteList;
    }

    public void ClickEvent(){
        movieAdapter.setClickListener(new MovieAdapter.RecyclerViewClickListener() {
            @Override
            public void ItemClicked(View v, int position) {

                Movie movie=new Movie();
                movie=Movies.get(position);
                MoviesInfo.putParcelable("Movie",movie);
                if (!IsTablet) {
                    Intent in = new Intent( CurrentActivity , DetailedActivity.class);
                    in.putExtra("MoviesInfo", MoviesInfo);
                    startActivity(in);
                } else {

                     DetailedFragment detailedFragment1=new DetailedFragment();
                    detailedFragment1.setArguments(MoviesInfo);
                    getFragmentManager().beginTransaction().replace(R.id.DetailedFragment,detailedFragment1).commit();

                }
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(view!=null) {
                  checkFrag=false;
                    TextView textView = (TextView) view;
                    String SpinnerKey = textView.getText() + "";
                    if (SpinnerKey.equals("Popular Movies")){
                        collectData("Popular Movies");
                        mySharedPreferences.setUserSetting(SpinnerKey);
                        DetailedFragment.IsFavouriteSelected(false);
                    }

                    else if (SpinnerKey.equals("Top Rated Movies")){
                        collectData("Top Rated Movies");
                        mySharedPreferences.setUserSetting(SpinnerKey);
                        DetailedFragment.IsFavouriteSelected(false);
                    }

                    else if (SpinnerKey.equals("Favourite Movies")){
                        mySharedPreferences.setUserSetting(SpinnerKey);
                        DisplayFavouriteMovies();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public static boolean NetworkState()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
