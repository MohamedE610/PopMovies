package com.example.e610.finalpopmovies.Models;

import android.net.Network;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.e610.finalpopmovies.Utils.NetworkResponse;

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
public class Review   implements Parcelable {
    private String Url;
    private String ID;
    private String Author;
    private String content;

    public Review(){ }

    public static Review ParsingReviewData(String JsonData )
    {
        Review review=new Review();
        try {

            JSONObject jj=new JSONObject(JsonData);
            JSONArray ja=jj.getJSONArray("results");
            JSONObject j=ja.getJSONObject(0);


            review.setID(j.getString("id"));
            review.setAuthor(j.getString("author"));
            review.setContent(j.getString("content"));

        } catch (JSONException e) {
            e.  printStackTrace();
        }
        finally {
            return review;
        }

      /*  int counter = 0;
        ArrayList<Review> ReviewList = new ArrayList<Review>();
        try {

            JSONObject jsonObject=new JSONObject(JsonData);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            while (counter < jsonArray.length()) {
                JSONObject objectJS = jsonArray.getJSONObject(counter);
                Review review=new Review();

                review.setID(objectJS.getString("id"));
                review.setAuthor(objectJS.getString("author"));
                review.setContent(objectJS.getString("content"));
                ReviewList.add(review);
                counter++;
            }
        } catch (JSONException e) {
            e.  printStackTrace();
        }
      return ReviewList;*/
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    protected Review(Parcel in) {
        Url = in.readString();
        ID = in.readString();
        Author = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Url);
        dest.writeString(ID);
        dest.writeString(Author);
        dest.writeString(content);
    }

    @SuppressWarnings("unused")
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}