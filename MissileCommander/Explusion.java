import java.awt.*;

public class Explusion
{
    public float ex , x , y , hinSpeed , wegSpeed , max;
    public boolean zur�ck;
    public Explusion(float x ,float y, float hinSpeed , float wegSpeed , float max)
    {
        ex = 1;
        this.x = x;
        this.y = y;
        this.hinSpeed = hinSpeed;
        this.wegSpeed = wegSpeed; 
        this.max = max;
        zur�ck = false;
    }
    
    public void weiter(){
        if(zur�ck == false){
            ex += hinSpeed;
        }else{
            ex -= wegSpeed;
        }
        if(ex > max){
            ex = max;
            zur�ck = true;
        }
    }
    
    public void malen(Graphics an){
        an.setColor(Color.white);
        an.fillOval( (int)x - (int)ex , (int)y - (int)ex , (int)ex * 2 , (int)ex * 2);
    }
    public boolean fertig(){
        boolean re = false;
        if(ex < 1 && zur�ck == true){
            re = true;
        }
        return re;
    }
}
