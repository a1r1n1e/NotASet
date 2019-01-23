package ru.whobuys.vovch.notaset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_actitvity);

        TextView  singleButton = (TextView) findViewById(R.id.singleplayer);
        singleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LauncherActivity.this, SingleActivity.class);
                LauncherActivity.this.startActivity(intent);
            }
        });

        TextView  oneFourButton = (TextView) findViewById(R.id.four_players);
        oneFourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LauncherActivity.this, OneFourActivity.class);
                LauncherActivity.this.startActivity(intent);
            }
        });

        TextView multiplayerButton = (TextView) findViewById(R.id.network);
        multiplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LauncherActivity.this, MultiplayerActivity.class);
                LauncherActivity.this.startActivity(intent);
            }
        });
    }
}
