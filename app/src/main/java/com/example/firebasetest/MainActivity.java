package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID="My Channel";
    private static final int NOTIFICATION_ID=100;

    float temperatureValue;


    TextView temp;
    TextView humidity;
    TextView ammonia;

    Switch lights;
    Switch fans;
    Switch heaters;
    Switch pump;
    Switch food;

    Button automatic;
    Button buttonInfo;

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
        setContentView(R.layout.activity_main);

        temp = findViewById(R.id.temperature);
        lights = findViewById(R.id.switchLights);
        fans = findViewById(R.id.switchFans);
        heaters = findViewById(R.id.switchHeaters);
        pump = findViewById(R.id.switchPump);
        food = findViewById(R.id.switchFood);
        humidity = findViewById(R.id.humidity);
        ammonia = findViewById(R.id.ammonia);
        buttonInfo = findViewById(R.id.buttonInfo);
        automatic = findViewById(R.id.buttonAutomatic);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        //Fetching temperature value
        referenceTemp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.getValue().toString();
                    String val = data.substring(13, data.length() - 1);
                    float t = Float.parseFloat(val);
                    temperatureValue=t;
                    if (t > 27) {
                        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.fan, null);
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                        Bitmap largeIcon = bitmapDrawable.getBitmap();

                        Notification notificationTemp;
                        notificationTemp = new Notification.Builder(MainActivity.this)
                                .setLargeIcon(largeIcon)
                                .setSmallIcon(R.drawable.app_icon)
                                .setContentText("The temperature is above the appropriate range.")
                                .setSubText("Warning !!")
                                .setChannelId(CHANNEL_ID)
                                .build();

                        nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
                        nm.notify(NOTIFICATION_ID, notificationTemp);
                    } else if (t < 20) {
                        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.heater, null);
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                        Bitmap largeIcon = bitmapDrawable.getBitmap();

//                            NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//                            Notification notification;

                        Notification notificationTemp;
                        notificationTemp = new Notification.Builder(MainActivity.this)
                                .setLargeIcon(largeIcon)
                                .setSmallIcon(R.drawable.app_icon)
                                .setContentText("The temperature is below the appropriate range.Please switch ON the heaters.")
                                .setSubText("Warning !!")
                                .setChannelId(CHANNEL_ID)
                                .build();

                        nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
                        nm.notify(NOTIFICATION_ID, notificationTemp);
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
                    if (h > 75) {
                        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.fan, null);
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                        Bitmap largeIcon = bitmapDrawable.getBitmap();

                        Notification notificationHum;
                        notificationHum = new Notification.Builder(MainActivity.this)
                                .setLargeIcon(largeIcon)
                                .setSmallIcon(R.drawable.app_icon)
                                .setContentText("The humidity is above the appropriate range.Please provide ventilation.")
                                .setSubText("Warning !!")
                                .setChannelId(CHANNEL_ID)
                                .build();

                        nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
                        nm.notify(NOTIFICATION_ID, notificationHum);
                    }
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



        heaters.setTextColor(Color.parseColor("#000000"));
        lights.setTextColor(Color.parseColor("#000000"));
        fans.setTextColor(Color.parseColor("#000000"));
        pump.setTextColor(Color.parseColor("#000000"));
        food.setTextColor(Color.parseColor("#000000"));



                //Toggle the heaters
                heaters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (heaters.isChecked()) {
                            referenceHeater.child("heater").setValue(1);
                        } else {

                            referenceHeater.child("heater").setValue(0);
                        }


                    }
                });

                //Toggle the lights
                lights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (lights.isChecked()) {

                            referenceLight.child("light").setValue(1);
                        } else {
                            referenceLight.child("light").setValue(0);
                        }
                    }
                });

                //Toggle the fans

                fans.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (fans.isChecked()) {

                            referenceFan.child("fan").setValue(1);
                        } else {
                            referenceFan.child("fan").setValue(0);
                        }
                    }
                });

                //Toggle the pump
                pump.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (pump.isChecked()) {
                            referencePump.child("pump").setValue(1);
                            referencePump.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String data = snapshot.getValue().toString();
                                        int d = Integer.parseInt(data.substring(data.length() - 2, data.length() - 1));
                                        if (d == 0) {
                                            pump.setChecked(false);
                                        } } }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        } else {
                            referencePump.child("pump").setValue(0);
                        }
                    }
                });

//            food.setEnabled(false);
                food.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (food.isChecked()) {
                            referenceFood.child("food").setValue(1);
                            referenceFood.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String data = snapshot.getValue().toString();

                                        int d = Integer.parseInt(data.substring(data.length() - 2, data.length() - 1));
                                        if (d == 0) {
                                            food.setChecked(false);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            referenceFood.child("food").setValue(0);
                        }
                    }
                });




            try {
                buttonInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, OtherActivity.class);
                        startActivity(intent);
                    }
                });
            } catch (Exception e) {
                buttonInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, OtherActivity.class);
                        startActivity(intent);
                    }
                });
            }

            try {
                automatic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        referenceAutomatic.child("automatic").setValue(1);
                        Intent intent=new Intent(MainActivity.this,AutomaticMode.class);
                        startActivity(intent);
                    }
                });
            }
            catch(Exception e)
            {
                automatic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        referenceAutomatic.child("automatic").setValue(1);
                        Intent intent=new Intent(MainActivity.this,AutomaticMode.class);
                        startActivity(intent);
                    }
                });
            }


    }


}