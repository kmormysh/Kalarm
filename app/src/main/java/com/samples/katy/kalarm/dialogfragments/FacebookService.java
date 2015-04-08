package com.samples.katy.kalarm.dialogfragments;

import android.app.Activity;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class FacebookService implements Session.StatusCallback, Request.GraphUserCallback {

    private GraphUser graphUser;

    public void login(Activity activity) {
        Session session = Session.getActiveSession();
        Session.openActiveSession(activity, true, this);
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
        }
    }

    @Override
    public void onCompleted(GraphUser graphUser, Response response) {
        this.graphUser = graphUser;
    }

    public GraphUser getGraphUser() {
        return graphUser;
    }


}