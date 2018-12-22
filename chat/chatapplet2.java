import java.net.*;
import java.io.*;
import java.awt.*;
import java.applet.*;

public class chatapplet2 extends Applet implements Runnable
{
public static final int PORT = 8765;
Socket socket;
DataInputStream in;
PrintStream out;
TextField inputfield , name;
TextArea outputarea;
Thread thread;
boolean warte = true;
String Name;

    public void init()
    {
        inputfield = new TextField();
        inputfield.setEditable(false);
        name = new TextField(1);
        outputarea = new TextArea();
        outputarea.setFont( new Font("Dialog", Font.PLAIN, 12));
        outputarea.setEditable(false);
    
        this.setLayout(new BorderLayout());
        this.add("North", name);
        this.add("Center", outputarea);
        this.add("South", inputfield);
            this.setBackground(Color.lightGray);
        this.setForeground(Color.black);
        inputfield.setBackground(Color.lightGray);
        outputarea.setBackground(Color.lightGray);
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
            System.exit(1);
        }
    
        say("Verbindung zum Server aufgenommen...");
    
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
        inputfield.setBackground(Color.white);
        outputarea.setBackground(Color.white);
        Start();
        String line;
    
        try
        {
            while(true)
            {
                line = in.readLine();
                if(line!=null)
        outputarea.appendText(line+'\n' );
            }
        } catch (IOException e) { say("Verbindung zum Server abgebrochen"); }
    }
    
    public boolean action(Event e, Object what)
    {
        if (e.target==name)
        {
            String Name=(String) e.arg;
            this.Name = Name;
            warte = false;
            say("Willkommen :" + this.Name);
            return true;
        }else{
            if (e.target==inputfield)
            {
                String inp=(String) e.arg;
                out.println(Name + " : " + inp);
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
}

