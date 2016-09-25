package com.example.e610.finalpopmovies.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.e610.finalpopmovies.Fragments.DetailedFragment;
import com.example.e610.finalpopmovies.R;

/**
 * Created by E610 on 21/09/2016.
 */
public class DetailedActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_activity);

        DetailedFragment detailedFragment=new DetailedFragment();

        Bundle MoviesInfo= getIntent().getBundleExtra("MoviesInfo");
        detailedFragment.setArguments(MoviesInfo);
        getFragmentManager().beginTransaction().add(R.id.DetailedFragment,detailedFragment).commit();

    }


}

