package com.chainreaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class GameMenu extends Activity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		LinearLayout ll = new LinearLayout(this);
		PuzzleButton puzzle = new PuzzleButton(this);
		ArcadeButton arcade = new ArcadeButton(this);
		ll.addView(puzzle);
		ll.addView(arcade);
		setContentView(ll);
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
	public class GameButton extends View
	{
		int width;
		int height;
		int x;
		int y;
		Bitmap bSkin;
		public GameButton(Context context) {
			super(context);
			width = 100;
			height = 100;
			//layout(50, 50, width, height);
			LayoutParams par = new LinearLayout.LayoutParams(width, height);
			setLayoutParams(par);
			bSkin = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("square", "drawable", "com.chainreaction")), width, height, false);
			setBackgroundResource(getResources().getIdentifier("square", "drawable", "com.chainreaction"));
			OnTouchListener OTL = new OnTouchListener()
			{

				public boolean onTouch(View v, MotionEvent event) {
					x = (int) event.getX();
					y = (int) event.getY();
					if (check())
					{
						causeNewActivity();
					}
						
					return true;
				}
				
			};
			setOnTouchListener(OTL);
		}
		public void causeNewActivity(){}
		public boolean check()
		{
			if (Color.alpha(bSkin.getPixel(x, y))==0)return false;
			else return true;
			
		}
	}
	public class PuzzleButton extends GameButton
	{
		public PuzzleButton(Context context) 
		{
			super(context);
			bSkin = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("puzzleskin", "drawable", "com.chainreaction")), width, height, false);
			setBackgroundResource(getResources().getIdentifier("puzzleskin", "drawable", "com.chainreaction"));
		}
		public void causeNewActivity()
		{
			newActivity("PuzzleGame");
		}
	}
	public class ArcadeButton extends GameButton
	{
		public ArcadeButton(Context context) 
		{
			super(context);
			bSkin = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("arcadeskin", "drawable", "com.chainreaction")), width, height, false);
			setBackgroundResource(getResources().getIdentifier("arcadeskin", "drawable", "com.chainreaction"));
		}
		public void causeNewActivity()
		{
			newActivity("ArcadeGame");
		}
	}
}