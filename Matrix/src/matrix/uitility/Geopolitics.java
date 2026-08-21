package matrix.uitility;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Geopolitics extends JPanel implements java.awt.event.ActionListener {

    private double redAngle = 0;
    private double greenAngle = Math.toRadians(120);

    private final int R = 130; // circle radius
    private final int ORBIT = 75;
    private final Timer timer;

    public Geopolitics() {
        setPreferredSize(new Dimension(800, 750));
        setBackground(Color.WHITE);
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        redAngle += 0.015;   // clockwise
        greenAngle -= 0.015; // anti-clockwise
        repaint();
    }

    private void drawCenteredString(Graphics2D g2, String text, double centerX, double centerY, int maxWidth) {
        Font font = new Font("SansSerif", Font.BOLD, 14);
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        
        // Simple word wrap for long text
        if (fm.stringWidth(text) > maxWidth) {
            String[] words = text.split(" ");
            String line1 = "";
            String line2 = "";
            // Manual split for these specific strings
            if (text.contains("RussiaAndAllies")) {
                line1 = "RussiaAndAllies";
                line2 = "Countries";
            } else if (text.contains("AmericaAndAllies")) {
                line1 = "AmericaAndAllies";
                line2 = "Countries";
            } else {
                line1 = text;
            }
            
            int yOffset = -8;
            for (String line : new String[]{line1, line2}) {
                if(line.isEmpty()) continue;
                Rectangle2D bounds = fm.getStringBounds(line, g2);
                g2.drawString(line, (float)(centerX - bounds.getWidth()/2), (float)(centerY + yOffset + fm.getAscent()/2));
                yOffset += 18;
            }
        } else {
            Rectangle2D bounds = fm.getStringBounds(text, g2);
            g2.drawString(text, (float)(centerX - bounds.getWidth()/2), (float)(centerY + fm.getAscent()/2 - 2));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        // Blue - Static (top)
        double bx = cx;
        double by = cy - ORBIT;
        Ellipse2D blue = new Ellipse2D.Double(bx - R, by - R, R * 2, R * 2);

        // Red - Clockwise
        double rx = cx + ORBIT * Math.cos(redAngle);
        double ry = cy + ORBIT * Math.sin(redAngle);
        Ellipse2D red = new Ellipse2D.Double(rx - R, ry - R, R * 2, R * 2);

        // Green - Anti-Clockwise
        double gx = cx + ORBIT * Math.cos(greenAngle);
        double gy = cy + ORBIT * Math.sin(greenAngle);
        Ellipse2D green = new Ellipse2D.Double(gx - R, gy - R, R * 2, R * 2);

        // Fill circles
        g2.setColor(new Color(220, 50, 50, 200));
        g2.fill(red);

        g2.setColor(new Color(50, 180, 50, 200));
        g2.fill(green);

        g2.setColor(new Color(60, 120, 255, 200));
        g2.fill(blue);

        // Common overlapping area of 3 circles
        Area common = new Area(red);
        common.intersect(new Area(green));
        common.intersect(new Area(blue));

        double overlapCenterX = cx;
        double overlapCenterY = cy;
        if (!common.isEmpty()) {
            Rectangle2D bounds = common.getBounds2D();
            overlapCenterX = bounds.getCenterX();
            overlapCenterY = bounds.getCenterY();
            
            g2.setColor(new Color(139, 69, 19)); // Brown
            g2.fill(common);
        }

        // Outlines
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.BLACK);
        g2.draw(red);
        g2.draw(green);
        g2.draw(blue);

        // Text inside circles - White for contrast
        g2.setColor(Color.WHITE);
        drawCenteredString(g2, "RussiaAndAllies", rx, ry, 150);
        drawCenteredString(g2, "AmericaAndAllies", gx, gy, 150);
        drawCenteredString(g2, "India", bx, by, 150);

        // Text inside brown overlap
        if (!common.isEmpty()) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();
            String matrixText = "Real Matrix";
            Rectangle2D tb = fm.getStringBounds(matrixText, g2);
            g2.drawString(matrixText, (float)(overlapCenterX - tb.getWidth()/2), (float)(overlapCenterY + 5));
        }

        g2.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Geopolitics - Real Matrix");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new Geopolitics());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}