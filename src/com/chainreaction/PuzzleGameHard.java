package com.chainreaction;

import android.os.Bundle;

public class PuzzleGameHard extends PuzzleGame
{
	private int panicSpawn = 20;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		setPanicSpawn(panicSpawn);
		super.onCreate(savedInstanceState);
	}
}
