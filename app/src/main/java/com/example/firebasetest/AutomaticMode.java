package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AutomaticMode extends AppCompatActivity {

    private static final String CHANNEL_ID="My Channel";
    private static final int NOTIFICATION_ID=100;

    float temperatureValue;
    float lowTemp;
    float highTemp;


    TextView temp;
    TextView humidity;
    TextView ammonia;

    EditText tempLow;
    EditText tempHigh;


    FirebaseDatabase database=FirebaseDatabase.getInstance();

    DatabaseReference reference=database.getReference().child("test");
    DatabaseReference referenceTemp=database.getReference().child("Temperature");
    DatabaseReference referenceHumid=database.getReference().child("Humidity");
    DatabaseReference referenceLight=database.getReference().child("Light");
    DatabaseReference referenceHeater=database.getReference().child("Heater");
    DatabaseReference referenceFan=database.getReference().child("Fan");
    DatabaseReference referenceAmmonia=database.getReference().child("Ammonia");
    DatabaseReference referencePump=database.getReference().child("Pump");
    DatabaseReference referenceFood=database.getReference().child("Food");
    DatabaseReference referenceAutomatic=database.getReference().child("Automatic");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic_mode);

        temp = findViewById(R.id.temperature);

        humidity = findViewById(R.id.humidity);
        ammonia = findViewById(R.id.ammonia);
        tempLow=findViewById(R.id.temperatureLow);
        tempHigh=findViewById(R.id.temperatureHigh);








        //Fetching temperature value
        referenceTemp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.getValue().toString();
                    String val = data.substring(13, data.length() - 1);
                    float t = Float.parseFloat(val);
                    temperatureValue=t;
                    lowTemp=Float.parseFloat(tempLow.getText().toString().trim());
                    highTemp=Float.parseFloat(tempHigh.getText().toString().trim());
                    if(temperatureValue<=lowTemp)
                    {
                        referenceFan.child("fan").setValue(0);
                        referenceHeater.child("heater").setValue(1);
                    }
                    else if(temperatureValue>=highTemp)
                    {
                        referenceFan.child("fan").setValue(1);
                        referenceHeater.child("heater").setValue(0);
                    }
                    else{
                        referenceFan.child("fan").setValue(0);
                        referenceHeater.child("heater").setValue(0);
                    }
                    temp.setText("The temperature is: " + val);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Fetching humidity value
        referenceHumid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.getValue().toString();
                    String val = data.substring(10, data.length() - 1);
                    humidity.setText("The humidity percentage is: " + val + "%");
                    float h = Float.parseFloat(val);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Fetching ammonia gas sensor value
        referenceAmmonia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.getValue().toString();
                    String val = data.substring(9, data.length() - 1);
                    ammonia.setText("The AQI is: " + val);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    @Override
    public void onBackPressed() {
        referenceAutomatic.child("automatic").setValue(0);
        referenceFan.child("fan").setValue(0);
        referenceHeater.child("heater").setValue(0);
        this.onDestroy();
        super.onBackPressed();
    }


}