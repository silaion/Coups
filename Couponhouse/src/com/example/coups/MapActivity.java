package com.example.coups;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class MapActivity extends FragmentActivity implements LocationListener {

    GoogleMap gmap;
    Geocoder geocoder;
    Location myLocation;
    LocationManager locationManager;
    String provider;
    double latPoint, lngPoint;
    ArrayList<HashMap<String, Object>> addr;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapActivity.this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);

        if (provider == null) {  //위치정보 설정이 안되어 있으면 설정하는 엑티비티로 이동합니다
            new AlertDialog.Builder(MapActivity.this)
                    .setTitle("위치서비스 동의")
                    .setNeutralButton("이동", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                        }
                    }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            })
                    .show();
        } else {   //위치 정보 설정이 되어 있으면 현재위치를 받아옵니다
            locationManager.requestLocationUpdates(provider, 1, 1, MapActivity.this);
            setUpMapIfNeeded();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//위치설정 엑티비티 종료 후
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, true);
                if (provider == null) {//사용자가 위치설정동의 안했을때 종료
                    finish();
                } else {//사용자가 위치설정 동의 했을때
                    locationManager.requestLocationUpdates(provider, 1L, 2F, MapActivity.this);
                    setUpMapIfNeeded();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    private void setUpMapIfNeeded() {
        if (gmap == null) {
            gmap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps)).getMap();
            if (gmap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        gmap.setMyLocationEnabled(true);
        gmap.getMyLocation();

    }


    boolean locationTag = true;

    @Override
    public void onLocationChanged(Location location) {
        if (locationTag) {//한번만 위치를 가져오기 위해서 tag를 주었습니다
            Log.d("myLog", "onLocationChanged: !!" + "onLocationChanged!!");
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            Toast.makeText(MapActivity.this, "위도  : " + lat + " 경도: " + lng, Toast.LENGTH_SHORT).show();
            locationTag = false;
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}