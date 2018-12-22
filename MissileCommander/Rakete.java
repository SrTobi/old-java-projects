import java.awt.*;

public class Rakete
{
    public float xp , yp, xo ,yo, Speed;
    public int xs , ys , xz , yz;
    public int böse;
    
    public Rakete(int XS,int YS,int XZ,int YZ,float Speed, int böse)
    {
        xs = XS;
        ys = YS;
        xp = (float)XS;
        yp = (float)YS;
        xz = XZ;
        yz = YZ;
        this.Speed = Speed;
        this.böse = böse;
        richtung();
    }
    public void fly(){
        xp -= xo;
        yp -= yo;
    }
    public void malen(Graphics an){
        if(böse > -1){
            an.setColor(Color.red);
        }else{
            an.setColor(Color.blue);
        }
        an.drawLine((int)xp,(int)yp,xs,ys);
        an.drawOval(xz - 2 , yz - 2 , 2 , 2);
    }
    public boolean fertig(){
        boolean re = false;
        if(Pytago(xp,yp,(float)xz,(float)yz) < 3){
            re = true;
        }
        return re;
    }
    public void richtung(){
        float x = xp - (float)xz;
        float y = yp - (float)yz;
        if(Math.abs(x) > Math.abs(y)){
            xo = x / Math.abs(x) * Speed;
            yo = y / Math.abs(x) * Speed;
        }else{
            xo = x / Math.abs(y) * Speed;
            yo = y / Math.abs(y) * Speed;
        }
    }
    
    public float Pytago(float von_x,float von_y,float zu_x,float zu_y){
        float x_weg = Math.abs(zu_x - von_x);
        float y_weg = Math.abs(zu_y - von_y);
        return (float)Math.sqrt(x_weg * x_weg + y_weg * y_weg);
    }
}
