import javax.swing.*;
import java.awt.*;

public class LabelRunner implements Runnable {
    private JLabel label;
    @Override
    public void run() {
        int currentTrans = 2;
        int wait = 0;
       // System.out.println(label);
        while (currentTrans != 255) {
            while(wait != 3000){
                wait++;
            }
            int r = this.label.getForeground().getRed();
            int g = this.label.getForeground().getGreen();
            int b = this.label.getForeground().getBlue();

         //   this.label.setForeground(new Color(r,g,b, 2.2F));
          //  System.out.println("Adding +1 to transparency: " + label.getForeground().getTransparency());
            //TransparentLabel t = (TransparentLabel) label;
          //  t.setAlpha(currentTrans);

             //this.label.setForeground(new Color(r,g,b, currentTrans));
          //  t.setForeground(new Color(r,g,b, currentTrans));
           // label = t;

           // this.label.repaint();


            currentTrans++;

        }
    }

    public void setLabel(JLabel label){
        this.label = label;
    }
}
