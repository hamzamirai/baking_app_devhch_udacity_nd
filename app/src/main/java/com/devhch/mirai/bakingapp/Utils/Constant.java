package com.devhch.mirai.bakingapp.Utils;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/26/2020.
 */
public final class Constant {

    public Constant() {
    }

    /** The base baking URL */
    public static final String BAKING_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    /** Extra for the recipe to be received in the intent */
    public static final String EXTRA_RECIPE = "recipe";

    /** Extra for the step index to be received in the intent */
    public static final String EXTRA_STEP= "step";
    public static final String EXTRA_STEP_BUNDLE = "step_bundle";
    public static final String EXTRA_LIST_STEP= "steps_list";

    public static final String STATE_PLAYBACK_POSITION = "state_playback_position";
    public static final String STATE_CURRENT_WINDOW = "state_current_window";
    public static final String STATE_PLAY_WHEN_READY = "state_play_when_ready";

    public static final String TWO_PANE = "two_pane";

    public static final String EXTRA_RECIPE_NAME = "recipe_name";
    public static final String EXTRA_RECIPE_DESCRIPTION = "recipe_description";

    /** Extra for the step index to be received in the intent */
    public static final String EXTRA_STEP_INDEX = "step_index";

    /** Constant string for saving the current state of StepDetailFragment */
    public static final String SAVE_STEP = "save_step";
    public static final String STATE_STEP_INDEX = "state_step_index";

    /** Constants for ExoPlayer */
    public static final float PLAYER_PLAYBACK_SPEED = 1f;
    public static final int REWIND_INCREMENT = 3000;
    public static final int FAST_FORWARD_INCREMENT = 3000;
    public static final int START_POSITION = 0;

    /** Constants for position number */
    public static final int POSITION_ZERO = 0;
    public static final int POSITION_ONE = 1;
    public static final int POSITION_TWO = 2;
    public static final int POSITION_THREE = 3;
    public static final int NUM_POSITION_FOUR = 4;

    /** Constants used for MainActivityBasicTest */
    public static final String RECIPE_NAME_AT_ZERO = "Nutella Pie";
    public static final String RECIPE_NAME_AT_ONE = "Brownies";
    public static final String RECIPE_NAME_AT_TWO = "Yellow Cake";
    public static final String RECIPE_NAME_AT_THREE = "Brownies";

}
