package com.mansamusa.driver.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mansamusa.driver.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    @BindView(R.id.backbtn)
    ImageView backbtn;
    @BindView(R.id.txt_add)
    TextView txt_add;

    private String lati="";
    private String longi="";
    private Marker marker;
    private String cityName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);

        if (getIntent().getStringExtra("lati")!=null){
            lati=getIntent().getStringExtra("lati");
        }else {
            lati="";
        }

        if (getIntent().getStringExtra("longi")!=null){
            longi=getIntent().getStringExtra("longi");
        }else {
            longi="";
        }



        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(Double.parseDouble(lati),Double.parseDouble(longi), 1);
            if (addresses.size() >0) {
                cityName = addresses.get(0).getAddressLine(0);
                txt_add.setText(cityName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.backbtn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.backbtn:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(Double.parseDouble(lati),Double.parseDouble(longi));
        marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.wfh_map)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lati),Double.parseDouble(longi)), 12.0f));

    }
}

