package com.samples.katy.kalarm.utils;

import android.app.Activity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.samples.katy.kalarm.models.pojo.SocialUser;

import java.util.Arrays;

public class FacebookService implements FacebookCallback<LoginResult>, SocialNetworkService {

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private SocialUser socialUser;
    private LoginManager loginManager;
    private LoginCompletedListener loginCompletedListener;

    @Override
    public void logout() {
        loginManager.logOut();
        socialUser = new SocialUser();
    }

    @Override
    public void login(Activity activity, LoginCompletedListener newProfileListener) {
        this.loginCompletedListener = newProfileListener;
        loginManager.logInWithReadPermissions(activity, Arrays.asList("public_profile"));
    }

    public FacebookService(Activity activity) {
        socialUser = new SocialUser();
        FacebookSdk.sdkInitialize(activity);
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

    public CallbackManager getCallbackManager() {
        return this.callbackManager;
    }

    public ProfileTracker getProfileTracker() {
        return this.profileTracker;
    }

    private void handleNewProfile(Profile profile) {
        socialUser.setUserId(profile.getId());
        socialUser.setUserName(profile.getName());
        if (this.loginCompletedListener != null) {
            this.loginCompletedListener.onComplete(socialUser);
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Profile currentProfile = Profile.getCurrentProfile();
        if (currentProfile != null) {
            handleNewProfile(currentProfile);
        }
    }

    @Override
    public void onCancel() { }

    @Override
    public void onError(FacebookException e) {
        this.loginCompletedListener.onError();
    }
}