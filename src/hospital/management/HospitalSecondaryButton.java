package hospital.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HospitalSecondaryButton extends JButton {
    private boolean isHovered = false;
    
    public HospitalSecondaryButton() {
        super("Cancel");
        initButton();
    }
    
    public HospitalSecondaryButton(String text) {
        super(text);
        initButton();
    }
    
    private void initButton() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(new Color(44, 62, 80));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(120, 40));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color startColor = isHovered ? new Color(188, 255, 254) : new Color(168, 237, 234);
        Color endColor = isHovered ? new Color(255, 234, 247) : new Color(254, 214, 227);
        
        GradientPaint gradient = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        
        // Light border
        g2d.setColor(new Color(44, 62, 80, 20));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        
        g2d.dispose();
        super.paintComponent(g);
    }
}