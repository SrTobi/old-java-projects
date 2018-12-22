import java.awt.*;
import java.applet.*;
import java.lang.Object;
import java.lang.Integer;
import java.util.*;
import java.awt.event.*;
import java.lang.Math;
import java.util.ArrayList;

public class Missile_Commander extends Applet
implements MouseListener , Runnable
{
Thread myThread;
Random rand;
Color color;

Image Anzeige;
Graphics an;

ArrayList rak;
ArrayList ex;

AudioClip Sounds[];

boolean schuss = false;
int menu;
int silo[];
int maxSilo;
boolean Stadt[];
float Speed;
int ExplusionsRadius;
float RückgangsSpeed;
int explusion;
int AusbreitungsSpeed;
float gSpeed;
int wellen;
boolean ende;
boolean levelStart;
int level;
int abgeschossene_Raketen;
int Punkte;
int HighScore;
boolean PunkteZählen;
int City;
int Übrige_Raketen;
boolean pause;



    public void init()
    {
        rand = new Random();
        
        Anzeige = createImage(700,500);
        an = Anzeige.getGraphics();
        
        rak = new ArrayList();
        ex = new ArrayList();
        
        Stadt = new boolean[6];
        for(int i = 0 ; i != 6 ; i++){
            Stadt[i] = true;
        }
        
        pause = false;
        
        silo = new int[3];
        addMouseListener(this);
    }
    
    public void start()
    {
        myThread = new Thread(this);
        myThread.start();
        
        for(int i = 0 ;i != 3 ;i++){
            silo[i] = 10;
        }
        maxSilo = 10;
        
        
        
        
        
        Speed = 5.0f;
        ExplusionsRadius = 30;
        RückgangsSpeed = 0.1f;
        AusbreitungsSpeed = 100;
        levelStart = false;
        PunkteZählen = false;
    }
    
    public void stop(){
        myThread.stop();
    }
    
    public void run(){
        // Laden der Sounds
        schuss = true;
        String LadeSounds[] = { "RaketenExplusion.wav" , "Missile.wav" ,  "tada.wav" , "TargetLock.wav"};
        int size = LadeSounds.length;
        
        Sounds = new AudioClip[size];
        
        Graphics g = getGraphics();
        
        for(int i = 0; i < size ; i++){
            Sounds[i] = getAudioClip(getDocumentBase() , LadeSounds[i] ); 
            g.setColor(Color.yellow);
            g.fillRect(270 , 200 , 160 , 100);
            g.setColor(new Color(200,200, 0));
            g.fillRect(285 , 230 , 130 , 40);
            g.setColor(Color.blue);
            g.fillRect(285 , 230 , (int)((130.0f / (float)size) * (float)i) , 40);
            g.setColor(Color.white);
            g.drawString("Lade..." , 325 , 253);
        }
        g.setColor(Color.blue);
        g.fillRect(285 , 230 , 130 , 40);
        try{Thread.sleep(300);}
        catch(InterruptedException e){}
        
        
        
        level = 1;
        while(level < 26){
            schuss = false;
            gSpeed = 0.5f + ((float)level * 0.05f);
            wellen = level + rand.nextInt( level * 2 ) + 1;
            ende = false;
            LevelStart();
            schuss = true;
            while(!ende){
                if(!pause){
                    play();
                    gegner();
                    zerstören();
                    if(rak.size() == 0 && ex.size() == 0 && wellen == 0){
                        ende = true;
                    }
                }
                repaint();
                try{Thread.sleep(10);}
                catch(InterruptedException e){}
            }
            schuss = false;
            PunkteZählen();
            level++;
        }
    }
    
    public void farbe(int r,int g,int b)
    {
        color = new Color(r,g,b);
        an.setColor(color);
    }
    
    public void mouseEntered(MouseEvent e){
        if(pause){
            schuss = true;
            pause = false;
        }      
    }
    public void mouseExited(MouseEvent e){
        if(schuss){
            pause = true;
            schuss = false;
        }
    }
    public void mouseClicked(MouseEvent e){}
    public void mousePressed(MouseEvent e){
        int button = e.getButton();
        if(schuss){
            if(button == 1){
                int mx = e.getX();
                int my = e.getY();
                if(my < 410){
                    int n[] = { Math.abs( 50 - mx ) , Math.abs( 350 - mx ) , Math.abs( 650-mx ) };
                    int nah = 0;
                    if(silo[0] == 0){
                        if(silo[1] == 0){
                            nah = 2;
                        }else{
                            nah = 1;
                        }
                    }
                    if(n[nah] > n[1] && silo [1] != 0){
                        nah = 1;
                    }
                    if(n[nah] > n[2] && silo [2] != 0){
                        nah = 2;
                    }
                    
                    if(silo[nah] != 0){
                        silo[nah]--;
                        Rakete r = new Rakete( 50 + nah * 300 , 435 , mx , my , Speed , -1);
                        
                        rak.add(r);
                        
                        Sounds[1].play();
                    }
                }
            }
        }
        e.consume();
    }
    public void mouseReleased(MouseEvent e){}
    
    
    public void update(Graphics g){
        malen();
        g.drawImage(Anzeige , 0 , 0 , this );
    }
    
    public void paint(Graphics g)
    {
        update(g);
    }
    
    public float Pytago(int von_x , int von_y ,int zu_x ,int zu_y ){
        int x_weg = zu_x - von_x;
        int y_weg = zu_y - von_y;
        if( x_weg < 0){ x_weg = - x_weg;}
        if( y_weg < 0){ y_weg = - y_weg;}
        return (float)Math.sqrt( x_weg * x_weg + y_weg * y_weg);
    }
    
    public void malen(){
        hintergrund();
        if(levelStart)
            Male_LevelStart();
        male_Raketen();
        male_Explusion();
        Stadt();
        Font font = new Font("3",Font.PLAIN,30);
        farbe(10,0,255);
        an.setFont(font);
        an.drawString("Punkte " + Punkte , 1 , 25 );
        an.drawString("Level " + level , 580 , 25 );
        if(pause)
            Male_pause();
        if(PunkteZählen)
            Male_PunkteZählen();
    }
    
    public void Male_LevelStart(){
        Font font = new Font("3",Font.PLAIN,100);
        farbe(10,0,255);
        an.setFont(font);
        an.drawString("Level "+level,200,200);
        Font font2 = new Font("3",Font.PLAIN,12);
        an.setFont(font2);
    }
    
    public void Male_PunkteZählen(){
        farbe(255,255,0);
        an.fillRect(90,100,520,240);
        farbe(10,0,255);
        an.drawString("Level " + level ,100,150);
        for(int i = 152 ; i < 3 + 152 ; i++){
            an.drawLine(95,i,600,i);
        }
        an.drawString("Städte:" + City + "(" + City * 200 + ")" ,100,190);
        an.drawString("Raketen:" + Übrige_Raketen + "(" + Übrige_Raketen * 50 + ")" ,100,230);
        an.drawString("Treffer:" + abgeschossene_Raketen + "(" + abgeschossene_Raketen * 100 + ")" ,100,270);
        
    
    }
    
    public void Male_pause(){
        farbe(255,255,255);
        an.fillRect(150,100,400,200);
        farbe(255,0,0);
        an.fillRect(180,130,340,140);
        farbe(255,255,255);
        Font font = new Font("3",Font.PLAIN,110);
        an.setFont(font);
        an.drawString("Pause",190,230);        
    }
    
    public void hintergrund(){
        Font font = new Font("3",Font.PLAIN,12);
        an.setFont(font);
        farbe(0,0,0);
        an.fillRect(0,0,700,500);
        farbe(255,255,0);
        an.fillRect(0,450,700,50);
        for(int i = 0 ;i != 3 ;i++){
            hügel(15 + i * 300 , i);
        }
    }
    
    public void male_Raketen(){
        for(int i = 0; i != rak.size(); i++){
            Rakete r = (Rakete)rak.get(i);
            r.malen(an);
        }
    }
    
    public void male_Explusion(){
        for(int i = 0; i != ex.size();i++){
            Explusion e = (Explusion)ex.get(i);
            e.malen(an);
        }
    }
    
    public void hügel(int x , int i ){
        int y = 435;
        farbe(255,255,0);
        int X[] = { x , x + 20 , x + 50, x + 70};
        int Y[] = { y + 20 , y , y , y + 20};
        an.fillPolygon(X,Y,4);
        farbe(0,200,0);
        an.drawString(silo[i] + "/" + maxSilo , x + 20 , y + 20);
    }
    
    public void Stadt(){
        an.setColor(Color.blue);
        int x = 0;
        for(int i = 0; i != 6; i++){
            if(Stadt[i]){
                if(i < 3){
                    x = i * 70 + 98;
                }else{
                    x = i * 70 + 198;
                }
                int X[] = new int[22];
                int Y[] = {20,8,8,0,0,16,16,4,4,16,16,4,4,12,12,8,8,12,12,16,16,20};
                for(int x_index=0; x_index < 22; x_index += 2){
                    X[x_index] = x_index * 2 + x;
                    X[x_index + 1]=x_index * 2 + x;
                }
                for(int y_index = 0; y_index < 22; y_index++){
                    Y[y_index] += 430;
                }
                
                an.fillPolygon(X,Y,22);
            }
        }
    }
    
    public void LevelStart(){
        levelStart = true;
        for(int i = 0; i < 3;i++){
            silo[i] = maxSilo;
        }
        abgeschossene_Raketen = 0;
        repaint();
        for(int i = 0; i < 3; i++){
            Sounds[3].play();
            try{Thread.sleep(700);}
            catch(InterruptedException e){}
        }
        levelStart = false;
    }
    
    public void PunkteZählen(){
        PunkteZählen = true;
        City = 0;
        for(int i = 0; i < 6; i++){
            if(Stadt[i]){
                Punkte += 200;
                City++;
            }
        }
        Übrige_Raketen = silo[0] + silo[1] + silo[2];
        Punkte += Übrige_Raketen * 50;
        HighScore += Punkte;
        repaint();
        try{Thread.sleep(5000);}
        catch(InterruptedException e){}
        PunkteZählen = false;
    }
    
    public void play(){
        for(int i = 0; i != rak.size(); i++){
            Rakete r = (Rakete)rak.get(i);
            r.fly();
            if(r.fertig() == true){
                NeuEx(r.xp,r.yp);
                if(r.böse != -1)
                    Stadt[r.böse] = false;
                rak.remove(i);
                i--;
            }
        }
    
        for(int i = 0; i != ex.size(); i++){
            Explusion e = (Explusion)ex.get(i);
            e.weiter();
            if(e.fertig() == true){
                ex.remove(i);
                i--;
            }
        }
    }
    
    public void NeuEx(float x,float y){
        Explusion e = new Explusion(x,y,AusbreitungsSpeed,RückgangsSpeed,ExplusionsRadius);
        ex.add(e);
        Sounds[0].play();
    }
    
    public void gegner(){
        if(rand.nextInt(150 - level * 5)==1 && wellen>0){
            wellen--;
            int z = rand.nextInt(6);
            while(!Stadt[z]){
                z = rand.nextInt(6);
            }
            int x;
            if(z < 3){
                x = z * 70 + 100;
            }else{
                x = z * 70 + 200;
            }
            Rakete r = new Rakete(rand.nextInt(701),0,x + 20,445,gSpeed + rand.nextInt(2) / 3,z);
            rak.add(r);
        }
    }
    
    public void zerstören(){
        for(int i=0; i != ex.size(); i++){
            Explusion e = (Explusion)ex.get(i);
            
            for(int in = 0; in != rak.size(); in++){
                Rakete r = (Rakete)rak.get(in);
                
                if(e.ex > Pytago((int)e.x,(int)e.y,(int)r.xp,(int)r.yp) && r.böse != -1){
                    NeuEx(r.xp,r.yp);
                    rak.remove(in);
                          in--;
                    Punkte += 100;
                    abgeschossene_Raketen++;
                }
            }
        }
    }
}