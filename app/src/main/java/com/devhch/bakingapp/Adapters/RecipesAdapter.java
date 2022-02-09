package com.devhch.bakingapp.Adapters;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/25/2020.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devhch.bakingapp.Models.Recipe;
import com.devhch.bakingapp.R;
import com.devhch.bakingapp.RecipeDetailsActivity;
import com.devhch.bakingapp.databinding.RecipesCardBinding;


import java.util.ArrayList;
import java.util.List;

import static com.devhch.bakingapp.Utils.Constant.EXTRA_LIST_STEP;
import static com.devhch.bakingapp.Utils.Constant.EXTRA_RECIPE;
import static com.devhch.bakingapp.Utils.Constant.EXTRA_RECIPE_NAME;
import static com.devhch.bakingapp.Utils.Constant.EXTRA_STEP_BUNDLE;
import static com.devhch.bakingapp.Utils.Constant.EXTRA_STEP_INDEX;

/**
 * {@link RecipesAdapter} is a {@link RecyclerView.Adapter} that can provide the layout for
 * each list item based on a data source which is a list of {@link Recipe} objects.
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    /**
     * Context of the app
     */
    private Context mContext;
    private List<Recipe> mRecipes;

    public RecipesAdapter(Context context, List<Recipe> recipes) {
        mContext = context;
        mRecipes = recipes;
    }

    @NonNull
    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecipesCardBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.recipes_card, parent, false);
        return new RecipesAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapterViewHolder holder, int position) {
        holder.binding.recipeName.setText(
                String.format("%s - %s ", mRecipes.get(position).getId(), mRecipes.get(position).getName()));
        holder.binding.recipeServings.append("" + mRecipes.get(position).getServings());

        switch (mRecipes.get(position).getName()) {
            case "Nutella Pie":
                holder.binding.recipeImage.setImageResource(R.drawable.nutella_pie);
                break;
            case "Brownies":
                holder.binding.recipeImage.setImageResource(R.drawable.brownies);
                break;
            case "Yellow Cake":
                holder.binding.recipeImage.setImageResource(R.drawable.yellow_cake);
                break;
            default:
                holder.binding.recipeImage.setImageResource(R.drawable.cheesecake);
        }

        holder.binding.recipeImage.setOnClickListener(v -> {
            Intent mIntent = new Intent(mContext, RecipeDetailsActivity.class);
            Bundle bundle = new Bundle();
            Recipe recipe = mRecipes.get(position);
            bundle.putParcelable(EXTRA_RECIPE, recipe);
            mIntent.putExtra(EXTRA_RECIPE, bundle);

            Bundle bundleForTwoPane = new Bundle();
            bundleForTwoPane.putInt(EXTRA_STEP_INDEX, position);
            bundleForTwoPane.putString(EXTRA_RECIPE_NAME, recipe.getName());
            bundleForTwoPane.putParcelableArrayList(EXTRA_LIST_STEP,
                    (ArrayList<? extends Parcelable>) recipe.getSteps());

            mIntent.putExtra(EXTRA_STEP_BUNDLE, bundleForTwoPane);
            mContext.startActivity(mIntent);

        });
    }


    @Override
    public int getItemCount() {
        return mRecipes != null ? mRecipes.size() : 0;
    }

    public static class RecipesAdapterViewHolder extends RecyclerView.ViewHolder {

        private RecipesCardBinding binding;

        public RecipesAdapterViewHolder(@NonNull RecipesCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
