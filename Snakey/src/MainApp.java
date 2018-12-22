import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;


public class MainApp extends MIDlet 
{
	private Display		display;
	private GameMain	Game;
	
	public MainApp()
	{
		display = Display.getDisplay(this);
		
		Game = new GameMain(this);
		Game.start();
		
		display.setCurrent(Game);
	}
	
	public Display getDisplay() {
		return display;
	}
	
	public void startApp()
	{
		Game.Pause(false);
	}
	
	public void pauseApp()
	{
		Game.Pause(true);
	}
	
	public void destroyApp(boolean unconditional) {
		exit();
	}
	
	public void exit() {
		System.gc();
		//destroyApp(false);
		notifyDestroyed();
	}
}