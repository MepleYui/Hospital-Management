package hospital.management;
import javax.swing.*;
import java.awt.*;

public class Gradient3 extends JPanel {
    
    // Healthcare Green Gradient Colors
    private final Color startColor = new Color(168, 230, 207);   // #56ab2f - Forest Green
    private final Color endColor = new Color(86, 171, 47);   // #a8e6cf - Pale Green
    
    public Gradient3() {
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