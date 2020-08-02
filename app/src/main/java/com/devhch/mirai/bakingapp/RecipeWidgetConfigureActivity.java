package com.devhch.mirai.bakingapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.devhch.mirai.bakingapp.Api.RetrofitClient;
import com.devhch.mirai.bakingapp.Database.Database;
import com.devhch.mirai.bakingapp.Interface.BakingInterface;
import com.devhch.mirai.bakingapp.Interface.OnRequestFinishedListener;
import com.devhch.mirai.bakingapp.Models.Ingredient;
import com.devhch.mirai.bakingapp.Models.Recipe;
import com.devhch.mirai.bakingapp.Models.Widget;
import com.devhch.mirai.bakingapp.Widget.RecipeWidget;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/27/2020.
 */
public class RecipeWidgetConfigureActivity extends Activity implements OnRequestFinishedListener {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    ProgressDialog dialog;
    private ArrayList<Recipe> recipes;
    private Spinner spinner;


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = RecipeWidgetConfigureActivity.this;

            int position = spinner.getSelectedItemPosition();
            Widget model = new Widget(recipes.get(position).getName(),
                    (ArrayList<Ingredient>) recipes.get(position).getIngredients());

            Database db = new Database(RecipeWidgetConfigureActivity.this);
            db.insertItem(model, mAppWidgetId);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RecipeWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public RecipeWidgetConfigureActivity() {
        super();
    }


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setResult(RESULT_CANCELED);

        setContentView(R.layout.recipe_widget_configure);

        spinner = (Spinner) findViewById(R.id.spinner);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        //RetrofitClient retrofitClient = new RetrofitClient();
        BakingInterface bakingInterface =
                RetrofitClient.getClient().create(BakingInterface.class);

        Call<List<Recipe>> recipeCall = bakingInterface.getRecipes();
        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        dialog.dismiss();
                        recipes = (ArrayList<Recipe>) response.body();

                        String[]values= new  String [recipes.size()];
                        for(int i=0; i < recipes.size();i++)
                        {
                            values[i]=recipes.get(i).getName();
                        }

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, values);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(spinnerArrayAdapter);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call,@NonNull Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "There is a problem try again later ! or make sure you are connected to internet"
                        , Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onFailure(String message) {
        dialog.dismiss();
        Toast.makeText(this, "There is a problem try again later ! or make sure you are connected to internet"
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Response<List<Recipe>> response) {
        dialog.dismiss();
        recipes = (ArrayList<Recipe>) response.body();

        String[] values = new String[recipes.size()];
        for (int i = 0; i < recipes.size(); i++) {
            values[i] = recipes.get(i).getName();
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }
}
