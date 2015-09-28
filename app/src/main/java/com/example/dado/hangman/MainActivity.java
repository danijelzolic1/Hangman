package com.example.dado.hangman;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playBtn = (Button)findViewById(R.id.playBtn);
        playBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.playBtn) {
            Intent playIntent = new Intent(this, GameActivity.class);
            this.startActivity(playIntent);
        }
    }
}
