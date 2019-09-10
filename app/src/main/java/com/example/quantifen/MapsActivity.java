package com.example.quantifen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int LOCATION_REQUEST_CODE = 101;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;
    private Location currentLocation;

    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 20000; /* 20 sec */

    private LocationManager locationManager;
    private LatLng myLocation;
    private boolean isPermission;
    private GoogleApiClient mGoogleApiClient;

    private Button  injectionSitesButton;
    private Button  rehabilitationCentersButton;
    private Button  naloxoneSitesButton;

    ArrayList<LatLng> injectionSites = new ArrayList<>();
    ArrayList<LatLng> rehabilitationCenters = new ArrayList<>();
    ArrayList<LatLng> naloxoneSites = new ArrayList<>();

    ArrayList<String> injectionSitesName = new ArrayList<>();
    ArrayList<String> rehabilitationCentersName = new ArrayList<>();
    ArrayList<String> naloxoneSitesName = new ArrayList<>();

    LatLng quantifenhq = new LatLng(45.457004, -73.640360);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (requestSinglePermission()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            //it was pre written
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            checkLocation(); //check whether location service is enable or not in your  phone
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        //mMap.setOnMyLocationClickListener(onMyLocationClickListener);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.setMinZoomPreference(11);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        addSites();

        injectionSitesButton = findViewById(R.id.injection_sites_button);
        rehabilitationCentersButton = findViewById(R.id.rehabilitation_centers_button);
        naloxoneSitesButton = findViewById(R.id.nalozone_sites_button);

        injectionSitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(injectionSitesButton.getText().equals("Show\nInjection Sites")){
                    addMarkerToSites(mMap, injectionSites, injectionSitesName, 1);
                    injectionSitesButton.setText("Hide\nInjection Sites");
                }else if(injectionSitesButton.getText().equals("Hide\nInjection Sites")){
                    mMap.clear();
                    injectionSitesButton.setText("Show\nInjection Sites");
                    mMap.addMarker(new MarkerOptions().position(quantifenhq).title("Quantifen HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    if(rehabilitationCentersButton.getText().equals("Hide\nRehabilitation Centers")){
                        addMarkerToSites(mMap, rehabilitationCenters, rehabilitationCentersName, 2);
                    }
                    if(naloxoneSitesButton.getText().equals("Hide\nNaloxone Sites")){
                        addMarkerToSites(mMap, naloxoneSites, naloxoneSitesName, 3);
                    }
                }
            }
        });


        rehabilitationCentersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rehabilitationCentersButton.getText().equals("Show\nRehabilitation Centers")){
                    addMarkerToSites(mMap, rehabilitationCenters, rehabilitationCentersName, 2);
                    rehabilitationCentersButton.setText("Hide\nRehabilitation Centers");
                }else if(rehabilitationCentersButton.getText().equals("Hide\nRehabilitation Centers")){
                    mMap.clear();
                    rehabilitationCentersButton.setText("Show\nRehabilitation Centers");
                    mMap.addMarker(new MarkerOptions().position(quantifenhq).title("Quantifen HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    if(injectionSitesButton.getText().equals("Hide\nInjection Sites")){
                        addMarkerToSites(mMap, injectionSites, injectionSitesName, 1);
                    }
                    if(naloxoneSitesButton.getText().equals("Hide\nNaloxone Sites")){
                        addMarkerToSites(mMap, naloxoneSites, naloxoneSitesName, 3);
                    }
                }

            }
        });

        naloxoneSitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(naloxoneSitesButton.getText().equals("Show\nNaloxone Sites")){
                    addMarkerToSites(mMap, naloxoneSites, naloxoneSitesName, 3);
                    naloxoneSitesButton.setText("Hide\nNaloxone Sites");
                }else if(naloxoneSitesButton.getText().equals("Hide\nNaloxone Sites")){
                    mMap.clear();
                    naloxoneSitesButton.setText("Show\nNaloxone Sites");
                    mMap.addMarker(new MarkerOptions().position(quantifenhq).title("Quantifen HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    if(injectionSitesButton.getText().equals("Hide\nInjection Sites")){
                        addMarkerToSites(mMap, injectionSites, injectionSitesName, 1);
                    }
                    if(rehabilitationCentersButton.getText().equals("Hide\nRehabilitation Centers")){
                        addMarkerToSites(mMap, rehabilitationCenters, rehabilitationCentersName, 2);
                    }
                }

            }
        });


        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(quantifenhq).title("Quantifen HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(quantifenhq));

        if(myLocation != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (currentLocation == null) {
            startLocationUpdates();
        }
        if (currentLocation != null) {

        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location){
        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean requestSinglePermission() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        //Single Permission is granted
                        Toast.makeText(MapsActivity.this, "Single permission is granted!", Toast.LENGTH_SHORT).show();
                        isPermission = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            isPermission = false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        return isPermission;

    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(15);
                    return false;
                }
            };

    /*
    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {
                    myLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.setMinZoomPreference(12);

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(myLocation);

                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.CYAN);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };
*/

    public void addSites(){
        //Add injection sites
        injectionSites.add(new LatLng(45.521830, -73.561813)); //Spectre de Rue Inc
        injectionSites.add(new LatLng(45.510479, -73.562403)); //Cactus Montreal
        injectionSitesName.add("Spectre de Rue Inc");
        injectionSitesName.add("Cactus Montreal");

        rehabilitationCenters.add(new LatLng(45.666410, -73.494427)); //Centre de réadaptation en dépendance de Montréal
        rehabilitationCenters.add(new LatLng(45.512281, -73.573758)); //Centre de réadaptation en dépendance de Montréal
        rehabilitationCenters.add(new LatLng(45.553975, -73.648598)); //Centre de réadaptation en dépendance de Montréal
        rehabilitationCenters.add(new LatLng(45.465675, -73.627886)); //Head & Hands
        rehabilitationCenters.add(new LatLng(45.503055, -73.569286)); //PsyMontréal - Psychologist
        rehabilitationCenters.add(new LatLng(45.510478, -73.562404)); //Cactus Montréal
        rehabilitationCenters.add(new LatLng(45.486077, -73.588120)); //Chubikphysio
        rehabilitationCenters.add(new LatLng(45.521830, -73.561814)); //Spectre de Rue Inc
        rehabilitationCenters.add(new LatLng(45.499825, -73.573489)); //

        rehabilitationCentersName.add("Centre de réadaptation en dépendance de Montréal");
        rehabilitationCentersName.add("Centre de réadaptation en dépendance de Montréal");
        rehabilitationCentersName.add("Centre de réadaptation en dépendance de Montréal");
        rehabilitationCentersName.add("Head & Hands");
        rehabilitationCentersName.add("PsyMontréal - Psychologist");
        rehabilitationCentersName.add("Cactus Montréal");
        rehabilitationCentersName.add("Chubikphysio");
        rehabilitationCentersName.add("Spectre de Rue Inc");
        rehabilitationCentersName.add(" ");

        naloxoneSites.add(new LatLng(45.520575, -73.626332)); //1365 Beaumont Ave

        naloxoneSitesName.add("1365 Beaumont Ave");

    }

    public void addMarkerToSites(GoogleMap mMap, ArrayList<LatLng> locations, ArrayList<String> locationName, int code){

        if (code == 1) {
            for (int i = 0; i < locations.size(); i++) {
                mMap.addMarker(new MarkerOptions().position(locations.get(i)).title(locationName.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
        }else if (code == 2){
            for (int i = 0; i < locations.size(); i++) {
                mMap.addMarker(new MarkerOptions().position(locations.get(i)).title(locationName.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
        }else if(code == 3){
            for (int i = 0; i < locations.size(); i++) {
                mMap.addMarker(new MarkerOptions().position(locations.get(i)).title(locationName.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
    }
}
