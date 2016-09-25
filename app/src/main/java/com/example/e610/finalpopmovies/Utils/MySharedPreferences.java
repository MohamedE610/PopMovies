package com.example.e610.finalpopmovies.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.e610.finalpopmovies.Fragments.DetailedFragment;
import com.example.e610.finalpopmovies.Models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by E610 on 23/09/2016.
 */
public class MySharedPreferences {



    ArrayList<String> MoviesFavouriteList;
    Context context;
    String FileName;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public MySharedPreferences(Context context,String FileName){
        this.context=context;
        this.FileName=FileName;
        this.sharedPref = context.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        this.editor=sharedPref.edit();
        MoviesFavouriteList=new ArrayList<>();
    }
    //"FavouriteMovies"
    public  void SaveData(Movie movie){

        Gson gson = new Gson();

        String json = gson.toJson(movie);
        Log.d("gason", json);
        editor.putString(movie.getId(),json);
        editor.commit();

        MoviesFavouriteList.add(movie.getId());

        Gson gson1=new Gson();
        String FavouriteList =gson1.toJson(MoviesFavouriteList);
        editor.putString("Favourites",FavouriteList);
        editor.commit();


    }

    public Movie RetriveData(String id){

        Gson gson = new Gson();
        String json = sharedPref.getString(id,"");

        if(json.equals(""))
            return null;
        Movie m = gson.fromJson(json, Movie.class);

        return m ;
    }
    public void RemoveMovie(String id){

        editor.remove(id);
        editor.commit();
        MoviesFavouriteList=getFavouriteMovielist();
        MoviesFavouriteList.remove(id);
        Gson gson1=new Gson();
        String FavouriteList =gson1.toJson(MoviesFavouriteList);
        editor.putString("Favourites",FavouriteList);
        editor.commit();


    }
    public boolean CheckFavourite(String id){

        if(getFavouriteMovielist()==null)
             return false;
        else
            MoviesFavouriteList = getFavouriteMovielist();

        for (String s : MoviesFavouriteList)
            if(s.equals(id))
                return true;

        return false;

    }

    public void setUserSetting(String UserSetting){
        editor.putString("UserSetting",UserSetting);
        editor.commit();
    }

    public String getUserSetting(){

        String UserSetting=sharedPref.getString("UserSetting","");

        return UserSetting;
    }
    public boolean IsFirstTime(){
        String check=sharedPref.getString("FirstTime","");

        if(check.equals("yes"))
            return false;
         return true;
    }

    public void FirstTime(){
        editor.putString("FirstTime","yes");
        editor.commit();
    }

    public void Clear(){
        editor.clear();
        editor.commit();
    }

    public ArrayList<String> getFavouriteMovielist(){
        Gson gson = new Gson();
        String json = sharedPref.getString("Favourites","");
        if(json.equals(""))
            return null;
        ArrayList<String> list=new ArrayList<>();
        Type type=new TypeToken<ArrayList<String>>() {}.getType();
        list = gson.fromJson(json,type );
        return list;
    }
}
