package matrix;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class Matrix extends JPanel{
	
	
	private static final long serialVersionUID = 1L;
	
	
    private double angle = 0; // Current rotation angle in radians

    public Matrix() {
        // Timer triggers roughly every 16ms (~60 FPS) for an infinite loop
        Timer timer = new Timer(16, e -> {
            angle += 0.15; // Speed of rotation
            if (angle >= Math.PI * 2) {
                angle -= Math.PI * 2; // Keep angle bounded
            }
            repaint(); // Trigger panel redraw
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable Antialiasing for smooth vector rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // --- Draw Shadow ---
        g2d.setColor(new Color(200, 200, 200, 120));
        g2d.fillOval(centerX - 40, centerY + 85, 80, 15);

        // --- Draw the Top Body ---
        
        // 1. Handle / Peg (Top Knob)
        g2d.setColor(new Color(139, 69, 19)); // Saddle Brown
        g2d.fillRect(centerX - 4, centerY - 80, 8, 30);
        g2d.fillOval(centerX - 8, centerY - 85, 16, 10);

        // 2. Cone Base (Top Section)
        Path2D topCone = new Path2D.Double();
        topCone.moveTo(centerX, centerY - 50);
        topCone.lineTo(centerX + 70, centerY);
        topCone.lineTo(centerX - 70, centerY);
        topCone.closePath();
        
        //g2d.setColor(new Color(220, 50, 50)); // Red upper body
        g2d.setColor(new Color(0, 128, 0)); // Green upper body
        
        
        g2d.fill(topCone);

        // 3. Bottom Tip (Lower Cone)
        Path2D bottomTip = new Path2D.Double();
        bottomTip.moveTo(centerX - 70, centerY);
        bottomTip.lineTo(centerX + 70, centerY);
        bottomTip.lineTo(centerX, centerY + 90);
        bottomTip.closePath();
        
        //g2d.setColor(new Color(220, 50, 50)); // Darker red lower body
        g2d.setColor(new Color(0, 128, 0)); // Green lower body
        
        g2d.fill(bottomTip);

        // 4. Metal Spinning Tip Point
        g2d.setColor(Color.GRAY);
        g2d.fillOval(centerX - 3, centerY + 87, 6, 6);

        // --- Spinning Bands / Pattern ---
        // Simulates rotational movement using squashed elliptical bands
        drawSpinningBands(g2d, centerX, centerY);
    }

    private void drawSpinningBands(Graphics2D g2d, int cx, int cy) {
        // Draw stripes that deform along an elliptical path to mimic 3D motion
        int bandWidth = 140;
        int bandHeight = 30; // Perspective flattening

        // We cycle through 4 color segments around the circumference
        Color[] stripeColors = {
            new Color(255, 215, 0), // Gold
            new Color(30, 144, 255), // Dodger Blue
            new Color(46, 139, 87),  // Sea Green
            new Color(255, 140, 0)   // Dark Orange
        };

        for (int i = 0; i < 4; i++) {
            double currentAngle = angle + (i * Math.PI / 2);
            
            // X position calculation creates the 3D perspective oscillation
            int stripeX = (int) (Math.cos(currentAngle) * (bandWidth / 2 - 10));
            int stripeSize = (int) (Math.sin(currentAngle) * 8) + 12;

            // Only draw elements on the front-facing semi-circle
            if (Math.sin(currentAngle) > -0.3) {
                g2d.setColor(stripeColors[i]);
                g2d.fillOval(cx + stripeX - stripeSize / 2, cy - bandHeight / 4, stripeSize, 18);
            }
        }

        // Rim Outline
        g2d.setColor(new Color(100, 20, 20));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(cx - bandWidth / 2, cy - bandHeight / 2, bandWidth, bandHeight);
    }

   
  	public static void main(String args[]) {
		
		System.out.println("Overlapping East and West Matrix like 2 circles overlapping with common area of two circles is the reality");
		
		JFrame frame = new JFrame("Matrix");
        Matrix game = new Matrix();
        game.setBackground(new Color(245, 245, 245));
        
        
        frame.add(game);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
        frame.setLocationRelativeTo(null); // Center on screen
        
        MatrixThreadManager matrixThreadManager = new MatrixThreadManager();
        matrixThreadManager.alternateBetweenEastAndWestMatrix();
        
        
        frame.setVisible(true);	
		
		
	}
}