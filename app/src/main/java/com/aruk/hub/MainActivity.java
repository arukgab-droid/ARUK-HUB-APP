package com.aruk.hub;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {

    private TextToSpeech tts;
    private EditText hourInput, minuteInput;
    private TextView statusText;
    private int gold = Color.rgb(245, 190, 55);
    private int background = Color.rgb(10, 12, 18);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(background);
        getWindow().setNavigationBarColor(background);
        getWindow().getDecorView().setSystemUiVisibility(0);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 28, 24, 24);
        root.setBackgroundColor(background);

        TextView brand = new TextView(this);
        brand.setText("ARUK HUB");
        brand.setTextSize(34);
        brand.setTextColor(gold);
        brand.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        root.addView(brand);

        TextView subtitle = new TextView(this);
        subtitle.setText("YOUR DAILY MOMENT OF STRENGTH");
        subtitle.setTextSize(11);
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setPadding(0, 5, 0, 22);
        root.addView(subtitle);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(20, 20, 20, 20);
        card.setBackground(round(Color.rgb(24, 27, 36), 22));
        root.addView(card, new LinearLayout.LayoutParams(
                -1, -2));

        TextView heading = new TextView(this);
        heading.setText("DAILY BRIEFING");
        heading.setTextColor(gold);
        heading.setTextSize(13);
        heading.setTypeface(null, Typeface.BOLD);
        card.addView(heading);

        TextView description = new TextView(this);
        description.setText(
            "A new day. A stronger mindset.\nListen, reflect, and keep growing."
        );
        description.setTextColor(Color.WHITE);
        description.setTextSize(17);
        description.setPadding(0, 14, 0, 20);
        card.addView(description);

        TextView timeLabel = new TextView(this);
        timeLabel.setText("SET YOUR DAILY TIME");
        timeLabel.setTextColor(Color.LTGRAY);
        timeLabel.setTextSize(12);
        timeLabel.setPadding(0, 8, 0, 10);
        card.addView(timeLabel);

        LinearLayout times = new LinearLayout(this);
        times.setOrientation(LinearLayout.HORIZONTAL);

        hourInput = new EditText(this);
        hourInput.setSingleLine(true);
        hourInput.setInputType(2);
        hourInput.setHint("Hour  (0–23)");
        hourInput.setHintTextColor(Color.GRAY);
        hourInput.setTextColor(Color.WHITE);
        hourInput.setText("3");
        hourInput.setPadding(14, 8, 14, 8);
        hourInput.setBackground(round(Color.rgb(40, 43, 52), 12));

        minuteInput = new EditText(this);
        minuteInput.setSingleLine(true);
        minuteInput.setInputType(2);
        minuteInput.setHint("Minute  (0–59)");
        minuteInput.setHintTextColor(Color.GRAY);
        minuteInput.setTextColor(Color.WHITE);
        minuteInput.setText("30");
        minuteInput.setPadding(14, 8, 14, 8);
        minuteInput.setBackground(round(Color.rgb(40, 43, 52), 12));

        LinearLayout.LayoutParams timeParams =
                new LinearLayout.LayoutParams(0, 58, 1);
        timeParams.setMargins(0, 0, 8, 0);
        times.addView(hourInput, timeParams);

        LinearLayout.LayoutParams minuteParams =
                new LinearLayout.LayoutParams(0, 58, 1);
        minuteParams.setMargins(8, 0, 0, 0);
        times.addView(minuteInput, minuteParams);
        card.addView(times);

        Button enable = makeButton("SAVE DAILY SCHEDULE", gold,
                Color.BLACK);
        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(-1, 56);
        buttonParams.setMargins(0, 18, 0, 0);
        card.addView(enable, buttonParams);

        Button test = makeButton("▶️   TEST VOICE NOW",
                Color.rgb(42, 45, 55), Color.WHITE);
        LinearLayout.LayoutParams testParams =
                new LinearLayout.LayoutParams(-1, 54);
        testParams.setMargins(0, 12, 0, 0);
        root.addView(test, testParams);

        Button cancel = makeButton("TURN SCHEDULE OFF",
                Color.rgb(42, 30, 30), Color.rgb(255, 150, 150));
        LinearLayout.LayoutParams cancelParams =
                new LinearLayout.LayoutParams(-1, 50);
        cancelParams.setMargins(0, 10, 0, 0);
        root.addView(cancel, cancelParams);

        statusText = new TextView(this);
        statusText.setText("●  Ready to begin");
        statusText.setTextColor(Color.LTGRAY);
        statusText.setTextSize(13);
        statusText.setPadding(2, 20, 2, 0);
        root.addView(statusText);

        TextView footer = new TextView(this);
        footer.setText(
            "\nDISCIPLINE  •  GROWTH  •  HOPE\n\n" +
            "“Become stronger than yesterday.”"
        );
        footer.setGravity(Gravity.CENTER);
        footer.setTextColor(gold);
        footer.setTextSize(13);
        footer.setPadding(0, 25, 0, 10);
        root.addView(footer);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.addView(root);
        setContentView(scroll);

        tts = new TextToSpeech(this, result -> {
            if (result == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.UK);
                tts.setSpeechRate(0.88f);
                tts.setPitch(0.90f);
            }
        });

        enable.setOnClickListener(v -> saveSchedule());
        test.setOnClickListener(v -> testVoice());
        cancel.setOnClickListener(v -> cancelSchedule());
    }

    GradientDrawable round(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }

    Button makeButton(String text, int bg, int fg) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextColor(fg);
        b.setTextSize(13);
        b.setTypeface(null, Typeface.BOLD);
        b.setAllCaps(false);
        b.setBackground(round(bg, 15));
        return b;
    }

    void testVoice() {
        if (tts != null) {
            String message =
                "We are reporting now. Today's life lesson. " +
                "Small steps taken consistently create extraordinary results. " +
                "I hope tomorrow is better than today. Until then, " +
                "keep fighting, keep growing, and become the person " +
                "you were meant to be.";
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "TEST");
            statusText.setText("●  Voice test started");
        } else {
            statusText.setText("Voice engine is starting. Try again shortly.");
        }
    }

    void saveSchedule() {
        int hour, minute;
        try {
            hour = Integer.parseInt(hourInput.getText().toString());
            minute = Integer.parseInt(minuteInput.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Enter a valid time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            Toast.makeText(this, "Hour 0–23; minute 0–59", Toast.LENGTH_LONG).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= 31) {
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (!am.canScheduleExactAlarms()) {
                startActivity(new Intent(
                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM));
                Toast.makeText(this,
                    "Allow alarms, then return and tap Save again.",
                    Toast.LENGTH_LONG).show();
                return;
            }
        }

        scheduleAt(hour, minute);
        getSharedPreferences("aruk", MODE_PRIVATE)
                .edit().putBoolean("enabled", true)
                .putInt("hour", hour).putInt("minute", minute).apply();

        statusText.setText(String.format(
                Locale.UK, "●  Scheduled daily at %02d:%02d", hour, minute));
        Toast.makeText(this, "Schedule saved", Toast.LENGTH_SHORT).show();
    }

    void scheduleAt(int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        if (c.getTimeInMillis() <= System.currentTimeMillis()) {
            c.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent i = new Intent(this, BriefingReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
            this, 1, i,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
    }

    void cancelSchedule() {
        Intent i = new Intent(this, BriefingReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
            this, 1, i,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(pi);
        pi.cancel();

        getSharedPreferences("aruk", MODE_PRIVATE)
                .edit().putBoolean("enabled", false).apply();
        statusText.setText("●  Daily schedule is off");
        Toast.makeText(this, "Schedule turned off", Toast.LENGTH_SHORT).show();
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
