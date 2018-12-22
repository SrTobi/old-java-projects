package Client;

 

import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import Misc.SendGameInit;


public class LoginWindow extends JFrame
	implements Runnable
{

	
	private static final long serialVersionUID = 1L;
	
	Container 	con;
	ConnectionToServer ServerCon;

	Button 		StartButton;
	TextArea	Output;
	TextField	InputName;
	TextField	InputIp;
	TextField	InputChat;
	Label		L_Head;
	Label		L_Ip;
	Label		L_Name;
	
	boolean 		go;
	Thread  		running;
	SendGameInit	gi;
	
	public LoginWindow()
	{
		
		ServerCon = null;
		go = false;
		
		
		this.setTitle("Login");
		this.setResizable(false);
		this.setSize( 500, 300);
		this.setLocationRelativeTo( null );
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		con = this.getContentPane();
		con.setLayout(null); 
		con.setBackground( Color.PINK );
		
		StartButton = new Button("Connect");
		StartButton.setBounds(420, 220, 70, 30);
		StartButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e )
                {
                	Connect();
                }
        } );
	
		Output = new TextArea();
		Output.setBounds(30, 130, 380, 100);
		Output.setBackground( Color.white);
		Output.setForeground( Color.black);
		Output.setEditable(false);
	
		InputChat = new TextField();
		InputChat.setBounds(30, 240, 380, 20);
		InputChat.setEnabled(false);
		InputChat.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e )
                {
                	 if( ServerCon.IsConnected() )
                	 {
                		 String text = InputChat.getText();
                		 if (!text.equals(""))
                		 {
                			 ServerCon.Send( InputChat.getText() );
                		 }
                		 InputChat.setText("");
                	 }   		 
                }
        } );
		
		L_Head = new Label("TradeWar");
		L_Head.setBounds(130, 10, 300, 50);
		L_Head.setFont( new Font( null, 0, 50));
		
		L_Name = new Label("Name:");
		L_Name.setBounds(30, 70, 40, 12);
		
		L_Ip = new Label("Ip:");
		L_Ip.setBounds(30, 90, 40, 16);
		
		InputName = new TextField();
		InputName.setBounds(80, 67, 150, 20);
		
		InputIp = new TextField();
		InputIp.setBounds(80, 87, 150, 20);
		
		con.add(InputIp);
		con.add(InputName);
		con.add(L_Ip);
		con.add(L_Name);
		con.add(L_Head);
		con.add(InputChat);
		con.add(Output);
		con.add(StartButton);
		this.setVisible(true);
		
		Say("---- Wilkommen bei TradeWar ----");
	}
	
	
	public void Say( String msg)
	{
		Output.append(msg + "\n");
	}
	
	public void Connect()
	{
		if(ServerCon != null)
		{
			Say("Disconnected !!!");
			Disconnect();
			return;
		}
		InputName.setEnabled(false);
		InputIp.setEnabled(false);
		StartButton.setLabel("Disconnect");
		ServerCon = new ConnectionToServer();

		running = new Thread(this);
		running.start();
	}
	
	public void run()
	{
		if(ServerCon.Connect(InputIp.getText(), InputName.getText() , Output))
		{
			InputChat.setEnabled(true);
		}else {
			Disconnect();
			return;			
		}
		
		Object obj;
		Say("Bereit !!!");
		do
		{
			obj = null;
			obj = ServerCon.Recv();

			if(obj instanceof String)
			{
				Say((String)obj);
			}
			if(obj instanceof SendGameInit)
			{
				gi = ((SendGameInit)obj);
				Say("Starte Spiel mit " + gi.PlayerNum );
				go = true;
				return;
			}
			if(obj == null)
			{
				Disconnect();
			}
		}while(ServerCon != null);
		
		Say("Verbindung beendet !");
	}
	
	public ConnectionToServer GetCon()
	{
		if(go)
			return ServerCon;
		else
			return null;
	}
	
	
	public void Disconnect()
	{
		InputName.setEnabled(true);
		InputIp.setEnabled(true);
		InputChat.setEnabled(false);
		InputChat.setText("");
		StartButton.setLabel("Connect");
		
		if(ServerCon != null) {
			ServerCon.Disconnect();
			ServerCon = null;
		}
	}
}




























