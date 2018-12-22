import java.awt.*;
import java.applet.Applet;
import java.lang.Object;
import java.lang.Integer;
import java.util.*;
import java.awt.event.*;

public class Drehen extends Applet implements Runnable
{
Thread myThread;

Image Anzeige;
Graphics an;

int mult = 50;
int a = 250;

boolean sch = false;

    public void init()
    {
        Anzeige = createImage(500,500);
        an = Anzeige.getGraphics();
    }

    public void start()
    {
        myThread = new Thread(this);
        myThread.start();
    }

    public void run(){
        int G = 0;
        int g;
        int g2;
        while(true){
            g = sin(G);
            g2 = sin(G)* 2;
            
            an.setColor(Color.white);
            an.fillRect(0,0,500,500);
            
            an.setColor(Color.black);
            poly(0,0,g,2,g2,1);
            poly(0,0,g2,-1,g,-2);
            poly(0,0,-g,-2,-g2,-1);
            poly(0,0,-g2,1,-g,2);
            
            an.setColor(Color.red);
            poly(0,0,g,2,-g,2);
            poly(0,0,g,-2,-g,-2);
            poly(0,0,g2,1,g2,-1);
            poly(0,0,-g2,1,-g2,-1);
            
            an.setColor(Color.black);
            int x[] = { a +   g, a +   -g, a +   -g2, a +   -g2, a +   -g, a +   g, a +   g2, a +   g2};
            int y[] = { a + mult *  2, a + mult *  2, a + mult *  1, a + mult *  -1, a + mult *  -2, a + mult *  -2, a + mult *  -1, a + mult *  1};
            if(sch == false){
                an.drawPolygon(x,y,8);
            }else{
                an.fillPolygon(x,y,8);
            }
            an.drawString(""+G,10,10);
            repaint();
            
            try {Thread.sleep(3);}                      
            catch (InterruptedException e) { }
            
            G += 8;
            if(G == 1800){
                G = 0;
                if(sch == true){
                    sch = false;
                }else{
                    sch = true;
                }
            }
        }
    }
    
    public void stop(){
        myThread.stop();
    }

    public void update( Graphics g){
        g.drawImage( Anzeige ,0 ,0 , this);
    }
    
    public void paint(Graphics g)
    {
        update(g);
    }
    
    public int sin(int grad){
        return (int)(Math.sin((double)grad / 180 / Math.PI)*mult);
    }
    
    public void poly(int x1,int y1,int x2,int y2,int x3,int y3){
        int x[] = { a + x1, a + x2 , a + x3 };
        int y[] = { a + y1 * mult , a + y2 * mult , a + y3 * mult};
        an.fillPolygon(x,y,3);
    }
}
