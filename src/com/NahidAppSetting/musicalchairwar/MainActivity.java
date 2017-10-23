package com.NahidAppSetting.musicalchairwar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener {

	private ImageButton btnPlay;
	private ImageButton btnForward;
	private ImageButton btnBackward;
	private ImageButton btnNext;
	private ImageButton btnPrevious;
	private ImageButton btnPlaylist;
	private ImageButton btnSetting;
	private ImageButton btnRepeat;
	private ImageButton btnShuffle;
	private SeekBar songProgressBar;
	private TextView songTitleLabel;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	private ImageView backgroudImage;
	// Media Player
	private  MediaPlayer mp;
	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();;
	private SongsManager songManager;
	private Utilities utils;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private int currentSongIndex = 0; 
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	
	public static int pauseStartTime;
	public static int pauseEndTime;
	public static int pauseTime;
	public int intervalTime;
	
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
	
	
	
	public static boolean isAutoPause= false;
	
	TimerTask mTimerTask;
	final Handler handler = new Handler();
	Timer t = new Timer();	
	TextView hTextView;
	
	private int nCounter = 0;
	
	View dialogView;
	Dialog dialog;
	TextView countdownTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_test);	
		

		 
		// All player buttons
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnForward = (ImageButton) findViewById(R.id.btnForward);
		btnBackward = (ImageButton) findViewById(R.id.btnBackward);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
		btnSetting = (ImageButton) findViewById(R.id.btnSetting);
		btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
		btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		songTitleLabel = (TextView) findViewById(R.id.songTitle);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
		backgroudImage=(ImageView)findViewById(R.id.backImage);
		
		eTxtEnd= (EditText)findViewById(R.id.etxtPauseEnd);
		eTxtStart= (EditText)findViewById(R.id.etxtPauseStart);
		ePausTime= (EditText)findViewById(R.id.etxtPauseTime);
		
		btnPausePlus=(ImageButton)findViewById(R.id.btn_st_pause_plus);
		btnPauseMinus=(ImageButton)findViewById(R.id.btn_stng_pause_minus);
		btnStartPlus=(ImageButton)findViewById(R.id.btn_st_start_plus);
		btnStartMinus=(ImageButton)findViewById(R.id.btn_st_start_minus);
		btnEndPlus=(ImageButton)findViewById(R.id.btn_st_end_plus);
		btnEndMinus=(ImageButton)findViewById(R.id.btn_st_end_minus);
		


		pauseEndTime= SettingActivity.pauseEnd;
		pauseStartTime= SettingActivity.pauseStart;
		pauseTime= SettingActivity.pauseTime;
		
		eTxtEnd.setText(String.valueOf(pauseEndTime));
		eTxtStart.setText(String.valueOf(pauseStartTime));
		ePausTime.setText(String.valueOf(pauseTime));
		
		hTextView = (TextView)findViewById(R.id.textView1);
		SongsManager sManager= new SongsManager();
		String mediaPath= sManager.GetMediaPath();
		// Mediaplayer
		mp = new MediaPlayer();
		songManager = new SongsManager();
		utils = new Utilities();
		
		// Listeners
		songProgressBar.setOnSeekBarChangeListener(this); // Important
		mp.setOnCompletionListener(this); // Important

		// Getting all songs list
		songsList = songManager.getPlayList();
		


				
		/**
		 * Play button click event
		 * plays a song and changes button to pause image
		 * pauses a song and changes button to play image
		 * */
		
		btnPlay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				backgroudImage=(ImageView)findViewById(R.id.backImage);
				// check for already playing
				if(mp.isPlaying()){
					if(mp!=null){
						isAutoPause=false;
						mp.pause();
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
						backgroudImage.setImageResource(R.drawable.musicoff);

					}
				}else{
					// Resume song
					if(mp!=null){

						mp.start();
						// Changing button image to pause button
						btnPlay.setImageResource(R.drawable.btn_pause);
						backgroudImage.setImageResource(R.drawable.musicon);

					}
				}
				
			}
		});
		
		/**
		 * Forward button click event
		 * Forwards song specified seconds
		 * */
		btnForward.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// get current song position				
				int currentPosition = mp.getCurrentPosition();
				// check if seekForward time is lesser than song duration
				if(currentPosition + seekForwardTime <= mp.getDuration()){
					// forward song
					mp.seekTo(currentPosition + seekForwardTime);
				}else{
					// forward to end position
					mp.seekTo(mp.getDuration());
				}
			}
		});
		
		/**
		 * Backward button click event
		 * Backward song to specified seconds
		 * */
		btnBackward.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {			
				int currentPosition = mp.getCurrentPosition();
				// check if seekBackward time is greater than 0 sec
				if(currentPosition - seekBackwardTime >= 0){
					// backward song
					mp.seekTo(currentPosition - seekBackwardTime);
				}else{
					// backward to starting position
					mp.seekTo(0);
				}
				
			}
		});
		
		/**
		 * Next button click event
		 * Plays next song by taking currentSongIndex + 1
		 * */
		btnNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// check if next song is there or not
				if(currentSongIndex < (songsList.size() - 1)){
					playSong(currentSongIndex + 1);
					currentSongIndex = currentSongIndex + 1;
				}else{
					// play first song
					playSong(0);
					currentSongIndex = 0;
				}
				
			}
		});
		
		/**
		 * Back button click event
		 * Plays previous song by currentSongIndex - 1
		 * */
		btnPrevious.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(currentSongIndex > 0){
					playSong(currentSongIndex - 1);
					currentSongIndex = currentSongIndex - 1;
				}else{
					// play last song
					playSong(songsList.size() - 1);
					currentSongIndex = songsList.size() - 1;
				}
				
			}
		});
		
		/**
		 * Button Click event for Repeat button
		 * Enables repeat flag to true
		 * */
		btnRepeat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isRepeat){
					isRepeat = false;
					Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				}else{
					// make repeat to true
					isRepeat = true;
					Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
					// make shuffle to false
					isShuffle = false;
					btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
				}	
			}
		});
		
		/**
		 * Button Click event for Shuffle button
		 * Enables shuffle flag to true
		 * */
		btnShuffle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isShuffle){
					isShuffle = false;
					Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
				}else{
					// make repeat to true
					isShuffle= true;
					Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
					// make shuffle to false
					isRepeat = false;
					btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				}	
			}
		});
		
		/**
		 * Button Click event for Play list click event
		 * Launches list activity which displays list of songs
		 * */
		btnPlaylist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				Intent i = new Intent(getApplicationContext(), PlayListActivity.class);
				Intent i = new Intent(getApplicationContext(), ListFolder.class);
				startActivityForResult(i, 100);			
			}
		});
		
		/**
		 * Button Click event for Setting click event
		 * Launches list activity which displays Setting Panel
		 * */
		btnSetting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), SettingActivity.class);
				startActivityForResult(i, 100);			
			}
		});
		
		
		
		
		
		eTxtEnd.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {						
						pauseEndTime=Integer.parseInt(eTxtEnd.getText().toString());
						editor.putInt(PAUSE_END_TIME_KEY,pauseEndTime );
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
						pauseStartTime=Integer.parseInt(eTxtStart.getText().toString());
						editor.putInt(PAUSE_START_TIME_KEY,pauseStartTime );
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
				if(pauseStartTime < 100)
				{
					pauseStartTime=pauseStartTime + 1;
					eTxtStart.setText(String.valueOf(pauseStartTime));
					editor.putInt(PAUSE_START_TIME_KEY,pauseStartTime );
					editor.commit();	
				}
			}
		});
		
		btnStartMinus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pauseStartTime > 0)
				{
					pauseStartTime=pauseStartTime - 1;
					eTxtStart.setText(String.valueOf(pauseStartTime));
					editor.putInt(PAUSE_START_TIME_KEY,pauseStartTime );
					editor.commit();	
				}
			}
		});
		
		btnEndPlus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pauseEndTime < 100)
				{
					pauseEndTime=pauseEndTime + 1;
					eTxtEnd.setText(String.valueOf(pauseEndTime));
					editor.putInt(PAUSE_END_TIME_KEY,pauseEndTime );
					editor.commit();
				}
			}
		});
		
		
		btnEndMinus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pauseEndTime > 0)
				{
					pauseEndTime=pauseEndTime - 1;
					eTxtEnd.setText(String.valueOf(pauseEndTime));
					editor.putInt(PAUSE_END_TIME_KEY,pauseEndTime );
					editor.commit();		
				}
			}
		});

		
	}
	

	  
	/**
	 * Receiving song index from playlist view
	 * and play the song
	 * */
	@Override
    protected void onActivityResult(int requestCode,
                                     int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
         	 currentSongIndex = data.getExtras().getInt("songIndex");
         	 // play selected song
 			songsList = songManager.getPlayList();
             playSong(currentSongIndex);
        }
 
    }
	
	/**
	 * Function to play a song
	 * @param songIndex - index of song
	 * */
	public void  playSong(int songIndex){
		// Play song
		try {
			isAutoPause=false;
			   pauseStartTime= SettingActivity.pauseStart;
			   pauseEndTime= SettingActivity.pauseEnd;
			   pauseTime= SettingActivity.pauseTime;
			   Random randTime= new Random();
			   intervalTime= pauseStartTime + randTime.nextInt((pauseEndTime - pauseStartTime) + 1);
			   
        	mp.reset();
			mp.setDataSource(songsList.get(songIndex).get("songPath"));
			mp.prepare();
			mp.start();
			// Displaying Song title
			String songTitle = songsList.get(songIndex).get("songTitle");
        	songTitleLabel.setText(songTitle);
			
        	// Changing Button Image to pause image
			btnPlay.setImageResource(R.drawable.btn_pause);
			
			// set Progress bar values
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);
			
			// Updating progress bar
			updateProgressBar();			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);        
    }	
	
	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		   public void run() {
			   backgroudImage=(ImageView)findViewById(R.id.backImage);
			   long totalDuration = mp.getDuration();
			   long currentDuration = mp.getCurrentPosition();

			   
			   if((currentDuration / 1000) == intervalTime)
			   {
               
				   isAutoPause=true;
					mp.pause();
					btnPlay.setImageResource(R.drawable.btn_play);
					backgroudImage.setImageResource(R.drawable.musicoff);
					
					pauseStartTime= (int) (pauseStartTime + (currentDuration / 1000));
					pauseEndTime= (int) (pauseEndTime + (currentDuration / 1000));	
					Random randTime= new Random();
					intervalTime= pauseStartTime + randTime.nextInt((pauseEndTime - pauseStartTime) + 1);
					
				   songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
				   // Displaying time completed playing
				   songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
				   
				   // Updating progress bar
				   int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
				   //Log.d("Progress", ""+progress);
				   songProgressBar.setProgress(progress);	
				   Toast.makeText(getApplicationContext(), "Pause for "+ pauseTime + "Seconds.", Toast.LENGTH_SHORT).show();
					mHandler.postDelayed(this, pauseTime * 1000);
			   }
			   else
			   {	
				   if(!mp.isPlaying() && isAutoPause == true)
				   {
					   mp.start();
					   btnPlay.setImageResource(R.drawable.btn_pause);
					   backgroudImage.setImageResource(R.drawable.musicon);
				   }
				   
/*				   if((currentDuration / 1000) > pauseStartTime)
				   {
						pauseStartTime= (int) (pauseStartTime + (currentDuration / 1000));
						pauseEndTime= (int) (pauseEndTime + (currentDuration / 1000));	
						Random randTime= new Random();
						intervalTime= pauseStartTime + randTime.nextInt((pauseEndTime - pauseStartTime) + 1);
				   }
				   else if((currentDuration / 1000) > pauseStartTime)*/
				   // Displaying Total Duration time
				   songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
				   // Displaying time completed playing
				   songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
				   
				   // Updating progress bar
				   int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
				   //Log.d("Progress", ""+progress);
				   songProgressBar.setProgress(progress);
				   
				   // Running this thread after 100 milliseconds
			       mHandler.postDelayed(this, 1000);
			   }
		   }
		   
		   public void stop()
		   {
			  this.stop(); 
		   }

		};
		
	    public void doTimerTask(){
	    	 
	    	mTimerTask = new TimerTask() {
	    	        public void run() {
	    	                handler.post(new Runnable() {
	    	                        public void run() {
	    	                        	nCounter++;
	                                        // update TextView
	    	                        	hTextView.setText("Timer: " + nCounter);
	    	                        	mp.pause();
	    	                        	Log.d("TIMER", "TimerTask run");
	    	                        }
	    	               });
	    	        }};
	 
	            // public void schedule (TimerTask task, long delay, long period) 
	    	    t.schedule(mTimerTask, 5000, 10000);  // 
	 
	    	 }
	    
  	  public void stopTask(){
  		 
   	   if(mTimerTask!=null){

   	      Log.d("TIMER", "timer canceled");
   	      mTimerTask.cancel();
   	   }
   	 }
		
	/**
	 * 
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
		
	}

	/**
	 * When user starts moving the progress handler
	 * */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
    }
	
	/**
	 * When user stops moving the progress hanlder
	 * */
	@Override
    public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
		
		// forward or backward to certain seconds
		mp.seekTo(currentPosition);
		
		// update timer progress again
		updateProgressBar();
    }

	/**
	 * On Song Playing completed
	 * if repeat is ON play same song again
	 * if shuffle is ON play random song
	 * */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		
		// check for repeat is ON or OFF
		if(isRepeat){
			// repeat is on play same song again
			playSong(currentSongIndex);
		} else if(isShuffle){
			// shuffle is on - play a random song
			Random rand = new Random();
			currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
			playSong(currentSongIndex);
		} else{
			// no repeat or shuffle ON - play next song
			if(currentSongIndex < (songsList.size() - 1)){
				playSong(currentSongIndex + 1);
				currentSongIndex = currentSongIndex + 1;
			}else{
				// play first song
				playSong(0);
				currentSongIndex = 0;
			}
		}
	}
	
	@Override
	 public void onDestroy(){
	 super.onDestroy();
	 	mHandler.removeCallbacks(mUpdateTimeTask);
	    mp.release();
	    
	 }
	
}