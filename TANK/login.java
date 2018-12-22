import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;





public class login {

	private static final long serialVersionUID = 1L;
	protected Color DGreen;
	
	Container con;
	
	TextField ip_text;
	TextField Name;
	TextField text;
	
	Button Start;
	
	Label ip_Label;
	Label Name_Label;
	Label TANK_Label;
	
	TextArea ta;
	
	public login(Container con)
	{
		this.con = con;
		
		con.setLayout(null); 
		DGreen = new Color( 0 , 180 , 0 );
		
		Name = new TextField();
		Name.setBounds(100, 100, 100, 23);
		
		text = new TextField();
		text.setBounds(20, 410, 500, 23);
		text.disable();
		
		TANK_Label = new Label("TANK");
		TANK_Label.setBackground( DGreen );
		TANK_Label.setBounds(240, 5, 300, 100);
		TANK_Label.setFont( new Font(null, 0, 100) );
		
		
		Name_Label = new Label("Name:");
		Name_Label.setBounds(20, 100 , 80, 23);
		Name_Label.setBackground( DGreen);
		
		ip_text = new TextField();
		ip_text.setBounds(100, 150, 165, 23);
		
		ip_Label = new Label("IP-Aresse:");
		ip_Label.setBounds(20, 150 , 80, 23);
		ip_Label.setBackground( DGreen);
		
		Start = new Button("Start");
		Start.setBounds(680, 420, 100, 30);

		
		ta = new TextArea();
		ta.setBounds(20, 270, 500, 130);
		ta.setEditable( false );
		ta.setBackground( Color.white);
		ta.setForeground( Color.black);
		
		init();
		say("Willkommen bei TANK");
		say("");
	}
	
	public void init()
	{
		con.add(TANK_Label);
		con.add(Name_Label);
		con.add(Name);
		con.add(ip_Label);
		con.add(ip_text);
		con.add(ta);
		con.add(Start);
		con.add(text);
		con.setBackground( DGreen );
	}
	
	public void remove()
	{
		con.remove(TANK_Label);
		con.remove(Name_Label);
		con.remove(Name);
		con.remove(ip_Label);
		con.remove(ip_text);
		con.remove(ta);
		con.remove(Start);
		con.remove(text);
		con.setBackground( Color.white );
	}
	
	public void say(String Text)
	{
		ta.append( Text + "\n" );
	}
	
	public String[] get()
	{
		String temp[] = new String[2];
		temp[0] = Name.getText();
		temp[1] = ip_text.getText();
		say("Versuche Verbindung zum Server mit IP:");
		say(temp[1] + " aufzubauen... \n");
		return temp;
	}
}
