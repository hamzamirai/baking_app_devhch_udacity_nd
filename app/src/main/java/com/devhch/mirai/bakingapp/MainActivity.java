package com.devhch.mirai.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.devhch.mirai.bakingapp.Adapters.RecipesAdapter;
import com.devhch.mirai.bakingapp.Api.RetrofitClient;
import com.devhch.mirai.bakingapp.Interface.BakingInterface;
import com.devhch.mirai.bakingapp.Models.Recipe;
import com.devhch.mirai.bakingapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // We Create a data binding instance called mBinding of type ActivityMainBinding
    private ActivityMainBinding mBinding;

    private RecipesAdapter mRecipesAdapter;

    private List<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * DataBindUtil.setContentView replaces our normal call of setContent view.
         * DataBindingUtil also created our ActivityMainBinding that we will eventually use to
         * display all of our data.
         */
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.noInternetConnection.append(getString(R.string.make_sure_that_you_are_connected));

        //Animation animationFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        //Animation animationFromBottom = AnimationUtils.loadAnimation(this, R.anim.slide_from_bottom);

        //mBinding.tvWelcome.setAnimation(animationFromLeft);
        //mBinding.tvBakeToday.setAnimation(animationFromLeft);
        //mBinding.noInternetConnection.setAnimation(animationFromBottom);

        if (isNetworkAvailable()) {
            mBinding.noInternetConnection.setVisibility(View.GONE);
            // Create a LayoutManager and RecipeAdapter and set them to the RecyclerView
            initAdapter();

        } else {
            mBinding.noInternetConnection.setVisibility(View.VISIBLE);
            mBinding.noInternetConnection.setOnClickListener(v -> {
                if (isNetworkAvailable()) {
                    // Create a LayoutManager and RecipeAdapter and set them to the RecyclerView
                    initAdapter();
                    mBinding.noInternetConnection.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Creates a LayoutManager and RecipeAdapter and set them to the RecyclerView
     */
    private void initAdapter() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mBinding.recipesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mBinding.recipesRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        // Create an empty ArrayList
        mRecipes = new ArrayList<>();

        loadJSON();

        // create some animation to recycler view item loading
        //LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MainActivity.this,
         //       R.anim.layout_slide_bottom);
        //mBinding.recipesRecyclerView.setLayoutAnimation(controller);
        //mBinding.recipesRecyclerView.scheduleLayoutAnimation();

    }

    private void loadJSON() {
        //RetrofitClient retrofitClient = new RetrofitClient();
        BakingInterface bakingInterface =
                RetrofitClient.getClient().create(BakingInterface.class);

        Call<List<Recipe>> recipeCall = bakingInterface.getRecipes();
        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Recipe> recipes = response.body();
                        mRecipes.clear();
                        mRecipes.addAll(recipes);

                        mRecipesAdapter = new RecipesAdapter(MainActivity.this, mRecipes);
                        mBinding.recipesRecyclerView.setAdapter(mRecipesAdapter);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {

            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private CountingIdlingResource mIdlingResource= new CountingIdlingResource("Loading_Data");
    /**
     * Only called from test, creates and returns a new {@link com.devhch.mirai.bakingapp.Utils.MainIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public CountingIdlingResource getIdlingResource() {
        return mIdlingResource;
    }

}