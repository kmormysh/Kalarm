package com.samples.katy.kalarm.activities;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.TextView;
import android.os.Handler;

import com.samples.katy.kalarm.models.AlarmManager;
import com.samples.katy.kalarm.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AlarmWakeUpActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();
    private PowerManager.WakeLock mWakeLock;
    private MediaPlayer mediaPlayer;
    private static final int WAKELOCK_TIMEOUT = 50 * 1000;
    private static final float HALF_OF_MAX_VOLUME = 0.6f;

    @InjectView(R.id.alarmTime) TextView btnAlarmTime;
    @OnClick(R.id.snooze) void snooze() {
        mediaPlayer.stop();
    };

    @OnClick (R.id.dismiss) void dismiss() {
        mediaPlayer.stop();
        finish();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.alarm_pop_up_screen);
        ButterKnife.inject(this);

        //Play alarm tone
        String tone =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
        mediaPlayer = new MediaPlayer();
        try {
            if (tone != null && !tone.equals("")) {
                Uri toneUri = Uri.parse(tone);
                if (toneUri != null) {
                    mediaPlayer.setDataSource(this, toneUri);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVolume(HALF_OF_MAX_VOLUME, HALF_OF_MAX_VOLUME);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String time = String.format("%02d:%02d", getIntent().getStringExtra(AlarmManager.HOURS),
                getIntent().getStringExtra(AlarmManager.MINUTES));
        btnAlarmTime.setText(time);

        Runnable releaseWakelock = new Runnable() {

            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                }
            }
        };

        new Handler().postDelayed(releaseWakelock, WAKELOCK_TIMEOUT);
    }


    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();

        // Set the window to keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        // Acquire wakelock
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (mWakeLock == null) {
            mWakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        }

        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }
}
