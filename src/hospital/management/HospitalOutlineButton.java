package hospital.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HospitalOutlineButton extends JButton {
    private boolean isHovered = false;
    private Color borderColor = new Color(102, 126, 234);
    
    public HospitalOutlineButton() {
        super("Forgot Password");
        initButton();
    }
    
    public HospitalOutlineButton(String text) {
        super(text);
        initButton();
    }
    
    private void initButton() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(borderColor);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(140, 35));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                setForeground(Color.WHITE);
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                setForeground(borderColor);
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (isHovered) {
            g2d.setColor(borderColor);
            g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        }
        
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 8, 8);
        
        g2d.dispose();
        super.paintComponent(g);
    }
}