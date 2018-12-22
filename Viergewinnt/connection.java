import java.net.*;
import java.io.*;

class connection extends Thread
{
protected Socket client;
protected DataInputStream in;
protected PrintStream out;
protected Spiel server;
protected String Name = "";
protected int send;



    public connection(int send , Spiel server, Socket client)
    {
        this.send = send;
        this.server=server;
        this.client=client;
    
        try
        {
            in = new DataInputStream(client.getInputStream());
            out = new PrintStream(client.getOutputStream());
        } catch (IOException e)
        {
            try { client.close(); } catch (IOException e2) {} ;
            System.err.println("Fehler beim Erzeugen der Streams: " + e);
            return;
        }
    
        this.start();
    }
    
    public void run()
    {
        String line;
        boolean weiter = true;
        
        
        try
        {
            while(weiter)
            {
                line=in.readLine();
                if(line!=null){
                    if(Name == ""){
                        Name = line;
                    }else{
                        say(line);
                    }
                }else{
                    weiter = false;
                    this.stop();
                }
            }
        } catch (IOException e)
        {
            System.out.println("Fehler:" + e);
        }
    }
    public void Name(String s){
        while(Name == ""){}
        say(s+Name);
    }
   
    public void say(String msg){
        server.say(send,msg);
    }
}