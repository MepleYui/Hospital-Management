package hospital.management;

import javax.swing.*;
import java.awt.*;

public class Gradient extends JPanel {
    
    // Convert hex colors to RGB
    private final Color startColor = new Color(168, 237, 234); // #a8edea
    private final Color endColor = new Color(254, 214, 227);   // #fed6e3
    
    public Gradient() {
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