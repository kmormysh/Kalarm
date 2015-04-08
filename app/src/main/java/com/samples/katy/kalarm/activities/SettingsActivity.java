package com.samples.katy.kalarm.activities;

import android.annotation.TargetApi;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samples.katy.kalarm.R;
import com.samples.katy.kalarm.utils.FacebookService;
import com.samples.katy.kalarm.models.pojo.SocialUser;
import com.samples.katy.kalarm.utils.SocialNetworkService;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingsActivity extends PreferenceActivity implements SocialNetworkService.LoginCompletedListener {
    private static final boolean ALWAYS_SIMPLE_PREFS = false;
    private SharedPreferences sharedPreferences;

    @InjectView(R.id.login_text)
    TextView loginText;
    @InjectView(R.id.fb_username)
    TextView fbUserName;
    @InjectView(R.id.fb_login)
    ImageButton fb_login;
    @InjectView(R.id.successful_login)
    LinearLayout logoutLayout;

    @OnClick(R.id.fb_logout)
    void logout() {
        facebookService.logout();
        addLogoutToLayout();
    }

    @OnClick(R.id.fb_login)
    void fbLogin() {
        facebookService = new FacebookService();
        facebookService.login(this, this);

        addLogoutToLayout();
    }

    private static FacebookService facebookService;

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

        addLogoutToLayout();
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
    public void onComplete(SocialUser user) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("userId", user.getUserId());
        editor.putString("userName", user.getUserName());
        editor.commit();
    }

    @Override
    public void onError() {

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

    private void addLogoutToLayout() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = sharedPreferences.getString("userName", "");
        if (!userName.equals("")) {
            fbUserName.setText(userName);
            fb_login.setVisibility(View.INVISIBLE);
            logoutLayout.setVisibility(View.VISIBLE);
        } else {
            sharedPreferences.edit().remove("userName");
            sharedPreferences.edit().remove("userId");
            sharedPreferences.edit().commit();
            fb_login.setVisibility(View.VISIBLE);
            logoutLayout.setVisibility(View.INVISIBLE);
        }
    }
}
