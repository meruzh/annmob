package com.ImageViewSvg;

/**
 * 
 * @author Pavel.B.Chernov (based on ideas of kushnarev)
 *
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        final TextView textViewToChange = (TextView) findViewById(R.id.addressView);
        textViewToChange.setText("Annoisy is short for “annoying noises”. " +
        		"Now you are not alone against any noises that we can fight together." +
        		" Go “next” to create point on map and start recording…");
        Button closeButton = (Button)this.findViewById(R.id.topBtn);
        closeButton.setOnClickListener(new OnClickListener() {

        	public void onClick(View view) {
        		Intent myIntent = new Intent(view.getContext(), Maps.class);
                startActivityForResult(myIntent, 0);
        		
        	//finish();
          }
        }); 
    }

//	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
//		return false;
//	}
    
    /*
     * Walks through the objects tree
     * When meets ImageView - sets its width and height 
     */
 /*   private void SetChildsWidthHeight(ViewGroup view, int width, int height) {
    	int i;
    	for (i = 0; i < view.getChildCount(); ++i) {
    		View child = view.getChildAt(i);
    		if (child instanceof ViewGroup) {
    			SetChildsWidthHeight((ViewGroup)child, width, height);
    		}
    		else if (child instanceof ImageView) {
    			ImageView img = (ImageView) child;
    			img.setLayoutParams(new TableRow.LayoutParams(width, height));
    		}
    	}
    }

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
    		TableLayout table = (TableLayout) findViewById(R.id.TableView);
        	if (table == null) {
        		return;
            }

        	// Set uniform width and height for all ImageViews in table
        	SetChildsWidthHeight(table, table.getWidth() / 3, table.getHeight() / 2);
        }
	} */
}