import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class Raten
	extends Applet
{
	
	private int Zahl;
	private ArrayList<String> Eingabe;
	private Random rand;
	private int Ein = 0;  
	private int Versuch = 0;
	
	public void init()
	{
		Eingabe = new ArrayList<String>();
		rand 	= new Random();
    }

    public void start()
    {
    	Eingabe.add("Wähle eine Zahl zwischen 0 und 100 aus und errate dis Zufallszahl.");
    	
    	Zahl	= rand.nextInt() % 100;
    	Zahl	= Math.abs(Zahl);
    	
    	Versuch = 0;
    	
    	repaint();  
    }
    
	public boolean keyDown(Event e,int code)
    {
		if(code < 58 && code > 47 && Ein < 11)
		{
			Ein *= 10;   
			Ein += code - 48;
		}
		
		
		if(code == 10)
		{
			if(Ein != Zahl)
			{
				if(Ein > Zahl)
				{    
					Eingabe.add(Ein + " ist zu hoch !");
					Versuch++;
				}else{
					Eingabe.add(Ein + " ist zu niedrig !");  
					Versuch++;
				}
			}else{
				String Gramm = " Versuchen";
				
				if(Versuch == 1)
					Gramm = " Versuch";
				
				Eingabe.add("Du hast " + Ein + " mit " + Versuch + Gramm + " erraten.");
				
				while(Eingabe.size() != 1)
					Eingabe.remove(0);
				
				start();
			}
			Ein = 0;
		}
		
		repaint();
		
		return true;
    }   
    
    public void paint(Graphics g)
    {
    	int index	= Eingabe.size() - 1;
    	int Ziel	= 10;
    	int draw	= 202;
    	
    	if(Eingabe.size() < 10)
    		Ziel = Eingabe.size();
    	
    	g.setColor(Color.gray);
    	g.fillRect(0, 0, 520, 300);
    	
    	g.setColor(Color.yellow);
    	g.fillRect(20, 216, 30, 20);

    	g.setColor(Color.blue);
    	g.fillRect(20, 250, 30, 20);

    	g.setColor(Color.black);
    	g.drawString("" + Versuch, 22, 264);
    	g.drawString("" + Ein, 22, 230);

    	g.setColor(Color.red);
    	g.fillRect(10, 10, 500, 200);

    	g.setColor(Color.black);
    	while(Ziel != 0)
    	{
    		g.drawString(Eingabe.get(index) + "", 20, draw);
    		
    		Ziel--;
    		index--;
    		
    		draw -= 20;
    	}
	}
}
