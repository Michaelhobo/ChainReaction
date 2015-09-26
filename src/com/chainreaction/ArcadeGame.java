package com.chainreaction;

import android.os.Bundle;

public class ArcadeGame extends GameActivity
{
	private int limit = 50;
	private int spawnNum;

	public void onCreate(Bundle savedInstanceState) 
	{
		setTitleName("Arcade Mode");
		super.onCreate(savedInstanceState);
		setModeName("Arcade");
	}

	public void timedTask()
	{
		if (spawnNum < limit - getTiles().size())
		generateTiles(spawnNum);
		else generateTiles(limit-getTiles().size());
		if (getTiles().size() >= limit)
		{
			endGame();
		}
		super.timedTask();
	}

	protected void setSpawnNum(int spawnNum)
	{
		this.spawnNum = spawnNum;
	}
}