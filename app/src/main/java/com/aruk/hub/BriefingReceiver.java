package com.aruk.hub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class BriefingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final TextToSpeech[] holder = new TextToSpeech[1];

        holder[0] = new TextToSpeech(
            context.getApplicationContext(),
            status -> {
                if (status == TextToSpeech.SUCCESS) {
                    TextToSpeech voice = holder[0];
                    voice.setLanguage(Locale.UK);
                    voice.setSpeechRate(0.88f);
                    voice.setPitch(0.85f);

                    String message =
                        "We are reporting at three thirty A.M. " +
                        "Today's life lesson: Small steps every day create a stronger future. " +
                        "I hope tomorrow is better than today. Until then, keep fighting, " +
                        "keep growing, and become the person you were meant to be.";

                    voice.speak(
                        message,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "ARUK_HUB"
                    );
                }
            }
        );
    }
}
