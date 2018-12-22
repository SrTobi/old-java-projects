import java.net.*;
import java.io.*;
import java.util.*;

public class chatserver implements Runnable
{
public static final int PORT = 8765;
protected ServerSocket listen;
protected Vector connections;
Thread connect;


    public static void main(String[] args)
    {
        new chatserver();
    }
    
    public chatserver()
    {
        try{
            listen = new ServerSocket(PORT);
        } catch (IOException e)
        {
            System.err.println("Fehler beim Erzeugen der Sockets:"+e);
            System.exit(1);
        }
    
        connections = new Vector();
    
        connect = new Thread(this);
        connect.start();
    }
    
    public void run()
    {
        try
        {
            while(true)
            {
                Socket client=listen.accept();
    
                System.out.println("Neuer User - Nummer:"+connections.size());
                connection c = new connection(this, client);
                connections.addElement(c);
            }
        } catch (IOException e)
        {
            System.err.println("Fehler beim Warten auf Verbindungen:"+e);
            System.exit(1);
        }
    }

    public void broadcast(String msg)
    {
        int i;
        connection you;
    
        for (i=0; i<connections.size(); i++)
        {
                try{
                    you = (connection) connections.elementAt(i);
                    if(you.isAlive()){
                        System.out.println("Neue Nachricht:"+msg+i);
                        you.out.println(msg);
                    }
                }catch (Exception e){
                    System.err.println("Fehler:"+e);
                }
        }   
    }
}