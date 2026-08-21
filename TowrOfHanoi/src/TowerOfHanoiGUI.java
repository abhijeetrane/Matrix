import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class TowerOfHanoiGUI extends JPanel {
    private int nDisks;
    private Stack<Integer>[] towers;
    private int moves = 0;
    private final int rodWidth = 10;
    private final int baseHeight = 15;
    private final int diskHeight = 20;

    public TowerOfHanoiGUI(int n) {
        this.nDisks = n;
        towers = new Stack[3];
        for (int i = 0; i < 3; i++) {
            towers[i] = new Stack<>();
        }
        // Initialize source tower (tower 0) with disks in descending order
        for (int i = n; i > 0; i--) {
            towers[0].push(i);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        
        // Draw rods
        g.setColor(new Color(139, 69, 19)); // Brown
        int rodSpacing = w / 4;
        for (int i = 0; i < 3; i++) {
            int x = rodSpacing * (i + 1) - rodWidth / 2;
            g.fillRect(x, h / 4, rodWidth, h / 2);
            //g.fillRect(x - 50, h / 4 + h / 2, 110, baseHeight); // Base
        }

        // Draw Disks
        g.setColor(Color.BLUE);
        for (int i = 0; i < 3; i++) {
            int xBase = rodSpacing * (i + 1);
            int yBottom = h / 4 + h / 2 + baseHeight - diskHeight;
            for (int j = 0; j < towers[i].size(); j++) {
                int diskSize = towers[i].get(j);
                int dWidth = 20 + diskSize * 20;
                //g.fillRoundRect(xBase - dWidth / 2, yBottom - (j * diskHeight), dWidth, diskHeight, 10, 10);
                g.fillRect(xBase - dWidth / 2, yBottom - (j * diskHeight), dWidth, diskHeight);
            
                // 2. Draw the border (the outline)
                g.setColor(Color.BLACK);
                g.drawRect(xBase - dWidth / 2, yBottom - (j * diskHeight), dWidth, diskHeight);
            
                
                g.setColor(Color.BLUE);
            }
        }
        
        g.setColor(Color.BLACK);
        g.drawString("Moves: " + moves, 20, 20);
    }

    public void moveDisk(int from, int to) {
        int disk = towers[from].pop();
        towers[to].push(disk);
        moves++;
        repaint();
        try {
            Thread.sleep(500); // Animation delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Recursive algorithm to solve Tower of Hanoi
    public void solve(int n, int src, int dest, int aux) {
        if (n == 1) {
            moveDisk(src, dest);
            return;
        }
        solve(n - 1, src, aux, dest);
        moveDisk(src, dest);
        solve(n - 1, aux, dest, src);
    }

    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog("Enter number of disks (1-18):");
        if (input == null) return;
        int n = Integer.parseInt(input);

        JFrame frame = new JFrame("Tower of Hanoi");
        TowerOfHanoiGUI game = new TowerOfHanoiGUI(n);
        frame.add(game);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Run solver in a separate thread to avoid freezing the GUI
        new Thread(() -> {
            game.solve(n, 0, 2, 1);
            JOptionPane.showMessageDialog(frame, "Solved in " + game.moves + " moves!");
        }).start();
    }
}
