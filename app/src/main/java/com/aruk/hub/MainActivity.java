package com.aruk.hub;

import android.app.*;
import android.os.*;
import android.content.*;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    private TextToSpeech tts;
    private boolean ready = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);
        layout.setPadding(32, 50, 32, 32);
        layout.setBackgroundColor(0xFF080A0F);

        TextView title = new TextView(this);
        title.setText("ARUK HUB");
        title.setTextSize(32);
        title.setTextColor(0xFFFFD700);
        layout.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("03:30 AM • DAILY LIFE LESSON");
        subtitle.setTextColor(0xFFFFFFFF);
        subtitle.setTextSize(16);
        layout.addView(subtitle);

        Button test = new Button(this);
        test.setText("TEST VOICE");
        layout.addView(test);

        Button enable = new Button(this);
        enable.setText("ENABLE DAILY 3:30 AM");
        layout.addView(enable);

        setContentView(layout);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.UK);
                ready = result != TextToSpeech.LANG_MISSING_DATA
                        && result != TextToSpeech.LANG_NOT_SUPPORTED;
                tts.setSpeechRate(0.88f);
                tts.setPitch(0.85f);
            }
        });

        test.setOnClickListener(v -> {
            if (ready) {
                speak("We are reporting at three thirty A.M. "
                    + "Today's life lesson: Small steps every day create a stronger future. "
                    + "I hope tomorrow is better than today. Until then, keep fighting, "
                    + "keep growing, and become the person you were meant to be.");
            } else {
                Toast.makeText(this, "Voice is loading. Try again.",
                    Toast.LENGTH_SHORT).show();
            }
        });

        enable.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 31) {
                AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (!manager.canScheduleExactAlarms()) {
                    startActivity(new Intent(
                        Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM));
                    return;
                }
            }
            scheduleAlarm();
            Toast.makeText(this, "Briefing scheduled",
                Toast.LENGTH_LONG).show();
        });
    }

    private void speak(String text) {
        if (tts != null && ready) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ARUK_HUB");
        }
    }

    private void scheduleAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, BriefingReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(
            this, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, 3);
        time.set(Calendar.MINUTE, 30);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);

        if (time.getTimeInMillis() <= System.currentTimeMillis()) {
            time.add(Calendar.DAY_OF_YEAR, 1);
        }

        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pending);
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
