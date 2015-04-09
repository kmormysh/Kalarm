package com.samples.katy.kalarm.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.samples.katy.kalarm.models.SocialNetworkService;
import com.samples.katy.kalarm.models.pojo.SocialUser;

import java.util.Arrays;

public class FacebookService implements FacebookCallback<LoginResult>, SocialNetworkService {

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private SocialUser socialUser;
    private LoginManager loginManager;
    private LoginCompletedListener loginCompletedListener;

    public FacebookService(Context context) {
        socialUser = new SocialUser();
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, this);
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                if (profile2 != null) {
                    handleNewProfile(profile2);
                } else {
                    socialUser = new SocialUser();
                }
            }
        };
    }

    @Override
    public void logout() {
        loginManager.logOut();
    }

    @Override
    public void login(Activity activity, LoginCompletedListener newProfileListener) {
        this.loginCompletedListener = newProfileListener;
        loginManager.logInWithReadPermissions(activity, Arrays.asList("public_profile"));
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Profile currentProfile = Profile.getCurrentProfile();
        if (currentProfile != null) {
            handleNewProfile(currentProfile);
        }
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onError(FacebookException e) {
        if (this.loginCompletedListener != null) {
            this.loginCompletedListener.onError();
        }
    }

    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    public void handleOnActivityDestroy(){
        profileTracker.stopTracking();
    }

    private void handleNewProfile(Profile profile) {
        socialUser.setUserId(profile.getId());
        socialUser.setUserName(profile.getName());
        if (this.loginCompletedListener != null) {
            this.loginCompletedListener.onComplete(socialUser);
        }
    }
}
