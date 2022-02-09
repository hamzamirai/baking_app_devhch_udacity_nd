package com.devhch.bakingapp.Utils;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created By Hamza Chaouki [Mirai Dev] On 7/31/2020.
 */
public final class MainIdlingResource implements IdlingResource {

    private static MainIdlingResource sMainIdlingResource;

    @Nullable
    private volatile ResourceCallback mCallback;

    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }

    public static MainIdlingResource getInstance(){
        if(sMainIdlingResource == null){
            sMainIdlingResource = new MainIdlingResource();
        }
        return sMainIdlingResource;
    }


}

