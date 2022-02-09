package com.devhch.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.devhch.bakingapp.Models.Ingredient;
import com.devhch.bakingapp.Models.Recipe;
import com.devhch.bakingapp.Models.Step;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.devhch.bakingapp.Utils.Constant.EXTRA_LIST_STEP;
import static com.devhch.bakingapp.Utils.Constant.EXTRA_RECIPE;
import static com.devhch.bakingapp.Utils.Constant.EXTRA_RECIPE_DESCRIPTION;
import static com.devhch.bakingapp.Utils.Constant.EXTRA_RECIPE_NAME;
import static com.devhch.bakingapp.Utils.Constant.EXTRA_STEP_BUNDLE;
import static com.devhch.bakingapp.Utils.Constant.EXTRA_STEP_INDEX;
import static com.devhch.bakingapp.Utils.Constant.RECIPE_NAME_AT_ZERO;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/31/2020.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class StepDetailsActivityTest {

    private static final float QUANTITY = (float) 2.0;
    private static final String MEASURE = "CUP";
    private static final String INGREDIENT = "Graham Cracker crumbs";

    private static final int STEP_ID = 0;
    private static final String STEP_SHORT_DESCRIPTION = "Recipe Introduction";
    private static final String STEP_DESCRIPTION = "Recipe Introduction";
    private static final String STEP_VIDEO_URL = "";
    private static final String STEP_THUMBNAIL_URL = "";

    private static final int RECIPE_ID = 0;
    private static final int EXTRA_STEP_POSITION = 0;
    private static final int RECIPE_SERVINGS = 8;
    private static final String RECIPE_IMAGE = "";

    @Rule
    public ActivityTestRule<StepDetailsActivity> mActivityTestRule
            = new ActivityTestRule<StepDetailsActivity>(StepDetailsActivity.class) {

        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

            Recipe recipe = new Recipe(RECIPE_ID, RECIPE_NAME_AT_ZERO,
                    getIngredientsForTest(), getStepsForTest(), RECIPE_SERVINGS, RECIPE_IMAGE);

            Bundle b = new Bundle();
            b.putParcelable(EXTRA_RECIPE, recipe);

            Bundle bundleForTwoPane = new Bundle();
            bundleForTwoPane.putInt(EXTRA_STEP_INDEX, EXTRA_STEP_POSITION);
            bundleForTwoPane.putString(EXTRA_RECIPE_NAME, recipe.getName());
            bundleForTwoPane.putString(EXTRA_RECIPE_DESCRIPTION, recipe.getSteps().get(0).getDescription());
            bundleForTwoPane.putParcelableArrayList(EXTRA_LIST_STEP,
                    (ArrayList<? extends Parcelable>) getStepsForTest());

            Intent result = new Intent(targetContext, StepDetailsActivity.class);
            result.putExtra(EXTRA_RECIPE, b);
            result.putExtra(EXTRA_STEP_BUNDLE, bundleForTwoPane);


            return result;
        }
    };

    @Before
    public void setUp() {
        IdlingRegistry.getInstance().register(mActivityTestRule.getActivity().getIdlingResource());
    }

    @Test
    public void confirmDescriptionPageHasDescriptionTextOfFirstStep() {
        onView(withId(R.id.description)).check(matches(isDisplayed()));
    }

    @Test
    public void navButtonClicked_shouldChangeDescriptionText() {
        onView(withId(R.id.next_button)).perform(click());

        onView(withText(getStepsForTest().get(1).getDescription())).check(matches(isDisplayed()));

        onView(withId(R.id.back_button)).perform(click());

    }

    /**
     * Returns the list of ingredients
     */
    private List<Ingredient> getIngredientsForTest() {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient(QUANTITY, MEASURE, INGREDIENT);
        ingredients.add(ingredient);
        return ingredients;
    }

    /**
     * Returns the list of steps
     */
    private List<Step> getStepsForTest() {
        List<Step> mSteps = new ArrayList<>();
        mSteps.add( new Step(0, "Recipe Introduction", "Recipe Introduction",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", ""));

        mSteps.add( new Step(1, "Starting prep", "1. Preheat the oven to 350\\u00b0F. Butter a 9\\\" deep dish pie pan.",
                "", ""));

        mSteps.add( new Step(2, "Prep the cookie crust.", "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4", ""));

        return mSteps;
    }
}
