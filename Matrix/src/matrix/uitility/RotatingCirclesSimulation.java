package matrix.uitility;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RotatingCirclesSimulation extends JFrame {

	private final int numCores;
	private final AnimationPanel animationPanel;

	public RotatingCirclesSimulation(int numCores) {
        this.numCores = numCores;

        setTitle("Concurrency & Parallelism Simulator - Rotating Circles");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        animationPanel = new AnimationPanel();
        add(animationPanel, BorderLayout.CENTER);

        // Header showing concurrency configuration			  
				  
		  JLabel infoLabelFirst = new JLabel("Running 2 Rotation Task on "+numCores+" cores \r\n ", SwingConstants.CENTER );
				  
		  infoLabelFirst.setFont(new Font("SansSerif",
		  Font.BOLD, 15));
		  
		  infoLabelFirst.setBorder(BorderFactory.createEmptyBorder(10, 0,
		  10, 0));
		  
		  add(infoLabelFirst, BorderLayout.NORTH);

		  JLabel infoLabelSecond = new JLabel("If numCores == 1: Concurrency via Time-Slicing. "
			  		+ "If numCores >= 2: Parallel execution on separate thread.  "
			  		+ "Red color is East Matrix, Green color is West Matrix and  Blue color is Real Matrix\r\n", SwingConstants.CENTER );
					  
			  infoLabelSecond.setFont(new Font("SansSerif",
			  Font.BOLD, 15));
			  infoLabelSecond.setBorder(BorderFactory.createEmptyBorder(10, 0,
			  10, 0));
			  
			  add(infoLabelSecond, BorderLayout.NORTH);

		  
		  
        startSimulation();
    }

	private void startSimulation() {
		// Creates a fixed thread pool based on user input
		// If numCores == 1: Concurrency via Time-Slicing
		// If numCores >= 2: Parallel execution on separate threads
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(numCores);

		// Task 1: Anti-clockwise rotation (decrements angle)
		executor.scheduleAtFixedRate(() -> {
			animationPanel.updateAngle1(-0.03);
			animationPanel.repaint();
		}, 0, 16, TimeUnit.MILLISECONDS);

		// Task 2: Clockwise rotation (increments angle)
		executor.scheduleAtFixedRate(() -> {
			animationPanel.updateAngle2(0.03);
			animationPanel.repaint();
		}, 0, 16, TimeUnit.MILLISECONDS);
	}

	// Canvas for rendering the circles and their intersection
	private static class AnimationPanel extends JPanel {
		private double angle1 = 0; // Anti-clockwise task angle
		private double angle2 = 0; // Clockwise task angle

		private final double orbitRadius = 90; // Distance from center
		private final double circleRadius = 110; // Radius of rotating circles

		public synchronized void updateAngle1(double delta) {
			this.angle1 += delta;
		}

		public synchronized void updateAngle2(double delta) {
			this.angle2 += delta;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();

			// Antialiasing for smooth circles
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int centerX = getWidth() / 2;
			int centerY = getHeight() / 2;

			// Compute current position for Circle 1 (Anti-Clockwise)
			double currentAngle1;
			double currentAngle2;
			synchronized (this) {
				currentAngle1 = this.angle1;
				currentAngle2 = this.angle2;
			}

			double x1 = centerX + orbitRadius * Math.cos(currentAngle1) - circleRadius;
			double y1 = centerY + orbitRadius * Math.sin(currentAngle1) - circleRadius;

			// Compute current position for Circle 2 (Clockwise)
			double x2 = centerX + orbitRadius * Math.cos(currentAngle2) - circleRadius;
			double y2 = centerY + orbitRadius * Math.sin(currentAngle2) - circleRadius;

			// Define geometric circle shapes
			Ellipse2D.Double circle1 = new Ellipse2D.Double(x1, y1, circleRadius * 2, circleRadius * 2);
			Ellipse2D.Double circle2 = new Ellipse2D.Double(x2, y2, circleRadius * 2, circleRadius * 2);

			// Compute overlapping region (Intersection)
			Area area1 = new Area(circle1);
			Area area2 = new Area(circle2);
			Area overlap = new Area(circle1);
			overlap.intersect(area2); // Keeps only the overlapping blue region

			// Subtract overlap from individual circles so green doesn't obscure blue
			area1.subtract(overlap);
			area2.subtract(overlap);

			// 1. Draw Green non-overlapping areas
			g2d.setColor(Color.GREEN); // Green
			g2d.fill(area1);

			g2d.setColor(Color.RED); // Green
			g2d.fill(area2);

			// 2. Draw Blue overlapping area
			g2d.setColor(Color.BLUE); // Blue
			g2d.fill(overlap);

			// 3. Outlines
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(2));
			g2d.draw(circle1);
			g2d.draw(circle2);

			// Draw center orbit pivot point
			// g2d.setColor(Color.RED);
			// g2d.fillOval(centerX - 4, centerY - 4, 8, 8);

			g2d.dispose();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			int systemCores = Runtime.getRuntime().availableProcessors();

			String coresInput = JOptionPane
					.showInputDialog(null,
							"Enter number of CPU cores/threads to allocate for the 2 tasks:\n(System detected cores: "
									+ systemCores + ")",
							"Concurrency & Parallelism Setup", JOptionPane.QUESTION_MESSAGE);

			if (coresInput == null || coresInput.trim().isEmpty()) {
				System.exit(0);
			}

			try {
				int cores = Integer.parseInt(coresInput.trim());

				if (cores <= 0) {
					JOptionPane.showMessageDialog(null, "CPU Core count must be a positive integer.", "Error",
							JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}

				

				RotatingCirclesSimulation frame = new RotatingCirclesSimulation(cores);

				
				frame.setSize(600, 500);
				
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				frame.setLocationRelativeTo(null);
				
				frame.setVisible(true);
				
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid number format. Exiting.", "Error",
						JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		});
	}
}