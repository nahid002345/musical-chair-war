package com.NahidAppSetting.musicalchairwar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DirectoryBrowser extends ListActivity {
   
	private String mediaPath;
    private List<String> items = null;
    List<File> m_entries = new ArrayList< File >();
    File m_currentDir;
    SongsManager sManager= new SongsManager(); 
	private SharedPreferences shrPrefer;
	private SharedPreferences.Editor editor;
	private static String MEDIA_PATH_KEY="MediaPath";
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.directory_list);  
		shrPrefer= getPreferences(MODE_PRIVATE);
		editor= getPreferences(MODE_PRIVATE).edit();
		mediaPath= shrPrefer.getString(MEDIA_PATH_KEY, sManager.GetMediaPath());
        m_currentDir= new File(mediaPath);
        getFiles(new File(sManager.GetMediaPath()).listFiles());
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        int selectedRow = (int)id;
        if(selectedRow == 0){
            getFiles(m_currentDir.getParentFile().listFiles());
        }
        else
        {
        	m_currentDir = m_entries.get( position );
            if(m_currentDir.isDirectory()){
                getFiles(m_currentDir.listFiles());
                
            }
            else{
                 AlertDialog show = new AlertDialog.Builder(getApplicationContext())
                 .setTitle("This file is not a directory")
                 .setNeutralButton("OK", new DialogInterface.OnClickListener(){
                     public void onClick(DialogInterface dialog, int button){
                         //do nothing
                     }
                 })
                 .show();
            }
        }
    }
    
    public void btnSaveDirActionOnClick(View button){
    	
		editor.putString(MEDIA_PATH_KEY,m_currentDir.getPath());
		editor.commit();
    	sManager.Set_Media_Path(m_currentDir.getPath());
    } 
    
    private void getFiles(File[] files){
        m_entries.clear();
		if ( m_currentDir.getParent() != null )
            m_entries.add( new File("..") );
        if ( files != null )
        {
        	for ( File file : files )
            {
                if ( !file.isDirectory() )
                    continue; 
                m_entries.add( file);
            }
        }
 
        Collections.sort( m_entries, new Comparator<File>() {
                public int compare(File f1, File f2)
                {
                    return f1.getName().toLowerCase().compareTo( f2.getName().toLowerCase() );
                }
        } );
        ArrayAdapter<File> fileList = new ArrayAdapter<File>(this,R.layout.file_list_row,m_entries);
        setListAdapter(fileList);
    }
    
    @Override
    public void onBackPressed() {
    	finish();
    }
} 