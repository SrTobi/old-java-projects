package Server;

 

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Spieler extends Thread
	implements Runnable
{

	Socket 				Client;
	
	ServerMain 			MainServer;
	ServerConnection	ServerCon;
	
	ObjectInputStream 	in;
	ObjectOutputStream	out;
	int 				id;
	String				Name;
	boolean				IsAlive;
	
	public Spieler( ServerMain ms, ServerConnection sc, Socket s, int id)
	{
		MainServer = ms;
		ServerCon  = sc;
		Client = s;
		this.id = id;
		
		try
        {
			out = new ObjectOutputStream(s.getOutputStream());
            in 	= new ObjectInputStream(s.getInputStream());
        } catch (IOException e)
        {
            try { 
            	s.close(); 
            } catch (IOException e2){} ;
            MainServer.Say("Fehler beim erzeugen eines Streams !");
            MainServer.Say("Fehler: " + e);
            return;
        }
	}
	
	public boolean WaitForReg(String Servername)
	{		
		try {
			Object sendObj = in.readObject();
			if(sendObj instanceof String)
			{
				MainServer.Say("Neuer Spieler ( " + sendObj + " )");
				Name = (String) sendObj;
				out.writeObject(new String(Servername));
				IsAlive = true;
				this.start();
				return true;
			}			
			
		} catch (IOException e)
		{}catch (ClassNotFoundException e) {}
		
		MainServer.Say("Fehler beim registrieren !!!");
		return false;
	}
	
	public void run()
	{
		try {
			while(true)
			{
				Object obj = in.readObject();
				if(obj == null)
					break;
				ServerCon.Parse(obj, id, Name);
			}
		} catch (IOException e) {
			System.out.println("IoException: " + e);
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found: " + e);
		}
		MainServer.Say(Name + " hat sich disconneced !!!");
		if(MainServer.started)
			ServerCon.SendAll(Name);
		else
			ServerCon.SendAll("*" + Name + " hat die Lobby verlassen!!!");
		
		for(int i = 0; i < ServerCon.User.size(); i++) {
			if(ServerCon.User.elementAt(i).id == id)
				ServerCon.User.remove(i);
		}
	}
	
}
