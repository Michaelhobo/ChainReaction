package com.chainreaction;

import android.os.Bundle;

public class ArcadeGameOlympic extends ArcadeGame
{
	private int spawnNum = 14;
	private int interval = 750;

	public void onCreate(Bundle savedInstanceState) 
	{
		setSpawnNum(spawnNum);
		setInterval(interval);
		super.onCreate(savedInstanceState);
	}
}
