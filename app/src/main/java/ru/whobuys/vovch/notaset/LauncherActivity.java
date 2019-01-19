package ru.whobuys.vovch.notaset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_actitvity);

        Button  oneFourButton = (Button) findViewById(R.id.no_network);
        oneFourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LauncherActivity.this, OneFourActivity.class);
                LauncherActivity.this.startActivity(intent);
            }
        });

        Button multiplayerButton = (Button) findViewById(R.id.network);
        multiplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LauncherActivity.this, MultiplayerActivity.class);
                LauncherActivity.this.startActivity(intent);
            }
        });
    }
}
