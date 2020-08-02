package com.devhch.mirai.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.devhch.mirai.bakingapp.Fragments.StepDetailsFragment;
import com.devhch.mirai.bakingapp.Models.Step;
import com.devhch.mirai.bakingapp.databinding.ActivityStepDetailsBinding;

import java.util.ArrayList;
import java.util.List;

import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_LIST_STEP;
import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_RECIPE_DESCRIPTION;
import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_RECIPE_NAME;
import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_STEP_BUNDLE;
import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_STEP_INDEX;

public class StepDetailsActivity extends AppCompatActivity {

    private ActivityStepDetailsBinding mActivityStepDetailsBinding;

    private List<Step> mSteps;
    //private Recipe mRecipe;

    private int mStepPosition;
    private Bundle mBundle;
    private String shortDescription;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mActivityStepDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_step_details);

        // Get the recipe data from the RecipeDetailsActivity
        mSteps = new ArrayList<>();
        getStepsListData();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle( recipeName);
        actionBar.setSubtitle(shortDescription);

        // Only create a new fragment when there is no previously saved state
        if (savedInstanceState == null) {
            // Create a new StepDetailFragment
            //StepDetailsFragment stepDetailsFragment = new StepDetailsFragment(this, mSteps, mStepPosition);
            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
            if (mBundle != null)
                stepDetailsFragment.setArguments(mBundle);

            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_details_fragment, stepDetailsFragment)
                    .commit();
        }

    }

    private void getStepsListData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_STEP_BUNDLE)) {
                mBundle = intent.getBundleExtra(EXTRA_STEP_BUNDLE);
                assert mBundle != null;
                mSteps = mBundle.getParcelableArrayList(EXTRA_LIST_STEP);
                mStepPosition = mBundle.getInt(EXTRA_STEP_INDEX, 0);
                recipeName = mBundle.getString(EXTRA_RECIPE_NAME);
                shortDescription = mBundle.getString(EXTRA_RECIPE_DESCRIPTION);

            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) mSteps);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSteps = savedInstanceState.getParcelableArrayList("steps");
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