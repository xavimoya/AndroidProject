package xavi.smartalarm;

/*
 * Created by Xavi on 22/3/17.
 */


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int RESULT_LOCATION = 101;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng position = new LatLng(0,0);
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mf = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mf.getMapAsync(this);

        editText = (EditText) findViewById(R.id.TFaddress);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void onSearch(View view) {
        EditText location_tf = (EditText) findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if (!location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);


            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MapsActivity.this, R.string.location_notfound,Toast.LENGTH_SHORT).show();
            }
            if (addressList != null && addressList.size()==1) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.marker)));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                position = latLng;

            }else{
                Toast.makeText(MapsActivity.this, R.string.location_notfound,Toast.LENGTH_SHORT).show();
            }


        }
    }

    public void onZoom(View view) {
        if (view.getId() == R.id.Bzoomin) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if (view.getId() == R.id.Bzoomout) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    public void changeType(View view) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title(getString(R.string.marker)));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

           // Toast.makeText(MapsActivity.this, R.string.error_location,Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
            return;
        }
        mMap.setMyLocationEnabled(true);


    }

    public void onSaveLocation(View view){
    Geocoder geocoder = new Geocoder(this);

        try {
            List<Address> addressList = geocoder.getFromLocation(position.latitude,position.longitude,1);
            String locatename = addressList.get(0).getLocality();
            String street = addressList.get(0).getAddressLine(0);
            Intent i = new Intent();
            i.putExtra(getString(R.string.location),locatename);
            i. putExtra(getString(R.string.street), street);
            i.putExtra(getString(R.string.latitude),position.latitude);
            i.putExtra(getString(R.string.longitude),position.longitude);
            setResult(RESULT_LOCATION,i);
            finish();
        } catch (Exception e){
            Toast.makeText(MapsActivity.this,R.string.problemsSaveLocation,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        setUpMap();
        final Geocoder geocoder = new Geocoder(this);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.marker)));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                position = latLng;
                List<Address> addressList;
                try {
                    addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
                    String location,street;
                    location = addressList.get(0).getLocality();
                    street = addressList.get(0).getAddressLine(0);
                    if(location==null)location="";
                    if(street==null)street="";
                    editText.setText(location + ", " +street);
                } catch (IOException e) {
                    Toast.makeText(MapsActivity.this, R.string.location_notfound,Toast.LENGTH_SHORT).show();
                    //  e.printStackTrace();
                }catch (IndexOutOfBoundsException ioobe){
                    Toast.makeText(MapsActivity.this, R.string.location_notfound2,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


