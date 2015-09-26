package com.chainreaction;

import android.os.Bundle;

public class ArcadeGameEasy extends ArcadeGame
{
	private int spawnNum = 3;
	private int interval = 2500;

	public void onCreate(Bundle savedInstanceState) 
	{
		setSpawnNum(spawnNum);
		setInterval(interval);
		super.onCreate(savedInstanceState);
	}
}
