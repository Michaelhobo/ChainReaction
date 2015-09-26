package com.chainreaction;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Play extends ListActivity
{
	String[] items = {"Arcade", "Puzzle", "Custom", "Shop", "Achievements", "Settings", "Rules"};

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
	}
	public void onListItemClick(ListView list, View view, int position, long id)
	{
		super.onListItemClick(list, view, position, id);
		if (position <= 1)
			newActivity(items[position] + "GameMenu");
		else if (position == 3)
			newActivity(items[position] + "Menu");
		else if(position == 5)
			newActivity(items[position]);
	}
	protected void newActivity(String name) 
	{
		try
		{
			Intent intent = new Intent(this, Class.forName("com.chainreaction." + name));
			startActivity(intent);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	protected void createDialog(String title)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(title);
		builder.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
	}
}