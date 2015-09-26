package com.chainreaction;

import android.os.Bundle;

public class PuzzleGameOlympic extends PuzzleGame
{
	private int panicSpawn = 63;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		setPanicSpawn(panicSpawn);
		super.onCreate(savedInstanceState);
		while (!testValidGame())
		{
			clearTiles();
			generateTiles(getSpawnNum());
		}
	}
	
	@Override
	public void setABC()
	{
		setA(getPrevTile().getWild());
		setB(getTiles().get(getIndexTouched()).getColor()==getPrevTile().getColor()&&!(getTiles().get(getIndexTouched()).getShape().equals(getPrevTile().getShape()))&&(getNextCondition().equals("color")||getNextCondition().equals("wild")));
		setC(getTiles().get(getIndexTouched()).getShape().equals(getPrevTile().getShape())&&getTiles().get(getIndexTouched()).getColor()!=getPrevTile().getColor()&&(getNextCondition().equals("shape")||getNextCondition().equals("wild")));
	}
	public boolean testValidGame()
	{
	int[][] counterGrid = new int[getShapes().size()][getColors().size()];
	for(int i=0; i < getTiles().size(); i++)
	{
	counterGrid[getShapes().indexOf(getTiles().get(i).getShape())][getColors().indexOf(getTiles().get(i).getColor())]++;
	}
	int evenCount = 0;
	int counter = 0;
	int lineMax = 0;
	int lineSum = 0;
	boolean isEven=false;
	boolean okSums = true;

	for (int s = 0; s < getShapes().size(); s++)
	{
	for(int sp = 0; sp < getColors().size(); sp++)
	{
	counter+=counterGrid[s][sp];
	lineMax = Math.max(lineMax, counterGrid[s][sp]);
	}
	if (counter %2==0) evenCount++;
	counter = 0;

	if (Math.abs(lineMax-lineSum) >lineMax) okSums = false;

	lineMax = 0;
	}

	for (int c = 0; c < getColors().size(); c++)
	{
	for(int cp=0;cp<getShapes().size();cp++)
	{
	counter+=counterGrid[cp][c];
	lineMax = Math.max(lineMax, counterGrid[cp][c]);
	}
	if (counter%2==0) evenCount++;
	counter = 0;
	if (Math.abs(lineMax-lineSum)>lineMax) okSums = false;
	lineMax = 0;
	}
	if (evenCount>=getShapes().size() * getColors().size()) isEven=true;


	return isEven && okSums;
	}
}