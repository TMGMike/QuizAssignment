import javax.swing.*;
import java.awt.*;

public class TransparentLabel extends JLabel {
    private float alpha;
    @Override
    protected void paintComponent(Graphics g){
        Composite alphaComposite = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, this.alpha );

        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setComposite( alphaComposite );

        super.paintComponent( g2d );
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}