import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;


public class Spiel extends Thread implements Runnable,
	MouseListener, MouseMotionListener
{

	TANK_Main main;
	Container con;
	SpielerInfo Spieler[];
	
	Graphics g;	
	Graphics an;
	Graphics ct;
	
	Graphics2D an_2d;
	Graphics2D ct_2d;
	
	AffineTransform at;


	
	Image Anzeige;
	Image Schatten;
	
	public Spiel(TANK_Main main, Container con, SpielerInfo[] Spieler)
	{
		this.main = main;
		this.con = con;
		this.Spieler = Spieler;
		
		Anzeige = con.createImage(800,500);
		Schatten = con.createImage(800,500);
		
		an = Anzeige.getGraphics();
		ct = Schatten.getGraphics();
		
		g = con.getGraphics();
		
		an_2d = (Graphics2D) an;
		ct_2d = (Graphics2D) ct;

		at = new AffineTransform();



		
		this.start();
		con.addMouseListener( this );
        con.addMouseMotionListener( this );
	}
	
	public void run()
	{
		while(true)
		{
			SpielerInfo s;
			for (int i = 0; i < Spieler.length; i++)
			{
				if ( main.ID != i)
				{
					s = (SpielerInfo) Spieler[i];
					s.weiter();
				}
			}
			paint();
			try {Thread.sleep(20);}
			catch (InterruptedException e) {}
		}
	}
	
	public void paint ()
	{
		buffer();
		g.drawImage(Anzeige, 0, 0,con);
	}
	
	
	private void buffer()
	{
		ct.setColor(Color.black);
		ct.fillRect(0, 0, 800, 500);
		
	}

	public void drehen(int drehung)
	{
		at.setToRotation((double)drehung * Math.PI / 180 );
		an_2d.transform(at);
	}
	
	public void mouseEntered( MouseEvent e ) { }
    public void mouseExited( MouseEvent e ) { }
    public void mouseClicked( MouseEvent e ) { }
    public void mousePressed( MouseEvent e ) { }   
    public void mouseReleased( MouseEvent e ) { }
    public void mouseMoved( MouseEvent e ) { }
    public void mouseDragged( MouseEvent e ) { }
    
}
