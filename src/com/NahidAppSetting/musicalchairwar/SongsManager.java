package com.NahidAppSetting.musicalchairwar;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SongsManager {
	// SDCard Path
	static String MEDIA_PATH = new String("/sdcard/Sounds/");
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	
	// Constructor
	public SongsManager(){		
	}
	
	public SongsManager(String m_path){
		
		MEDIA_PATH= m_path;
	}
	
	public String GetMediaPath()
	{
		String path= MEDIA_PATH;
		return path;
	}
	
	public void Set_Media_Path(String M_Path)
	{
		MEDIA_PATH= M_Path;
	}
	
	/**
	 * Function to read all mp3 files from sdcard
	 * and store the details in ArrayList
	 * */
	public ArrayList<HashMap<String, String>> getPlayList(){
		File home = new File(MEDIA_PATH);
		songsList.clear();
		if (home.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : home.listFiles(new FileExtensionFilter())) {
				HashMap<String, String> song = new HashMap<String, String>();
				song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
				song.put("songPath", file.getPath());
				songsList.add(song);
			}
		}
		// return songs list array
		return songsList;
	}
	
	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}
}
