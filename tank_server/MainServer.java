import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
//import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.Toolkit;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

//import javax.swing.ImageIcon;
import javax.swing.JFrame;



public class MainServer implements Runnable{
	
	JFrame fenster;
	TextArea ta;
	Button start;
	Container Frame_Pane;
	
	protected Vector User;
	Thread Lauf;
	
	protected ServerSocket server;
	protected final int PORT = 2276;
	
	public MainServer ()
	{
		/*	
		 * 	  Initalisiere das Fester
		 */

		//ImageIcon icon = new ImageIcon("TANK.ico");

		fenster = new JFrame("Tank - Server");
		fenster.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ); 
	    fenster.setSize( 500, 300 ); 
	    // fenster.setIconImage( icon.getImage() );
	    fenster.setResizable(false);  
	    
	    Frame_Pane = fenster.getContentPane();
	    
	    ta = new TextArea(13,100);
	    ta.setBackground( Color.BLACK );
        ta.setForeground( Color.white );
        ta.setFont( new Font( "Dialog", Font.PLAIN, 15 ) );
        ta.setEnabled( true );
        ta.setEditable( false );
        
        start = new Button("Start");
        //start.disable();
        start.addActionListener( new ActionListener() {
            /**
             * Invoked when a botton pressed
             */
            public void actionPerformed( ActionEvent e )
                {
            		int sp = User.size();
            		say("Starte das Spiel mit " + sp + " Spielern !!!");
            		String msg = "~" + sp + "<>";
            		Spieler s;
            		int t = 2;
            		int x = 100;
            		int y = 50;
            		int grad = 90;
            		for(int i = 0; i < sp; i++)
            		{
            			if(t == 2)
            			{
            				t = 1;
                    		x = 100;
                    		grad = 90;
            			}else{
            				t = 1;
                    		x = 4900;
                    		y += 100;
                    		grad = 270;
            			}
            			
            			s = (Spieler) User.get(i);
            			msg += s.ID + "<>" + s.Name + "<>" + x + "<>" + y + "<>" + grad +  "<>" + t + "<>";
            		}
            		send(-1,"System",msg);
                }
        } );
        
        // Füge TextArea ein
        Frame_Pane.setLayout(new BorderLayout());
        Frame_Pane.add(BorderLayout.NORTH,ta);
        Frame_Pane.add(BorderLayout.SOUTH,start);
        
        // Mache Fenster sichtbar
	    fenster.setVisible( true );
	    
	    /*
	     *   Starte Serverinitalisierung
	     */
	    
	    say("Versuche Server zu erstellen...");
	    try{
            server = new ServerSocket(PORT);
        } catch (IOException e)
        {
            say("Server konnte nicht erstellt werden !!!");
            say("Fehler:" + e);
        }
        InetAddress adressen = null;
        try {
			adressen = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			say("Fehler beim finden der Server-IP !!!");
		}
		
		/*
		 *     Andere Sachen werden initalisiert
		 */
		
		User = new Vector();
		Lauf = new Thread(this);
		Lauf.start();
		
        say("Server erstellt !");
        say("IP des Servers: " + adressen.getHostAddress());
        say("Name des Servers: " + adressen.getHostName());
        say("");
        say("Warte auf Spieler...");
	}
	
	public void run ()
	{
		while(true)
		{
			try
			{
				Socket client = server.accept();
				Spieler S = new Spieler( client ,this,User.size());
				User.addElement(S);
				
			}catch (IOException e)
			{
				
			}
		}
	}
	
	public void send(int ID,String Name,String msg)
	{
		if (msg.indexOf("$") == 0)
			say(msg.substring(1));
		for(int i = 0;i < User.size();i++)
		{
			if(i != ID)
			{
				Spieler S = (Spieler) User.get(i);
				S.send(msg);
			}
		}
	}
	
	public void say(String text)
	{
		ta.appendText(text + "\n");
	}
	
	public static void main ( String[] args )
	{
		new MainServer();
	}
}
