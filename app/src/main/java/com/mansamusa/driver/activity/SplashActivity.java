package com.mansamusa.driver.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mansamusa.driver.database.SPreferenceKey;
import com.mansamusa.driver.database.SharedPreferenceWriter;
import com.mansamusa.driver.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
    private final int SPLASH_DISPLAY_LENGTH = 500;
    MediaPlayer mediaPlayer;
    GoogleApiClient googleApiClient;
    List<String> listPermissionsNeeded;
    private static final int REQUEST_CHECK_SETTINGS_GPS = 27;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 255;
    Location mylocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.alak_pa);
//        mediaPlayer.start();
        getDeviceToken();
        setUpGClient();


    }


    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    // second
    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }
    }

    //fourth
    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(SplashActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, (LocationListener) this);
                    PendingResult result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    //Toast.makeText(SplashActivity.this, "permission allow", Toast.LENGTH_SHORT).show();
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(SplashActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);


                                            directionTimeClass();


                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        //  Toast.makeText(SplashActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                                        status.startResolutionForResult(SplashActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied. However, we have no way to fix the
                                    // settings so we won't show the dialog.
                                    //finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }



    private void directionTimeClass() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String userToken= SharedPreferenceWriter.getInstance(SplashActivity.this).getString(SPreferenceKey.TOKEN);
                if (userToken.equals("")){
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }else {
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //denied
                Log.e("denied", permission);
                showSettingLocation(this);

            } else {
                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                    //allowed
                    Log.e("allowed", permission);
                    getMyLocation();
                } else {
                    //set to never ask again
                    Log.e("set to never ask again", permission);
                    //startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                    Toast.makeText(SplashActivity.this, "Grant the permission to use this app.", Toast.LENGTH_SHORT).show();
                    // User selected the Never Ask Again Option
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 0);


                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                        break;
                }
                break;
        }
    }

    protected void onStop() {
        super.onStop();
//        if(mediaPlayer.isPlaying()){ //Must check if it's playing, otherwise it may be a NPE
//            mediaPlayer.pause(); //Pauses the sound
//        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    public void showSettingLocation(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("Turn on your GPS and give Just Clubbing access to your location.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
               /* Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(intent, PERMISSION_REQUEST_CODE);
                dialog.dismiss();*/


                checkPermissions();
                dialog.dismiss();

            }
        });


        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        if (!(activity).isFinishing()) {
            alert.show();
        }
    }




    private void getDeviceToken() {

        final Thread thread = new Thread() {
            @Override
            public void run() {
                Log.e(">>>>>>>>>>>>>>", "thred IS running");
                try {
//                    if (SharedPreferenceWriter.getInstance(SplashActivity.this).getString(SPreferenceKey.DEVICETOKEN).isEmpty()) {

                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                            if(!task.isSuccessful()){
                                return;
                            }
                            String token = Objects.requireNonNull(task.getResult()).getToken();
                            Log.e("Generated Device Token", "-->" + token);
                            Log.d("devicetoken:", token);
                            if (token == null) {
                                getDeviceToken();
                            } else {
                                if (token!=null && !token.isEmpty()) {
                                    SharedPreferenceWriter.getInstance(SplashActivity.this).writeStringValue(SPreferenceKey.DEVICETOKEN, token);
                                }
                            }
                        });

//                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                super.run();
            }
        };
        thread.start();
    }






}
