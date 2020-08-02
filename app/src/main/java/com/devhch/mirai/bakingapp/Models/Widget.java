package com.devhch.mirai.bakingapp.Models;

import java.util.ArrayList;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/27/2020.
 */

public class Widget {
    public String recipeTitle;
    public ArrayList<Ingredient> ingredients;

    public Widget(String recipeTitle, ArrayList<Ingredient> ingredients) {
        this.recipeTitle = recipeTitle;
        this.ingredients = ingredients;
    }

}