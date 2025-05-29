package hospital.management;

import javax.swing.*;
import java.awt.*;

public class Gradient extends JPanel {
    
    // Convert hex colors to RGB
    private final Color startColor = new Color(168, 237, 234); // #a8edea
    private final Color endColor = new Color(254, 214, 227);   // #fed6e3
    
    public Gradient() {
        super();
        setOpaque(false); // Make sure the panel is transparent for the gradient to show
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing for smoother gradients
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Get panel dimensions
        int width = getWidth();
        int height = getHeight();
        
        // Create diagonal gradient (135 degrees)
        GradientPaint gradient = new GradientPaint(
            0, 0, startColor,           // Start point and color (top-left)
            width, height, endColor     // End point and color (bottom-right)
        );
        
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
    }
}