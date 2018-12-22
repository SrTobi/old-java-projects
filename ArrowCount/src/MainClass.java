import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class MainClass extends MIDlet
	implements CommandListener
{
	public Display display;
	
	public Form StartForm;
	public Form MainForm;
	public int	StdArrowNumber;
	public int	ArrowCount;
	public int	IncCount;
	
	static final Command OkCommand = new Command ("Weiter", Command.SCREEN, 1);
	static final Command NextCommand = new Command ("Addieren", Command.SCREEN, 1);

	public TextField	StdArrowInput;
	public StringItem	ArrowCountLable;
	public StringItem	IncCountLable;
	public TextField	NewArrowUnput;
	
	
	public MainClass()
	{
		display = Display.getDisplay (this);
		
		StartForm = new Form("ArrowCount");
		MainForm = new Form("ArrowCount");
		
		
		ArrowCount	= 0;
		IncCount	= 0;
		
		StdArrowInput	= new TextField("Standart Pfeile:", "5", 3,TextField.NUMERIC);
		
		StartForm.append(StdArrowInput);
		StartForm.addCommand(OkCommand);
		StartForm.setCommandListener(this);
		
		
		
		NewArrowUnput	= new TextField("Anzahl Pfeile:", "0", 3, TextField.NUMERIC);
		ArrowCountLable	= new StringItem("Pfeile:", "0");
		IncCountLable	= new StringItem("Durchgänge:", "0");
		
		MainForm.append(NewArrowUnput);
		MainForm.append(ArrowCountLable);
		MainForm.append(IncCountLable);
		MainForm.addCommand(NextCommand);
		MainForm.setCommandListener(this);
	}
	
	

	
	
	public void commandAction (Command c, Displayable d)
	{
		if(c == OkCommand)
		{
			try
			{
				StdArrowNumber = Integer.parseInt(StdArrowInput.getString());
			}catch(Exception e){
				StdArrowNumber= 5;
			}
			
			NewArrowUnput.setString("" + StdArrowNumber);
			display.setCurrent(MainForm);
		}
		if(c == NextCommand)
		{
			int input;
			try
			{
				input = Integer.parseInt(NewArrowUnput.getString());
			}catch(Exception e){
				input= 5;
			}
			
			ArrowCount += input;
			ArrowCountLable.setText("" + ArrowCount );
			IncCountLable.setText("" + ++IncCount );
			NewArrowUnput.setString("" + StdArrowNumber);
		}
	}
	
	
	
	

	protected void destroyApp(boolean arg0)
		throws MIDletStateChangeException
	{

	}

	protected void pauseApp()
	{

	}

	protected void startApp()
		throws MIDletStateChangeException
	{
		display.setCurrent(StartForm);
	}

}
