package com.devhch.mirai.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.devhch.mirai.bakingapp.Fragments.IngredientsFragment;
import com.devhch.mirai.bakingapp.Fragments.StepDetailsFragment;
import com.devhch.mirai.bakingapp.Fragments.StepsFragment;
import com.devhch.mirai.bakingapp.Models.Recipe;
import com.devhch.mirai.bakingapp.Models.Step;
import com.devhch.mirai.bakingapp.databinding.ActivityRecipeDetailsBinding;


import java.util.ArrayList;
import java.util.List;

import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_LIST_STEP;
import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_RECIPE;
import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_RECIPE_DESCRIPTION;
import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_RECIPE_NAME;
import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_STEP_BUNDLE;
import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_STEP_INDEX;

public class RecipeDetailsActivity extends AppCompatActivity implements StepsFragment.OnStepClickListener {

    private ActivityRecipeDetailsBinding mBinding;

    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;

    private Recipe mRecipe;
    private List<Step> mSteps;

    private Bundle mBundleForTwoPane;

    private int mStepPosition;

    private IngredientsFragment ingredientsFragmentIngredients;
    private StepsFragment stepsFragment;
    private StepDetailsFragment stepDetailsFragment;

    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * DataBindUtil.setContentView replaces our normal call of setContent view.
         * DataBindingUtil also created our ActivityRecipeDetailsBinding that we will eventually use to
         * display all of our data.
         */
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_details);

        // Get the recipe data from the MainActivity
        getRecipeData();
        assert mRecipe != null;

        setTitle(recipeName);

        // Determine if you're creating a two-pane or single-pane display
        if (mBinding.stepDetailsFragment != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;

            if (savedInstanceState == null) {
                //set Ingredients

                ingredientsFragmentIngredients = new IngredientsFragment(this, mRecipe.getIngredients());
                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManagerIngredients = getSupportFragmentManager();
                fragmentManagerIngredients.beginTransaction()
                        .add(R.id.fragment_for_ingredients, ingredientsFragmentIngredients)
                        .commit();

                // Create a new StepDetailFragment
                stepsFragment = new StepsFragment(this, mSteps);
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_RECIPE_NAME, mRecipe.getName());
                bundle.putBoolean("two_pane", true);
                bundle.putInt(EXTRA_STEP_INDEX, mStepPosition);
                bundle.putParcelableArrayList(EXTRA_LIST_STEP,
                        (ArrayList<? extends Parcelable>) mSteps);

                stepsFragment.setArguments(bundle);

                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_for_step, stepsFragment)
                        .commit();

                // Create a new StepDetailFragment
                //StepDetailsFragment stepDetailsFragment = new StepDetailsFragment(this, mSteps, mStepPosition);
                stepDetailsFragment = new StepDetailsFragment();
                stepDetailsFragment.setArguments(bundle);
                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManagerTablet = getSupportFragmentManager();
                fragmentManagerTablet.beginTransaction()
                        .add(R.id.step_details_fragment, stepDetailsFragment)
                        .commit();
            }

        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;

            //set Ingredients
            IngredientsFragment ingredientsFragmentIngredients = new IngredientsFragment(this, mRecipe.getIngredients());
            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManagerIngredients = getSupportFragmentManager();
            fragmentManagerIngredients.beginTransaction()
                    .add(R.id.fragment_for_ingredients, ingredientsFragmentIngredients)
                    .commit();
            //End IngredientsFragment

            // Create a new StepsFragment
            StepsFragment stepsFragment = new StepsFragment(this, mSteps);
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_RECIPE_NAME, mRecipe.getName());
            stepsFragment.setArguments(bundle);

            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_for_step, stepsFragment)
                    .commit();
        }
    }

    /**
     * Gets recipe data from the MainActivity
     *
     * @return The Recipe data
     */
    private void getRecipeData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_RECIPE)) {
                // Receive the Recipe object which contains ID, name, ingredients, steps, servings,
                // and image of the recipe
                Bundle bundle = intent.getBundleExtra(EXTRA_RECIPE);
                assert bundle != null;
                mRecipe = bundle.getParcelable(EXTRA_RECIPE);
                assert mRecipe != null;
                mSteps = mRecipe.getSteps();
                recipeName = mRecipe.getName();
            }

            if (intent.hasExtra(EXTRA_STEP_BUNDLE)) {
                mBundleForTwoPane = intent.getBundleExtra(EXTRA_STEP_BUNDLE);
                assert mBundleForTwoPane != null;
                //steps = mBundleForTwoPane.getParcelableArrayList(EXTRA_LIST_STEP);
                mStepPosition = mBundleForTwoPane.getInt(EXTRA_STEP_INDEX, 0);
                recipeName = mBundleForTwoPane.getString(EXTRA_RECIPE_NAME);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("recipe", mRecipe);
        outState.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) mSteps);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipe = savedInstanceState.getParcelable("recipe");
        mSteps = savedInstanceState.getParcelableArrayList("steps");
    }

    @Override
    public void onStepSelected(int stepIndex) {
        if (mTwoPane) {

            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
            Bundle bundle = new Bundle();

            bundle.putInt(EXTRA_STEP_INDEX, stepIndex);
            bundle.putString(EXTRA_RECIPE_NAME, recipeName);
            bundle.putBoolean("two_pane", true);
            bundle.putParcelableArrayList(EXTRA_LIST_STEP,
                    (ArrayList<? extends Parcelable>) mSteps);
                stepDetailsFragment.setArguments(bundle);

            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.step_details_fragment, stepDetailsFragment)
                    .commit();

        } else {

            Intent mIntent = new Intent(this, StepDetailsActivity.class);
            Bundle bundleIntent = new Bundle();

            bundleIntent.putInt(EXTRA_STEP_INDEX, stepIndex);
            bundleIntent.putString(EXTRA_RECIPE_NAME, recipeName);
            bundleIntent.putString(EXTRA_RECIPE_DESCRIPTION, mSteps.get(stepIndex).getShortDescription());
            bundleIntent.putParcelableArrayList(EXTRA_LIST_STEP,
                    (ArrayList<? extends Parcelable>) mSteps);

            mIntent.putExtra(EXTRA_STEP_BUNDLE, bundleIntent);
            startActivity(mIntent);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        getRecipeData();
    }
}