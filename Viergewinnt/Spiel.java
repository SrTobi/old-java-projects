import java.net.*;
import java.io.*;
import java.util.*;

public class Spiel extends Thread
{
connection con[];


    public Spiel(Socket client)
    {

        con = new connection[2];
        con[0] = new connection(1,this , client);
    
        this.start();
    }
   
    public void Neu(Socket client){
        con[1] = new connection(0,this , client);
        con[0].Name("+");
        con[1].Name("#");
    }
    
    
    public void say(int i ,String msg){
        con[i].out.println(msg);
    }
}
