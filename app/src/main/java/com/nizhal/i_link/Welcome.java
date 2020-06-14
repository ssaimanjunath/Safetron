package com.nizhal.i_link;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import at.markushi.ui.CircleButton;

public class Welcome extends AppCompatActivity {
    public static TextView source, destn;
    public static SourceSheet sourceBottomSheetFragment;
    public static DestSheet destBottomSheetFragment;
    CircleButton search;
    CardView cardViewWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*ActionBar actionBar = getSupportActionBar();
        TextView tv = new TextView(getApplicationContext());
        Typeface typeface = ResourcesCompat.getFont(this, R.font.merlo_regular);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView

        tv.setLayoutParams(lp);
        tv.setText("iLink Hacks"); // ActionBar title text
        tv.setTextSize(22);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTypeface(typeface);
        actionBar.setCustomView(tv);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);*/

        setContentView(R.layout.activity_welcome);

//        SharedPreferences.Editor editor = getSharedPreferences("bus_pref", MODE_PRIVATE).edit();
//        editor.putInt("book_tkt", 0);
//        editor.apply();

        source = findViewById(R.id.source);
        destn = findViewById(R.id.destn);
        cardViewWelcome = findViewById(R.id.cardViewWelcome);

        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSourceBottomSheetDialogFragment();
            }
        });

        destn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDestdBottomSheetDialogFragment();
            }
        });

        search = findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(source.getText().equals("") || destn.getText().equals(""))
                {
                    FancyToast.makeText(getBaseContext(),"Source and Destination are Mandatory",FancyToast.LENGTH_SHORT,FancyToast.WARNING, false).show();
                }
                else
                {
                    Intent i = new Intent(getBaseContext(), MainPage.class);
                    i.putExtra("source", source.getText());
                    i.putExtra("dest", destn.getText());
                    startActivity(i);
                }
            }
        });

        SharedPreferences prefs = getSharedPreferences("bus_pref", MODE_PRIVATE);
        int book_id = prefs.getInt("book_tkt", 0); //0 is the default value.

        if(book_id!=0)
        {
            cardViewWelcome.setVisibility(View.VISIBLE);
        }
        else
        {
            cardViewWelcome.setVisibility(View.INVISIBLE);
        }

        cardViewWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), BookingConfirmation.class);
                i.putExtra("source", source.getText());
                i.putExtra("dest", destn.getText());
                startActivity(i);
            }
        });

    }

    public void showSourceBottomSheetDialogFragment() {
        sourceBottomSheetFragment = new SourceSheet();
        sourceBottomSheetFragment.show(getSupportFragmentManager(), sourceBottomSheetFragment.getTag());
    }

    public void showDestdBottomSheetDialogFragment() {
        destBottomSheetFragment = new DestSheet();
        destBottomSheetFragment.show(getSupportFragmentManager(), destBottomSheetFragment.getTag());
    }
}
