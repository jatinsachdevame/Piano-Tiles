package com.example.pianotiles;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    int id;
    Button button;
    int start;
    int score;
    int[] index = {0,0,0,0};
    Random random;
    TextView current_score,bestscore;
    private static SharedPreferences sharedPreferences;
    int play = 0;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        current_score = ((TextView) findViewById(R.id.score));
        bestscore = ((TextView) findViewById(R.id.bestscore));
        if(sharedPreferences == null)
            sharedPreferences = this.getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
        int returned_score = Integer.valueOf(read("score", "0"));
        bestscore.setText("Best Score : " + Integer.toString(returned_score));
        initialize();



    }

    public void startGame(View view) {
        view.animate();

        if (play == 0) {
            play = 1;
            mp = MediaPlayer.create(this,R.raw.song);
            mp.start();
        }

        String s = view.toString();
        int i = s.indexOf("app:id/b");
        String s2 = s.substring(i+8);
        if (s2.length() == 2) {
            s2 = s2.substring(0,1);
        } else {
            s2 = s2.substring(0,2);
        }

        id = Integer.valueOf(s2);

        if (index[3] == id) {
            for(int j =0;j<4;++j) {
                String s4 = "b" + Integer.toString(index[j]);
                int resID = getResources().getIdentifier(s4, "id", getPackageName());
                button = ((Button) findViewById(resID));
                button.setBackgroundResource(android.R.drawable.btn_default);
            }
            score++;
            current_score.setText("Score : "+Integer.toString(score));
            painttiles();
        } else {
            play = 0;
            mp.stop();
            String msg = "Your score is " + Integer.valueOf(score);
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
            current_score.setText("Score : 0");
            int returned_score = Integer.valueOf(read("score", "1"));
            if(returned_score !=0) {
                if(returned_score<score) {
                    write("score",Integer.toString(score));
                    bestscore.setText("Best Score : " + Integer.toString(score));
                }
                else {
                    bestscore.setText("Best Score : " + Integer.toString(returned_score));
                }
            }

            score = 0;
            initialize();
        }

    }



    public void painttiles() {
        random = new Random();
        index[3] = index[2]+5;
        index[2] = index[1]+5;
        index[1] = index[0]+5;
        index[0] = random.nextInt(5) +1;


        for(int j =0;j<4;++j) {
            String s4 = "b" + Integer.toString(index[j]);
            int resID = getResources().getIdentifier(s4, "id", getPackageName());
            button = ((Button) findViewById(resID));
            button.setBackgroundColor(Color.rgb(0, 0, 0));
        }
    }

    public void initialize() {

        score = 0;
        for(int j =1;j<=25;++j) {
            String s4 = "b" + Integer.toString(j);
            int resID = getResources().getIdentifier(s4, "id", getPackageName());
            button = ((Button) findViewById(resID));
            button.setBackgroundResource(android.R.drawable.btn_default);
        }

        random = new Random();


        index[0] = random.nextInt(5) +1;
        index[1] = random.nextInt(5) +6;
        index[2] = random.nextInt(5) +11;
        index[3] = random.nextInt(  5) +16;

        for(int j =0;j<4;++j) {
            String s4 = "b" + Integer.toString(index[j]);
            int resID = getResources().getIdentifier(s4, "id", getPackageName());
            button = ((Button) findViewById(resID));
            button.setBackgroundColor(Color.rgb(0, 0, 0));
        }
    }

    public static String read(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    @Override
    protected void onDestroy() {
        mp.stop();
        super.onDestroy();
    }
}