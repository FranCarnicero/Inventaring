package com.example.inventaring;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button IntentCreate = (Button)findViewById(R.id.CreateQRButton);
        IntentCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CreateQR.class);

                startActivity(i);
            }
        });
        Button IntentScan = (Button)findViewById(R.id.ScanQRButton);

        IntentScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ScanQR.class);
                startActivity(i);
            }
        });

        Button SHOWDATABASE = (Button) findViewById(R.id.SHOWDATABASE);

        SHOWDATABASE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, ShowData.class);
                startActivity(i);


            }
        });





    }

}