package com.devhch.mirai.bakingapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.devhch.mirai.bakingapp.databinding.StepDetailsFragmentBinding;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.devhch.mirai.bakingapp.Models.Step;
import com.devhch.mirai.bakingapp.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;
import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_LIST_STEP;
import static com.devhch.mirai.bakingapp.Utils.Constant.EXTRA_STEP_INDEX;
import static com.devhch.mirai.bakingapp.Utils.Constant.FAST_FORWARD_INCREMENT;
import static com.devhch.mirai.bakingapp.Utils.Constant.PLAYER_PLAYBACK_SPEED;
import static com.devhch.mirai.bakingapp.Utils.Constant.REWIND_INCREMENT;
import static com.devhch.mirai.bakingapp.Utils.Constant.SAVE_STEP;
import static com.devhch.mirai.bakingapp.Utils.Constant.START_POSITION;
import static com.devhch.mirai.bakingapp.Utils.Constant.STATE_CURRENT_WINDOW;
import static com.devhch.mirai.bakingapp.Utils.Constant.STATE_PLAYBACK_POSITION;
import static com.devhch.mirai.bakingapp.Utils.Constant.STATE_PLAY_WHEN_READY;
import static com.devhch.mirai.bakingapp.Utils.Constant.STATE_STEP_INDEX;
import static com.devhch.mirai.bakingapp.Utils.Constant.TWO_PANE;


/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/27/2020.
 */
public class StepDetailsFragment extends Fragment implements Player.EventListener {

    // Tag for a MediaSessionCompat
    private static final String TAG = StepDetailsFragment.class.getSimpleName();

    private StepDetailsFragmentBinding mStepDetailsFragmentBinding;

    private Activity mActivity;

    private List<Step> steps;
    private int mStepIndex;
    private SimpleExoPlayer mExoPlayer;

    private boolean mTwoPane;

    private long mPlaybackPosition;

    /**
     * Initialize with 0 to start from the first item in the TimeLine
     */
    private int mCurrentWindow;

    private boolean mPlayWhenReady;

    private String mVideoUrl;

    private static MediaSessionCompat sMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private String mThumbnailUrl;
    /**
     * Indicate whether the step of the recipe has a video URL
     */
    private boolean mHasVideoUrl = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Instantiate RecipeFragmentForCellPhonesBinding using DataBindingUtil
        mStepDetailsFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.step_details_fragment, container, false);
        View root = mStepDetailsFragmentBinding.getRoot();

        mActivity = getActivity();

        Bundle bundle = getArguments();
        assert bundle != null;

        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(SAVE_STEP);
            mStepIndex = savedInstanceState.getInt(STATE_STEP_INDEX);
            mTwoPane = savedInstanceState.getBoolean(TWO_PANE);

            mPlaybackPosition = savedInstanceState.getLong(STATE_PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(STATE_CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(STATE_PLAY_WHEN_READY);
        } else {

            steps = bundle.getParcelableArrayList(EXTRA_LIST_STEP);
            mStepIndex = bundle.getInt(EXTRA_STEP_INDEX);
            mTwoPane = bundle.getBoolean(TWO_PANE);

            // Clear the start position
            mCurrentWindow = C.INDEX_UNSET;
            mPlaybackPosition = C.TIME_UNSET;
            mPlayWhenReady = true;
        }


        // If the Step exists, set the description to the TextView and handle media URL.
        // Otherwise, create a Log statement that indicates that the step was not found
        if (steps != null) {

            // Set correct description
            mStepDetailsFragmentBinding.description.setText(steps.get(mStepIndex).getDescription());
            mStepDetailsFragmentBinding.currentStep.setText(
                    String.format("%s / %s ", (mStepIndex + 1), steps.size()));

            // Handles video URL and thumbnail URL
            handleMediaUrl();

        } else {
            Toast.makeText(mActivity, "This fragment has a null step", Toast.LENGTH_SHORT).show();
        }

        // Initialize the Media Session
        initializeMediaSession();

        //initializePlayer(mHasVideoUrl);
        //show();

        mStepDetailsFragmentBinding.nextButton.setOnClickListener(v -> {
            if (mStepIndex == steps.size() - 1)
                return;
            mStepIndex++;

            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();

            Bundle mBundle = new Bundle();
            mBundle.putParcelableArrayList(EXTRA_LIST_STEP, (ArrayList<? extends Parcelable>) steps);
            mBundle.putInt(EXTRA_STEP_INDEX, mStepIndex);
            //mBundle.getString(EXTRA_RECIPE_NAME, );
            // mBundle.getString(EXTRA_RECIPE_DESCRIPTION);

            stepDetailsFragment.setArguments(mBundle);
            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.step_details_fragment, stepDetailsFragment)
                    .commit();


        });

        mStepDetailsFragmentBinding.backButton.setOnClickListener(v -> {
            if (mStepIndex == 0)
                return;
            mStepIndex--;

            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();

            Bundle mBundle = new Bundle();
            mBundle.putParcelableArrayList(EXTRA_LIST_STEP, (ArrayList<? extends Parcelable>) steps);
            mBundle.putInt(EXTRA_STEP_INDEX, mStepIndex);
            //mBundle.getString(EXTRA_RECIPE_NAME, );
            // mBundle.getString(EXTRA_RECIPE_DESCRIPTION);

            stepDetailsFragment.setArguments(mBundle);
            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.step_details_fragment, stepDetailsFragment)
                    .commit();

        });


        hideButtonAtBeginningEnd();
        return root;
    }

    private void initializePlayer(boolean hasVideoUrl) {
        // Check if the step of the recipe has a video
        if (steps.get(mStepIndex).getVideoURL().isEmpty() && steps.get(mStepIndex).getThumbnailURL().isEmpty()) {
            mStepDetailsFragmentBinding.playerView.setVisibility(GONE);
            mStepDetailsFragmentBinding.image.setVisibility(GONE);
            mStepDetailsFragmentBinding.empty.setVisibility(View.VISIBLE);
        } else if (!steps.get(mStepIndex).getVideoURL().isEmpty()) {
            mVideoUrl = steps.get(mStepIndex).getVideoURL();
            mStepDetailsFragmentBinding.empty.setVisibility(View.GONE);
            mStepDetailsFragmentBinding.image.setVisibility(GONE);
            mStepDetailsFragmentBinding.playerView.setVisibility(View.VISIBLE);

            if (hasVideoUrl) {
                if (mExoPlayer == null) {
                    // Create an instance of the ExoPlayer
                    DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(mActivity);
                    TrackSelector trackSelector = new DefaultTrackSelector();
                    LoadControl loadControl = new DefaultLoadControl();
                    mExoPlayer = ExoPlayerFactory.newSimpleInstance(mActivity,
                            defaultRenderersFactory, trackSelector, loadControl);

                    // Set the ExoPlayer to the playerView
                    mStepDetailsFragmentBinding.playerView.setPlayer(mExoPlayer);

                    mExoPlayer.setPlayWhenReady(mPlayWhenReady);
                }

                // Set the Player.EventListener to this fragment
                mExoPlayer.addListener(this);

                // Prepare the MediaSource
                Uri mediaUri = Uri.parse(mVideoUrl);
                MediaSource mediaSource = buildMediaSource(mediaUri);

                // Restore the playback position
                boolean haveStartPosition = mCurrentWindow != C.INDEX_UNSET;
                if (haveStartPosition) {
                    mExoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
                }
                // The boolean flags indicate whether to reset position and state of the player
                mExoPlayer.prepare(mediaSource, !haveStartPosition, false);

                mStepDetailsFragmentBinding.description.setText(steps.get(mStepIndex).getDescription());
                mStepDetailsFragmentBinding.currentStep.setText(
                        String.format("%s / %s ", (mStepIndex + 1), steps.size()));
            }

        } else {
            String imageUrl = steps.get(mStepIndex).getThumbnailURL();
            mStepDetailsFragmentBinding.empty.setVisibility(View.GONE);
            mStepDetailsFragmentBinding.playerView.setVisibility(View.GONE);
            mStepDetailsFragmentBinding.image.setVisibility(View.VISIBLE);

            Glide.with(mActivity)
                    .load(imageUrl)
                    .placeholder(R.drawable.step_details_not_found)
                    .into(mStepDetailsFragmentBinding.image);
        }

        hideSystemUi();
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            updateCurrentPosition();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat
        sMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls
        sMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible
        sMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_REWIND |
                                PlaybackStateCompat.ACTION_FAST_FORWARD |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        sMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller
        sMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the fragment is active
        sMediaSession.setActive(true);
    }

    private void handleMediaUrl() {
        // Get video URL and thumbnail URL from the step of the recipe
        mVideoUrl = steps.get(mStepIndex).getVideoURL();
        mThumbnailUrl = steps.get(mStepIndex).getThumbnailURL();

        // Check if the thumbnail URL contains an "mp4" file
        // Step 5 of the Nutella Pie has an mp4 file in the thumbnail URL
        if (mThumbnailUrl.contains(getResources().getString(R.string.mp4))) {
            mVideoUrl = mThumbnailUrl;
        }

        if (!mVideoUrl.isEmpty()) {
            // If the video URL exists, set the boolean variable to true
            mHasVideoUrl = true;
        } else if (!mThumbnailUrl.isEmpty()) {
            // If the thumbnail URL exists, load thumbnail with Picasso
            mStepDetailsFragmentBinding.playerView.setVisibility(View.GONE);
            Glide.with(mActivity)
                    .load(mThumbnailUrl)
                    .error(R.drawable.step_details_not_found)
                    .placeholder(R.drawable.step_details_not_found)
                    .into(mStepDetailsFragmentBinding.image);
        } else {
            // If the step of the recipe has no visual media, load chef image
            mStepDetailsFragmentBinding.playerView.setVisibility(View.GONE);
            mStepDetailsFragmentBinding.image.setImageResource(R.drawable.step_details_not_found);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // End the Media session when it is no longer needed
        sMediaSession.setActive(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Starting with API level 24 Android supports multiple windows. As our app can be visible
        // but not active in split window mode, we need to initialize the player in onStart().
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            // Initialize the player if the step of the recipe has a video URL
            initializePlayer(mHasVideoUrl);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        // Before API level 24 we wait as long as possible until we grab resources, so we wait until
        // onResume() before initializing the player.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M || mExoPlayer == null) {
            // Initialize the player if the step of the recipe has a video URL
            initializePlayer(mHasVideoUrl);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_STEP_INDEX, mStepIndex);
        outState.putBoolean(TWO_PANE, mTwoPane);
        outState.putParcelableArrayList(SAVE_STEP, (ArrayList<? extends Parcelable>) steps);

        // Store the playback position to our bundle
        updateCurrentPosition();
        outState.putLong(STATE_PLAYBACK_POSITION, mPlaybackPosition);
        outState.putInt(STATE_CURRENT_WINDOW, mCurrentWindow);
        outState.putBoolean(STATE_PLAY_WHEN_READY, mPlayWhenReady);
    }

    /**
     * Updates the current state of the player
     */
    private void updateCurrentPosition() {
        if (mExoPlayer != null) {
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        if (isSinglePaneLand()) {
            mStepDetailsFragmentBinding.playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            mStepDetailsFragmentBinding.empty.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            mStepDetailsFragmentBinding.image.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }
    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification
     *
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY && playWhenReady) {
            // When ExoPlayer is playing, update the PlayBackState
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), PLAYER_PLAYBACK_SPEED);

            // When ExoPlayer is playing, hide the previous and next button
            hideButtonWhenPlaying();
        } else if (playbackState == Player.STATE_READY) {
            // When ExoPlayer is paused, update the PlayBackState
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), PLAYER_PLAYBACK_SPEED);

            // When ExoPlayer is paused, show the previous and next button
            showButtonWhenPausedEnded();
        } else if (playbackState == Player.STATE_ENDED) {
            // When ExoPlayer is ended, show the previous and next button
            showButtonWhenPausedEnded();
        }
        sMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


    /**
     * When ExoPlayer is playing, hide the previous and next button in the two-pane tablet or
     * single-pane landscape mode.
     */
    private void hideButtonWhenPlaying() {
        if (mTwoPane | isSinglePaneLand()) {
            mStepDetailsFragmentBinding.backButton.setVisibility(View.GONE);
            mStepDetailsFragmentBinding.nextButton.setVisibility(View.GONE);
        }
    }

    /**
     * When ExoPlayer is paused or ended, show the previous and next button in the two-pane tablet or
     * single-pane landscape mode.
     */
    private void showButtonWhenPausedEnded() {
        if (mTwoPane | isSinglePaneLand()) {
            mStepDetailsFragmentBinding.backButton.setVisibility(View.VISIBLE);
            mStepDetailsFragmentBinding.nextButton.setVisibility(View.VISIBLE);
        }

        // Hide next button at the end of the step and the previous button at the beginning.
        hideButtonAtBeginningEnd();
    }


    /**
     * Returns true in a single-pane landscape mode.
     */
    private boolean isSinglePaneLand() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && !mTwoPane;
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onRewind() {
            mExoPlayer.seekTo(Math.max(mExoPlayer.getCurrentPosition()
                    - REWIND_INCREMENT, START_POSITION));
        }

        @Override
        public void onFastForward() {
            long duration = mExoPlayer.getDuration();
            mExoPlayer.seekTo(Math.min(mExoPlayer.getCurrentPosition()
                    + FAST_FORWARD_INCREMENT, duration));
        }
    }

    /**
     * Hide next button at the end of the step and the previous button at the beginning.
     */
    private void hideButtonAtBeginningEnd() {
        if (mStepIndex <= 0) {
            mStepDetailsFragmentBinding.backButton.setVisibility(GONE);
        } else {
            mStepDetailsFragmentBinding.backButton.setVisibility(View.VISIBLE);
        }

        if (mStepIndex >= steps.size() - 1) {
            mStepDetailsFragmentBinding.nextButton.setVisibility(GONE);
        } else {
            mStepDetailsFragmentBinding.nextButton.setVisibility(View.VISIBLE);
        }
    }

}
