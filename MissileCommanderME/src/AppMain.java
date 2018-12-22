import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class AppMain
	extends MIDlet
{
	private Display		DisplayInst;
	
	private GameClass	CurrentGameClass;
	
	
	
	public AppMain()
	{
		DisplayInst = Display.getDisplay(this);
		
		
		SetGameClass( new TouchStartScreen(this) );
	}
	
	
	public void SetGameClass(GameClass newGameClass)
	{
		CurrentGameClass = newGameClass;
		CurrentGameClass.OnInit();
		
		DisplayInst.setCurrent(CurrentGameClass);
	}
	

	protected void startApp()
		throws MIDletStateChangeException
	{
		DisplayInst.setCurrent(CurrentGameClass);
		CurrentGameClass.OnPause(false);
	}
	
	
	protected void destroyApp(boolean arg0)
		throws MIDletStateChangeException
	{
		exit();
	}

	
	protected void pauseApp()
	{
		CurrentGameClass.OnPause(true);
	}
	
	public void exit()
	{
		System.gc();
		notifyDestroyed();
	}
}
