package com.samples.katy.kalarm.models;

import android.app.Activity;
import android.content.Intent;

import com.samples.katy.kalarm.models.pojo.SocialUser;

public interface SocialNetworkService {

    public void login(Activity activity, LoginCompletedListener listener);

    public void logout();

    public interface LoginCompletedListener {
        public void onComplete(SocialUser user);

        public void onError();
    }
}
