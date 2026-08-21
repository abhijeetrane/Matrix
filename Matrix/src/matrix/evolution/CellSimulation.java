package matrix.evolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CellSimulation extends JFrame {
    private final SimulationPanel canvas;

    public CellSimulation() {
        setTitle("Cell Division Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        canvas = new SimulationPanel();
        add(canvas, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton splitButton = new JButton("Trigger Split");
        JButton resetButton = new JButton("Reset");

        splitButton.addActionListener(e -> canvas.triggerManualSplit());
        resetButton.addActionListener(e -> canvas.resetSimulation());

        controlPanel.add(splitButton);
        controlPanel.add(resetButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CellSimulation().setVisible(true));
    }
}

class Cell implements Runnable {
    public float x, y;
    public float vx, vy;
    public int size;
    public Color color;
    public String type; // "Eukaryote", "Prokaryote", "Multicellular"
    private final SimulationPanel panel;
    private boolean active = true;
    private int age = 0;

    public Cell(float x, float y, String type, SimulationPanel panel) {
        this.x = x;
        this.y = y;
        this.panel = panel;
        this.type = type;

        Random rand = new Random();
        this.vx = (rand.nextFloat() - 0.5f) * 2.0f;
        this.vy = (rand.nextFloat() - 0.5f) * 2.0f;

        switch (type) {
            case "Eukaryote":
                this.size = 50;
                this.color = new Color(138, 43, 226); // Purple
                break;
            case "Prokaryote":
                this.size = 30;
                this.color = new Color(30, 144, 255); // Blue
                break;
            case "Multicellular":
            default:
                this.size = 20;
                this.color = new Color(50, 205, 50); // Green
                break;
        }
    }

    @Override
    public void run() {
        while (active && panel.isRunning()) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                break;
            }

            x += vx;
            y += vy;

            // Bounce off boundaries
            if (x < size / 2f || x > panel.getWidth() - size / 2f) {
                vx = -vx;
                x = Math.max(size / 2f, Math.min(panel.getWidth() - size / 2f, x));
            }
            if (y < size / 2f || y > panel.getHeight() - size / 2f) {
                vy = -vy;
                y = Math.max(size / 2f, Math.min(panel.getHeight() - size / 2f, y));
            }

            age++;
            // Automatic maturation and division logic
            if (age > 150 && panel.getCellCount() < 60) {
                age = 0;
                panel.requestSplit(this);
            }
        }
    }

    public void stop() {
        active = false;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval((int) (x - size / 2), (int) (y - size / 2), size, size);
        g2d.setColor(Color.BLACK);
        g2d.drawOval((int) (x - size / 2), (int) (y - size / 2), size, size);
        
        // Draw inner nucleus/detail
        g2d.setColor(Color.WHITE);
        g2d.fillOval((int) x - 3, (int) y - 3, 6, 6);
    }
}

class SimulationPanel extends JPanel implements ActionListener {
    private final List<Cell> cells = new ArrayList<>();
    private final List<Thread> threads = new ArrayList<>();
    private final Timer renderTimer;
    private boolean running = true;

    public SimulationPanel() {
        setBackground(Color.WHITE);
        resetSimulation();
        renderTimer = new Timer(30, this);
        renderTimer.start();
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized int getCellCount() {
        return cells.size();
    }

    public synchronized void resetSimulation() {
        stopAll();
        cells.clear();
        threads.clear();
        running = true;
        // Start with an initial Eukaryotic Cell
        addCell(400, 300, "Eukaryote");
    }

    private void addCell(float x, float y, String type) {
        Cell cell = new Cell(x, y, type, this);
        cells.add(cell);
        Thread t = new Thread(cell);
        threads.add(t);
        t.start();
    }

    public synchronized void requestSplit(Cell parent) {
        if (!cells.contains(parent)) return;

        String nextType;
        if (parent.type.equals("Eukaryote")) {
            nextType = "Prokaryote";
        } else if (parent.type.equals("Prokaryote")) {
            nextType = "Multicellular";
        } else {
            nextType = "Multicellular";
        }

        // Split position offset
        addCell(parent.x + 15, parent.y + 15, nextType);
    }

    public void triggerManualSplit() {
        synchronized (this) {
            if (cells.isEmpty()) return;
            Cell target = cells.get(new Random().nextInt(cells.size()));
            requestSplit(target);
        }
    }

    private void stopAll() {
        running = false;
        for (Cell c : cells) {
            c.stop();
        }
        for (Thread t : threads) {
            t.interrupt();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        synchronized (this) {
            for (Cell c : cells) {
                c.draw(g2d);
            }
        }

        // Display status info
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2d.drawString("Total Cells / Entities: " + cells.size(), 20, 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
