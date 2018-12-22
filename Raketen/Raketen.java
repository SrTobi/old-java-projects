import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.util.Random;

public class Raketen
	extends Applet implements Runnable
{
	
	private static final long serialVersionUID = 1L;
	private Thread myThread;
	private Random rand;
	private int Punkte;
	
	
	private int gx = -50;
	private int gy = -50;
	private int rx = -50;
	private int ry = -50;
	
	
	private boolean rak = false;
	private int taste = 0;
	private boolean start = false;
	private boolean win = false;
	
	private int explusion = 1;
	private int geschwindigkeit = 10;

    public void init()
    {
    	rand = new Random();
    }

    public void start()
    {
    	myThread = new Thread(this);
    	myThread.start();
    }

    public void run()
    {
    	while(true)
    	{
    		gx = rand.nextInt(170) + 10;  
    		gy = 10;
    		rx = 97;
    		ry = 185;
			win = false;
			rak = false;
			start = false;
			explusion = 1;
			
			repaint();
			
			while(!start);
			
			while(!win)
			{
				if(rak)
				{
					if(rx > 13 && taste == -1 || rx < 177 && taste == 1){ 
						rx +=taste*2;
					}
					ry--;
				}
				
				if(Check())
					win = true;
				
				if(gy > 190)
					win = true;

				repaint();

				gy += 1;
				
				try {Thread.sleep(geschwindigkeit);}
				catch (InterruptedException e) { }   
			}
			
			while(explusion != 15)
			{
				explusion++;
				repaint();
				
				try {Thread.sleep(70);}
				catch (InterruptedException e) { }
			}
			
			if(gy > 190)
				Punkte--;
			else
				Punkte++;
		}
	}
    
    
    
    public boolean keyDown(Event e, int code)
    {
    	if(code == 32 && start)
    		rak = true;

    	if(code == 10 || code == 32)
    		start = true;

    	if(code == 1006)
    		taste = -1;

    	if(code == 1007)
    		taste = 1;

    	if(code == 49)
    		geschwindigkeit = 7;

    	if(code == 50)
    		geschwindigkeit = 10;
    		
    	return true;  
}
    
    
    public boolean keyUp(Event e, int code)
    {
    	if(code == 1006 || code == 1007)
    		taste = 0;
    	return true; 
    }
    
        
    public void update(Graphics g)
    {
    	g.fillRect(0, 0, 200, 200);
     
    	g.setColor(Color.white);
    	g.drawRect(5, 5, 190,190);
    	g.fillOval(90, 186, 15, 15);
	     
    	g.setColor(Color.black);
    	g.fillRect(0, 196, 200, 100);
     
    	g.setColor(Color.white);
    	g.drawString("Punkte:" + Punkte , 20,230);
    	g.fillOval(gx, gy, 5, 5);
     
    	if(rak)
    	{
    		g.drawLine(rx, ry, rx - taste * 2, ry + 5);
    		g.setColor(Color.red);
    		g.fillOval(rx - taste * 2 - 1, ry + 5, 4 , 4);
    		g.setColor(Color.white);
    	}
     
    	if(!start)
    		g.drawString("Drücke Enter !", 55, 90);
         
    	if(win)
    	{
    		g.setColor(Color.red);
    		g.fillOval(gx - explusion + 2,gy - explusion + 2,explusion * 2 , explusion * 2);
    		if(explusion > 4)
    		{
    			g.setColor(Color.yellow);
    			g.fillOval(gx - explusion + 4,gy - explusion + 4,explusion * 2 - 4 , explusion * 2 - 4);
    		}
    	}
	}
    
    
    public void paint(Graphics g)
    {
    	update(g);
    }
    
    
    public boolean Check()
    {
    	if(rx < gx + 6 && rx > gx - 1 && ry > gy - 4 && ry < gy + 5)
    		return true;
    	
   		return false;
    }
}
