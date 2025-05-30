package hospital.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HospitalEmergencyButton extends JButton {
    private boolean isHovered = false;
    
    public HospitalEmergencyButton() {
        super("Emergency");
        initButton();
    }
    
    public HospitalEmergencyButton(String text) {
        super(text);
        initButton();
    }
    
    private void initButton() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
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
        
        Color startColor = isHovered ? new Color(255, 127, 127) : new Color(255, 107, 107);
        Color endColor = isHovered ? new Color(255, 110, 56) : new Color(238, 90, 36);
        
        GradientPaint gradient = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        
        g2d.dispose();
        super.paintComponent(g);
    }
}