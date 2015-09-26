package com.chainreaction;

import android.os.Bundle;

public class ShopScreenColors extends ShopScreenGeneric
{
	private final String[] listElements = {"blue", "yellow", "red", "green", "purple"};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initiateShop(listElements);
	}
}