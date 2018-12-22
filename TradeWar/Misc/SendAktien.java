package Misc;

 

import java.io.Serializable;

public class SendAktien implements Serializable
{

	private static final long serialVersionUID = 1562491416074446875L;

	int werte[];
	
	public SendAktien(int w[])
	{
		werte = new int[5];
		for(int i = 0; i < 5; i++)
			werte[i] = w[i];
	}
	
	public int Get(int i)
	{
		return werte[i];
	}
}
