package matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FourQuadrantCircles extends JPanel implements ActionListener {

    private final int RADIUS = 60; // Radius of all 4 circles
    private double angle = 0;      // Current rotation angle in radians
    private final Timer timer;

    public FourQuadrantCircles() {
        // Dark background for contrast (especially for the white circle)
        setBackground(new Color(25, 25, 25));
        
        // Timer updates animation at ~60 FPS
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable Anti-aliasing for smooth rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        // ------------------------------------------------------------------
        // 1. Draw Axes and Labels
        // ------------------------------------------------------------------
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1.5f));

        // Horizontal Axis: Time Axis (Left <-> Right)
        g2d.drawLine(0, centerY, width, centerY);
        g2d.drawString("Time (+)", width - 60, centerY - 10);
        g2d.drawString("Time (-)", 10, centerY - 10);

        // Vertical Axis: Space Axis (Up <-> Down)
        g2d.drawLine(centerX, 0, centerX, height);
        g2d.drawString("Space (+)", centerX + 10, 20);
        g2d.drawString("Space (-)", centerX + 10, height - 20);

        // Draw Origin (0,0) marker
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString("(0,0)", centerX + 5, centerY + 15);

        // ------------------------------------------------------------------
        // 2. Calculate Circle Centers and Orbital Rotations
        // ------------------------------------------------------------------
        // Each circle's center revolves around an orbital anchor point on its respective axis
        // so that the circle constantly passes through the origin (0,0).

        // Quadrant 1 (+Space, +Time) -> Red Circle -> Clockwise
        // Diameter along +Space axis (Center initially at (0, +R))
        drawOrbitingCircle(g2d, centerX, centerY, 0, RADIUS, -angle, Color.RED);

        // Quadrant 2 (+Space, -Time) -> Blue Circle -> Anti-Clockwise
        // Diameter along -Time axis (Center initially at (-R, 0))
        drawOrbitingCircle(g2d, centerX, centerY, -RADIUS, 0, angle, Color.BLUE);

        // Quadrant 3 (-Space, -Time) -> Green Circle -> Anti-Clockwise
        // Diameter along -Space axis (Center initially at (0, -R))
        drawOrbitingCircle(g2d, centerX, centerY, 0, -RADIUS, angle, Color.GREEN);

        // Quadrant 4 (-Space, +Time) -> White Circle -> Clockwise
        // Diameter along +Time axis (Center initially at (+R, 0))
        drawOrbitingCircle(g2d, centerX, centerY, RADIUS, 0, -angle, Color.WHITE);
    }

    /**
     * Helper method to render an orbiting circle passing through the origin.
     *
     * @param g2d       Graphics context
     * @param originX   Screen X of (0,0)
     * @param originY   Screen Y of (0,0)
     * @param anchorX   Initial X offset of circle center relative to origin
     * @param anchorY   Initial Y offset of circle center relative to origin (Cartesian)
     * @param rotAngle  Rotation angle (positive = anti-clockwise, negative = clockwise)
     * @param color     Circle outline color
     */
    private void drawOrbitingCircle(Graphics2D g2d, int originX, int originY, 
                                    int anchorX, int anchorY, double rotAngle, Color color) {
        
        // Rotate the circle's center around the origin (0,0)
        double currentCenterX = anchorX * Math.cos(rotAngle) - anchorY * Math.sin(rotAngle);
        double currentCenterY = anchorX * Math.sin(rotAngle) + anchorY * Math.cos(rotAngle);

        // Convert Cartesian coordinates to Swing Pixel Coordinates:
        // Swing X = originX + X
        // Swing Y = originY - Y (since Swing Y increases downwards)
        int screenCenterX = (int) Math.round(originX + currentCenterX);
        int screenCenterY = (int) Math.round(originY - currentCenterY);

        // Bounding box top-left corner for drawOval
        int drawX = screenCenterX - RADIUS;
        int drawY = screenCenterY - RADIUS;

        // Render Circle
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawOval(drawX, drawY, 2 * RADIUS, 2 * RADIUS);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Increment angle for continuous animation (~1.5 degrees per frame)
        angle += 0.025;
        if (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Space-Time 4-Quadrant Circle Motion");
            FourQuadrantCircles panel = new FourQuadrantCircles();
            
            frame.add(panel);
            frame.setSize(800, 800);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}