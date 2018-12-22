import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;


public class TouchStartScreen
	extends GameClass
{
	private AppMain AppMainClass;
	
	static final Font FONT_HIGHT = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD,	Font.SIZE_LARGE);
	static final Font FONT_LOW	 = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN,	Font.SIZE_MEDIUM);
	
	private int ScreenX, ScreenY;
	
	
	public TouchStartScreen(AppMain appClass)
	{
		AppMainClass = appClass;
		
		ScreenX = getWidth();
		ScreenY = getHeight();
	}
	
	
	public void OnInit()
	{

	}
	
	
	public void pointerPressed(int x, int y)
	{
		AppMainClass.SetGameClass( new GameMain(AppMainClass) );
	}
	
	
	public void paint (Graphics g)
	{
		g.setColor(0xDDDDDD);
		g.fillRect(0, 0, ScreenX, ScreenY);
		
		g.setColor(0x000000);
		
		g.setFont(FONT_LOW);
		g.drawString("by Tobias Kahlert", ScreenX - 2, ScreenY - 2, Graphics.BOTTOM | Graphics.RIGHT);
		
		g.setFont(FONT_HIGHT);
		String s = "Touch the Screen";
		
		
		g.drawString(s, (ScreenX / 2) - (FONT_HIGHT.stringWidth(s) / 2), (ScreenY - FONT_HIGHT.getHeight()) / 2 , Graphics.TOP | Graphics.LEFT);
	}


	public void OnPause(boolean paused)
	{
		
	}
}
