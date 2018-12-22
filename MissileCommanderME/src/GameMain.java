import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;



public class GameMain extends GameClass
	implements Runnable
{
	AppMain	AppMainClass;
	
	
	static final Font FONT_HIGHT = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD,	Font.SIZE_LARGE);
	static final Font FONT_LOW	 = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN,	Font.SIZE_MEDIUM);
	
	
	final static int S_START_SCREEN = 1;
	final static int S_GAME			= 2;
	final static int S_STATISTIC	= 3;
	final static int S_GAMEOVER		= 2;
	
	final static int CITY_NUM		= 6;
	final static int SILO_NUM		= 3;
	
	final static int POINTS_CITY			= 150;
	final static int POINTS_MISSILE			= 100;
	final static int POINTS_LEFT_MISSILE	= 15;
	final static int POINTS_NEXT_CITY		= 15000;
	
	private Thread		GameThread;
	private boolean		IsPaused;
	
	
	private int			CurState;

	private int 		ScreenX, ScreenY;
	private boolean		IsPlay;
	
	
	private int			MissileSilos[];
	private boolean 	City[];
	private int			CityLeft;
	
	
	private int			PlayerPoints;
	
	private int			CurLevel;
	private int			MissileCount;
	private int			MissilesLeft;
	private long		NextMissile;
	
	private int			NewCityStack;
	private int			NextNewCity;
	private int			PreLevelPoints;
	
	private Vector		Missiles;
	private	Vector		Explusions;
	
	private String		NewCityString;
	
	
	// Standard Background
	final static int	StdBGSize = 30;
	private Image		StdBackground;
	private Graphics	StdBGG;
	private Image		RenderTarget;
	private Graphics	RTG;


	
	
	private long		StartCountDown;
	private	long		StartTimeCount;
	
	private long		StartFrameTime;
	private long		FrameTime;
	
	
	
	
	private Random randGenerator;
	
	
	

	public GameMain(AppMain appClass)
	{		
		AppMainClass = appClass;
		

		this.setFullScreenMode(true);
		
		ScreenX = 320;
		ScreenY = 240;
		
		randGenerator = new Random();
		randGenerator.setSeed(System.currentTimeMillis());
		
		
		MissileSilos	= new int[SILO_NUM];
		City			= new boolean[CITY_NUM];
		
		StdBackground	= Image.createImage( ScreenX, StdBGSize);
		StdBGG			= StdBackground.getGraphics();
		
		RenderTarget	= Image.createImage( ScreenX, ScreenY);
		RTG				= RenderTarget.getGraphics();
		
		Missiles		= new Vector();
		Explusions		= new Vector();
		
		CurLevel		= 0;
		PlayerPoints	= 0;
		
		StartCountDown	= 0;
		StartTimeCount	= 0;
		
		StartFrameTime	= 0;
		FrameTime		= 0;
		
		CityLeft		= CITY_NUM;
		
		for(int i = 0; i < CITY_NUM; ++i)
			City[i] = true;
		
		
		NewCityStack	= 0;
		PreLevelPoints	= 0;
		NextNewCity		= 0;
		InitLevel();
		
	}
	
	
	public void OnInit()
	{
		IsPlay		= true;
		IsPaused	= false;
		GameThread	= new Thread(this);
		GameThread.start();
	}
	
	
	// Main Game Loop
	public void run()
	{
		Graphics g = getGraphics();
		while (IsPlay)
		{
			if(!IsPaused)
			{
				StartFrameTime = System.currentTimeMillis();
			
				RTG.setColor(0, 0, 0);
				RTG.fillRect(0, 0, ScreenX, ScreenY);
				RTG.drawImage(StdBackground, 0, ScreenY - StdBGSize, Graphics.TOP | Graphics.LEFT);
				
				switch(CurState)
				{
				default:
				case S_START_SCREEN:	HandleStartScreen(RTG); break;
				case S_GAME:			HandleGame(RTG); break;
				case S_STATISTIC:		HandleStatistic(RTG); break;
				}
				
				Render(g);
				FrameTime = System.currentTimeMillis() - StartFrameTime;
			}
		}
	}
	
	
	private void InitLevel()
	{
		++CurLevel;
		CurState	= S_START_SCREEN;
		
		
		for(int i = 0; i < SILO_NUM; ++i)
			MissileSilos[i] = 8 + 2 * CurLevel;
		
		MissilesLeft	= CurLevel + randGenerator.nextInt(CurLevel * 3);
		
		NextMissile		= randGenerator.nextInt(5000) + 300;
		MissileCount	= 0;
		
		Missiles.removeAllElements();
		
		RefreshStdBackground();
		
		StartCountDown	= 0;
		StartTimeCount	= 0;
		PreLevelPoints	= PlayerPoints;
		
	}
	
	
	private void RefreshStdBackground()
	{
		StdBGG.setColor( 0, 0, 0);
		StdBGG.fillRect( 0, 0, ScreenX, StdBGSize);
		
		// Hills
		StdBGG.setColor(255, 255, 0);
		StdBGG.fillRect(0, StdBGSize - 20, ScreenX, 20);
		
		int hillSize	= 50;
		int hillUp		= 10;
		
		for(int x = 0; x < ScreenX; x += ((ScreenX - 150) / 2) + 50 )
		{
			StdBGG.fillTriangle(x, 			StdBGSize - 20, x + hillUp,				StdBGSize - 30, x + hillSize, StdBGSize - 20);
			StdBGG.fillTriangle(x + hillUp, StdBGSize - 30, x + hillSize - hillUp,	StdBGSize - 30, x + hillSize, StdBGSize - 20);
		}
		
		StdBGG.setColor( 0, 0, 255);
		
		for(int i = 0; i < CITY_NUM; ++i)
		{
			if(City[i])
			{
				int x = 24 * i + 58 + ((i >= ( CITY_NUM / 2))? (65) : (0));
				StdBGG.fillRect(x		, StdBGSize - 22,	20, 3);
				
				StdBGG.fillRect(x		, StdBGSize - 24,	2,	5);
				StdBGG.fillRect(x + 2	, StdBGSize - 22,	2,	3);
				StdBGG.fillRect(x + 4	, StdBGSize - 26,	2,	7);
				StdBGG.fillRect(x + 6	, StdBGSize - 28,	2,	9);
				StdBGG.fillRect(x + 8	, StdBGSize - 22,	2,	3);
				StdBGG.fillRect(x + 10	, StdBGSize - 28,	2,	9);
				StdBGG.fillRect(x + 12	, StdBGSize - 24,	2,	5);
				StdBGG.fillRect(x + 14	, StdBGSize - 26,	2,	7);
				StdBGG.fillRect(x + 16	, StdBGSize - 22,	2,	3);
				StdBGG.fillRect(x + 18	, StdBGSize - 24,	2,	5);
			}
		}
	}
	
	
	public void HandleStartScreen(Graphics g)
	{
		g.setColor(0, 0, 255);
		g.setFont(FONT_HIGHT);
		String s = "Achtung " + (3 - StartCountDown);
		
		g.drawString(	s,
						(int)((ScreenX - FONT_HIGHT.stringWidth(s)) / 2),
						(int)(ScreenY / 2),
						Graphics.LEFT | Graphics.BOTTOM);
		
		if(StartTimeCount > 1000)
		{
			StartTimeCount = 0;
			++StartCountDown;
			
			if(StartCountDown == 3)
			{
				CurState = S_GAME;
			}
		}else
			StartTimeCount += FrameTime;
	}
	
	
	
	public void HandleGame(Graphics g)
	{
		if(NextMissile < 0 && MissilesLeft > 0)
		{
			--MissilesLeft;
			NextMissile = randGenerator.nextInt(5000 - ((CurLevel < 20)? CurLevel * 200 : 4000)) + 300;
			
			int i = 0;
			do{
				i = randGenerator.nextInt(CITY_NUM);
			}while(!City[i]);


			Missiles.addElement( new Missile(	i,
												randGenerator.nextInt(ScreenX),
												0,
												24 * i + 68 + ((i >= ( CITY_NUM / 2))? (65) : (0)),
												ScreenY - 30,
												CurLevel + 2) );
		}else
			NextMissile -= FrameTime;
		
		
		
		for(int i = 0; i < Missiles.size(); ++i)
		{
			Missile m = (Missile)Missiles.elementAt(i);
			m.Update(FrameTime);
			
			if(m.IsOnTarget())
			{
				Explusions.addElement( new Explusion( m.GetX(), m.GetY(), 50) );
				
				
				if(!m.IsFromPlayer())
				{
					City[m.GetTarget()] = false;
					--CityLeft;
					if(CityLeft == 0)
						CurState = S_GAMEOVER;
					RefreshStdBackground();
				}
				
				
				m = null;
				Missiles.removeElementAt(i);
			}else{
				m.Render(g);
			}
		}
		
		for(int i = 0; i < Explusions.size(); ++i)
		{
			Explusion ex = (Explusion)Explusions.elementAt(i);
			
			ex.Update(FrameTime);
			
			
			if(ex.IsAway())
			{
				ex = null;
				Explusions.removeElementAt(i);
			}else{
				
				for(int j = 0; j < Missiles.size(); ++j)
				{
					Missile m = (Missile)Missiles.elementAt(j);
					if(m.IsFromPlayer())
						continue;
					
					if(ex.Collides(m))
					{
						PlayerPoints += POINTS_MISSILE;
						++MissileCount;
						Explusions.addElement( new Explusion( m.GetX(), m.GetY(), 60) );
						Missiles.removeElementAt(j);
					}
				}
				
				ex.Render(g);
			}
		}
		
		if(	Missiles.size() == 0 &&
				Explusions.size() == 0 &&
				MissilesLeft == 0)
		{
			CurState = S_STATISTIC;
			
			int rest = 0;
			for(int i = 0; i < SILO_NUM; ++i)
				rest += MissileSilos[i];
			
			PlayerPoints += CityLeft * POINTS_CITY * (CurLevel + 1) / 2 + rest;
			
			int t = NewCityStack;
			NextNewCity += PlayerPoints - PreLevelPoints;
			
			NewCityStack += (int)(NextNewCity / POINTS_NEXT_CITY);
			NextNewCity	= NextNewCity % POINTS_NEXT_CITY;
			
			if(NewCityStack > 0)
			{
				NewCityString =	"+" + (NewCityStack - t) + " (" + NewCityStack + ")";
			}else
				NewCityString = "";
			
			for(int i = 0; i < CITY_NUM && NewCityStack > 0; ++i)
			{
				if(!City[i])
				{
					City[i] = true;
					--NewCityStack;
					++CityLeft;
				}
			}
			RefreshStdBackground();
		}
	}
	
	
	public void HandleStatistic(Graphics g)
	{
		g.setColor(255, 255, 0);
		g.fillRect(30, 30, ScreenX - 60, ScreenY - 80);
		
		g.setColor(0x0000FF);
		g.setFont(FONT_HIGHT);
		
		g.drawString("Level "+ CurLevel, 40, 40, Graphics.TOP | Graphics.LEFT);
		g.drawLine(40, 42 + FONT_HIGHT.getHeight(), ScreenX - 70, 42 + FONT_HIGHT.getHeight());
		
		g.setFont(FONT_LOW);
		
		g.drawString("Treffer:", 40, 42 + FONT_HIGHT.getHeight() + FONT_LOW.getHeight(), Graphics.TOP | Graphics.LEFT);
		g.drawString(MissileCount + " (" + MissileCount * POINTS_MISSILE + ")", 180, 42 + FONT_HIGHT.getHeight() + FONT_LOW.getHeight(), Graphics.TOP | Graphics.LEFT);
		
		int rest = 0;
		for(int i = 0; i < SILO_NUM; ++i)
			rest += MissileSilos[i];
		
		g.drawString("Raketenrest:", 40, 42 + FONT_HIGHT.getHeight() + FONT_LOW.getHeight() * 2, Graphics.TOP | Graphics.LEFT);
		g.drawString(rest + " (" + (rest * POINTS_LEFT_MISSILE) + ")", 180, 42 + FONT_HIGHT.getHeight() + FONT_LOW.getHeight() * 2, Graphics.TOP | Graphics.LEFT);
	
		g.drawString("Städte:", 40, 42 + FONT_HIGHT.getHeight() + FONT_LOW.getHeight() * 3, Graphics.TOP | Graphics.LEFT);
		g.drawString(CityLeft + " (" + (CityLeft * POINTS_CITY * (CurLevel + 1) / 2) + ")", 180, 42 + FONT_HIGHT.getHeight() + FONT_LOW.getHeight() * 3, Graphics.TOP | Graphics.LEFT);
		
		g.drawString(	NewCityString,
						60,
						48 + FONT_HIGHT.getHeight() + FONT_LOW.getHeight() * 4,
						Graphics.TOP | Graphics.LEFT);
		
	}
	
	
	
	public void Render(Graphics g)
	{
		RTG.setColor(0, 0, 255);
		RTG.setFont(FONT_HIGHT);
		RTG.drawString("Punkte: " + PlayerPoints, 2, 2, Graphics.LEFT | Graphics.TOP);
		RTG.drawString("Level " + CurLevel, ScreenX - 2, 2, Graphics.RIGHT | Graphics.TOP);
	
		
		RTG.setFont(FONT_LOW);
		for(int i = 0; i < SILO_NUM; ++i)
			RTG.drawString(	"" + MissileSilos[i],
							135 * i + 25 - (FONT_LOW.stringWidth("" + MissileSilos[i])/2),
							ScreenY - 4,
							Graphics.LEFT | Graphics.BOTTOM);
		
	
		//g.drawImage(RenderTarget, 0, 0, Graphics.TOP | Graphics.LEFT);
		g.drawRegion(RenderTarget, 0, 0, ScreenX, ScreenY, Sprite.TRANS_ROT270, 0, 0, Graphics.TOP | Graphics.LEFT);

		flushGraphics();
	}
	
	
	
	

	public void OnPause(boolean paused)
	{
		IsPaused = paused;
	}
	
	public void hideNotify()
	{
		OnPause(true);
	}
		
	public void showNotify()
	{
		OnPause(false);
	}
	
	
	public void pointerPressed(int _x, int _y)
	{
		if(CurState == S_GAME)
		{
			int x = ScreenX - _y;
			int y = _x;
			
			if(y < ScreenY - 30)
			{
                int n[] = { Math.abs( 25 - x ) , Math.abs( (ScreenX / 2) - x ) , Math.abs( (ScreenX - 25) - x ) };
                int near = 0;
                if(MissileSilos[0] == 0){
                    if(MissileSilos[1] == 0){
                    	near = 2;
                    }else{
                    	near = 1;
                    }
                }
                if(n[near] > n[1] && MissileSilos[1] != 0){
                	near = 1;
                }
                if(n[near] > n[2] && MissileSilos[2] != 0){
                	near = 2;
                }
                
                if(MissileSilos[near] != 0)
                {
                	--MissileSilos[near];

                    Missiles.addElement( new Missile(-1, 25 + near * 135, ScreenY - 30, x, y, 20) );
            
                }
            }
		}else
			if(CurState == S_STATISTIC)
			{
				InitLevel();
			}
	}
}
