package com.ImageViewSvg;


import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.ImageViewSvg.MyLocation;
import com.ImageViewSvg.MyLocation.LocationResult;


 
    

public class Maps extends MapActivity {
	private MapView mapView;
	MapController mc;
    GeoPoint p;
    double lat, lng;
    class MapOverlay extends com.google.android.maps.Overlay
    {
        @Override
        public boolean draw(Canvas canvas, MapView mapView, 
        boolean shadow, long when) 
        {
            super.draw(canvas, mapView, shadow);                   
 
            //---translate the GeoPoint to screen pixels---
            Point screenPts = new Point();
            mapView.getProjection().toPixels(p, screenPts);
 
            //---add the marker---
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ping);            
            canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
            return true;
        }

    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	//Intent intent = new Intent(Intent.ACTION_SEND);
            	//intent.setType("text/plain");
            	//intent.putExtra(Intent.EXTRA_TEXT, "http://www.google.fr/");
            	//startActivity(Intent.createChooser(intent, "Share with"));
            	//finish();
            	Intent myIntent = new Intent(v.getContext(), Record.class);
            	Bundle b = new Bundle();
            	b.putDouble("lat", lat);
            	b.putDouble("lng", lng);
            	myIntent.putExtras(b);
            	startActivityForResult(myIntent, 0);
            }
        });
		mapView = (MapView) findViewById(R.id.map_view);
		LocationResult locationResult = new LocationResult(){
	        @Override
	        public void gotLocation(final Location location){
	            lat = location.getLatitude();
	            lng = location.getLongitude();
	            mc = mapView.getController();
	            
	            p = new GeoPoint(
	                    (int) (lat * 1E6), 
	                    (int) (lng * 1E6));
	            
	            mc.animateTo(p);
	            mc.setZoom(17); 
	            MapOverlay mapOverlay = new MapOverlay();
	            List<Overlay> listOfOverlays = mapView.getOverlays();
	            listOfOverlays.clear();
	            listOfOverlays.add(mapOverlay);        
	            mapView.invalidate();
	            

	        }
	    };
	    MyLocation myLocation = new MyLocation();
	    myLocation.getLocation(this, locationResult);
		
		//mapView.setBuiltInZoomControls(true);
	    //MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, mapView);
	    //myLocationOverlay.enableMyLocation();
	    //mapView.getOverlays().add(myLocationOverlay);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}

