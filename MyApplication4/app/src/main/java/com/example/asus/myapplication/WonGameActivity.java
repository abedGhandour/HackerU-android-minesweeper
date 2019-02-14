package com.example.asus.myapplication;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WonGameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won_game);

        final LinearLayout lm =findViewById(R.id.verticalLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            TextView product = new TextView(this);
        @SuppressLint("DefaultLocale") String tempText = String.format("It Took You: %d minutes and %d seconds", GameActivity.timeElapsed / 60, GameActivity.timeElapsed % 60);
        product.setText(tempText);
            ll.addView(product);
            lm.addView(ll);
    }
}
