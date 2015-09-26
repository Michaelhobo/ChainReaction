package com.chainreaction;

import android.os.Bundle;

public class ShopScreenPowerups extends ShopScreenGeneric
{
	private String[] listElements = {"Deep Freeze", "2X Multiplier", "4X Multiplier", "Tile Charge", "Wipeout!"};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initiateShop(listElements);
	}
}