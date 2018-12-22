package Client;

import java.awt.TextArea;

public class CheatProtection extends Thread{

	private long clicks;
	private boolean running;
	private TextArea output;
	
	CheatProtection()
	{
		clicks = 0;
		running = false;
		output = null;
	}
	
	public void setOutput(TextArea out) {
		output = out;
	}
	
	void click() {
		clicks++;
	}	
	
	public void run() {
		running = true;
		while(running) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			
			if(clicks >= 25) {
				if(output != null) {
					output.append("Cheater! Beschwer dich bei tobi!");
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {}
				System.exit(0);
			}
			
			System.out.println("Clicks: " + clicks);
			
			clicks = 0;
		}
	}
}
