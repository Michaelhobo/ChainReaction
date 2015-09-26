package com.chainreaction;

import android.os.Bundle;

public class PuzzleGameMedium extends PuzzleGame
{
	private int panicSpawn = 10;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		setPanicSpawn(panicSpawn);
		super.onCreate(savedInstanceState);
	}
}
