package Client;

 

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JSlider;

import Misc.SendAttack;
import Misc.SendGameInit;
import Misc.SendWar;



public class FightWindow extends JFrame
	implements ActionListener, Runnable
{
	private static final long serialVersionUID = 9042338554085646601L;
	
	ConnectionToServer 	Server;
	Game				game;
	GameWindow			MainWindow;
	SendGameInit		Player;
	Container			con;
	
	private static final String Names[] = { "Panzer:", "Buggies:", "Einheiten:", "Flugzeuge:", "Helis:" };
	
	Label		UnitsNames[];
	Label		Units[];
	Label		Live;
	Checkbox	Enemys[];
	Button		Buy[];
	JSlider		BuyControl;
	
	TextArea	Output;
	int			Lives;
	int			units[];
	Thread		go;
	
	CheatProtection cheatP;
	
	public FightWindow(ConnectionToServer s, Game g, GameWindow gw, SendGameInit gi, CheatProtection protection)
	{
		Server 		= s;
		game 		= g;
		MainWindow 	= gw;
		Player		= gi;
		Lives		= 3000 * (Player.PlayerNum - 1) + 2000;
		
		units		= new int[5];
		
		cheatP = protection;
		
		Dimension dim = getToolkit().getScreenSize();

		this.setTitle("TradeWar - Fight");
		this.setResizable(false);
		this.setSize( 300, 500 + 20 * (Player.PlayerNum - 1)  );
		this.setLocation((int)(dim.width / 2) + 250 , (int)(dim.height / 2) - 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		con = this.getContentPane();
		con.setLayout(null); 
		con.setBackground( Color.PINK );
		
		Enemys 		= new Checkbox[Player.PlayerNum - 1];
		Buy			= new Button[5];
		Units		= new Label[5];
		UnitsNames	= new Label[5];
		
		Font f = new Font(null, 0, 15);
		
		int p = 0;
		
		for(int i = 0; i < Player.PlayerNum; i++ )
		{
			if(!Player.player.elementAt(i).equals(game.Name))
			{
				Enemys[p] = new Checkbox( (String) Player.player.elementAt(i) );
				Enemys[p].setBounds(10, 5 + 20 * p, 300, 20);
				Enemys[p].setBackground( Color.PINK );
				Enemys[p].setForeground(Color.BLUE);
				Enemys[p].setFont(f);
				
				con.add(Enemys[p]);
				p++;
			}
			
		}
		
		int YStart = (Player.PlayerNum - 1) * 20 + 5;
		
		Live = new Label("Leben: " + Lives);
		Live.setBounds( 10, YStart, 300, 20 );
		Live.setFont(new Font(null, 0, 19));
		
		con.add(Live);
		
		Font f2 = new Font(null, 0, 17);
		
		for(int i = 0; i < 5; i++)
		{
			UnitsNames[i] = new Label(Names[i]);
			UnitsNames[i].setBounds(10, YStart + 25 + i * 20, 140, 18);
			UnitsNames[i].setFont(f2);
			
			con.add(UnitsNames[i]);
			
			Units[i] = new Label("0");
			Units[i].setBounds(150, YStart + 25 + i * 20, 80, 18);
			Units[i].setFont(f2);
			
			con.add(Units[i]);
			
			Buy[i] = new Button("Kaufen");
			Buy[i].setBounds(230, YStart + 25 + i * 20, 50, 18);
			Buy[i].addActionListener(this);
			
			con.add(Buy[i]);
		}
		
		
		
		BuyControl = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
		BuyControl.setBounds(5, YStart + 130, 280, 45);
		BuyControl.setBackground(Color.PINK);
		BuyControl.setMinorTickSpacing(1);
		BuyControl.setMajorTickSpacing(3);
		BuyControl.setPaintTicks(true);
		BuyControl.setPaintLabels(true);

		
		con.add(BuyControl);
		
		
		Output = new TextArea();
		Output.setBounds(0, YStart + 175, 294, 290);
		Output.setBackground(Color.BLACK);
		Output.setForeground(Color.RED);
		Output.setEditable(false);
		Output.append("GoGoGo !!!\n");
		
		con.add(Output);
		cheatP.setOutput(Output);
		
		go = new Thread(this);
		go.start();
		
		
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		for(int i = 0; i < 5; i++)
		{
			if(e.getSource() == Buy[i])
			{
				cheatP.click();
				if(game.gwindow.money < 2500 * BuyControl.getValue())
					BuyControl.setValue( (int)Math.floor(game.gwindow.money / ( 2500))  );
				if(game.gwindow.money >= 2500 * BuyControl.getValue())
				{
					game.gwindow.money -= 2500 * BuyControl.getValue();
					game.gwindow.Money.setText("Geld: " + game.gwindow.money);
					units[i] += BuyControl.getValue();
					Units[i].setText("" + units[i]);
				}
			}
		}
	}
	
	public void run()
	{
		while(true)
		{
			try {
				Thread.sleep( (long) ( (Math.random() * 1200 + 250) / (Player.PlayerNum - 1) ) );
			} catch (InterruptedException e) {}
			
			int p = (int) (Math.random() * (Player.PlayerNum - 1) ) ;
			
			if( Enemys[p].getState() )
			{
				
				int temp[] = { units[0],units[1], units[2],units[3],units[3], units[4] };
				
				Server.Send( new SendAttack(  game.Name, Enemys[p].getLabel(), temp, MainWindow.money ) );
			}
		}
	}

	public SendWar Atk(SendAttack attack)
	{
		int p1[]  = {	(int) Math.max(units[0] - attack.Units[4] * 0.25, 0),
						(int) Math.max(units[1] - attack.Units[0] * 0.25, 0),
						(int) Math.max(units[2] - attack.Units[1] * 0.25, 0),
						(int) Math.max(units[3] - attack.Units[2] * 0.25, 0),
						(int) Math.max(units[4] - attack.Units[3] * 0.25, 0)};
		
		
		int p2[]  = {	(int) Math.max(attack.Units[0] - units[4] * 0.25, 0),
						(int) Math.max(attack.Units[1] - units[0] * 0.25, 0),
						(int) Math.max(attack.Units[2] - units[1] * 0.25, 0),
						(int) Math.max(attack.Units[3] - units[2] * 0.25, 0),
						(int) Math.max(attack.Units[4] - units[3] * 0.25, 0)};
		int _p1	= 0;
		int _p2 = 0;
		
		for(int i = 0; i < 5;i++)
			_p1 += p1[i];
		
		for(int i = 0; i < 5;i++)
			_p2 += p2[i];
		
		double end_p1 =  (_p1 > 0)? 1 - (_p2 * 0.05 / _p1) : 0;
		double end_p2 =  (_p2 > 0)? 1 - (_p1 * 0.05 / _p2) : 0;
		
		if( end_p1 < 0)
			end_p1 = 0;
		
		if( end_p2 < 0)
			end_p2 = 0;
		
		for( int i = 0; i < 5; i++)
		{
			p1[i] = (int) Math.floor(p1[i] * end_p1);
			p2[i] = (int) Math.floor(p2[i] * end_p2);
		}
		

		int def = (int) Math.abs( _p1 - _p2);
		int loseLive = (int)Math.floor(def * 0.4);
		
		Akk( new SendWar(attack.Ziel, attack.Name, p1, (_p1 >= _p2)? true : false, loseLive, 0) );
		
		return new SendWar(attack.Name, attack.Ziel, p2, (_p1 < _p2)? true : false, loseLive, 0);
	}
	
	public void Akk(SendWar w)
	{
		if(w.Win)
		{
			Output.append("+" + w.Name +  " (" + w.Leben + ") !\n");
			MainWindow.money += w.Geld;
			Lives += w.Leben;
		}else{
			Output.append("-" + w.Name + "\n");
			Lives -= w.Leben;
			
			MainWindow.money -= w.Geld;
		}
		Live.setText("Leben: " + Lives);
		MainWindow.Money.setText("Geld: " + MainWindow.money);
		
		for(int i = 0; i < 5; i++)
		{
			units[i] = w.Units[i];
			Units[i].setText("" + w.Units[i]);
		}
		
		if(Lives < 0)
		{
			Server.Disconnect();
			Output.append("\nDu hast verloren !!!\n");
			for(int i = 10; i > 0; i--)
			{
				Output.append(i + " Sekunden...\n");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
			System.exit(0);
			
		}
	}

	public void AkkPlayer(String str)
	{
		for(int i = 0; i < Player.PlayerNum - 1; i++)
		{
			if(Enemys[i].getLabel().equals( str ))
			{
				Enemys[i].setEnabled(false);
				Enemys[i].setState(false);
				Output.append(str + " wurde vernichtet !!!\n");
			}
		}
	}
	
}


















