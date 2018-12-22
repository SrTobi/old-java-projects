import java.net.*;
import java.io.*;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class chatapplet2 extends Applet 
    implements Runnable , MouseListener, MouseMotionListener
{
public static final int PORT = 8273;
Socket socket;
DataInputStream in;
PrintStream out;
TextField inputfield , name;
TextArea outputarea;
Thread thread;
boolean warte = true;
String Name;
String Gegner = "";
int Feld[][];
Image Anzeige;
Graphics an;
final int kreise = 50;
int x,y;
boolean turn = false;

    public void init()
    {
        inputfield = new TextField();
        inputfield.setEditable(false);
        name = new TextField();
        outputarea = new TextArea();
        outputarea.setFont( new Font("Dialog", Font.BOLD, 15));
        outputarea.setEditable(false);
        this.setLayout(new BorderLayout());
        this.add("North", name);
        this.add("East", outputarea );
        this.add("South", inputfield);
        this.setBackground(Color.blue);
        inputfield.setBackground(new Color(150,150,255));
        outputarea.setBackground(new Color(150,150,255));
        name.setBackground(new Color(150,150,255));
        
        Anzeige = createImage(351,370);
        an = Anzeige.getGraphics();
        Feld = new int[7][5];
        
        addMouseListener( this );
        addMouseMotionListener( this );
    }
    
    public void start(){
        if (thread == null)
        {
            thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
    }
    public void Start()
    {
        
        try
        {
            socket = new Socket(this.getCodeBase().getHost(), PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
        } catch (IOException e)
        {
            this.showStatus(e.toString());
            say("Verbindung zum Server fehlgeschlagen!");
            stop();
            System.exit(1);
        }
    
        say("Verbindung zum Server aufgenommen...");
        say("Bitte warten ...");
    
    }
    
    public void stop()
    {
        try
        {
            socket.close();
        } catch (IOException e)
        {
            this.showStatus(e.toString());
        }
    
        if ((thread !=null) && thread.isAlive())
        {
            thread.stop();
            thread = null;
        }
    }
    
    public void run()
    {
        while(warte){
        }
        name.setEditable(false);
        inputfield.setEditable(true);
        Start();
        get_Name();
        
        
        String line;
    
        try
        {
            while(true)
            {
                line = in.readLine();
                if(line!=null){
                    int L = line.length();
                    if(line.indexOf("$") == 0){
                        outputarea.appendText(Gegner + " : " + line.substring( 1 , L ) + '\n' );
                    }
                }
            }
        } catch (IOException e) { say("Verbindung zum Server abgebrochen"); }
    }
    
    public boolean action(Event e, Object what)
    {
        if (e.target==name)
        {
            String Name=(String) e.arg;
            if(Name.length() > 2){
                this.Name = Name;
                warte = false;
                say("Willkommen :" + this.Name);
            }else{
                say("Mindestens 3 Zeichen");
            }
            return true;
        }else{
            if (e.target==inputfield)
            {
                String inp=(String) e.arg;
                outputarea.appendText(Name + " : " + inp + "\n");
                out.println("$" + inp);
                inputfield.setText("");
                return true;
            }
        }
        return false;
    }
    
    public void say(String msg)
    {
        outputarea.appendText("*** "+msg+" ***\n");
    }
    
    public void get_Name(){
        out.println(Name);
        String line = "";
        try{
            while(Gegner == ""){
                line = in.readLine();
                if(line.length() > 3){
                    Gegner = line.substring(1,line.length());
                    if(line.indexOf("#") == 0){
                        turn = true;
                    }
                }
            }
        } catch (IOException e) { say("Verbindung zum Server abgebrochen");stop(); }
        say("Ihr Gegner ist: "+Gegner);
    }
    
    public void update( Graphics g ) {
        an.setColor(new Color(0,0,100));
        an.fillRect(0,100,351,370);
        an.setColor(Color.blue);
        an.fillRect(0,0,351,100);
        for(int y = 0; y != 5; y++){
            for(int x = 0; x != 7 ; x++){
                if(Feld[x][y] == 1)
                    an.setColor(Color.yellow);
                if(Feld[x][y] == 2)
                    an.setColor(Color.red);
                an.fillOval(x * kreise , y * kreise + 101, kreise ,kreise);
            }
        }
        if(turn){
            int x = (this.x - 110) / kreise * kreise + kreise / 2 - 10;
            int X[] = { x , x + 20 , x + 20 , x + 30 , x + 10 , x - 10 , x};
            int Y[] = { 10 , 10 , 50 , 50 , 70 , 50 , 50};
            an.setColor(Color.black);
            an.fillPolygon( X , Y , 7);
        }
        
        g.drawImage( Anzeige, 110 ,23, this );
    }

    public void paint( Graphics g ) {
        update( g );
    }
    
    public void mouseEntered( MouseEvent e ) { }
    public void mouseExited( MouseEvent e ) { }
    public void mouseClicked( MouseEvent e ) { }
    public void mousePressed( MouseEvent e ) {
        check(e.getX(),e.getY());
        e.consume();
    }
    
    public void mouseReleased( MouseEvent e ) { }
    public void mouseMoved( MouseEvent e ) {
        x = e.getX();
        y = e.getY();
        repaint();
        e.consume();
    }
    public void mouseDragged( MouseEvent e ) {}
    
    public void check( int X ,int Y){
        say(X + "-" + Y);
        if(X > 109 ) {  
            int x = (X - 110) / kreise;
            int y = 0;
            for(y = 0 ; Feld[x][y + 1] == 0 ; y++){}
            say(x + "," + y);
            Feld[x][y] = 1;
        }else{
            
            say("nein");
        }
    }//&& this.x < 472 )      //&& this.y > 110 && this.y < 480
}

