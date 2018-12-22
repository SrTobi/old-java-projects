import javax.microedition.lcdui.game.GameCanvas;


public abstract class GameClass
 	extends GameCanvas
{
	public GameClass()
	{
		super(true);
	}

	abstract void OnPause(boolean bPaused);
	abstract void OnInit();
}
