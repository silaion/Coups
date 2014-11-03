package com.example.coups;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends FragmentActivity {

    private GoogleMap gmap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);

        LatLng startingPoint = new LatLng(37.0116353, 127.2642351);
        LatLng mark = new LatLng(37.011352, 127.26297);

        gmap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16));
//        gmap.addMarker(new MarkerOptions().position(mark));
	}
}
