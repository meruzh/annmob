package com.ImageViewSvg;


import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class Record extends Activity
{
	Double lat, lng;
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private DefaultHttpClient mHttpClient;
    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;
    private TextView tv = null;
    private String mResponsestr = null;

    private MediaPlayer   mPlayer = null;

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }



    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public void onResponse() {
        setContentView(R.layout.record);

		JSONObject obj;
		try {
			
            obj = new JSONObject(mResponsestr);
            obj = obj.getJSONObject("noise");
            
            SeekBar mSeekBarAir = (SeekBar)findViewById(R.id.SeekBar05);
            TextView t = (TextView)findViewById(R.id.EditText05);
            t.setText("Neighbourhood  ("+obj.getString("aircraft")+"db)");
            mSeekBarAir.setProgress(150 + Integer.parseInt(obj.getString("aircraft")));
                     
            SeekBar mSeekBarCons = (SeekBar)findViewById(R.id.SeekBar02);
            TextView t1 = (TextView)findViewById(R.id.EditText02);
            t1.setText("Construction  ("+obj.getString("construction")+"db)");
            mSeekBarCons.setProgress(150 + Integer.parseInt(obj.getString("construction")));
            
            SeekBar mSeekBarMusic = (SeekBar)findViewById(R.id.SeekBar03);
            TextView t2 = (TextView)findViewById(R.id.EditText01);
            t2.setText("Loud Music  ("+obj.getString("music")+"db)");
            mSeekBarMusic.setProgress(150 + Integer.parseInt(obj.getString("music")));
            
            SeekBar mSeekBarNeghb = (SeekBar)findViewById(R.id.SeekBar04);
            TextView t3 = (TextView)findViewById(R.id.EditText04);
            t3.setText("Airplanes  ("+obj.getString("neighbourhood")+"db)");
            mSeekBarNeghb.setProgress(150 + Integer.parseInt(obj.getString("neighbourhood")));
            
            SeekBar mSeekBar = (SeekBar)findViewById(R.id.SeekBar01);            
            TextView t4 = (TextView)findViewById(R.id.EditText03);
            t4.setText("Traffic  ("+obj.getString("traffic")+"db)");
            mSeekBar.setProgress(150 + Integer.parseInt(obj.getString("traffic")));
            
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Button closeButton1 = (Button) findViewById(R.id.button);
        //Intent myIntent = new Intent(v.getContext(), Share.class);
        //startActivityForResult(myIntent, 0);
        closeButton1.setOnClickListener(new OnClickListener() {

        	public void onClick(View view) {
        		//Intent myIntent = new Intent(view.getContext(), Share.class);
                //startActivityForResult(myIntent, 0);
        		Intent intent = new Intent(Intent.ACTION_SEND);
              	intent.setType("text/plain");
               	intent.putExtra(Intent.EXTRA_TEXT, "http://www.annoisy.org/256");
               	startActivity(Intent.createChooser(intent, "Share with"));
        	//finish();
          }
        }); 

    	    
    }

    
    public void postData() {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        mHttpClient = new DefaultHttpClient(params);
        try {

        	Bundle b = getIntent().getExtras();
        	lat = b.getDouble("lat", 0);
        	lng = b.getDouble("lng", 0);
        	HttpPost httppost = new HttpPost("http://annoisy.org/services/upload.php?type=noise&lng="+lng.toString()+"&lat="+lat.toString()+"&zoom=16");
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);  
            File amr = new File(mFileName);
            multipartEntity.addPart("upload_field", new FileBody(amr));
            httppost.setEntity(multipartEntity);
            
            mHttpClient.execute(httppost, new NoiseUploadResponseHandler());

        } catch (Exception e) {
        	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("MyException Occured");
            dialog.setMessage(e.toString());
            dialog.setNeutralButton("Cool", null);
            dialog.create().show();
       }
    
     } 
    
    private class NoiseUploadResponseHandler implements ResponseHandler {

        public Object handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {

            HttpEntity r_entity = response.getEntity();
            mResponsestr = EntityUtils.toString(r_entity);
            onResponse();

            return null;
        }

    }
    
    class RecordButton extends Button {
        boolean mStartRecording = true;
        
        public boolean onTouchEvent(android.view.MotionEvent event) {
        	if (!mStartRecording)
        	{
                tv.setText("Uploading please wait.");
        	}
        	return super.onTouchEvent(event);
        };
        
        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
            		Context mContext = getApplicationContext();
                    Resources res = mContext.getResources();
                    Drawable recorderBut = res.getDrawable(R.drawable.recorder_pushed);
                    mRecordButton.setBackgroundDrawable(recorderBut);
                    tv.setText("Recording. Press button to stop.");

                } else {
            		Context mContext = getApplicationContext();
                    Resources res = mContext.getResources();
                    Drawable recorderBut = res.getDrawable(R.drawable.recorder);
                    mRecordButton.setBackgroundDrawable(recorderBut);
                    postData();
                    //mSeekBar.setProgress(150);

                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setOnClickListener(clicker);
        }
    }


    public Record() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.amr";
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        RelativeLayout ll = new RelativeLayout(this);
        ll.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        ll.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        
        LinearLayout llay = new LinearLayout(this); 
        ll.addView(llay, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mRecordButton = new RecordButton(this);
		Context mContext = getApplicationContext();
        Resources res = mContext.getResources();
        Drawable recorderBut = res.getDrawable(R.drawable.recorder);
        mRecordButton.setBackgroundDrawable(recorderBut);
        llay.addView(mRecordButton,  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        
        tv = new TextView(this);        
        tv.setText("Start Recording");
        tv.setTextSize(18);
        tv.setTextColor(Color.GREEN);
        llay.addView(tv,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        setContentView(ll);
          
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}