import javax.microedition.lcdui.Graphics;


public class Explusion {

	private int X,Y;
	private int Size;
	
	
	public Explusion( int x, int y, int maxSize)
	{
		X = x;
		Y = y;
		Size = maxSize*1000;
	}
	
	public void Update(long frameTime)
	{
		Size -= frameTime*15;
	}
	
	public void Render(Graphics g)
	{
		g.setColor(0xFFFFFF);
		g.fillArc(X - Size/2000, Y - Size/2000, Size/1000, Size/1000, 0, 360);
	}
	
	public boolean IsAway()
	{
		return (Size < 0);
	}
	
	public boolean Collides(Missile m)
	{
		return m.InRange(X, Y, Size/2000);
	}
}
