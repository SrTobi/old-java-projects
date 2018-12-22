import javax.microedition.lcdui.Graphics;


public class Missile {
	
	private int		CurX, CurY;
	private int		StartX, StartY;
	private int		TargetX, TargetY;
	private int		MoveX, MoveY;
	
	private int		FromPlayer;
	
	public Missile(int fromPlayer, int startX, int startY, int targetX, int targetY, int speed)
	{
		FromPlayer = fromPlayer;
		
		StartX	= startX * 1000;
		StartY	= startY * 1000;
		CurX	= StartX;
		CurY	= StartY;
		TargetX	= targetX * 1000;
		TargetY = targetY * 1000;
		
		
		int x = (StartX - TargetX);
		int y = (StartY - TargetY);
		
		if(Math.abs(x) > Math.abs(y))
		{
			MoveX = (int)(( (x / (Math.abs(x)/1000) ) * speed) / 100);
			MoveY = (int)(( (y / (Math.abs(x)/1000) ) * speed) / 100);
		}else{
			MoveX = (int)(( (x / (Math.abs(y)/1000) ) * speed) / 100);
			MoveY = (int)(( (y / (Math.abs(y)/1000) ) * speed) / 100);
		}
		
	}
	
	public void Update(long frameTime)
	{
		CurX -= MoveX * frameTime;
		CurY -= MoveY * frameTime;
	}
	
	
	public void Render(Graphics g)
	{
		g.setColor(IsFromPlayer()? 0x0000FF : 0xFF0000);
		g.drawLine(StartX / 1000, StartY / 1000, CurX / 1000, CurY / 1000);
		
		if(IsFromPlayer())
			g.drawArc(TargetX / 1000 - 1, TargetY / 1000 - 1, 2, 2, 0, 360);
	}
	
	public boolean IsOnTarget()
	{
		if(IsFromPlayer())
			return (CurY < TargetY);
		else
			return (CurY > TargetY);
	}
	
	public boolean InRange(int x, int y, int space)
	{
		int t1 = (GetX() - x);
		int t2 = (GetY() - y);
		
		t1 = (t1 * t1) + (t2 * t2);
		
		return (t1 < space*space);
	}
	
	public int GetX()	{ return CurX / 1000; }
	public int GetY()	{ return CurY / 1000; }
	
	public boolean	IsFromPlayer()	{ return (FromPlayer == -1); }
	public int		GetTarget()		{ return FromPlayer; }
}
