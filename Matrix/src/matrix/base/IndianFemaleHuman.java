package matrix.base;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class IndianFemaleHuman extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Colors - Indian ethnicity
        Color skinColor = new Color(0xC68642);
        Color skinShadow = new Color(0xA56C35);
        Color hairColor = new Color(0x121212);
        Color kurtaColor = new Color(0xE84393); // pink kurta
        Color dupattaColor = new Color(0xF8A5C2);
        Color pantColor = new Color(0xFFFFFF);

        // Shadow
        g2.setColor(new Color(0, 0, 0, 25));
        g2.fillOval(centerX - 50, centerY + 185, 100, 18);

        // --- LEGS / SALWAR ---
        g2.setColor(pantColor);
        g2.fillRoundRect(centerX - 32, centerY + 80, 25, 95, 10, 10);
        g2.fillRoundRect(centerX + 7, centerY + 80, 25, 95, 10, 10);
        
        g2.setColor(kurtaColor);
        g2.fillRoundRect(centerX - 38, centerY + 80, 76, 50, 10, 10);

        // Shoes / Mojari
        g2.setColor(new Color(0x8B4513));
        g2.fillRoundRect(centerX - 35, centerY + 172, 32, 10, 8, 8);
        g2.fillRoundRect(centerX + 3, centerY + 172, 32, 10, 8, 8);

        // --- KURTA / TORSO ---
        g2.setColor(kurtaColor);
        Path2D torso = new Path2D.Double();
        torso.moveTo(centerX - 42, centerY - 5);
        torso.lineTo(centerX + 42, centerY - 5);
        torso.lineTo(centerX + 36, centerY + 85);
        torso.lineTo(centerX - 36, centerY + 85);
        torso.closePath();
        g2.fill(torso);

        // Dupatta border
        g2.setColor(dupattaColor);
        g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(centerX - 30, centerY, centerX - 45, centerY + 60);
        g2.drawLine(centerX + 30, centerY, centerX + 45, centerY + 60);

        // --- ARMS ---
        g2.setColor(skinColor);
        g2.setStroke(new BasicStroke(14, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(centerX - 42, centerY + 5, centerX - 65, centerY + 45);
        g2.drawLine(centerX + 42, centerY + 5, centerX + 65, centerY + 45);
        g2.drawLine(centerX - 65, centerY + 45, centerX - 58, centerY + 65);
        g2.drawLine(centerX + 65, centerY + 45, centerX + 58, centerY + 65);

        // Kurta sleeves - 3/4 sleeve
        g2.setColor(kurtaColor);
        g2.setStroke(new BasicStroke(18, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(centerX - 42, centerY + 5, centerX - 55, centerY + 25);
        g2.drawLine(centerX + 42, centerY + 5, centerX + 55, centerY + 25);

        // --- NECK ---
        g2.setColor(skinShadow);
        g2.fillRect(centerX - 10, centerY - 20, 20, 18);
        g2.setColor(skinColor);
        g2.fillRect(centerX - 10, centerY - 18, 20, 15);

        // --- HEAD ---
        int headW = 76;
        int headH = 88;
        int headX = centerX - headW / 2;
        int headY = centerY - 105;

        g2.setColor(skinColor);
        g2.fillOval(headX, headY, headW, headH);

        // --- HAIR - long black hair ---
        g2.setColor(hairColor);
        // Top volume
        g2.fillArc(headX - 4, headY - 8, headW + 8, 50, 0, 180);
        // Long hair behind
        Path2D hairBack = new Path2D.Double();
        hairBack.moveTo(headX, headY + 20);
        hairBack.curveTo(headX - 15, headY + 60, headX - 5, headY + 110, headX + 10, headY + 100);
        hairBack.lineTo(headX + 10, headY + 20);
        hairBack.closePath();
        g2.fill(hairBack);

        Path2D hairBack2 = new Path2D.Double();
        hairBack2.moveTo(headX + headW, headY + 20);
        hairBack2.curveTo(headX + headW + 15, headY + 60, headX + headW + 5, headY + 110, headX + headW - 10, headY + 100);
        hairBack2.lineTo(headX + headW - 10, headY + 20);
        hairBack2.closePath();
        g2.fill(hairBack2);

        // Side hair to frame face
        g2.fillRect(headX - 2, headY + 15, 10, 45);
        g2.fillRect(headX + headW - 8, headY + 15, 10, 45);

        // --- FACE FEATURES ---
        // Eyes
        g2.setColor(Color.WHITE);
        g2.fillOval(headX + 12, headY + 36, 15, 9);
        g2.fillOval(headX + 49, headY + 36, 15, 9);

        g2.setColor(new Color(0x3D2314)); // dark brown eyes
        g2.fillOval(headX + 17, headY + 38, 7, 7);
        g2.fillOval(headX + 54, headY + 38, 7, 7);

        // Eyeliner / Kajal
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawArc(headX + 12, headY + 35, 15, 9, 0, 180);
        g2.drawArc(headX + 49, headY + 35, 15, 9, 0, 180);
        g2.fillOval(headX + 19, headY + 40, 3, 3);
        g2.fillOval(headX + 56, headY + 40, 3, 3);

        // Eyebrows
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(headX + 11, headY + 32, headX + 28, headY + 33);
        g2.drawLine(headX + 48, headY + 33, headX + 65, headY + 32);

        // Bindi - cultural mark
        g2.setColor(new Color(0xCC0000));
        g2.fillOval(centerX - 2, headY + 30, 5, 5);

        // Nose
        g2.setColor(skinShadow);
        g2.setStroke(new BasicStroke(1.3f));
        Path2D nose = new Path2D.Double();
        nose.moveTo(centerX, headY + 42);
        nose.curveTo(centerX - 3, headY + 52, centerX - 2, headY + 58, centerX, headY + 58);
        g2.draw(nose);

        // Nose pin dot
        g2.setColor(new Color(0xFFD700));
        g2.fillOval(centerX - 10, headY + 55, 3, 3);

        // Lips / Smile
        g2.setColor(new Color(0xA52A2A));
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Path2D lips = new Path2D.Double();
        lips.moveTo(centerX - 10, headY + 64);
        lips.curveTo(centerX - 4, headY + 68, centerX + 4, headY + 68, centerX + 10, headY + 64);
        g2.draw(lips);
        // lower lip
        g2.setColor(new Color(0xC75B5B));
        g2.fillArc(centerX - 8, headY + 64, 16, 7, 200, 140);

        // Earrings - Jhumka style
        g2.setColor(new Color(0xFFD700));
        g2.fillOval(headX - 5, headY + 58, 7, 7);
        g2.fillOval(headX + headW - 2, headY + 58, 7, 7);

        // Ears
        g2.setColor(skinColor);
        g2.fillOval(headX - 6, headY + 32, 10, 18);
        g2.fillOval(headX + headW - 4, headY + 32, 10, 18);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Indian Female Human - Swing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new IndianFemaleHuman());
            frame.setSize(400, 500);
            frame.setLocationRelativeTo(null);
            frame.setBackground(new Color(0xFFF8F0));
            frame.setVisible(true);
        });
    }
}