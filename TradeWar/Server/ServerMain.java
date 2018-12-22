package Server;

 

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import Misc.SendGameInit;




public class ServerMain
{
	JFrame 		window;
	Button 		StartButton;
	TextArea	Output;
	
	
	// Spiel
	ServerConnection Server;
	boolean started;
	ServerGame game;
	
	// Konstruktor
	public ServerMain()
	{
		started = false;
		window = new JFrame("TradeWar - Server");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(400, 250);
		window.getContentPane().setLayout( new BorderLayout() );
		
		StartButton = new Button("Start");
		StartButton.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent e )
            {
				Start( e );
            }
        } );
		
		
		Output = new TextArea();
		Output.setBackground( Color.BLACK );
		Output.setForeground( Color.WHITE );
		Output.setEditable( false );
		
		// Buttons anbringen
		window.getContentPane().add(BorderLayout.SOUTH, StartButton);
		window.getContentPane().add(BorderLayout.CENTER, Output);
		
		window.setVisible(true);
		
		Server = new ServerConnection(this);
	}
	
	public void Start( ActionEvent e)
	{
		if(started)
			return;
		if(Server.User.size() == 0)
		{
			Say("Zu wenig Spieler !!!");
			return;
		}
		
		Say("Starte Spiel...");
		SendGameInit s = new SendGameInit();
		for(int i = 0; i < Server.User.size(); i++ )
			s.Add( ((Spieler)Server.User.elementAt(i)).Name );
		//Server.suspend();
		
		Server.SendAll(s);
		started = true;
		game = new ServerGame(Server);
		
	}
	
	public void Say(String msg)
	{
		Output.append(msg + "\n");
	}
	
	
	
	// Hauptfunktion
	public static void main( String[] args )
	{
		new ServerMain();
	}
	
}















