package com.terryyamg.voicecontroltest;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 9527; //對應用數字

    private ListView lvSpeak;
    private TextToSpeech toSpeech;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btMessage = (Button) findViewById(R.id.btMessage);
        lvSpeak = (ListView) findViewById(R.id.lvSpeak);
        showMic(); //顯示麥克風收音

        //TextToSpeech 初始化
        toSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    toSpeech.setLanguage(Locale.US);
                }
            }
        });

        //再說一次
        btMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMic();
            }
        });

    }

    //顯示麥克風收音
    private void showMic(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say OK!");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {

            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            lvSpeak.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, matches)); //列出辨識出的句子

            if (matches.contains("OK")) { //比對句子
                //Android 說 google
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String utteranceId=this.hashCode() + "";
                    toSpeech.speak("google", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                } else {
                    toSpeech.speak("google", TextToSpeech.QUEUE_FLUSH, null);
                }

            }else{
                Toast.makeText(this,"查無此句，請重試",Toast.LENGTH_SHORT).show();
            }
        }
    }

}