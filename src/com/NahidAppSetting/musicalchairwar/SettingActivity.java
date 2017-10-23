package com.NahidAppSetting.musicalchairwar;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class SettingActivity extends Activity {
	public static int pauseStart;
	public static int pauseEnd;
	public static int pauseTime;
	private Button btnSelectDirectory;
	private TextView txtDirectoryPath;
	private EditText eTxtStart;
	private EditText eTxtEnd;
	private EditText ePausTime;
	private ImageButton btnPausePlus;
	private ImageButton btnPauseMinus;	
	private ImageButton btnStartPlus;
	private ImageButton btnStartMinus;
	private ImageButton btnEndPlus;
	private ImageButton btnEndMinus;
	
	private SharedPreferences shrPrefer;
	private SharedPreferences.Editor editor;
	private static String PAUSE_START_TIME_KEY="PStartTime";
	private static String PAUSE_END_TIME_KEY="PEndTime";
	private static String PAUSE_TIME_KEY="PauseTime";
	

/*	public SettingActivity()
	{
		shrPrefer= getApplicationContext().getSharedPreferences("Value", 0);		
		pauseEnd= shrPrefer.getInt(PAUSE_END_TIME_KEY, 0);
		pauseStart= shrPrefer.getInt(PAUSE_START_TIME_KEY, 0);
		pauseTime= shrPrefer.getInt(PAUSE_TIME_KEY, 0);
	}*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		SongsManager sManage= new SongsManager();
		
		shrPrefer= getPreferences(MODE_PRIVATE);
		editor= getPreferences(MODE_PRIVATE).edit();
		
		txtDirectoryPath= (TextView)findViewById(R.id.txtDirectoryPath);
		btnSelectDirectory=(Button)findViewById(R.id.btnDirectory);
		eTxtEnd= (EditText)findViewById(R.id.etxtPauseEnd);
		eTxtStart= (EditText)findViewById(R.id.etxtPauseStart);
		ePausTime= (EditText)findViewById(R.id.etxtPauseTime);
		
		btnPausePlus=(ImageButton)findViewById(R.id.btn_st_pause_plus);
		btnPauseMinus=(ImageButton)findViewById(R.id.btn_stng_pause_minus);
		btnStartPlus=(ImageButton)findViewById(R.id.btn_st_start_plus);
		btnStartMinus=(ImageButton)findViewById(R.id.btn_st_start_minus);
		btnEndPlus=(ImageButton)findViewById(R.id.btn_st_end_plus);
		btnEndMinus=(ImageButton)findViewById(R.id.btn_st_end_minus);
		
		txtDirectoryPath.setText(sManage.GetMediaPath());
		pauseEnd= shrPrefer.getInt(PAUSE_END_TIME_KEY, 0);
		pauseStart= shrPrefer.getInt(PAUSE_START_TIME_KEY, 0);
		pauseTime= shrPrefer.getInt(PAUSE_TIME_KEY, 0);
		
		eTxtEnd.setText(String.valueOf(pauseEnd));
		eTxtStart.setText(String.valueOf(pauseStart));
		ePausTime.setText(String.valueOf(pauseTime));

		
		btnSelectDirectory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(getApplicationContext(), DirectoryBrowser.class);
				startActivityForResult(i, 100);		
			}
		});
		
		eTxtEnd.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {						
						pauseEnd=Integer.parseInt(eTxtEnd.getText().toString());
						editor.putInt(PAUSE_END_TIME_KEY,pauseEnd );
						editor.commit();
						return true;
			 
					}
				return false;
			}
		});
		
		ePausTime.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
						pauseTime=Integer.parseInt(ePausTime.getText().toString());
						editor.putInt(PAUSE_TIME_KEY,pauseTime );
						editor.commit();
						return true;
			 
					}
				return false;
			}
		});
		
		eTxtStart.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
						pauseStart=Integer.parseInt(eTxtStart.getText().toString());
						editor.putInt(PAUSE_START_TIME_KEY,pauseStart );
						editor.commit();
						return true;
			 
					}
				return false;
			}
		});
		
		btnPausePlus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pauseTime < 100)
				{
					pauseTime=pauseTime+1;
					ePausTime.setText(String.valueOf(pauseTime));
					editor.putInt(PAUSE_TIME_KEY,pauseTime );
					editor.commit();
				}
				
			}
		});
		
		btnPauseMinus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pauseTime > 0)
				{
					pauseTime=pauseTime - 1;
					ePausTime.setText(String.valueOf(pauseTime));
					editor.putInt(PAUSE_TIME_KEY,pauseTime );
					editor.commit();
				}
				
			}
		});
		
		
		btnStartPlus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pauseStart < 100)
				{
					pauseStart=pauseStart + 1;
					eTxtStart.setText(String.valueOf(pauseStart));
					editor.putInt(PAUSE_START_TIME_KEY,pauseStart );
					editor.commit();	
				}
			}
		});
		
		btnStartMinus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pauseStart > 0)
				{
					pauseStart=pauseStart - 1;
					eTxtStart.setText(String.valueOf(pauseStart));
					editor.putInt(PAUSE_START_TIME_KEY,pauseStart );
					editor.commit();	
				}
			}
		});
		
		btnEndPlus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pauseEnd < 100)
				{
					pauseEnd=pauseEnd + 1;
					eTxtEnd.setText(String.valueOf(pauseEnd));
					editor.putInt(PAUSE_END_TIME_KEY,pauseEnd );
					editor.commit();
				}
			}
		});
		
		
		btnEndMinus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pauseEnd > 0)
				{
					pauseEnd=pauseEnd - 1;
					eTxtEnd.setText(String.valueOf(pauseEnd));
					editor.putInt(PAUSE_END_TIME_KEY,pauseEnd );
					editor.commit();		
				}
			}
		});


	}	 
	@Override
	public void onResume()
	{  // After a pause OR at startup
	    super.onResume();
	    //Refresh your stuff here
		SongsManager sManage= new SongsManager();
		txtDirectoryPath.setText(sManage.GetMediaPath());
     }
}
