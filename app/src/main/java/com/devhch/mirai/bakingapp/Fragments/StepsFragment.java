package com.devhch.mirai.bakingapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devhch.mirai.bakingapp.Adapters.StepsAdapter;
import com.devhch.mirai.bakingapp.Models.Recipe;
import com.devhch.mirai.bakingapp.Models.Step;
import com.devhch.mirai.bakingapp.R;
import com.devhch.mirai.bakingapp.databinding.RecipeFragmentBinding;

import java.util.ArrayList;
import java.util.List;

import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_RECIPE_NAME;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/26/2020.
 */
public class StepsFragment extends Fragment implements StepsAdapter.StepsAdapterOnClickHandler {

    private RecipeFragmentBinding mRecipeFragmentBinding;

    private Context mContext;
    private Recipe mRecipe;

    private StepsAdapter mStepsAdapter;
    private List<Step> mSteps;

    public OnStepClickListener mCallback;

    /**
     * OnStepClickListener interface, calls a method in the host activity named onStepSelected
     */
    public interface OnStepClickListener {
        void onStepSelected(int stepIndex);
    }

    public StepsFragment() {
    }

    public StepsFragment(Context context, List<Step> steps) {
        mContext = context;
        mSteps = steps;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Instantiate RecipeFragmentForCellPhonesBinding using DataBindingUtil
        mRecipeFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.recipe_fragment, container, false);
        View root = mRecipeFragmentBinding.getRoot();

        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList("step_list");
        }

        Bundle bundle = getArguments();
        assert bundle != null;
        String recipeName = bundle.getString(EXTRA_RECIPE_NAME);

        // set Steps Data to recycler
        mRecipeFragmentBinding.stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mStepsAdapter = new StepsAdapter(getContext(), mSteps, recipeName, this);

        mRecipeFragmentBinding.stepsRecyclerView.setAdapter(mStepsAdapter);
        // Return the rootView
        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("step_list", (ArrayList<? extends Parcelable>) mSteps);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    @Override
    public void onItemClick(int stepIndex) {
        // Trigger the callback method and pass in the step index that was clicked
        mCallback.onStepSelected(stepIndex);
    }
}
