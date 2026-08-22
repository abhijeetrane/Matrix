package matrix.base;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class IndianMaleHuman extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Smooth rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Colors for Indian skin tone, hair, clothes
        Color skinColor = new Color(0xC68642); // warm brown skin tone
        Color skinShadow = new Color(0xA56C35);
        Color hairColor = new Color(0x1A1A1A);
        Color shirtColor = new Color(0x2E86DE);
        Color pantsColor = new Color(0x2C2C2C);

        // --- SHADOW ---
        g2.setColor(new Color(0,0,0,30));
        g2.fillOval(centerX - 50, centerY + 180, 100, 20);

        // --- LEGS / PANTS ---
        g2.setColor(pantsColor);
        g2.fillRoundRect(centerX - 35, centerY + 70, 28, 110, 10, 10);
        g2.fillRoundRect(centerX + 7, centerY + 70, 28, 110, 10, 10);

        // --- SHOES ---
        g2.setColor(new Color(0x3D2314));
        g2.fillRoundRect(centerX - 40, centerY + 175, 35, 12, 8, 8);
        g2.fillRoundRect(centerX + 5, centerY + 175, 35, 12, 8, 8);

        // --- SHIRT / TORSO ---
        g2.setColor(shirtColor);
        // torso shape
        Path2D torso = new Path2D.Double();
        torso.moveTo(centerX - 45, centerY - 10);
        torso.lineTo(centerX + 45, centerY - 10);
        torso.lineTo(centerX + 38, centerY + 75);
        torso.lineTo(centerX - 38, centerY + 75);
        torso.closePath();
        g2.fill(torso);

        // shirt collar
        g2.setColor(Color.WHITE);
        Polygon collarLeft = new Polygon(
            new int[]{centerX - 15, centerX - 2, centerX - 18},
            new int[]{centerY - 10, centerY - 10, centerY + 5},
            3);
        Polygon collarRight = new Polygon(
            new int[]{centerX + 15, centerX + 2, centerX + 18},
            new int[]{centerY - 10, centerY - 10, centerY + 5},
            3);
        g2.fillPolygon(collarLeft);
        g2.fillPolygon(collarRight);

        // --- ARMS ---
        g2.setColor(skinColor);
        // left arm
        g2.setStroke(new BasicStroke(18, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(centerX - 45, centerY, centerX - 75, centerY + 50);
        g2.drawLine(centerX - 75, centerY + 50, centerX - 65, centerY + 80);
        // right arm
        g2.drawLine(centerX + 45, centerY, centerX + 75, centerY + 50);
        g2.drawLine(centerX + 75, centerY + 50, centerX + 65, centerY + 80);
        
        // shirt sleeves - short
        g2.setColor(shirtColor);
        g2.setStroke(new BasicStroke(22, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(centerX - 45, centerY, centerX - 60, centerY + 15);
        g2.drawLine(centerX + 45, centerY, centerX + 60, centerY + 15);

        // --- NECK ---
        g2.setColor(skinShadow);
        g2.fillRect(centerX - 12, centerY - 25, 24, 20);
        g2.setColor(skinColor);
        g2.fillRect(centerX - 12, centerY - 22, 24, 17);

        // --- HEAD ---
        int headW = 80;
        int headH = 95;
        int headX = centerX - headW/2;
        int headY = centerY - 110;

        g2.setColor(skinColor);
        g2.fillOval(headX, headY, headW, headH);

        // --- HAIR ---
        g2.setColor(hairColor);
        // Top hair
        g2.fillArc(headX - 2, headY - 5, headW + 4, 55, 0, 180);
        // side hair fade
        g2.fillRect(headX - 2, headY + 15, 8, 30);
        g2.fillRect(headX + headW - 6, headY + 15, 8, 30);

        // --- FACE FEATURES ---
        // Eyes
        g2.setColor(Color.WHITE);
        g2.fillOval(headX + 15, headY + 38, 16, 10);
        g2.fillOval(headX + 49, headY + 38, 16, 10);

        g2.setColor(new Color(0x3D2314)); // dark brown eyes - common in India
        g2.fillOval(headX + 20, headY + 40, 8, 8);
        g2.fillOval(headX + 54, headY + 40, 8, 8);

        g2.setColor(Color.BLACK);
        g2.fillOval(headX + 22, headY + 42, 4, 4);
        g2.fillOval(headX + 56, headY + 42, 4, 4);

        // Eyebrows
        g2.setColor(hairColor);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(headX + 14, headY + 34, headX + 32, headY + 36);
        g2.drawLine(headX + 48, headY + 36, headX + 66, headY + 34);

        // Nose
        g2.setColor(skinShadow);
        g2.setStroke(new BasicStroke(1.5f));
        Path2D nose = new Path2D.Double();
        nose.moveTo(centerX, headY + 45);
        nose.curveTo(centerX - 4, headY + 55, centerX - 3, headY + 62, centerX, headY + 62);
        g2.draw(nose);

        // Mustache - common style
        g2.setColor(hairColor);
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        // simple mustache shape
        g2.fill(new Ellipse2D.Double(centerX - 16, headY + 64, 14, 5));
        g2.fill(new Ellipse2D.Double(centerX + 2, headY + 64, 14, 5));

        // Smile
        g2.setColor(new Color(0x8B4A2B));
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawArc(centerX - 12, headY + 66, 24, 12, 200, 140);

        // Ears
        g2.setColor(skinColor);
        g2.fillOval(headX - 7, headY + 35, 12, 22);
        g2.fillOval(headX + headW - 5, headY + 35, 12, 22);
        g2.setColor(skinShadow);
        g2.drawArc(headX - 2, headY + 40, 6, 12, 0, 180);
        g2.drawArc(headX + headW - 4, headY + 40, 6, 12, 0, 180);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Indian Male Human - Swing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new IndianMaleHuman());
            frame.setSize(400, 500);
            frame.setLocationRelativeTo(null);
            frame.setBackground(new Color(0xF5F5F5));
            frame.setVisible(true);
        });
    }
}