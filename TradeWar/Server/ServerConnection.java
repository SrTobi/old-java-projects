package Server;

 

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import Misc.SendAttack;
import Misc.SendWar;

public class ServerConnection extends Thread
	implements Runnable
{

	ServerMain 		MS;
	
	ServerSocket 	Server;
	final int 		PORT = 2262;
	Vector<Spieler>	User;
	
	int				NextId;
	InetAddress 	Adressen;
	
	public ServerConnection(ServerMain ms)
	{
		MS = ms;
		NextId = 0;
		ms.Say("Versuche Server zu erstellen...");
		
		
	    try{
            Server = new ServerSocket(PORT);
        } catch (IOException e)
        {
            ms.Say("Server konnte nicht erstellt werden !!!");
            ms.Say("Fehler:" + e);
            return;
        }
		
		try {
			Adressen = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			ms.Say("Fehler beim finden der Server-IP !!!");
			return;
		}
		
		ms.Say("Server erstellt !");
		ms.Say("IP des Servers: " + Adressen.getHostAddress());
        ms.Say("Name des Servers: " + Adressen.getHostName());
        ms.Say("");
        ms.Say("Warte auf Spieler...");
        
        User = new Vector<Spieler>();
        
        this.start();
		
	}
	
	public void run()
	{
		while(true)
		{
			try 
			{
				Socket NewClient = Server.accept();
				MS.Say("Neue Verbindung ( " + User.size() + " )...");
				Spieler s = new Spieler(MS, this, NewClient, NextId );
				if(s.WaitForReg(Adressen.getHostName()))
				{
					NextId++;
					User.addElement( s );
					SendAll("*" + s.Name + " betritt die Lobby");
				}else{
					MS.Say("Fehler beim registrieren !!!");
				}
				
			}catch (IOException e)
			{
				System.out.println("IoException: " + e);
			}
		}
	}
	
	public void SendAll(Object obj)
	{
		for(int i = 0; i < User.size(); i++ )
		{
			Spieler s = (Spieler) User.elementAt(i);
			if(s.isAlive()) {
				try {
					s.out.writeObject(obj);
				} catch (IOException e)
				{
					System.out.println("IoException: " + e);
				}
			}
		}
	}

	public void Parse(Object obj, int id, String name)
	{
		if(!MS.started && obj instanceof String)
		{
			MS.Say(name + ": " + (String)obj);
			SendAll(name + ": " + (String)obj);
		}
		
		if(MS.started && obj instanceof SendAttack)
		{
			SendAttack s = (SendAttack)obj;
			for(int i = 0; i < User.size(); i++)
			{
				Spieler   sp = (Spieler)User.elementAt(i);
				if(sp.Name.equals( s.Ziel ) )
				{
					try {
						sp.out.writeObject( s );
					} catch (IOException e) {}
				}
			}
		}
		
		if(MS.started && obj instanceof SendWar)
		{
			for(int i = 0; i < User.size(); i++)
			{
				SendWar s = (SendWar)obj;
				Spieler   sp = (Spieler)User.elementAt(i);
				if(sp.Name.equals( s.Ziel ) )
				{
					try {
						sp.out.writeObject( s );
					} catch (IOException e) {}
				}
			}
		}
		
	}	
}




























