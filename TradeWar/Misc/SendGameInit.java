package Misc;

 

import java.io.Serializable;
import java.util.Vector;

public class SendGameInit implements Serializable
{

	private static final long serialVersionUID = -3535393809725015106L;

	
	public int 	PlayerNum;
	public Vector<String> 	player;
	
	public SendGameInit()
	{
		player = new Vector<String>();
		PlayerNum = 0;
	}
	
	public void Add(String name)
	{
		player.addElement(name);
		PlayerNum++;
	}
}
