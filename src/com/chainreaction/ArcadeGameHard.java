package com.chainreaction;

import android.os.Bundle;

public class ArcadeGameHard extends ArcadeGame
{
	private int spawnNum = 10;
	private int interval = 1000;

	public void onCreate(Bundle savedInstanceState) 
	{
		setSpawnNum(spawnNum);
		super.onCreate(savedInstanceState);
		setInterval(interval);
	}
}
