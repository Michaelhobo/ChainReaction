package com.chainreaction;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.MotionEvent;

public class PuzzleGame extends GameActivity
{
	private int spawnNum = 50;
	private int panicSpawn;
	private int interval = 1000;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		setTitleName("Puzzle Mode");
		setInterval(interval);
		super.onCreate(savedInstanceState);
		addPanicButton(panicSpawn);
		setModeName("Puzzle");
		generateTiles(spawnNum);
	} 
	public void touchedtask(MotionEvent ev)
	{
		super.touchedTask(ev);
		if (getTiles().size()==0)
		{
			endGame();
		}
	}
	
	protected void setPanicSpawn(int panicSpawn)
	{
		this.panicSpawn = panicSpawn;
	}
	public int getSpawnNum()
	{
		return spawnNum;
	}
	
}
