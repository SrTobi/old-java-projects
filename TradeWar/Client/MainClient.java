package Client;

 



public class MainClient extends Thread
	implements Runnable
{
	LoginWindow 		login;
	ConnectionToServer 	s;
	Game				g;
	
	String 				Name;
	
	public MainClient()
	{
		login = new LoginWindow();
		this.start();
	}
	
	public void run()
	{
		do{
			s = login.GetCon();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {}
		}while(s == null);
	
		Name = login.InputName.getText();
		login.dispose();
		
		g = new Game(s, login.gi, Name);
	}

	
	
	public static void main ( String[] args )
	{
		new MainClient();
	}
}
