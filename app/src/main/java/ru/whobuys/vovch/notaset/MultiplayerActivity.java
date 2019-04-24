package ru.whobuys.vovch.notaset;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class MultiplayerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MultiplayerActivity.this,
                    new String[]{Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE}, 0);
        } else {
            // Permission has already been granted
        }

        setContentView(R.layout.activity_multiplayer);
        TextView createRoomTextView = (TextView) findViewById(R.id.create_room_textview);
        createRoomTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultiplayerActivity.this, RoomActivity.class);
                intent.putExtra("isHost", true);
                startActivity(intent);
            }
        });
        TextView connectToRoomTextView = (TextView) findViewById(R.id.connect_to_room_textview);
        connectToRoomTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultiplayerActivity.this, RoomActivity.class);
                intent.putExtra("isHost", false);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch(requestCode){
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //FirebaseMessaging.getInstance().setAutoInitEnabled(true);
                    //provider.firstRegistrationToServer();
                } else {
                    //TODO block app
                }
                break;
            }
        }
    }
}
