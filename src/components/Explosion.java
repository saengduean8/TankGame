package components;

import java.awt.*;

public class Explosion {

  public   float x;
  public   float y;
  private  int index;
  Image [] expImage;

    public Explosion(float x,float y){
        this.x = x;
        this.y = y;
        this.index = 0;
        this.expImage = new Image[6];
         expImage[0] = Toolkit.getDefaultToolkit().getImage("resources/explosion1_1.png");
         expImage[1] = Toolkit.getDefaultToolkit().getImage("resources/explosion1_2.png");
         expImage[2] = Toolkit.getDefaultToolkit().getImage("resources/explosion1_3.png");
         expImage[3] = Toolkit.getDefaultToolkit().getImage("resources/explosion1_4.png");
         expImage[4] = Toolkit.getDefaultToolkit().getImage("resources/explosion1_5.png");
         expImage[5] = Toolkit.getDefaultToolkit().getImage("resources/explosion1_6.png");
    }

    public Image nextImageOrNull(){

        if(index >= expImage.length ) {
            return null;
        }
        Image returnImage = expImage[index];
        index++;
        return returnImage;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

}
