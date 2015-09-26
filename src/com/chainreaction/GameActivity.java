package com.chainreaction;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameActivity extends Activity
{
	private ArrayList<String> shapes = new ArrayList<String>();
	private ArrayList<Integer> colors = new ArrayList<Integer>();
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	private ArrayList<Integer> tilesPosition = new ArrayList<Integer>();
	private ArrayList<String> types = new ArrayList<String>();

	private boolean a; //
	private boolean b; //
	private boolean c; //
	private boolean createWildTiles; //whether to create wild tiles or not.  
	private Bitmap[][] bitmaps; //an array containing all combinations of bitmaps. colors x shapes
	private Bitmap background; //background color for bitmaps
	private Bitmap shape; //the shape's bitmap
	private Bitmap pt; //previous tile
	private Bitmap wildBitmap;//bitmap used for all wild tiles

	private DisplayMetrics dm; //used to find the dimensions and properties of the screen

	private Grid grid;//the grid used to draw all shapes

	//private Handler handler = new Handler();

	private ImageView showPrevTile; //shows the previous tile as an image on the screen
	private ImageView screen;
	private int numRows = 9; //number of rows in grid
	private int numCols = 7; //number of columns in grid
	private int position; //position of tile to generate
	private int color; //color of tile to generate
	private int tileWidth; //width of tile
	private int tileHeight; //height of tile
	private int xCoord; //xCoord for OTL touch
	private int yCoord; //yCoord for OTL touch
	private int interval; //the interval between each tile
	private int points; //keeps track of points in the game
	private int indexTouched; //the index touched in the game grid, from x and y coordinates 
	
	private RelativeLayout rl; //the overall layout
	private LinearLayout aboveGrid; //the layout with objects above grid
	private LinearLayout belowGrid; //the layout with objects below grid

	private Random rand = new Random(); 

	private String message = ""; //message shown in createDialog()
	private String modeName = ""; //used to open the mode's game menu
	private String titleName = ""; //the name of the game mode that appears on screen
	private String shapeName; //shape of tile to generate
	private Timer timer = new Timer(); //timer that runs the game
	private String nextCondition; //next condition that must be fulfilled, either shape, color, or wild

	private MyTimerTask task = new MyTimerTask(); 

	private TextView tilesRemaining; //shows data for number of tiles on the grid and previous tile info
	private TextView title; //shows title in abovegrid


	private Tile prevTile; //previous tile clicked

	//ARUN AND MICHAEL, DO NOT CALL THIS METHOD UNTIL BELOWGRID HAS PROPERLY BEEN INITIALIZED AND POPULATED!!!
	protected void addPanicButton(int panicSpawn)
	{
		belowGrid.addView(new Panic(this, panicSpawn));
	}

	public class Panic extends View implements View.OnClickListener
	{
		private int spawnNum;

		public Panic(Context context, int spawnNum)
		{
			super(context);
			setOnClickListener(this);
			LayoutParams par = new LinearLayout.LayoutParams(dm.widthPixels/5, dm.heightPixels/10);
			setLayoutParams(par);
			setBackgroundResource(R.drawable.buy_button);
			this.spawnNum = spawnNum;
		}

		public void onClick(View v)
		{
			Runnable thing = new Runnable()
			{
				public void run()
				{
					if (spawnNum > (63 - tiles.size()))
						generateTiles(63 - tiles.size());
					else
						generateTiles(spawnNum);
				}
			};
			Handler thingy = new Handler();
			thingy.post(thing);
		}
	}

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//bitmaps = new Bitmap[3][3];
		createWildTiles = false;
		nextCondition="wild";
		initializeScreen();
		prepareGameInfo();
		timer.schedule(task, 5, interval);
		grid.setOnTouchListener(OTL);
	}
	
	int belowGridWidth;
	int belowGridHeight = 100;
	
	//Call this BEFORE onCreate()!
	protected void setInterval(int interval)
	{
		this.interval = interval;
	}
	
	public void initializeScreen()
	{
		shapes.add("circle");
		shapes.add("triangle2");
		shapes.add("square");
		colors.add(Color.RED);
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
		types.add("Normal");
		points = 0;
		bitmaps = new Bitmap[colors.size()][shapes.size()];
		prevTile = new Tile(0, shapes.get(rand.nextInt(shapes.size())), colors.get(rand.nextInt(colors.size())), true, "Normal");
		PowerManager pManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		WakeLock wLock = pManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Wake Lock");
		wLock.acquire();
		//Full screen. Call setContentView only AFTER going full screen.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		grid = new Grid(this);
		grid.setBackgroundResource(getResources().getIdentifier("circle", "drawable", "com.chainreaction"));

		aboveGrid = new LinearLayout(this);
		/*
		aboveGrid = new LinearLayout(this)
		{
			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
			{
				super.onMeasure(widthMeasureSpec,heightMeasureSpec);
				setMeasuredDimension(MeasureSpec.makeMeasureSpec(dm.widthPixels, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(dm.heightPixels/7, MeasureSpec.EXACTLY));
			}
		};*/
		//aboveGrid.setBackgroundResource(getResources().getIdentifier("square", "drawable", "com.chainreaction"));
		belowGrid = new LinearLayout(this);
		//belowGrid.setBackgroundResource(getResources().getIdentifier("belowgridskin", "drawable", "com.chainreaction"));
		rl = new RelativeLayout(this);
		 /* ViewTreeObserver vto = aboveGrid.getViewTreeObserver();
		
	        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
	            public void onGlobalLayout() {
	            	grid.layout(0, 0, dm.widthPixels, dm.heightPixels-belowGrid.getHeight());
	            }
	        });*/
		
		//rl.setBackgroundResource(getResources().getIdentifier("triangle", "drawable", "com.chainreaction"));

		PauseButton pause = new PauseButton(this);
		tilesRemaining = new TextView(this);
		tilesRemaining.setText("TR: " + tiles.size() + " PT: " + colorToString(prevTile.getColor()) + prevTile.getShape() + nextCondition);
		title = new TextView(this);
		aboveGrid.setId(1);
		grid.setId(2);
		belowGrid.setId(3);

		aboveGrid.addView(pause);
		belowGrid.addView(tilesRemaining);
		showPrevTile = new ImageView(this);
		belowGrid.addView(showPrevTile);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		belowGrid.setLayoutParams(params);

		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		aboveGrid.setLayoutParams(params);

		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ABOVE, belowGrid.getId());
		params.addRule(RelativeLayout.BELOW, aboveGrid.getId());
		grid.setLayoutParams(params);
		//belowGrid.measure(50, 50);
		//grid.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
		//belowGrid.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.EXACTLY);
		//aboveGrid.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.EXACTLY);
	//	aboveGrid.layout(0, 0, r, b)
    	//grid.layout(0, 0, grid.getWidth(), grid.getHeight());
		//grid.measure(widthMeasureSpec, heightMeasureSpec);
		//aboveGrid.layout(0, 0, dm.widthPixels, dm.heightPixels/8);
		//belowGrid.layout(0, 0, dm.widthPixels, dm.heightPixels/8);
    	grid.layout(0, 0, dm.widthPixels, dm.heightPixels*6/8);
		tileWidth = grid.getWidth()/7;
		tileHeight = grid.getHeight()/9;
		title.setText(titleName + "    Score: " + points);
		aboveGrid.addView(title);

		rl.addView(aboveGrid);
		rl.addView(belowGrid);
		rl.addView(grid);
		setContentView(rl);
	}

	public void prepareGameInfo() 
	{
		//Bitmap bmp;
		//int imgId = Integer.decode("R.Drawable.f");



		int currentColor;
		String currentShape;
		new Paint();
		Bitmap finalTile; 
		for (int c = 0; c < colors.size(); c++)
		{
			currentColor = colors.get(c);
			int colorId = getResources().getIdentifier(colorToString(currentColor), "drawable", "com.chainreaction");
			background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), colorId), tileWidth, tileHeight, false);
			for (int s = 0; s < shapes.size(); s++)
			{

				currentShape = shapes.get(s);
				int shapeId = getResources().getIdentifier(currentShape, "drawable", "com.chainreaction");
				shape = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), shapeId), tileWidth, tileHeight, false);
				for (int x = 0; x<tileWidth; x++)
				{
					for (int y = 0; y<tileHeight; y++)
					{
						if (shape.getPixel(x, y)==Color.BLACK)
						{
							shape.setPixel(x, y, Color.argb(120,0,0,0));
						}
					}
				}
				finalTile = overlay(background, shape);
				//cv.drawColor(currentColor);
				//cv.drawBitmap(bi, 0, 0, paint);
				//cv.drawBitmap(shape, 0, 0, paint);
				for (int x = 0; x<tileWidth; x++)
				{
					for (int y = 0; y<tileHeight; y++)
					{
						if (finalTile.getPixel(x, y)==Color.WHITE)
						{
							finalTile.setPixel(x, y, Color.argb(0,0,0,0));
						}
					}
				}
				bitmaps[c][s] = finalTile; 
			}
		}
		wildBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("wild", "drawable", "com.chainreaction")), tileWidth, tileHeight, false);
	}
	public Bitmap overlay(Bitmap under, Bitmap over)
	{
		Bitmap bmOverlay = Bitmap.createBitmap(tileWidth, tileHeight, Bitmap.Config.ARGB_8888);
		Canvas cv = new Canvas (bmOverlay);
		cv.drawBitmap(under, 0, 0, null);
		cv.drawBitmap(over, 0, 0, null);
		return bmOverlay;
	}
	public void onDestroy()
	{
		super.onDestroy();
		task.cancel();
		timer.purge();
		timer.cancel();
	}

	OnTouchListener OTL = new OnTouchListener()
	{
		public boolean onTouch(View v, MotionEvent event) 
		{
			touchedTask(event);
			return true;
		}
	};
	public void setABC()
	{
		a = prevTile.getWild();
		b = tiles.get(indexTouched).getColor()==prevTile.getColor()&&!(tiles.get(indexTouched).getShape().equals(prevTile.getShape()));
		c = tiles.get(indexTouched).getShape().equals(prevTile.getShape())&&tiles.get(indexTouched).getColor()!=prevTile.getColor();
	}
	public boolean touchedCorrectly()
	{
		indexTouched = tilesPosition.indexOf(findPosition(xCoord, yCoord));
		setABC();
		//boolean d = tiles.get(indexTouched).getColor()==prevTile.getColor()&&!(tiles.get(indexTouched).getShape().equals(prevTile.getShape()))&&(nextCondition.equals("color")||nextCondition.equals("wild")) && tiles.get(indexTouched).getType().equals("Wipeout!");

		if (a||b||c) return true;
		else return false;
	}
	public void touchedTask(MotionEvent ev)
	{
		xCoord = (int) ev.getX();
		yCoord = (int) ev.getY();
		if (xCoord>tileWidth * numCols || yCoord>tileHeight*numRows);
		else if (ev.getAction()==MotionEvent.ACTION_UP && tilesPosition.contains(findPosition(xCoord, yCoord)))
		{
			if (touchedCorrectly())
			{
				prevTile = tiles.get(indexTouched);
				if (a) nextCondition="wild";
				else if (b) nextCondition="shape";
				else if (c) nextCondition="color";
				tilesRemaining.setText("TR: " + tiles.size() + " PT: " + colorToString(prevTile.getColor()) + prevTile.getShape());
				tiles.remove(indexTouched);
				tilesPosition.remove(indexTouched);
				runOnUiThread (redrawGrid);
				tilesRemaining.setText("TR: " + tiles.size() + " PT: " + colorToString(prevTile.getColor()) + prevTile.getShape() + nextCondition);
				pt = bitmaps[colors.indexOf(prevTile.getColor())][shapes.indexOf(prevTile.getShape())];
				showPrevTile.setImageBitmap(pt);
				points++;
			}
			else if (points > 0)
				points--;
			title.setText(titleName + "    Score: " + points);
		}
	}
	Runnable redrawGrid = new Runnable()
	{
		public void run() 
		{
			grid.invalidate();
		}
	};
	public int findPosition(int x, int y)
	{
		int tilePosition = (int) (y / tileHeight * numCols + x / tileWidth);
		return tilePosition;
	}



	private class MyTimerTask extends TimerTask 
	{
		public void run() 
		{
			timedTask();
		}
	}

	public void timedTask() 
	{
		runOnUiThread(redrawGrid);
	}

	public void endGame()
	{
		task.cancel();
		timer.purge();
		timer.cancel();
		screen = new ImageView(this);
		grid.setOnTouchListener(null);
		screen.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("greyscreen", "drawable", "com.chainreaction")), grid.getWidth(), grid.getHeight(), false));
	}


	public void generateTiles(int num)
	{
		shapeName = "";
		color = 0;
		for (int i = 0; i < num; i++)
		{
			position = unusedPosition(rand.nextInt(numRows*numCols));
			shapeName = shapes.get(rand.nextInt(shapes.size()));
			color = colors.get(rand.nextInt(colors.size()));

			Tile newTile = new Tile(position, shapeName, color, createWildTiles, "Normal");
			//RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(tileWidth, tileHeight);
			//lParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.BELOW);
			//newTile.setLayoutParams(lParams);
			tiles.add(newTile);
			tilesPosition.add(position);
			//tilesRemaining.setText("TR: "+  " PT: ");

		}
		Runnable status = new Runnable()
		{
			public void run() 
			{
				tilesRemaining.setText("TR: " + tiles.size() + " PT: " + colorToString(prevTile.getColor()) + prevTile.getShape() + nextCondition);
			}
		};
		runOnUiThread(status);
		runOnUiThread(redrawGrid);
	}

	public int unusedPosition(int newPos)
	{
		if (tilesPosition.contains(newPos)) return unusedPosition(rand.nextInt(numRows*numCols));
		else return newPos;
	}

	
	
	public void createDialog(String title)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setCancelable(true);
		if (title.equals("Game Paused"))
		{
			builder.setPositiveButton("Resume", new Positive());
			builder.setNegativeButton("Home", new Negative());
		}
		AlertDialog alert = builder.create();
		alert.show();
	}

	public String colorToString(int colorInt)
	{
		String colorString = "";
		if (colorInt==Color.RED) colorString = "red";
		else if (colorInt==Color.BLUE) colorString = "blue";
		else if (colorInt==Color.GREEN) colorString = "green";
		return colorString;
	}

	protected void newActivity(String name) 
	{
		try
		{
			Intent intent = new Intent(this, Class.forName("com.chainreaction." + name));
			startActivity(intent);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<Tile> getTiles()
	{
		return tiles;
	}
	public void setModeName(String newModeName)
	{
		modeName = newModeName;
	}
	public void setTitleName(String newTitleName)
	{
		titleName = newTitleName;
	}
	public Bitmap getWildBitmap()
	{
		return wildBitmap;
	}
	public void setA(boolean bn)
	{
		a = bn;
	}
	public void setB(boolean bn)
	{
		b = bn;
	}
	public void setC(boolean bn)
	{
		c = bn;
	}
	public Tile getPrevTile()
	{
		return prevTile;
	}
	public int getIndexTouched()
	{
		return indexTouched;
	}
	public String getNextCondition()
	{
		return nextCondition;
	}
	public ArrayList<String> getShapes()
	{
		return shapes;
	}
	public ArrayList<Integer> getColors()
	{
		return colors;
	}
	public void clearTiles()
	{
		tiles.clear();
	}




	public class PauseButton extends Button implements android.view.View.OnClickListener
	{
		public PauseButton(Context context) 
		{
			super(context);
			setText("Pause");
			setOnClickListener(this);
		}
		public void onClick(View v)
		{
			task.cancel();
			timer.purge();
			createDialog("Game Paused");
		}
	}


	class Positive implements OnClickListener
	{
		public void onClick(DialogInterface dialog, int which) 
		{
			task = new MyTimerTask();
			timer.schedule(task, 5, interval);
		}
	}

	class Neutral implements OnClickListener
	{
		public void onClick(DialogInterface dialog, int which) 
		{
			//createDialog("Rules");
		}
	}

	class Negative implements OnClickListener
	{
		public void onClick(DialogInterface dialog, int which)
		{
			finish();
		}
	}

	public class Grid extends View
	{

		public Grid(Context context) 
		{
			super(context);
		}
		public void onDraw(Canvas canvas)
		{

			for (int i = 0; i < tiles.size(); i++)
			{
				canvas.drawBitmap(
						tiles.get(i).getBitmap(),
						(float) (tiles.get(i).getColNum() * tileWidth),
						(float) (tiles.get(i).getRowNum() * tileHeight),
						tiles.get(i).getPaint());
			}
		}
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
		{
				super.onMeasure(widthMeasureSpec,heightMeasureSpec);
				//int wide = MeasureSpec.getSize(widthMeasureSpec);
				//int high = MeasureSpec.getSize(heightMeasureSpec)/7;
				//setMeasuredDimension(wide, high);
			
		};
	}


	public class Tile 
	{
		private Paint paint;
		private int rowNum;
		private int colNum;
		private int pos;
		private String tileShape;
		private int tileColor;
		private Bitmap bm;
		private boolean isWild;
		private String type;
		public Tile(int posi, String shape, int color, boolean wild, String type) //Context context
		{
			pos = posi;
			tileShape = shape;
			tileColor = color;
			this.type = type;
			rowNum = posi/numCols;
			colNum = posi%numCols;
			paint = new Paint();
			if (wild==true) bm = getWildBitmap();
			else bm = bitmaps[colors.indexOf(tileColor)][shapes.indexOf(tileShape)];
			isWild = wild;

		}
		
		private Bitmap getBitmap()
		{
			return bm;
		}
		
		public int getPos()
		{
			return pos;
		}
		public Paint getPaint()
		{
			return paint;
		}
		public int getColor()
		{
			return tileColor;
		}
		public String getShape()
		{
			return tileShape;
		}
		public int getColNum()
		{
			return colNum;
		}
		public int getRowNum()
		{
			return rowNum;
		}
		public boolean getWild()
		{
			return isWild;
		}
		public String getType()
		{
			return type;
		}
		
		
	}
}