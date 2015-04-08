package com.samples.katy.kalarm.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.samples.katy.kalarm.models.pojo.SocialUser;

import java.security.MessageDigest;

public class FacebookService implements Session.StatusCallback, Request.GraphUserCallback {

    private SocialNetworkService.LoginCompletedListener loginCompletedListener;

    public void login(Activity activity, SocialNetworkService.LoginCompletedListener listener) {

        this.loginCompletedListener = listener;
        Session session = Session.openActiveSessionFromCache(activity);
        if (session == null) {
            Session.openActiveSession(activity, true, this);
        }
    }

    public void logout() {
        this.call(Session.getActiveSession(), SessionState.CLOSED, new Exception());
    }

    @Override
    public void call(Session session, SessionState sessionState, Exception e) {
        if (session.isOpened()) {
            Request.executeMeRequestAsync(session, this);
        } else {
            session.closeAndClearTokenInformation();
            session.close();
        }
    }

    @Override
    public void onCompleted(GraphUser graphUser, Response response) {
        this.loginCompletedListener.onComplete(new SocialUser(graphUser.getName(), graphUser.getId()));
    }
}