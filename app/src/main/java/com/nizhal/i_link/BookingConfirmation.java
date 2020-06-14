package com.nizhal.i_link;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.markushi.ui.CircleButton;

public class BookingConfirmation extends AppCompatActivity {

    TextView fromTicketConf, toTicketConf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_confirmation);

        SharedPreferences.Editor editor = getSharedPreferences("bus_pref", MODE_PRIVATE).edit();
        editor.putInt("book_tkt", 1);
        editor.apply();

        ImageView goBackConf = findViewById(R.id.goBackConf);
        goBackConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainPage.class);
                i.putExtra("source", fromTicketConf.getText());
                i.putExtra("dest", toTicketConf.getText());
                startActivity(i);
            }
        });

        fromTicketConf = findViewById(R.id.fromTicketConf);
        toTicketConf = findViewById(R.id.toTicketConf);

        Intent intent = getIntent();
        fromTicketConf.setText(intent.getExtras().getString("source"));
        toTicketConf.setText(intent.getExtras().getString("dest"));

    }
}

