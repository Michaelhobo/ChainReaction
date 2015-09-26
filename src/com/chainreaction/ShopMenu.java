package com.chainreaction;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShopMenu extends ListActivity
{
	String[] buttons = {"Tile Colors & Designs", "Powerups"};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		PowerManager pManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		WakeLock wLock = pManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Wake Lock");
		wLock.acquire();
		//Full screen. Call setContentView only AFTER going full screen.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buttons));
	}

	@Override
	protected void onListItemClick(ListView list, View view, int position, long id)
	{																								
		super.onListItemClick(list, view, position, id);
		if (buttons[position].equals("Tile Colors & Designs"))
		{
			activityStarter("ShopScreenColors");
		}
		if (buttons[position].equals("Powerups"))
		{
			activityStarter("ShopScreenPowerups");
		}
	}
	
	private void activityStarter(String name)
	{
		try
		{
			Class clazz = Class.forName("com.chainreaction." + name);
			Intent intent = new Intent(this, clazz);
			startActivity(intent);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}