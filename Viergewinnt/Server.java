import java.net.*;
import java.io.*;
import java.util.*;

public class Server implements Runnable
{
public static final int PORT = 8273;
protected ServerSocket verbindung;
protected Vector Spiele;
Thread connect;
boolean frei;

    public static void main(){
        new Server();
    }
    public Server()
    {
        try{
            verbindung = new ServerSocket(PORT);
        } catch (IOException e)
        {
            System.err.println("Fehler beim Erzeugen der Sockets:"+e);
            System.exit(1);
        }
        
        frei = true;
        
        Spiele = new Vector();
        
        connect = new Thread(this);
        connect.start();
    }
    
    public void run()
    {
        try
        {
            while(true)
            {
                Socket client = verbindung.accept();
                
                if(frei){
                    Spiel s = new Spiel(client);
                    Spiele.addElement(s);
                    frei = false;
                }else{
                    Spiel S = (Spiel) Spiele.elementAt(Spiele.size() - 1);
                    S.Neu(client);
                    frei = true;
                }
            }
        } catch (IOException e)
        {
            System.err.println("Fehler beim Warten auf Verbindungen:"+e);
            System.exit(1);
        }
    }
}
