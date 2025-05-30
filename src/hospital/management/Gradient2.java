package hospital.management;
import javax.swing.*;
import java.awt.*;

public class Gradient2 extends JPanel {
    
    // Professional Gray Gradient Colors
    private final Color startColor = new Color(189, 195, 199); // #bdc3c7 - Light Gray
    private final Color endColor = new Color(44, 62, 80);      // #2c3e50 - Charcoal
    
    public Gradient2() {
        super();
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        GradientPaint gradient = new GradientPaint(
            0, 0, startColor,
            width, height, endColor
        );
        
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
    }
}