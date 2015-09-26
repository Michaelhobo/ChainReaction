package com.chainreaction;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PuzzleGameMenu extends ListActivity
{
	private String[] items = {"Easy", "Medium", "Hard", "Olympic"};
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
    }
    public void onListItemClick(ListView list, View view, int position, long id)
    {
    	super.onListItemClick(list, view, position, id);
    	newActivity("PuzzleGame" + items[position]);
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
}
