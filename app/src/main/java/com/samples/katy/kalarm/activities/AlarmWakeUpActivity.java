package com.samples.katy.kalarm.activities;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;

import com.samples.katy.kalarm.utils.AlarmManager;
import com.samples.katy.kalarm.R;

public class AlarmWakeUpActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();
    private PowerManager.WakeLock mWakeLock;
    private MediaPlayer mediaPlayer;
    private static final int WAKELOCK_TIMEOUT = 50 * 1000;
    private static final float HALF_OF_MAX_VOLUME = 0.6f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.alarm_pop_up_screen);

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

        TextView btnAlarmTime = (TextView) findViewById(R.id.alarmTime);
        String time = String.format("%02d:%02d", getIntent().getStringExtra(AlarmManager.HOURS),
                getIntent().getStringExtra(AlarmManager.MINUTES));
        btnAlarmTime.setText(time);

        Button snooze = (Button) findViewById(R.id.snooze);
        snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
            }
        });

        Button dismiss = (Button) findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });

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
