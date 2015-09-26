package com.chainreaction;

import android.os.Bundle;

public class PuzzleGameEasy extends PuzzleGame
{
	private int panicSpawn = 5;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		setPanicSpawn(panicSpawn);
		super.onCreate(savedInstanceState);
	}
}
