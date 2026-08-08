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


/*
 * Matrix is simulation of Multiverse. 
 * Author: Abhijeet Rane
 * 03-August-2026 : Initial Version
 * 08-August-2026 : Update after rebirth and Matrix reboot or reload
 */
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

    
    protected void drawEastMatrix() {
    
    	
    } 	
    	@Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
           
            
            //East Matrix 
            Graphics2D g2dEast = (Graphics2D) g;
                   
            // Enable Antialiasing for smooth vector rendering
            g2dEast.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int centerXEast = getWidth() / 2;
            int centerYEast = getHeight() / 2;
                    

            // --- Draw Shadow ---
            g2dEast.setColor(new Color(200, 200, 200, 120));
            g2dEast.fillOval(centerXEast - 40, centerYEast + 85, 80, 15);

            // --- Draw the Top Body ---
            
            // 1. Handle / Peg (Top Knob)
            g2dEast.setColor(new Color(139, 69, 19)); // Saddle Brown
            g2dEast.fillRect(centerXEast - 4, centerYEast - 80, 8, 30);
            g2dEast.fillOval(centerXEast - 8, centerYEast - 85, 16, 10);

            // 2. Cone Base (Top Section)
            Path2D topConeEast = new Path2D.Double();
            topConeEast.moveTo(centerXEast, centerYEast - 50);
            topConeEast.lineTo(centerXEast + 70, centerYEast);
            topConeEast.lineTo(centerXEast - 70, centerYEast);
            topConeEast.closePath();
            
            g2dEast.setColor(new Color(220, 50, 50)); // Red upper body
            //g2d.setColor(new Color(0, 128, 0)); // Green upper body
            
            
            g2dEast.fill(topConeEast);

            // 3. Bottom Tip (Lower Cone)
            Path2D bottomTipEast = new Path2D.Double();
            bottomTipEast.moveTo(centerXEast - 70, centerYEast);
            bottomTipEast.lineTo(centerXEast + 70, centerYEast);
            bottomTipEast.lineTo(centerXEast, centerYEast + 90);
            bottomTipEast.closePath();
            
            g2dEast.setColor(new Color(220, 50, 50)); // Red lower body
            //g2d.setColor(new Color(0, 128, 0)); // Green lower body
            
            g2dEast.fill(bottomTipEast);

            // 4. Metal Spinning Tip Point
            g2dEast.setColor(Color.GRAY);
            g2dEast.fillOval(centerXEast - 3, centerYEast + 87, 6, 6);

            // --- Spinning Bands / Pattern ---
            // Simulates rotational movement using squashed elliptical bands
            drawSpinningBands(g2dEast, centerXEast, centerYEast);

         // Enable Antialiasing for smooth vector rendering
            g2dEast.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);       
            
            
            //West Matrix 
            Graphics2D g2dWest = (Graphics2D) g;
            
            int centerXWest = getWidth() / 4;
            int centerYWest = getHeight() / 4;
            

            // --- Draw Shadow ---
            g2dWest.setColor(new Color(200, 200, 200, 120));
            g2dWest.fillOval(centerXWest - 40, centerYWest + 85, 80, 15);

            // --- Draw the Top Body ---
            
            // 1. Handle / Peg (Top Knob)
            g2dWest.setColor(new Color(139, 69, 19)); // Saddle Brown
            g2dWest.fillRect(centerXWest - 4, centerYWest - 80, 8, 30);
            g2dWest.fillOval(centerXWest - 8, centerYWest - 85, 16, 10);

            // 2. Cone Base (Top Section)
            Path2D topConeWest = new Path2D.Double();
            topConeWest.moveTo(centerXWest, centerYWest - 50);
            topConeWest.lineTo(centerXWest + 70, centerYWest);
            topConeWest.lineTo(centerXWest - 70, centerYWest);
            topConeWest.closePath();
            
            g2dWest.setColor(new Color(220, 50, 50)); // Red upper body
            //g2d.setColor(new Color(0, 128, 0)); // Green upper body
            
            
            g2dWest.fill(topConeWest);

            // 3. Bottom Tip (Lower Cone)
            Path2D bottomTipWest = new Path2D.Double();
            bottomTipWest.moveTo(centerXWest - 70, centerYWest);
            bottomTipWest.lineTo(centerXWest + 70, centerYWest);
            bottomTipWest.lineTo(centerXWest, centerYWest + 90);
            bottomTipWest.closePath();
            
            g2dWest.setColor(new Color(220, 50, 50)); // Red lower body
            //g2d.setColor(new Color(0, 128, 0)); // Green lower body
            
            g2dWest.fill(bottomTipWest);

            // 4. Metal Spinning Tip Point
            g2dWest.setColor(Color.GRAY);
            g2dWest.fillOval(centerXWest - 3, centerYWest + 87, 6, 6);

            
            drawSpinningBands(g2dWest, centerXWest, centerYWest);
            
            
         // Enable Antialiasing for smooth vector rendering
            g2dWest.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);       
         
          

        }

    
    
    protected void drawWestMatrix() {
    	
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

        /*int count = 0;
        
        while(count < 1000 ) {
            
        	
           
       	
           try {
           Thread.sleep(5000);
           }catch(Exception ex) {
           	
           }
           	
        
           
       }*/

        
        frame.setVisible(true);	
		
		
	}
}