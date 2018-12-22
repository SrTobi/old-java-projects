import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;



public class TANK_Main implements Runnable {
	
	public final int PORT = 2276;
	JFrame fenster;
	Container Frame_Pane;
	login l;
	
	SpielerInfo Spieler[];
	
	int ID, Xpos, Ypos, grad, drehung, turm, turm_drehung, gesch, team;
	int muni , leben;
	boolean tod;
	
	boolean spiel = false;
	Spiel Spiel;
	
	String IP;
	String Name;
	
	Thread go;
	
	Socket connection;
	DataInputStream in;
	PrintStream out;
	
	public TANK_Main()
	{
		fenster = new JFrame();
		fenster.setSize(800,500);
		fenster.setResizable( false );
		fenster.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ); 
		fenster.setBackground(Color.white);
		fenster.setTitle("TANK    (by Tobias)");
		fenster.setLocationRelativeTo(null);
		fenster.setVisible(true);
		Frame_Pane = fenster.getContentPane();
		l = new login(Frame_Pane);
		l.text.addActionListener( new ActionListener() {
            /**
             * Invoked when a botton pressed
             */
            public void actionPerformed( ActionEvent e )
                {
                	 if( go.isAlive() )
                	 {
                		 String text = l.text.getText();
                		 if (!text.equals(""))
                		 {
                			 output(Name + ": " + text);
                			 send("$" + Name + ": " + text);
                			 l.text.setText("");
                		 }
                	 }   		 
                }
        } );
		l.Start.addActionListener( new ActionListener() {
            /**
             * Invoked when a botton pressed
             */
            public void actionPerformed( ActionEvent e )
                {
                	 verbinde();
                }
        } );
		go = new Thread(this);
	}
	
	public void verbinde ()
	{
		String temp[] = new String[2];
		temp = l.get();
		Name = temp[0];
		IP = temp[1];
		boolean ja = false;
		try {
			connection = new Socket(IP,PORT);
			in = new DataInputStream(connection.getInputStream());
            out = new PrintStream(connection.getOutputStream());
		} catch (UnknownHostException e) {
			fehler(e);
		} catch (IOException e) {
			fehler(e);
		}
		l.say("Der Server wurde gefunden und die Verbindung hergestellt !");
		l.say("Warte auf Autorisierung...");
		ja = true;
		if (ja)
		{
			out.println("§" + Name);
			boolean b = true;
			String line;
			while (b)
			{
				try
				{
					line = in.readLine();
					if( line.equals("###+++###"))
						b = false;
				}catch(IOException e)
				{
					fehler(e);
				}
			}
			l.say("Autorisierung erfolgreich erhallten !");
			l.say("");
			l.Start.disable();
			l.ip_text.disable();
			l.Name.disable();
			l.text.enable();
			go.start();
		}
	}
	
	public void run ()
	{
		String line;
		try
		{
			while(true)
			{	
				line = in.readLine();
				output(line);
				if(line.indexOf("~") == 0)
					Start_Spiel(line.substring(1));
				
				if(line.indexOf("$") == 0)
					output(line.substring(1));
			}
		}catch(IOException e)
		{
			l.say("Fehler:" + e);
		}
	}
	
	public void Start_Spiel (String line)
	{
		int i = line.indexOf("<>");
		int last = i + 2;
		String anzahl = line.substring(0, i);
		output("Hallo :" + i);
		Spieler = new SpielerInfo[Integer.parseInt(anzahl)];
		output(anzahl);
		int index = 0;
		String NAME;
		int info[] = new int[5];
		while ( line.indexOf("<>", i + 1) != -1 )
		{
			NAME = "";
			for(int i2 = 0; i2 < 5; )
			{
				i = line.indexOf("<>", i + 1);
				if(i2 == 1 && NAME.equals(""))
				{
					NAME = line.substring(last, i);
				}else{
					info[i2] = Integer.parseInt(line.substring(last, i));
					i2++;
				}
				last = i + 2; 
			}
			if(NAME.equals(Name))
			{
				ID = info[0];
				Xpos = info[1];
				Ypos = info[2];
				grad = info[3];
				team = info[4];
				
				drehung = 0;
				turm_drehung = 0;
				gesch = 0;
				tod = false;
			}
				Spieler[ index ] = new SpielerInfo(info[0], NAME, info[1], info[2], info[3], info[4]);

			index++;
		}
		l.remove();
		muni = 0;
		leben = 100;
		spiel = true;
		Spiel = new Spiel(this,Frame_Pane,Spieler);
	}
	
	public void output (String msg)
	{
		if(spiel)
		{
			
		}else{
			l.say(msg);
		}
	}
	
	public void send (String msg)
	{
		out.println(msg);
	}
	
	public void fehler(Exception e)
	{
		l.say("Der Versuch mit dem Server");
		l.say("verbindung auf zu nehmen ist fehlgesclagen !!!");
		l.say("Fehler: " + e + "\n");
	}
	
	public static void main (String[] args)
	{
		new TANK_Main();
	}
}
