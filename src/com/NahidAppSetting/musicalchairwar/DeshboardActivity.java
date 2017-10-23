package com.NahidAppSetting.musicalchairwar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DeshboardActivity extends Activity {
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.dashboard);
	    }
	 
	    /**
	     * Button click handler on Main activity
	     * @param v
	     */
	    public void onButtonClicker(View v)
	    {
	     Intent intent;
	 
	     switch (v.getId()) 
	     {
		  case R.id.btndplayer:
		   intent = new Intent(getApplicationContext(), MainActivity.class);
		   startActivity(intent);
		   break;
		 
		  case R.id.btndplaylist:
			Intent i = new Intent(getApplicationContext(), PlayListActivity.class);
			startActivityForResult(i, 100);	
		   break;
		 
		  case R.id.btndsetting:
		   intent = new Intent(getApplicationContext(), SettingActivity.class);
		   startActivity(intent);
		   break;
		 
		  case R.id.btndabout:
			   intent = new Intent(getApplicationContext(), ListFolder.class);
			   startActivity(intent);
		   break;
		   
		  case R.id.btndexit:
			   System.exit(0);
			   break;
			   
		  default:
		   break;
		  }
    }

}
