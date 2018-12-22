
public class SpielerInfo {

	int ID, Xpos, Ypos, grad, drehung, turm, turm_drehung, gesch, team;
	boolean tod;
	String name;
	
	public SpielerInfo (int ID, String name, int Xpos, int Ypos, int grad, int team)
	{
		this.ID = ID;
		this.name = name;
		this.Xpos = Xpos;
		this.Ypos = Ypos;
		this.grad = grad;
		this.team = team;
		turm = grad;

		drehung = 0;
		turm_drehung = 0;
		gesch = 0;
		tod = false;
	}
	
	public int[] get()
	{
		if (tod)
			return null;
		int i[] = new int[6];
		i[0] = Xpos;
		i[1] = Ypos;
		i[2] = grad;
		i[3] = drehung;
		i[4] = turm;
		i[5] = team;
		return i;	
	}
	
	public void weiter ()
	{
		if(!tod)
		{
			
		}
	}
	
}
