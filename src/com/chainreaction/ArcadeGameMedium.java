package com.chainreaction;

import android.os.Bundle;

public class ArcadeGameMedium extends ArcadeGame
{
	private int spawnNum = 6;
	private int interval = 1500;

	public void onCreate(Bundle savedInstanceState) 
	{
		setSpawnNum(spawnNum);
		setInterval(interval);
		super.onCreate(savedInstanceState);
	}
}
