package com.example.e610.finalpopmovies.Utils;

import com.example.e610.finalpopmovies.Models.Movie;

import java.util.ArrayList;

/**
 * Created by E610 on 21/09/2016.
 */
public interface NetworkResponse {


    void OnSuccess(String JsonData);
    void OnUpdate(boolean IsDataReceived);
}
