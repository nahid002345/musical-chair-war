
package com.NahidAppSetting.musicalchairwar;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.DateFormat;

import com.NahidAppSetting.musicalchairwar.*;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

public class ListFolder extends ListActivity {

	private File currentDir;
	private FileArrayAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentDir = new File("/sdcard/");
		fill(currentDir);
	}

	private void fill(File f) {
		File[] dirs = f.listFiles();
		this.setTitle("Current Directory: " + f.getName());
		List<ListModel> dir = new ArrayList<ListModel>();
		List<ListModel> fls = new ArrayList<ListModel>();
		try {
			for (File ff : dirs) {
				String name = ff.getName();
				Date lastModDate = new Date(ff.lastModified());
				DateFormat formater = DateFormat.getDateTimeInstance();
				String date_modify = formater.format(lastModDate);

				if (ff.isDirectory()) {
					File[] fbufTemp = ff.listFiles();
/*					if (IsThereAnyMP3File(fbufTemp)) {*/
					File[] fbuf = ff.listFiles();
					int buf = 0;
					if (fbuf != null) {
						buf = fbuf.length;
					} else
						buf = 0;
					String num_item = String.valueOf(buf);
					if (buf == 0)
						num_item = num_item + " item";
					else
						num_item = num_item + " items";

					// String formated = lastModDate.toString();
					dir.add(new ListModel(ff.getName(), num_item, date_modify,
							ff.getAbsolutePath(), "directory_icon"));
					//}
	
				} else {

						fls.add(new ListModel(ff.getName(), (ff.length() / 1024)
								+ " KB", date_modify, ff.getAbsolutePath(),
								"file_icon"));

				}
			}
		} catch (Exception e) {

		}

		dir.addAll(fls);
		if (!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0, new ListModel("..", "Parent Directory", "", f.getParent(),
							"directory_up"));
		adapter = new FileArrayAdapter(ListFolder.this, R.layout.playlist_item, dir);
		this.setListAdapter(adapter);
	}
	
	private boolean IsThereAnyMP3File(File[] dir)
	{
		boolean result=false;
		for (File ff : dir) {
			if (ff.isDirectory()) {
				if (ff.listFiles(new FileExtensionFilter()).length > 0) {
					result=true;
				}
			}
		}
		return result;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		ListModel o = adapter.getItem(position);
		if (o.getImage().equalsIgnoreCase("directory_icon")
				|| o.getImage().equalsIgnoreCase("directory_up")) {
			currentDir = new File(o.getPath());
			fill(currentDir);
		}

	}
	
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3") && dir.list().length > 0);
		}
	}

}