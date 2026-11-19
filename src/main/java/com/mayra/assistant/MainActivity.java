
package com.mayra.assistant;

import android.app.Activity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.content.Intent;
import android.widget.*;
import android.graphics.Color;
import android.view.Gravity;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    TextToSpeech speaker;
    TextView message;
    Button listen;
    static final int REQUEST_VOICE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(30, 30, 30, 30);
        layout.setBackgroundColor(Color.rgb(20, 15, 40));

        TextView title = new TextView(this);
        title.setText("💜 Mayra AI");
        title.setTextSize(32);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);

        message = new TextView(this);
        message.setText("হ্যালো! আমি Mayra AI।");
        message.setTextSize(20);
        message.setTextColor(Color.WHITE);
        message.setGravity(Gravity.CENTER);
        message.setPadding(10, 40, 10, 40);

        listen = new Button(this);
        listen.setText("🎤 কথা বলুন");
        listen.setTextSize(20);

        layout.addView(title);
        layout.addView(message);
        layout.addView(listen);

        setContentView(layout);

        speaker = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                speaker.setLanguage(new Locale("bn", "BD"));
            }
        });

        listen.setOnClickListener(v -> startListening());
    }

    private void startListening() {
        Intent intent = new Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        );
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            "bn-BD"
        );
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );

        try {
            startActivityForResult(intent, REQUEST_VOICE);
        } catch (Exception e) {
            message.setText("ভয়েস সার্ভিস পাওয়া যাচ্ছে না।");
        }
    }

    @Override
    protected void onActivityResult(
        int requestCode, int resultCode, Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VOICE &&
            resultCode == RESULT_OK && data != null) {

            ArrayList<String> results =
                data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                );

            if (results != null && !results.isEmpty()) {
                String text = results.get(0);
                message.setText("আপনি বলেছেন: " + text);
                speaker.speak(
                    "আপনি বলেছেন " + text,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "mayra_voice"
                );
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (speaker != null) {
            speaker.stop();
            speaker.shutdown();
        }
        super.onDestroy();
    }
}
