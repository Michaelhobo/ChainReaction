package com.chainreaction;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Settings extends Activity
{
	private static final String SETTINGS = "Settings";
	private SharedPreferences prefs;
	private String[] listElements;
	private RelativeLayout settingsLayout;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		prefs = getSharedPreferences(SETTINGS, 0);

		listElements = new String[6];
		listElements[0] = "Sound";
		listElements[1] = "Yellow";
		listElements[2] = "Red";
		listElements[3] = "Green";
		listElements[4] = "Black";
		listElements[5] = "Hi";

		createList();
	}

	private void createList()
	{
		settingsLayout = new RelativeLayout(this);

		//populating colorItems
		LinearLayout[] listElements2 = new LinearLayout[listElements.length];
		for (int i = 0; i < listElements2.length; i++)
		{
			LinearLayout temp = new LinearLayout(this);
			temp.setId(i+1);

			//populating linear layout
			LayoutParams par = new LinearLayout.LayoutParams(120, 60);
			
			//replace following:
			TextView settingsItem = new TextView(this);
			settingsItem.setText(listElements[i]);
			//with:
			
			//ImageView shopItem = new ImageView(this);
			//shopItem.setImageResource(getResources().getIdentifier(listElements[i], "drawable", "com.chainreaction"));
			
			settingsItem.setLayoutParams(par);
			settingsItem.setPadding(20, 0, 0, 10);
			temp.addView(settingsItem);

			CheckBox button = new CheckBox(this, listElements[i], prefs.getBoolean(listElements[i], false));
			button.setClickable(true);
			temp.addView(button);
			//done populating

			listElements2[i] = temp;

			//formatting linear layouts to be relative to each other. then, they are added to the relative layout verticalText.
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			if (i == 0)
			{
				params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
			}
			else if (i == listElements2.length)
			{
				params.addRule(RelativeLayout.BELOW, listElements2[i-1].getId());
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			}
			else
			{
				params.addRule(RelativeLayout.BELOW, listElements2[i-1].getId());
			}
			temp.setLayoutParams(params);
			settingsLayout.addView(temp);
		}
		setContentView(settingsLayout);
	}

	private class CheckBox extends View implements View.OnClickListener
	{
		private String itemName;
		private boolean isClicked;
		private CheckBox(Context context, String itemName, boolean isClicked)
		{
			super(context);
			this.itemName = itemName;
			this.isClicked = isClicked;
			setOnClickListener(this);
			LayoutParams par = new LinearLayout.LayoutParams(100, 50);
			setLayoutParams(par);

			if (isClicked == false)
				setBackgroundResource(R.drawable.red);
			else
				setBackgroundResource(R.drawable.green);
		}

		public void onClick(View v)
		{
			isClicked = !isClicked;
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(itemName, isClicked);
			editor.commit();
			
			if (isClicked == false)
				setBackgroundResource(R.drawable.red);
			else
				setBackgroundResource(R.drawable.green);
		}
	}
}