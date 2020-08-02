package com.devhch.mirai.bakingapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.devhch.mirai.bakingapp.Adapters.IngredientsAdapter;
import com.devhch.mirai.bakingapp.Models.Ingredient;
import com.devhch.mirai.bakingapp.R;
import com.devhch.mirai.bakingapp.databinding.IngredientsFragmentBinding;


import java.util.ArrayList;
import java.util.List;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/27/2020.
 */
public class IngredientsFragment extends Fragment {

    private IngredientsFragmentBinding mIngredientsFragmentBinding;

    private Handler sliderHandler = new Handler();

    private Context mContext;

    private List<Ingredient> mIngredients;

    private static String LIST_STATE = "list_state";
    private static final String CURRENT_ITEM = "current_item";

    private int currentItem;

    public IngredientsFragment() {
    }

    public IngredientsFragment(Context context, List<Ingredient> ingredients) {
        mContext = context;
        mIngredients = ingredients;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Instantiate RecipeFragmentForCellPhonesBinding using DataBindingUtil
        mIngredientsFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.ingredients_fragment, container, false);
        View root = mIngredientsFragmentBinding.getRoot();

        if (savedInstanceState != null) {
            mIngredients = savedInstanceState.getParcelableArrayList(LIST_STATE);
            currentItem = savedInstanceState.getInt(CURRENT_ITEM);
        }

        mIngredientsFragmentBinding.ingredientViewPagerSlider.setAdapter(
                new IngredientsAdapter(getContext(), mIngredients, mIngredientsFragmentBinding.ingredientViewPagerSlider));

        mIngredientsFragmentBinding.ingredientViewPagerSlider.setCurrentItem(currentItem);
        mIngredientsFragmentBinding.ingredientViewPagerSlider.setClipToPadding(false);
        mIngredientsFragmentBinding.ingredientViewPagerSlider.setClipChildren(false);
        mIngredientsFragmentBinding.ingredientViewPagerSlider.setOffscreenPageLimit(3);
        mIngredientsFragmentBinding.ingredientViewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_IF_CONTENT_SCROLLS);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });

        mIngredientsFragmentBinding.ingredientViewPagerSlider.setPageTransformer(compositePageTransformer);

        mIngredientsFragmentBinding.ingredientViewPagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(slideRunnable);
                sliderHandler.postDelayed(slideRunnable, 3000); // Slide duration 3 seconds
            }
        });

        return root;
    }

    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            mIngredientsFragmentBinding.ingredientViewPagerSlider.setCurrentItem(mIngredientsFragmentBinding.ingredientViewPagerSlider.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(slideRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();

        sliderHandler.postDelayed(slideRunnable, 3000);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE, (ArrayList<? extends Parcelable>) mIngredients);
        outState.putInt(CURRENT_ITEM, mIngredientsFragmentBinding.ingredientViewPagerSlider.getCurrentItem());

    }
}
