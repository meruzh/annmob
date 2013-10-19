package com.ImageViewSvg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Share extends Activity {
    /** Called when the activity is first created. */
/*    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);
        final TextView textViewToChange = (TextView) findViewById(R.id.address_View);
        textViewToChange.setText("SOME TEXT");
        Button closeButton = (Button)this.findViewById(R.id.button_share);
        closeButton.setOnClickListener(new OnClickListener() {

        	public void onClick(View view) {
        		// Perform action on click
            //	Intent intent = new Intent(Intent.ACTION_SEND);
            //	intent.setType("text/plain");
            //	intent.putExtra(Intent.EXTRA_TEXT, "http://www.annoisy.org/");
            //	startActivity(Intent.createChooser(intent, "Share with"));
        		
        	finish();
          }
        }); 

    }
    
}  */

	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.share);
	        final TextView textViewToChange = (TextView) findViewById(R.id.address_View);
	        textViewToChange.setText("Annoisy is short for “annoying noises”. " +
	        		"Now you are not alone against any noises that we can fight together." +
	        		" Go “next” to create point on map and start recording…");
	        Button closeButton = (Button)this.findViewById(R.id.Btn);
	        closeButton.setOnClickListener(new OnClickListener() {

	        	public void onClick(View view) {
	        		//Intent myIntent = new Intent(view.getContext(), Share.class);
	                //startActivityForResult(myIntent, 0);
	        		
	        	finish();
	          }
	        }); 
	    }
}