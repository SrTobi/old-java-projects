import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;


public class Spieler extends Thread implements Runnable{
	
	Socket client;
	MainServer Server;
	protected DataInputStream in;
	protected PrintStream out;
	
	String Name;
	int ID;
	
	
	public Spieler(Socket client,MainServer Server,int ID)
	{
		this.ID = ID;
		this.client = client;
		this.Server = Server;
		
		try
        {
            in = new DataInputStream(client.getInputStream());
            out = new PrintStream(client.getOutputStream());
        } catch (IOException e)
        {
            try { 
            	client.close(); 
            } catch (IOException e2){} ;
            Server.say("Fehler beim erzeugen eines Streams !");
            Server.say("Fehler: " + e);
            return;
        }
    
        this.start();
	}
	
	public void run ()
	{
		try
		{
			Name = "";
			String line;
			boolean weiter = true;
			while(weiter)
			{
				line = in.readLine();
				if (line != null)
				{
					if(Name == "")
					{
						if (line.indexOf("§") == 0)
						{
							Name = line.substring(1);
							send("###+++###");
							sendAll("$ ");
							sendAll("$" + Name + " hat den Server betreten !");
							sendAll("$ ");
						}
					}else{
						sendAll(line);
					}
				}else{
					this.stop();
					weiter = false;
				}
					
			}
		}catch(IOException e)
		{
			Server.say("Ein Streamfehler ist aufgetreten !");
			Server.say("Fehler: " + e);
		}
	}
	
	public void send (String msg)
	{
		out.println(msg);
	}
	
	public void sendAll(String msg)
	{
		Server.send(ID,Name,msg);
	}
}









