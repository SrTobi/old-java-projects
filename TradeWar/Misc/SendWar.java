package Misc;

 

import java.io.Serializable;

public class SendWar implements Serializable

{

	private static final long serialVersionUID = -5563467815290749040L;
	
	public String 	Ziel;
	public String 	Name;
	public int 		Units[];
	public boolean 	Win;
	public int		Leben;
	public int		Geld;
	
	public SendWar(String ziel, String name, int units[], boolean win, int  leben, int money)
	{
		Ziel 		= ziel;
		Name 		= name;
		Units 		= units;
		Win 		= win;
		Leben		= leben;
		Geld		= money;
	}
}
