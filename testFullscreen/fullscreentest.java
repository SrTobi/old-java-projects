import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;


public class fullscreentest implements Runnable, KeyListener 
{ 
   protected Frame frame; 
   protected GraphicsDevice graphicsdevice; 
    
   protected Thread animation; 
   int index;
   int Speed;
    
   public static void main(String[] Args) 
   { 
      fullscreentest test = new fullscreentest(); 
   } 

   fullscreentest() 
   { 
      animation = new Thread(this); 
      graphicsdevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(); 
      frame = new Frame(graphicsdevice.getDefaultConfiguration()); 
      frame.setUndecorated(true); 
      frame.setIgnoreRepaint(true); 
      frame.setResizable(false); 
      frame.addKeyListener(this); 
      graphicsdevice.setFullScreenWindow(frame); 
       
      if(graphicsdevice.isDisplayChangeSupported()) 
      { 
         try 
         { 
            graphicsdevice.setDisplayMode(new DisplayMode(1400,1000,16,DisplayMode.REFRESH_RATE_UNKNOWN)); 
         } 
         catch(Exception e) 
         { 
            System.out.println("Display mode error"); 
            System.exit(0); 
         } 
      } 
      animation.start(); 
   } 
    
   public void run() 
   { 
      Thread t = Thread.currentThread(); 
      frame.createBufferStrategy(2); 
       
      while(t == animation) 
      { 
         BufferStrategy bufferstrategy = frame.getBufferStrategy(); 
         do { 
            Graphics2D g2d = (Graphics2D) bufferstrategy.getDrawGraphics(); 
            paintFrame(g2d); 
            bufferstrategy.show(); 
            g2d.dispose(); 
         } while (bufferstrategy.contentsLost()); 
         try 
         { 
            Thread.sleep(33); 
         } 
         catch(Exception e) 
         { 
             
         }
         index = index + Speed;
      } 
   } 
    
   public void paintFrame(Graphics2D g) 
   { 
      g.setColor(Color.white);
      g.fillRect(0,0,1400,1000);
      g.setPaint(Color.BLACK); 
      g.fillRect(0,0,50 + index ,50 + index);
      g.drawString(""+Speed,200 + index ,200); 
   } 
    
   public void keyPressed(KeyEvent e) 
   { 
      if(e.getKeyCode() == KeyEvent.VK_ESCAPE) 
      { 
         animation.stop(); 
         System.exit(0); 
      } 
   } 
    
   public void keyReleased(KeyEvent e) 
   { 
       Speed++;
   } 
    
   public void keyTyped(KeyEvent e) 
   { 
   } 
}
