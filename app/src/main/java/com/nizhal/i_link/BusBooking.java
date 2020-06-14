package com.nizhal.i_link;

import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.markushi.ui.CircleButton;

public class BusBooking extends AppCompatActivity {

    public int selectCt = 0;
    List<String> bookedSeats;
    ArrayList<String> userSelection;
    String[] bookedSeatsStr = new String[]{"A1","B2","C1","C2","D3","B4","A5","B5","C6","A7","D7","C8","D8","A9","C9"};
    CircleButton confirmBook;
    TextView fromTicket, toTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_booking);
        userSelection = new ArrayList<>();
        bookedSeats = Arrays.asList(bookedSeatsStr);
        confirmBook = findViewById(R.id.confirmBook);

        fromTicket = findViewById(R.id.fromTicket);
        toTicket = findViewById(R.id.toTicket);

        Intent intent = getIntent();
        fromTicket.setText(intent.getExtras().getString("source"));
        toTicket.setText(intent.getExtras().getString("dest"));

        ImageView goBackBook = findViewById(R.id.goBackBook);
        goBackBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainPage.class);
                i.putExtra("source", fromTicket.getText());
                i.putExtra("dest", toTicket.getText());
                startActivity(i);
            }
        });

        confirmBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream is = null;
                String line = null;
                String result = null;
                StringBuilder sb;
                String message;
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                JSONObject object = new JSONObject();
                try {

                    object.put("name", "Sai");
                    object.put("source", "PVM");
                    object.put("dest", "TPM");
                    object.put("ticket_number", 2654);
                    object.put("bus_number", 91);
                    object.put("number_of_persons", 1);
                    object.put("amount", 45);

                } catch (Exception ex) {

                }

                try {
                    message = object.toString();
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://bus-buddy1.herokuapp.com/book");
                    HttpParams httpParams = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
                    httpPost.setParams(httpParams);
                    httpPost.setEntity(new StringEntity(message, "UTF8"));
                    httpPost.setHeader("Content-type", "application/json");
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity entity = httpResponse.getEntity();
                    is = entity.getContent();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(),"Server Timed out",Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                    is.close();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(),"Server Timed out",Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }
                Log.i("Res",result);
//                Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getBaseContext(), BookingConfirmation.class);
                        i.putExtra("source", fromTicket.getText());
                        i.putExtra("dest", toTicket.getText());
                        startActivity(i);
                    }
                }, 5000);

            }
        });

    }
    public void seatClicked(View view)
    {
        String[] arrOfStr = view.getResources().getResourceName(view.getId()).split("/", -2);
        ImageView iv = findViewById(view.getId());
        if(!bookedSeats.contains(arrOfStr[arrOfStr.length-1]) && !userSelection.contains(arrOfStr[arrOfStr.length-1]))
        {
            if(selectCt >= 3)
            {
                FancyToast.makeText(getBaseContext(),"A user can only book upto 3 seats.",FancyToast.LENGTH_SHORT,FancyToast.WARNING, false).show();
            }
            else
            {
                selectCt++;
                userSelection.add(arrOfStr[arrOfStr.length-1]);
                iv.setImageResource(R.drawable.your_seat_img);
            }
        }
        else if(!bookedSeats.contains(arrOfStr[arrOfStr.length-1]) && userSelection.contains(arrOfStr[arrOfStr.length-1]))
        {
            selectCt--;
            userSelection.remove(arrOfStr[arrOfStr.length-1]);
            iv.setImageResource(R.drawable.available_img);
        }
        else
        {
            FancyToast.makeText(getBaseContext(),"Seat Already Booked",FancyToast.LENGTH_SHORT,FancyToast.WARNING, false).show();
        }

        if(selectCt > 0)
        {
            confirmBook.setVisibility(View.VISIBLE);
        }
        else
        {
            confirmBook.setVisibility(View.INVISIBLE);
        }

    }

}

