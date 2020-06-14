package com.nizhal.i_link;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nizal.ripple.RippleBackground;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import at.markushi.ui.CircleButton;

public class MainPage extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private double b_longitude;
    private double b_latitude;

    Marker marker1;

    TextView crowd_level, approx_time, cost_t, speed_bus;

    ArrayList<Marker> markers;
    ArrayList<String> crowd, time, cost, speed;

    SupportMapFragment mapFragment;

    CircleButton bookTicket;

    TextView toggleMap, main_source, main_dest;
    LinearLayout lin_map, lin_stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        toggleMap = findViewById(R.id.toggleMap);
        lin_map = findViewById(R.id.lin_map);
        lin_stat = findViewById(R.id.lin_stat);

        crowd_level = findViewById(R.id.crowd_level);
        approx_time = findViewById(R.id.aprox_time);
        cost_t = findViewById(R.id.cost_t);
        speed_bus = findViewById(R.id.speed_bus);

        final RippleBackground rippleBackground = findViewById(R.id.ripple_stop);
        rippleBackground.startRippleAnimation();
        toggleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleMap.getText().equals("Hide Map")) {
                    toggleMap.setText("Show Map");
                    lin_map.setVisibility(View.INVISIBLE);
                    lin_stat.setVisibility(View.VISIBLE);
                } else if (toggleMap.getText().equals("Show Map")) {
                    toggleMap.setText("Hide Map");
                    lin_map.setVisibility(View.VISIBLE);
                    lin_stat.setVisibility(View.INVISIBLE);
                }
            }
        });

        markers = new ArrayList<>();
        crowd = new ArrayList<>();
        time = new ArrayList<>();
        cost =  new ArrayList<>();
        speed = new ArrayList<>();

        main_source = findViewById(R.id.main_source);
        main_dest = findViewById(R.id.main_dest);

        Intent intent = getIntent();
        main_source.setText(intent.getExtras().getString("source"));
        main_dest.setText(intent.getExtras().getString("dest"));

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bookTicket = findViewById(R.id.bookTicket);
        bookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getBaseContext(), BusBooking.class);
                in.putExtra("source", main_source.getText());
                in.putExtra("dest", main_dest.getText());
                startActivity(in);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getBaseContext(), R.raw.styling));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        b_latitude = 12.955430;
        b_longitude = 80.142630;

        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(b_latitude,b_longitude))      // Sets the center of the map to Mountain View
                .zoom(13)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();
        marker1 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(b_latitude,b_longitude))
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_destn_pin))
                .title("Bus Live Location"));


        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                return true;
            }
        });

        addMarkers();

        marker1.remove();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String tag = marker.getTag().toString();
                int id = Integer.parseInt(tag);


                if(crowd.get(id).equalsIgnoreCase("Low"))
                {
                    crowd_level.setTextColor(getResources().getColor(R.color.green));
                }
                else if(crowd.get(id).equalsIgnoreCase("Moderate"))
                {
                    crowd_level.setTextColor(getResources().getColor(R.color.yellow));
                }
                else
                {
                    crowd_level.setTextColor(getResources().getColor(R.color.redishPink));
                }
                crowd_level.setText(crowd.get(id));

                approx_time.setText(time.get(id));
                cost_t.setText("₹ "+cost.get(id));
                speed_bus.setText(speed.get(id));

                return false;
            }
        });
    }

    public void addMarkers()
    {
        InputStream is = null;
        String line = null;
        String result = null;
        StringBuilder sb;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://ilinkhack.herokuapp.com/busloc");
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
            httpPost.setParams(httpParams);
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

        JSONObject jsonObject;
        try {
            JSONArray jArray = new JSONArray(result);
            String sp = "";
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);

//                t_latitude = Double.parseDouble(json_data.getString("lat"));
//                t_longitude = Double.parseDouble(json_data.getString("long"));
                Marker tempMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(json_data.getString("lat")),Double.parseDouble(json_data.getString("long"))))
                        .draggable(false)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_destn_pin))
                        .title("Bus Live Location"));
                tempMarker.setTag(i);

                crowd.add(json_data.getString("level"));
                speed.add("21");//(json_data.getString("speed")
                cost.add("45");//json_data.getString("cost")
                time.add(json_data.getString("time"));

                markers.add(tempMarker);

                //System.out.println(json_data);
            }

        } catch (JSONException e) {
            Toast.makeText(getBaseContext(),"Server Timed out",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
