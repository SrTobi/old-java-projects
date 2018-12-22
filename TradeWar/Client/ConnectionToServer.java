package Client;

 

import java.awt.TextArea;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectionToServer
{
	final int 	PORT = 2262;
	Socket s;
	ObjectInputStream in;
	ObjectOutputStream out;
	
	String Ip;
	boolean IsCon;

	public ConnectionToServer()
	{
		Ip = null;
		s = null;
		in = null;
		out = null;
		IsCon = false;
	}
	
	public boolean Connect(String ip, String Name, TextArea output)
	{
		output.append("Verbinde zu Server mit der IP:\n" + ip + "\n");
		
		try {
			s = new Socket(ip,PORT);
            out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			
		} catch (UnknownHostException e) {
			output.append("Server wurde nicht gefunden !!!\nError: " + e + "\n");
			return false;
		} catch (IOException e) {
			output.append("Es konnte keine Verbindung\nhergestellt werden !!!\nError: " + e + "\n");
			return false;
		}
		output.append("Verbindung hergestellt !!!\n");
		output.append("Registriere bei Server...\n");
		
		Send(Name);
		Object obj= null;
		obj = Recv();
		if(obj != null)
		{
			if(obj instanceof String)
			{
				output.append("\nErfolgreich registriert !!!\nServername: <" + (String)obj + ">\n");
				IsCon = true;
				return true;
			}
		}
		output.append("\nFehler beim Registrieren !!!\n");
		
		return false;
	}
	
	public boolean IsConnected()
	{
		return IsCon;
	}
	
	public boolean Send(Object obj)
	{
		try {
			out.writeObject(obj);
			out.flush();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public Object Recv()
	{
		
		try {
			return in.readObject();
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
		return null;
	}
	
	public boolean Disconnect()
	{
		System.out.println("Disconnected...");
		if(s == null)
			return true;
		try {
			out.close();
			in.close();
			s.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}




































