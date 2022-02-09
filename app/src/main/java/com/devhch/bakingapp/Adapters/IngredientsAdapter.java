package com.devhch.bakingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.devhch.bakingapp.Models.Ingredient;
import com.devhch.bakingapp.R;
import com.devhch.bakingapp.databinding.IngredientCardBinding;


import java.util.List;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/26/2020.
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {

    private Context mContext;
    private List<Ingredient> mIngredients;
    private ViewPager2 mViewPager2;

    public IngredientsAdapter(Context context, List<Ingredient> ingredients, ViewPager2 viewPager2) {
        mContext = context;
        mIngredients = ingredients;
        mViewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        IngredientCardBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.ingredient_card, parent, false);
        return new IngredientsAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapterViewHolder holder, int position) {
        holder.binding.ingredient.setText(mIngredients.get(position).getIngredient());
        holder.binding.quantity.setText(String.valueOf(mIngredients.get(position).getQuantity()));
        holder.binding.measure.setText(mIngredients.get(position).getMeasure());

        if (position == mIngredients.size() - 2) {
            mViewPager2.post(mRunnable);
        }
    }

    @Override
    public int getItemCount() {
        return mIngredients != null ? mIngredients.size() : 0;
    }

    public static class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

        private IngredientCardBinding binding;

        public IngredientsAdapterViewHolder(@NonNull IngredientCardBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mIngredients.addAll(mIngredients);
            notifyDataSetChanged();
        }
    };
}
