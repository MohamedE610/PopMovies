package com.example.e610.finalpopmovies.Models;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by E610 on 21/09/2016.
 */
public class Trailer  implements Parcelable {
    private String  ID;
    private String Key;
    private String Name;

    public Trailer(){}

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public static   Trailer ParsingTrailerData(String JsonData)
    {

        Trailer trailer=new Trailer();
        try {

            JSONObject jj=new JSONObject(JsonData);
            JSONArray ja=jj.getJSONArray("results");
            JSONObject j=ja.getJSONObject(0);


            trailer.setID(j.getString("id"));
            trailer.setKey(j.getString("key"));
            trailer.setName(j.getString("name"));

        } catch (JSONException e) {
            e.  printStackTrace();
        }
     return trailer;
        /*int counter = 0;
        ArrayList<Trailer>TrailerList = new ArrayList<Trailer>();
        try {

            JSONObject jsonObject=new JSONObject(JsonData);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            while (counter < jsonArray.length()) {
                JSONObject objectJS = jsonArray.getJSONObject(counter);
                Trailer trailer=new Trailer();

                trailer.setID(objectJS.getString("id"));
                trailer.setKey(objectJS.getString("key"));
                trailer.setName(objectJS.getString("name"));
                TrailerList.add(trailer);
                counter++;
            }
        } catch (JSONException e) {
            e.  printStackTrace();
        }
        return TrailerList;*/
    }


    protected Trailer(Parcel in) {
        ID = in.readString();
        Key = in.readString();
        Name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(Key);
        dest.writeString(Name);
    }

    @SuppressWarnings("unused")
    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
