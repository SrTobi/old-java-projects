package Client;

 

import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import Misc.SendAktien;

public class GameWindow extends JFrame
	implements ActionListener
{
	
	private static final long serialVersionUID = -7228236547204437758L;
	private static final int[] upCousts = {0, 100000, 350000, 500000, 750000, 999999, 1000000, 1700000, 2500000, 5000000, 10000000};
	Container 	con;
	Game game;
	
	Button	ButtonBuy[];
	Button	ButtonSell[];
	Button	Upgrade;
	
	Label	Names[];
	Label	Price[];
	Label	AktienBesitz[];
	Label	AktienWert[];
	Label	Besitz[];
	
	Label	Money;
	
	int		maxBuy;
	int 	money;
	int		Werte[];
	int 	Aktien[];
	
	CheatProtection cheatP;
	
	public GameWindow(Game game, CheatProtection cp)
	{
		this.game = game;
		Werte = new int[5];
		Aktien= new int[5];
		
		money = 1000;
		maxBuy = 1;
		
		cheatP = cp;
		
		Dimension dim = getToolkit().getScreenSize();
		
		this.setTitle("TradeWar - " + game.Name);
		this.setResizable(false);
		this.setSize(800, 600);
		this.setLocation((int)(dim.width / 2) - 550 , (int)(dim.height / 2) - 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		con = this.getContentPane();
		con.setLayout(null); 
		con.setBackground( Color.PINK );
		
		ButtonBuy  	= new Button[5];
		ButtonSell 	= new Button[5];
		
		Names		= new Label[5];
		Price		= new Label[5];
		AktienBesitz= new Label[5];
		AktienWert	= new Label[5];
		Besitz		= new Label[5];
		
		Font f = new Font(null, 0, 30);
		Font f2= new Font(null, 0, 20);
		
		
		String names[] = { "Jan H Copters", "Miracle CPU´s", "Conni Chips", "Max Industries", "Tobi Coop" };
		
		for(int i = 0; i < 5;i++)
		{
			ButtonBuy[i] 	= new Button("Kaufen");
			ButtonBuy[i].setBounds(400, i * 100 + 20, 180, 80);
			ButtonBuy[i].setFont(f);
			ButtonBuy[i].addActionListener(this);
			
			ButtonSell[i]	= new Button("Verkaufen");
			ButtonSell[i].setBounds(600, i * 100 + 20, 180, 80);
			ButtonSell[i].setFont(f);
			ButtonSell[i].addActionListener(this);
			
			Names[i] = new Label(names[i]);
			Names[i].setBounds(10, 100 * i, 200, 100);
			Names[i].setFont(f);
			
			Price[i] = new Label("100");
			Price[i].setBounds(320, 20 + i * 100, 80, 40);
			Price[i].setFont(f2);	
			
			AktienBesitz[i] = new Label("0");
			AktienBesitz[i].setBounds(320, 50 + i * 100, 80, 40);
			AktienBesitz[i].setFont(f2);
			
			AktienWert[i] = new Label("Wert:");
			AktienWert[i].setBounds(220, 20 + i * 100, 100, 40);
			AktienWert[i].setFont(f2);	
			AktienWert[i].setForeground( Color.BLUE );
			
			Besitz[i] = new Label("Besitz");
			Besitz[i].setBounds(220, 50 + i * 100, 100, 40);
			Besitz[i].setFont(f2);
			Besitz[i].setForeground( Color.BLUE );

			
			con.add(ButtonBuy[i]);
			con.add(ButtonSell[i]);
			con.add(Names[i]);
			con.add(Price[i]);
			con.add(AktienBesitz[i]);
			con.add(AktienWert[i]);
			con.add(Besitz[i]);
		}
		
		Money = new Label("Geld: 1000");
		Money.setBounds(50, 510, 350, 90);
		Money.setFont(f);

		Upgrade	= new Button("1 (+ 10000)");
		Upgrade.setBounds(400, 510, 370, 40);
		Upgrade.setFont(f);
		Upgrade.addActionListener(this);
		
		con.add(Money);
		con.add(Upgrade);
		this.setVisible(true);
		
	}
	
	public void Akk(SendAktien a)
	{
		for(int i = 0; i < 5; i++)
		{
			if(Werte[i] != a.Get(i))
				if(Werte[i] > a.Get(i))
					Price[i].setForeground(Color.RED);
				else
					Price[i].setForeground(Color.GREEN);
			Werte[i] = a.Get(i);
			Price[i].setText(a.Get(i) + "");
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		for(int i = 0; i < 5; i++)
			if(e.getSource() == ButtonSell[i] && Aktien[i] > 0)
			{
				cheatP.click();
				int sold = Math.min(Aktien[i], maxBuy);
				
				money += Werte[i] * sold;
				Aktien[i] -= sold;
				Money.setText("Geld: " + money);
				AktienBesitz[i].setText("" + Aktien[i]);
			}
		
		if(money <= 0)
			return;
		
		for(int i = 0; i < 5; i++) {
			if(e.getSource() == ButtonBuy[i])
			{
				cheatP.click();
				int bought = 0;
				if(Werte[i] * maxBuy > money) {
					bought = 1;
				} else {
					bought = maxBuy;
				}
				money -= Werte[i] * bought;
				Aktien[i] += bought;
				Money.setText("Geld: " + money);
				AktienBesitz[i].setText("" + Aktien[i]);
			}
		}
		
		if( e.getSource() == Upgrade && money >= upCousts[maxBuy] )
		{
			cheatP.click();
			
			money -= upCousts[maxBuy];
			++maxBuy;
			
			if(maxBuy >= upCousts.length) {
				Upgrade.setLabel("" + maxBuy);
				Upgrade.setEnabled(false);
			} else {
				Upgrade.setLabel(maxBuy + " (+ " + upCousts[maxBuy] + ")");
			}
			Money.setText("Geld: " + money);
		}
	}
}



















