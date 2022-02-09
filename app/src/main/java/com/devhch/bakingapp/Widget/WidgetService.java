package com.devhch.bakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.devhch.bakingapp.Database.Database;
import com.devhch.bakingapp.Models.Ingredient;
import com.devhch.bakingapp.R;

import java.util.ArrayList;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/27/2020.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(this.getApplicationContext(), intent));
    }

    private static class ListProvider implements RemoteViewsFactory  {
        private ArrayList<Ingredient> ingredients;
        private Context mContext;
        private int appWidgetId;

        ListProvider(Context context, Intent intent) {
            this.mContext = context;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 1);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            ingredients = new ArrayList<>();

            Database database = new Database(mContext);
            ingredients = database.getIngredients(appWidgetId);

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

            views.setTextViewText(R.id.widget_recipe_name, ingredients.get(position).getIngredient());
            views.setTextViewText(R.id.widget_recipe_measure, ingredients.get(position).getQuantity()
                    + " " + ingredients.get(position).getMeasure());

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}

