package com.samples.katy.kalarm.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samples.katy.kalarm.R;
import com.samples.katy.kalarm.models.SocialNetworkService;
import com.samples.katy.kalarm.models.SocialServiceFactory;
import com.samples.katy.kalarm.models.pojo.SocialUser;
import com.samples.katy.kalarm.utils.FacebookService;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingsActivity extends PreferenceActivity implements SocialNetworkService.LoginCompletedListener {
    private static final boolean ALWAYS_SIMPLE_PREFS = false;
    private SharedPreferences sharedPreferences;
    private static SocialNetworkService socialNetworkService;
    private static final String SETTINGS_KEY_USER_ID = "userId";
    private static final String SETTINGS_KEY_USER_NAME = "userName";

    @InjectView(R.id.login_text)
    TextView loginText;
    @InjectView(R.id.fb_username)
    TextView fbUserName;
    @InjectView(R.id.fb_login)
    ImageButton fb_login;
    @InjectView(R.id.successful_login)
    RelativeLayout logoutLayout;

    @OnClick(R.id.fb_logout)
    void fbLogoutClick() {
        goToNotAuthenticatedUserState();
        SocialServiceFactory.Create(this, SocialServiceFactory.SocialNetworkType.FACEBOOK).logout();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SETTINGS_KEY_USER_NAME);
        editor.remove(SETTINGS_KEY_USER_ID);
        editor.commit();
    }

    @OnClick(R.id.fb_login)
    void fbLoginClick() {
        socialNetworkService = SocialServiceFactory.Create(this, SocialServiceFactory.SocialNetworkType.FACEBOOK);
        socialNetworkService.login(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupSimplePreferencesScreen();
        setContentView(R.layout.pref_fb_login);
        ButterKnife.inject(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.getString(SETTINGS_KEY_USER_ID, "").equals("")) {
            goToAuthenticatedUserState(sharedPreferences.getString(SETTINGS_KEY_USER_NAME, ""));
        } else {
            goToNotAuthenticatedUserState();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO: create an event for onActivityResult and remove this explicit cast
        if (socialNetworkService instanceof FacebookService)
            ((FacebookService) socialNetworkService).handleOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //TODO: create an event for onDestroy and remove this explicit cast
        if (socialNetworkService instanceof FacebookService)
            ((FacebookService) socialNetworkService).handleOnActivityDestroy();
    }

    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }
        addPreferencesFromResource(R.xml.pref_settings);
        bindPreferenceSummaryToValue(findPreference("ringtone"));
        bindPreferenceSummaryToValue(findPreference("snooze_length"));
        bindPreferenceSummaryToValue(findPreference("difficulty"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    private void goToAuthenticatedUserState(String name) {
        fbUserName.setText(name);
        fb_login.setVisibility(View.GONE);
        logoutLayout.setVisibility(View.VISIBLE);
    }

    private void goToNotAuthenticatedUserState() {
        fb_login.setVisibility(View.VISIBLE);
        logoutLayout.setVisibility(View.GONE);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        if (!isSimplePreferences(this)) {
            loadHeadersFromResource(R.xml.pref_headers, target);
        }
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                if (TextUtils.isEmpty(stringValue)) {
                    preference.setSummary(R.string.pref_ringtone_silent);
                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));
                    if (ringtone == null) {
                        preference.setSummary(null);
                    } else {
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public void onError() {
        Toast.makeText(this, getString(R.string.facebook_auth_failed_text), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete(SocialUser socialUser) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SETTINGS_KEY_USER_ID, socialUser.getUserId());
        editor.putString(SETTINGS_KEY_USER_NAME, socialUser.getUserName());
        editor.commit();
        goToAuthenticatedUserState(socialUser.getUserName());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings);
            bindPreferenceSummaryToValue(findPreference("ringtone"));
            bindPreferenceSummaryToValue(findPreference("snooze_length"));
            bindPreferenceSummaryToValue(findPreference("difficulty"));
        }
    }
}
