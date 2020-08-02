package com.devhch.mirai.bakingapp.Interface;

import com.devhch.mirai.bakingapp.Models.Recipe;

import java.util.List;

import retrofit2.Response;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/27/2020.
 */
public interface OnRequestFinishedListener {
    void onFailure(String message);

    void onResponse(Response<List<Recipe>> response);
}
