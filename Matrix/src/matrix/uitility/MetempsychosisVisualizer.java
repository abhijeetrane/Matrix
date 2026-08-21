package matrix.uitility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MetempsychosisVisualizer extends JFrame {

    private final SimulationPanel simulationPanel;
    private final JTextArea historyLog;
    private final JLabel currentRealmLabel;
    private final JLabel karmaLabel;
    private final JLabel lifetimeLabel;
    private final JProgressBar karmaBar;
    
    private int currentKarma = 50; // Range 0 - 100
    private int lifetimeCount = 1;
    private Realm currentRealm = Realm.HUMAN;
    private final List<String> lifeHistory = new ArrayList<>();
    private final Random random = new Random();
    private Timer autoTimer;

    public enum Realm {
        PLANT("Plant / Mineral", new Color(46, 139, 87), 0, 20),
        ANIMAL("Animal Form", new Color(218, 165, 32), 21, 45),
        HUMAN("Human Form", new Color(70, 130, 180), 46, 75),
        CELESTIAL("Celestial Realm", new Color(147, 112, 219), 76, 95),
        LIBERATION("Moksha / Nirvana", new Color(255, 215, 0), 96, 100);

        final String name;
        final Color color;
        final int minKarma;
        final int maxKarma;

        Realm(String name, Color color, int minKarma, int maxKarma) {
            this.name = name;
            this.color = color;
            this.minKarma = minKarma;
            this.maxKarma = maxKarma;
        }

        public static Realm getRealmForKarma(int karma) {
            for (Realm r : values()) {
                if (karma >= r.minKarma && karma <= r.maxKarma) {
                    return r;
                }
            }
            return HUMAN;
        }
    }

    public MetempsychosisVisualizer() {
        setTitle("Metempsychosis (Transmigration of Souls) Visualizer");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Canvas for Soul & Realm Wheel
        simulationPanel = new SimulationPanel();
        add(simulationPanel, BorderLayout.CENTER);

        // Right Control & Info Panel
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(320, 600));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Info Display
        JPanel statusGroup = new JPanel(new GridLayout(4, 1, 5, 5));
        statusGroup.setBorder(BorderFactory.createTitledBorder("Soul State"));
        
        lifetimeLabel = new JLabel("Lifetime: 1");
        currentRealmLabel = new JLabel("Current Form: " + currentRealm.name);
        karmaLabel = new JLabel("Karma Balance: " + currentKarma + " / 100");
        
        karmaBar = new JProgressBar(0, 100);
        karmaBar.setValue(currentKarma);
        karmaBar.setStringPainted(true);

        statusGroup.add(lifetimeLabel);
        statusGroup.add(currentRealmLabel);
        statusGroup.add(karmaLabel);
        statusGroup.add(karmaBar);

        // Actions / Karma Alteration
        JPanel actionPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Deeds in Current Life"));

        JButton btnGood = new JButton("Good Action (+10)");
        JButton btnBad = new JButton("Bad Action (-10)");
        JButton btnNext = new JButton("Die & Transmigrate");
        JToggleButton btnAuto = new JToggleButton("Auto Cycle");

        btnGood.addActionListener(e -> modifyKarma(10));
        btnBad.addActionListener(e -> modifyKarma(-10));
        btnNext.addActionListener(e -> reincarnate());

        autoTimer = new Timer(1500, e -> {
            // Random karma shift during auto-run
            modifyKarma(random.nextInt(31) - 15);
            reincarnate();
        });

        btnAuto.addActionListener(e -> {
            if (btnAuto.isSelected()) {
                btnAuto.setText("Stop Auto");
                autoTimer.start();
            } else {
                btnAuto.setText("Auto Cycle");
                autoTimer.stop();
            }
        });

        actionPanel.add(btnGood);
        actionPanel.add(btnBad);
        actionPanel.add(btnNext);
        actionPanel.add(btnAuto);

        // Reincarnation History Log
        historyLog = new JTextArea();
        historyLog.setEditable(false);
        historyLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane logScroll = new JScrollPane(historyLog);
        logScroll.setBorder(BorderFactory.createTitledBorder("Transmigration Ledger"));

        sidePanel.add(statusGroup);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(actionPanel);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(logScroll);

        add(sidePanel, BorderLayout.EAST);

        logHistory("Soul initialized in " + currentRealm.name + " (Karma: " + currentKarma + ")");
        setLocationRelativeTo(null);
    }

    private void modifyKarma(int delta) {
        if (currentRealm == Realm.LIBERATION) return;
        currentKarma = Math.max(0, Math.min(100, currentKarma + delta));
        karmaLabel.setText("Karma Balance: " + currentKarma + " / 100");
        karmaBar.setValue(currentKarma);
        simulationPanel.repaint();
    }

    private void reincarnate() {
        if (currentRealm == Realm.LIBERATION) {
            logHistory("Soul has reached Moksha/Nirvana. Cycle completed.");
            if (autoTimer.isRunning()) autoTimer.stop();
            return;
        }

        Realm nextRealm = Realm.getRealmForKarma(currentKarma);
        lifetimeCount++;

        String transition = "Life " + lifetimeCount + ": Transmigrated from " 
                + currentRealm.name + " -> " + nextRealm.name + " (Karma: " + currentKarma + ")";
        
        currentRealm = nextRealm;
        lifetimeLabel.setText("Lifetime: " + lifetimeCount);
        currentRealmLabel.setText("Current Form: " + currentRealm.name);
        
        logHistory(transition);

        // Animate the soul movement on canvas
        simulationPanel.animateMigration(currentRealm);
    }

    private void logHistory(String message) {
        lifeHistory.add(message);
        historyLog.append(message + "\n");
        historyLog.setCaretPosition(historyLog.getDocument().getLength());
    }

    // Inner Custom Graphics Panel
    private class SimulationPanel extends JPanel {
        private double animationAngle = 0;
        private Realm targetRealm = Realm.HUMAN;
        private final Timer animTimer;

        public SimulationPanel() {
            setBackground(new Color(20, 24, 33));
            animTimer = new Timer(30, e -> {
                animationAngle += 0.05;
                if (animationAngle >= 2 * Math.PI) {
                    animationAngle = 0;
                }
                repaint();
            });
            animTimer.start();
        }

        public void animateMigration(Realm target) {
            this.targetRealm = target;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int centerX = width / 2;
            int centerY = height / 2;
            int radius = Math.min(centerX, centerY) - 70;

            // Draw Wheel of Samsara (Sectors)
            Realm[] realms = Realm.values();
            int numRealms = realms.length;
            double angleStep = 2 * Math.PI / numRealms;

            for (int i = 0; i < numRealms; i++) {
                double startAngle = i * angleStep - Math.PI / 2;
                int x = (int) (centerX + radius * Math.cos(startAngle + angleStep / 2));
                int y = (int) (centerY + radius * Math.sin(startAngle + angleStep / 2));

                // Segment background highlight
                g2.setColor(realms[i] == currentRealm ? realms[i].color : realms[i].color.darker().darker());
                g2.fillArc(centerX - radius, centerY - radius, 2 * radius, 2 * radius,
                        (int) Math.toDegrees(-startAngle - angleStep), (int) Math.toDegrees(-angleStep));

                // Sector Label
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                int textX = x - fm.stringWidth(realms[i].name) / 2;
                g2.drawString(realms[i].name, textX, y);
            }

            // Central Hub - Soul Core
            int coreRadius = 45;
            g2.setColor(new Color(15, 15, 25));
            g2.fillOval(centerX - coreRadius, centerY - coreRadius, 2 * coreRadius, 2 * coreRadius);
            g2.setColor(Color.WHITE);
            g2.drawOval(centerX - coreRadius, centerY - coreRadius, 2 * coreRadius, 2 * coreRadius);

            // Soul Particle orbiting current realm
            int activeSector = currentRealm.ordinal();
            double activeAngle = activeSector * angleStep - Math.PI / 2 + angleStep / 2;
            
            // Pulsing orbit position
            double orbitDist = radius * 0.65 + Math.sin(animationAngle * 2) * 10;
            int soulX = (int) (centerX + orbitDist * Math.cos(activeAngle));
            int soulY = (int) (centerY + orbitDist * Math.sin(activeAngle));

            // Glowing Soul Orb
            RadialGradientPaint glow = new RadialGradientPaint(
                    soulX, soulY, 20,
                    new float[]{0.0f, 1.0f},
                    new Color[]{new Color(255, 255, 255, 255), new Color(0, 191, 255, 0)}
            );
            g2.setPaint(glow);
            g2.fillOval(soulX - 20, soulY - 20, 40, 40);

            g2.setColor(Color.WHITE);
            g2.fillOval(soulX - 6, soulY - 6, 12, 12);

            // Outer Frame Info
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("SansSerif", Font.ITALIC, 12));
            g2.drawString("The Eternal Wheel of Samsara", 15, height - 15);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new MetempsychosisVisualizer().setVisible(true);
        });
    }
}