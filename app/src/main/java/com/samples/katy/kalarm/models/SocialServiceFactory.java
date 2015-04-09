package com.samples.katy.kalarm.models;

import android.content.Context;

import com.samples.katy.kalarm.utils.FacebookService;

public class SocialServiceFactory {
    public static SocialNetworkService Create(Context context, SocialNetworkType type) {
        switch (type) {
            case FACEBOOK:
                return new FacebookService(context);
            default:
                throw new UnsupportedOperationException("not implemented yet ;(");
        }
    }

    public enum SocialNetworkType {FACEBOOK, TWITTER, VK, GOOGLE_PLUS}

}
