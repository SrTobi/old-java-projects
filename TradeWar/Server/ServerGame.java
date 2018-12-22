package Server;

 

import Misc.SendAktien;

public class ServerGame extends Thread
	implements Runnable
{
	ServerConnection s;
	int werte[];
	
	public ServerGame(ServerConnection s)
	{
		werte = new int[5];
		for(int i = 0; i < 5; i++)
			werte[i] = (int)(Math.random() % 4000 + 100);
		this.s = s;
		this.start();
	}
	
	public void run()
	{
		while(s.User.size() > 0)
		{

			try {
				Thread.sleep( (long) (Math.random() * 700 + 300));
			} catch (InterruptedException e) {}
			
			int p = (int)(Math.random() * 5);
			werte[ p ] += (int)(Math.random() * 5000 - 2500 - ((werte[p] > 5500)? 1000 : 0)    );
			
			if( werte[p] <= 0 )
				werte[p] = (int)(Math.random() * 200 + 20);
			s.SendAll(new SendAktien(werte));
		}
		
		
		s.MS.Say("Alle Spieler disconnected...");
		s.MS.Say("Beende Server...");
		for(int i = 3; i > 0; i--)
		{
			s.MS.Say(i + " Sekunden...");
			try {
				Thread.sleep( 1000 );
			} catch (InterruptedException e) {}
		}
		System.exit(0);
	}
}























