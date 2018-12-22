package Misc;

 

import java.io.Serializable;

public class SendAttack implements Serializable
{

	private static final long serialVersionUID = 415775188771779964L;

	public String 	Name;
	public String 	Ziel;
	public int		Units[];
	public int 		Geld;
	
	public SendAttack(String name, String ziel, int units[], int money )
	{
		Name 	= name;
		Ziel 	= ziel;
		Units 	= units;
		Geld	= money;
	}
}
