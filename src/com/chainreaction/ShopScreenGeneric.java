package com.chainreaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ShopScreenGeneric extends Activity
{
	private static final String PURCHASED_ITEMS = "PurchasedItems";
	private SharedPreferences shopPrefs;
	
	private boolean[] purchasedItems;

	private int money;
	private int price = 50;

	private RelativeLayout shopLayout;

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

		shopPrefs = getSharedPreferences(PURCHASED_ITEMS, 0);
	}

	protected void initiateShop(String[] listElements)
	{
		SharedPreferences.Editor editor = shopPrefs.edit();

		purchasedItems = new boolean[listElements.length];
		for (int i = 0; i < purchasedItems.length; i++)
		{
			purchasedItems[i] = shopPrefs.getBoolean(listElements[i], false);
		}
		money = shopPrefs.getInt("Money", 1000);

		editor.commit();
		
		shopLayout = new RelativeLayout(this);

		//populating colorItems
		LinearLayout[] listElements2 = new LinearLayout[listElements.length];
		for (int i = 0; i < listElements2.length; i++)
		{
			LinearLayout temp = new LinearLayout(this);
			temp.setId(i+1);

			//populating linear layout
			LayoutParams par = new LinearLayout.LayoutParams(120, 60);
			
			//replace following:
			TextView shopItem = new TextView(this);
			shopItem.setText(listElements[i]);
			//with:
			
			//ImageView shopItem = new ImageView(this);
			//shopItem.setImageResource(getResources().getIdentifier(listElements[i], "drawable", "com.chainreaction"));
			
			shopItem.setLayoutParams(par);
			shopItem.setPadding(20, 0, 0, 10);
			temp.addView(shopItem);

			Button2 button = new Button2(this, listElements[i]);
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
			shopLayout.addView(temp);
		}
		setContentView(shopLayout);
	}

	private class Button2 extends View implements View.OnClickListener
	{
		private String itemName;
		public Button2(Context context, String itemName)
		{
			super(context);
			this.itemName = itemName;
			setOnClickListener(this);
			if (shopPrefs.getBoolean(itemName, false) == true)
			{
				setVisibility(View.INVISIBLE);
			}
			LayoutParams par = new LinearLayout.LayoutParams(100, 50);
			setLayoutParams(par);
			setBackgroundResource(R.drawable.buy_button);
		}

		public void onClick(View v)
		{
			boolean isUnlocked = shopPrefs.getBoolean(itemName, false);
			if (isUnlocked == false)
			{
				createDialog(this);
			}
		}
	}

	private void createDialog(Button2 button)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setMessage("Purchase " + button.itemName + " for " + price + "?");
		builder.setPositiveButton("Purchase", new PositiveButton(price, button));
		builder.setNegativeButton("Back", new NegativeButton(button));
		builder.show();
	}

	private class PositiveButton implements OnClickListener
	{
		private int price;
		private String itemName;
		private Button2 button;

		private PositiveButton(int price, Button2 button)
		{
			this.price = price;
			this.itemName = button.itemName;
			this.button = button;
		}

		public void onClick(DialogInterface dialog, int which)
		{
			if (price <= money)
			{
				SharedPreferences.Editor editor = shopPrefs.edit();
				editor.putBoolean(itemName, true);
				money = money - price;
				editor.putInt("Money", money);
				editor.commit();
				button.setVisibility(View.INVISIBLE);
			}
			else
			{
				SharedPreferences.Editor editor = shopPrefs.edit();
				editor.putBoolean(itemName, false);
				editor.commit();
			}
		}
	}

	private class NegativeButton implements OnClickListener
	{
		private String itemName;

		private NegativeButton(Button2 button)
		{
			this.itemName = button.itemName;
		}

		public void onClick(DialogInterface dialog, int which)
		{
			SharedPreferences.Editor editor = shopPrefs.edit();
			editor.putBoolean(itemName, false);
			editor.commit();
		}
	}
}