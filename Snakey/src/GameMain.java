import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import com.sun.perseus.j2d.Point;


public class GameMain extends GameCanvas
	implements Runnable
{
	// ************************************ final Variables ************************************
	static final long MENU_DELAY = 100;
	static final long GAME_DELAY = 10; 
	
	static final int GS_MENU = 1;
	static final int GS_GAME = 2;
	
	static final int FE_NONE	= 0;
	static final int FE_WALL	= 1;
	static final int FE_SNAKE	= 2;
	static final int FE_BOX		= 3;
	
	
	static final int SD_UP		= 0;
	static final int SD_RIGHT	= 1;
	static final int SD_DOWN	= 2;
	static final int SD_LEFT	= 3;
	
	
	static final Font FONT_HIGHT = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD,	Font.SIZE_LARGE);
	static final Font FONT_LOW	 = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN,	Font.SIZE_MEDIUM);
	
	// ************************************ Public Variables ************************************
	private boolean	isPlay;
	private boolean isPaused;
	
	private MainApp	Midlet;
	
	private int GameState;
	
	private int ScreenSizeX;
	private int ScreenSizeY;
	
	private Random randGenerator;
	// ************************************ Menu Variables ************************************
	private int			FieldSize;
	private String[]	MenuItems = {"Start", "Field", "Speed", "Quit"};
	
	private int			ItemSelect;
	private int			GameSpeed;
	
	// ************************************ Game Variables ************************************
	private boolean	IsAlive;
	
	private int		OffsetX;
	private int		OffsetY;
	
	private int		BoxX;
	private int		BoxY;
	
	private int		TimeSteps;
	private int		NextStep;
	private int		SnakePush;
	
	private int 	Direction;
	private boolean	DirectionChanged;
	
	private int[][]	GameField;
	private Image	FieldImage;
	private Vector	SnakeElements;
	
	
	
	
	
	
	
	
	
	
	
	// Constructor and initialization
	public GameMain(MainApp midlet)
	{
		super(true);
		
		
		Midlet = midlet;
		
		isPaused = false;
		isPlay = true;
		
		ScreenSizeX = getWidth();
		ScreenSizeY = getHeight();
		
		GameState = GS_MENU;
		
		randGenerator = new Random();
		randGenerator.setSeed(System.currentTimeMillis());
		
		ItemSelect		= 0;
		FieldSize		= Math.min(ScreenSizeX, ScreenSizeY) / 5;
		MenuItems[1]	= "Field: " + FieldSize + "x" + FieldSize;
		
		GameSpeed 		= 5;
		MenuItems[2]	= "Speed: " + --GameSpeed;

	}

	// Automatically start thread for game loop
	public void start()
	{
		isPlay = true;
		Thread t = new Thread(this);
		t.start();
	}
	
	public void stop() { isPlay = false; }
	
	
	public void Pause(boolean paused)
	{
		isPaused = paused;
	}
	
	// Main Game Loop
	public void run()
	{
		Graphics g = getGraphics();
		while (isPlay == true)
		{
			if(!isPaused)
				switch(GameState)
				{
				case GS_MENU:	HandleMenu(g); break;
				case GS_GAME:	HandleGame(g); break;
				default:		GameState = GS_MENU;
				}
			
		}
	}
	
	
	public void HandleMenu(Graphics g)
	{
		g.setColor( 0x00ff00 );
		g.fillRect(0, 0, ScreenSizeX, ScreenSizeY);
		
		g.setColor( 0x000000 );
		
		g.setFont(FONT_HIGHT);
		g.drawString("Snakey", (ScreenSizeX - FONT_HIGHT.stringWidth("Snakey")) / 2, 20, Graphics.TOP | Graphics.LEFT );

		g.setFont(FONT_LOW);
		for(int i = 0; i < MenuItems.length; ++i)
		{
			if(i == ItemSelect)
				g.setColor( 0x0000ff );
			
			g.drawString(MenuItems[i], (ScreenSizeX - FONT_HIGHT.stringWidth(MenuItems[i])) / 2, 38 + 12 * i, Graphics.TOP | Graphics.LEFT);
			
			if(i == ItemSelect)
				g.setColor( 0x000000 );
		}
		
		flushGraphics();
		
		
		
		
		int keyStates = getKeyStates();
		
		
		if((keyStates & UP_PRESSED) != 0 && ItemSelect > 0)
			--ItemSelect;
		
		if((keyStates & DOWN_PRESSED) != 0 && ItemSelect < MenuItems.length - 1)
			++ItemSelect;
		
		
		
		if((keyStates & LEFT_PRESSED) != 0)
			if(ItemSelect == 1 && FieldSize > 10)
				MenuItems[1]	= "Field: " + --FieldSize + "x" + FieldSize;
			else
				if(ItemSelect == 2 && GameSpeed > 1)
					MenuItems[2]	= "Speed: " + --GameSpeed;
					
		
		if((keyStates & RIGHT_PRESSED) != 0)
			if(ItemSelect == 1 && FieldSize < (Math.min(ScreenSizeX, ScreenSizeY) / 5))
				MenuItems[1]	= "Field: " + ++FieldSize + "x" + FieldSize;
			else
				if(ItemSelect == 2 && GameSpeed < 10)
					MenuItems[2]	= "Speed: " + ++GameSpeed;
		
		
		
		
		if((keyStates & FIRE_PRESSED) != 0)
			switch(ItemSelect)
			{
				default:
					break;
				
				case 0:
					InitGame();
					break;
				case 3:
					Midlet.destroyApp(false);
					break;
			}
		
		try { Thread.sleep(MENU_DELAY); }
		catch (InterruptedException ie) {}
	}
	
	
	
	
	public void CreateBox()
	{
		int y, x;
		
		do{
			x = (int)(randGenerator.nextInt(FieldSize - 2));
			y = (int)(randGenerator.nextInt(FieldSize - 2));
			
		}while(GameField[x][y] != FE_NONE);
		
		
		BoxX = x;
		BoxY = y;
		GameField[x][y] = FE_BOX;
	}
	
	
	public void InitGame()
	{
		TimeSteps		= (11 - GameSpeed) * 2;
		NextStep		= TimeSteps;
	
		IsAlive			= true;
		SnakePush 		= 3;
		SnakeElements	= new Vector();
		SnakeElements.addElement( new Point(FieldSize / 2, FieldSize / 2) );
		Direction		= 0;
		DirectionChanged= false;
		
		OffsetX			= (ScreenSizeX - FieldSize * 5) / 2;
		OffsetY			= (ScreenSizeY - FieldSize * 5) / 2;
		
		FieldImage		= Image.createImage(FieldSize * 5 + 1, FieldSize * 5 + 1);
		Graphics ig		= FieldImage.getGraphics();
		
		GameField = new int[FieldSize][FieldSize];
		
		for(int i = 0; i < FieldSize; ++i)
			for(int j = 0; j < FieldSize; ++j)
			{
				if(i == 0 || i == FieldSize - 1 || j == 0 || j == FieldSize - 1)
				{
					GameField[i][j] = FE_WALL;
					
					ig.drawRect(i * 5,		j * 5, 		5, 5);
					ig.drawRect(i * 5 + 2,	j * 5 + 2, 	1, 1);
				}else
					GameField[i][j] = FE_NONE;
			}
		
		CreateBox();
		
		GameState = GS_GAME;
	}
	
	
	
	public void HandleGame(Graphics g)
	{
		
		int keyStates = getKeyStates();
		
		if(!DirectionChanged)
		{
			DirectionChanged = true;
			
			
			if((keyStates & UP_PRESSED) != 0 && Direction != SD_DOWN)
				Direction = SD_UP;
			else if((keyStates & DOWN_PRESSED) != 0 && Direction != SD_UP)
					Direction = SD_DOWN;
				else if((keyStates & LEFT_PRESSED) != 0 && Direction != SD_RIGHT)
						Direction = SD_LEFT;
					else if((keyStates & RIGHT_PRESSED) != 0 && Direction != SD_LEFT)
							Direction = SD_RIGHT;
						else
							DirectionChanged = false;
		}
		
		
		
		if(IsAlive && NextStep == 0)
		{
			DirectionChanged = false;
			
			
			Point f 	= (Point)SnakeElements.firstElement();
			Point fnew	= new Point(f.getX(), f.getY());
			switch(Direction)
			{
			case SD_UP:		fnew.setY(f.getY() - 1); break;
			case SD_RIGHT:	fnew.setX(f.getX() + 1); break;
			case SD_DOWN:	fnew.setY(f.getY() + 1); break;
			case SD_LEFT:	fnew.setX(f.getX() - 1); break;
			}
			
			if(GameField[(int) fnew.getX()][(int) fnew.getY()] != FE_NONE)
			{
				if(GameField[(int) fnew.getX()][(int) fnew.getY()] != FE_BOX)
				{
					IsAlive = false;
					return;
				}
				GameField[(int) fnew.getX()][(int) fnew.getY()] = FE_SNAKE;
				CreateBox();
				++SnakePush;
			}
			
			GameField[(int) fnew.getX()][(int) fnew.getY()] = FE_SNAKE;
			SnakeElements.insertElementAt(fnew, 0);
			
			if(SnakePush == 0)
			{
				Point l = (Point)SnakeElements.lastElement();
				GameField[(int) l.getX()][(int) l.getY()] = FE_NONE;
				
				SnakeElements.removeElementAt(SnakeElements.size() - 1);
			}else
				--SnakePush;
			
			NextStep = TimeSteps;
		}else
			--NextStep;
		
		
		
		// Render
		g.setColor( 0x00ff00 );
		g.fillRect(0, 0, ScreenSizeX, ScreenSizeY);
		
		g.drawImage(FieldImage, OffsetX, OffsetY, Graphics.TOP | Graphics.LEFT );
		
		if(IsAlive)
			g.setColor( 0x0000ff );
		else
			g.setColor( 0xff0000 );
		
		for(int i = 0; i < SnakeElements.size(); ++i)
		{
			Point p = (Point)SnakeElements.elementAt(i);
			g.drawRect(OffsetX + (int)p.getX() * 5,OffsetY + (int)p.getY() * 5, 5, 5);
		}
		
		if(!IsAlive)
		{
			g.setFont(FONT_HIGHT);
			g.drawString("Looser !!!", 0, 0, Graphics.TOP | Graphics.LEFT);
		}
		
		g.setColor( 0x00aa00 );
		g.drawRect(OffsetX + BoxX * 5,OffsetY + BoxY * 5, 5, 5);
		
			
		
		flushGraphics();
		
		try { Thread.sleep(GAME_DELAY); }
		catch (InterruptedException ie) {}
	}
}



















