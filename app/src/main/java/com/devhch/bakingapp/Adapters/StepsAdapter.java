package com.devhch.bakingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devhch.bakingapp.Models.Step;
import com.devhch.bakingapp.R;
import com.devhch.bakingapp.databinding.StepsCardBinding;

import java.util.List;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/26/2020.
 */
public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {

    private Context mContext;
    private List<Step> mSteps;
    private String mRecipeName;
    private boolean mTwoPane;

    public StepsAdapterOnClickHandler mOnClickHandler;

    public interface StepsAdapterOnClickHandler {
        void onItemClick(int stepIndex);
    }

    public StepsAdapter(Context context, List<Step> steps, String recipeName, StepsAdapterOnClickHandler onClickHandler) {
        this.mContext = context;
        this.mSteps = steps;
        this.mRecipeName = recipeName;
        mOnClickHandler = onClickHandler;
    }

    @NonNull
    @Override
    public StepsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        StepsCardBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.steps_card, parent, false);
        return new StepsAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapterViewHolder holder, int position) {
        holder.binding.tvStepId.setText(String.valueOf(position + 1));
        holder.binding.tvStepShortDescription.setText(mSteps.get(position).getShortDescription());

        holder.binding.viewParent.setOnClickListener(v -> {
            mOnClickHandler.onItemClick(position);
        });

    }

    @Override
    public int getItemCount() {
        return mSteps != null ? mSteps.size() : 0;
    }

    public static class StepsAdapterViewHolder extends RecyclerView.ViewHolder {

        private StepsCardBinding binding;

        public StepsAdapterViewHolder(@NonNull StepsCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
