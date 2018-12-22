package Client;

 

import Misc.SendAktien;
import Misc.SendAttack;
import Misc.SendGameInit;
import Misc.SendWar;


public class Game extends Thread
	implements Runnable
{
	ConnectionToServer 	ServerCon;
	GameWindow 			gwindow;
	FightWindow			fwindow;
	
	String 	Name;
	int		EnemyNum;
	
	
	public Game(ConnectionToServer s, SendGameInit gi, String name)
	{
		Name = name;
		ServerCon = s;
		

		CheatProtection cp = new CheatProtection();
		cp.start();
		
		gwindow = new GameWindow(this, cp);
		fwindow = new FightWindow(s, this, gwindow, gi, cp);
		EnemyNum = gi.PlayerNum - 1;
		this.start();
	}

	
	public void run()
	{
		Object obj = null;
		do
		{
			obj = ServerCon.Recv();
			
			if(obj instanceof SendAktien)
			{
				gwindow.Akk(    (SendAktien)obj    );
			}
			
			if(obj instanceof SendAttack)
			{
				ServerCon.Send(  fwindow.Atk(  (SendAttack)obj)   );
			}
			
			if(obj instanceof SendWar)
			{
				fwindow.Akk( (SendWar)obj  );
			}
			
			if(obj instanceof String)
			{
				String str = (String)obj;
				fwindow.AkkPlayer(str);
				EnemyNum--;
			}
			
			if(EnemyNum == 0)
			{
				ServerCon.Disconnect();
				fwindow.Output.append("\nDu hast gewonnen !!!\n");
				for(int i = 10; i > 0; i--)
				{
					fwindow.Output.append(i + " Sekunden...\n");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
				}
				System.exit(0);
			}
			
		}while(obj != null);
		
		fwindow.Output.append("Verbindung Verloren !!!\n");
		for(int i = 10; i > 0; i--)
		{
			fwindow.Output.append(i + " Sekunden...\n");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
		System.exit(0);
	}
}






















