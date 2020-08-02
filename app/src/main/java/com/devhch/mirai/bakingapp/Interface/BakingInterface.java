package com.devhch.mirai.bakingapp.Interface;

import com.devhch.mirai.bakingapp.Models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/26/2020.
 */
public interface BakingInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();

}
