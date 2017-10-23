package com.NahidAppSetting.musicalchairwar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DialogChooseDirectory implements OnItemClickListener, OnClickListener
{
    public interface Result
    {
        void onChooseDirectory( String dir );
    }
 
    List<File> m_entries = new ArrayList< File >();
    File m_currentDir;
    Context m_context;
    AlertDialog m_alertDialog;
    ListView m_list;
    Result m_result = null;
 
    public class DirAdapter extends ArrayAdapter< File >
    {
        public DirAdapter( int resid )
        {
            super( m_context, resid, m_entries );
        }
 
        // This function is called to show each view item
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            TextView textview = (TextView) super.getView( position, convertView, parent );
 
            if ( m_entries.get(position) == null )
            {
                textview.setText( ".." );
                textview.setCompoundDrawablesWithIntrinsicBounds( m_context.getResources().getDrawable( R.drawable.btn_pause ), null, null, null );
            }
            else
            {
                textview.setText( m_entries.get(position).getName() );
                textview.setCompoundDrawablesWithIntrinsicBounds( m_context.getResources().getDrawable( R.drawable.btn_play ), null, null, null );
            }
 
            return textview;
        }
    }
 
    private void listDirs()
    {
        m_entries.clear();
 
        // Get files
        File[] files = m_currentDir.listFiles();
 
        // Add the ".." entry
        if ( m_currentDir.getParent() != null )
            m_entries.add( new File("..") );
 
        if ( files != null )
        {
            for ( File file : files )
            {
                if ( !file.isDirectory() )
                    continue;
 
                m_entries.add( file );
            }
        }
 
        Collections.sort( m_entries, new Comparator<File>() {
                public int compare(File f1, File f2)
                {
                    return f1.getName().toLowerCase().compareTo( f2.getName().toLowerCase() );
                }
        } );
    }
 
    public DialogChooseDirectory( Context ctx, Result res, String startDir )
    {
        m_context = ctx;
        m_result = res;
 
        if ( startDir != null )
            m_currentDir = new File( startDir );
        else
            m_currentDir = Environment.getExternalStorageDirectory();
 
        listDirs();
        DirAdapter adapter = new DirAdapter( R.layout.file_list_row );
 
        AlertDialog.Builder builder = new AlertDialog.Builder( ctx );
        builder.setTitle( "File Directory Choose");
        builder.setAdapter( adapter, (android.content.DialogInterface.OnClickListener) this );
 
        builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   if ( m_result != null )
                       m_result.onChooseDirectory( m_currentDir.getAbsolutePath() );
                   dialog.dismiss();
               }
           });
 
        builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
               }
           });
 
        AlertDialog m_alertDialog = builder.create();
        m_list = m_alertDialog.getListView();
        m_list.setOnItemClickListener( this );
        m_alertDialog.show();
    }
 
    @Override
    public void onItemClick(AdapterView<?> arg0, View list, int pos, long id )
    {
        if ( pos < 0 || pos >= m_entries.size() )
            return;
 
        if ( m_entries.get( pos ).getName().equals( ".." ) )
            m_currentDir = m_currentDir.getParentFile();
        else
            m_currentDir = m_entries.get( pos );
 
        listDirs();
        DirAdapter adapter = new DirAdapter( R.layout.file_list_row );
        m_list.setAdapter( adapter );
    }
 
    public void onClick(DialogInterface dialog, int which)
    {
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}