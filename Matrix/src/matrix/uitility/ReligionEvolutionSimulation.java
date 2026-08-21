package matrix.uitility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

public class ReligionEvolutionSimulation extends JFrame {

    // --- Belief System Stages ---
    public enum BeliefSystem {
        ATHEIST_AGNOSTIC("Atheist / Agnostic / Tribal Non-Theism", new Color(120, 120, 120)),
        PAGAN_TRIBAL("Pagan Gods & Polytheism", new Color(139, 69, 19)),
        HINDUISM("Hinduism", new Color(255, 140, 0)),
        ZOROASTRIANISM("Zoroastrianism", new Color(186, 85, 211)),
        JAINISM("Jainism", new Color(255, 215, 0)),
        BUDDHISM("Buddhism", new Color(220, 20, 60)),
        SIKHISM("Sikhism", new Color(255, 105, 180)),
        JUDAISM("Judaism", new Color(30, 144, 255)),
        CHRISTIANITY("Christianity", new Color(70, 130, 180)),
        ISLAM("Islam", new Color(46, 139, 87));

        private final String displayName;
        private final Color color;

        BeliefSystem(String displayName, Color color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() { return displayName; }
        public Color getColor() { return color; }
    }

    // --- Simulation Agent ---
    public static class Agent {
        double x, y;
        double dx, dy;
        BeliefSystem belief;

        public Agent(double x, double y, BeliefSystem belief) {
            this.x = x;
            this.y = y;
            this.belief = belief;
            Random rand = new Random();
            this.dx = (rand.nextDouble() - 0.5) * 3;
            this.dy = (rand.nextDouble() - 0.5) * 3;
        }

        public void move(int width, int height) {
            x += dx;
            y += dy;

            if (x < 10 || x > width - 10) dx *= -1;
            if (y < 10 || y > height - 10) dy *= -1;
        }
    }

    // --- Simulation Variables ---
    private final List<Agent> agents = new ArrayList<>();
    private int simulationYears = -10000; // Start at 10,000 BCE
    private boolean isRunning = false;
    private final Timer timer;
    private final Random random = new Random();

    // --- UI Components ---
    private final JLabel timerLabel = new JLabel("Chronograph: 10000 BCE", SwingConstants.CENTER);
    private final JLabel stageLabel = new JLabel("Current Era: Tribal / Animism / Non-Theistic Phase", SwingConstants.CENTER);
    private final JButton toggleButton = new JButton("Start");
    private final JButton resetButton = new JButton("Reset");
    private final SimulationPanel simPanel = new SimulationPanel();
    private final StatsPanel statsPanel = new StatsPanel();

    public ReligionEvolutionSimulation() {
        setTitle("Historical Evolution of Human Belief Systems");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize 300 Agents
        resetAgents();

        // Top Control & Chronograph Panel
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(new Color(240, 240, 240));

        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        timerLabel.setForeground(new Color(20, 20, 20));
        stageLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));

        topPanel.add(timerLabel);
        topPanel.add(stageLabel);

        // Bottom Controls
        JPanel controlPanel = new JPanel();
        toggleButton.addActionListener(e -> toggleSimulation());
        resetButton.addActionListener(e -> resetSimulation());

        controlPanel.add(toggleButton);
        controlPanel.add(resetButton);

        // Center View
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, simPanel, statsPanel);
        mainSplit.setDividerLocation(750);

        add(topPanel, BorderLayout.NORTH);
        add(mainSplit, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Timer Loop (Fires every 40 ms)
        timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning) {
                    updateSimulation();
                    simPanel.repaint();
                    statsPanel.repaint();
                }
            }
        });
    }

    private void resetAgents() {
        agents.clear();
        for (int i = 0; i < 350; i++) {
            agents.add(new Agent(
                    100 + random.nextInt(550),
                    100 + random.nextInt(450),
                    BeliefSystem.ATHEIST_AGNOSTIC
            ));
        }
    }

    private void toggleSimulation() {
        isRunning = !isRunning;
        if (isRunning) {
            toggleButton.setText("Pause");
            timer.start();
        } else {
            toggleButton.setText("Resume");
        }
    }

    private void resetSimulation() {
        isRunning = false;
        timer.stop();
        toggleButton.setText("Start");
        simulationYears = -10000;
        resetAgents();
        updateTimerLabel();
        stageLabel.setText("Current Era: Tribal / Animism / Non-Theistic Phase");
        simPanel.repaint();
        statsPanel.repaint();
    }

    private void updateTimerLabel() {
        if (simulationYears < 0) {
            timerLabel.setText(String.format("Chronograph: %d BCE", Math.abs(simulationYears)));
        } else {
            timerLabel.setText(String.format("Chronograph: %d CE", simulationYears));
        }
    }

    // --- Core Timeline Logic & Transitions ---
    private void updateSimulation() {
        // Advance time linearly
        simulationYears += 25;
        if (simulationYears > 2026) {
            simulationYears = 2026;
            isRunning = false;
            toggleButton.setText("Start");
            timer.stop();
        }

        updateTimerLabel();

        // Transition Logic Based on History
        for (Agent a : agents) {
            a.move(simPanel.getWidth(), simPanel.getHeight());

            // 1. Primitive Paganism (c. 6000 BCE)
            if (simulationYears >= -6000 && simulationYears < -2000) {
                stageLabel.setText("Era: Early Paganism & Tribal Polytheism Shrines");
                if (a.belief == BeliefSystem.ATHEIST_AGNOSTIC && random.nextDouble() < 0.03) {
                    a.belief = BeliefSystem.PAGAN_TRIBAL;
                }
            }
            // 2. Organized Faiths: Hinduism & Zoroastrianism (c. 2000 BCE - 1500 BCE)
            else if (simulationYears >= -2000 && simulationYears < -600) {
                stageLabel.setText("Era: Emergence of Organized Polytheism & Dualism (Hinduism, Zoroastrianism)");
                if (a.belief == BeliefSystem.PAGAN_TRIBAL) {
                    double r = random.nextDouble();
                    if (r < 0.02) a.belief = BeliefSystem.HINDUISM;
                    else if (r < 0.03) a.belief = BeliefSystem.ZOROASTRIANISM;
                }
            }
            // 3. Indian Offshoots & Judaism (c. 600 BCE - 1 CE)
            else if (simulationYears >= -600 && simulationYears < 1) {
                stageLabel.setText("Era: Axial Age — Hinduism births Jainism & Buddhism; Rise of Abrahamic Roots");
                if (a.belief == BeliefSystem.HINDUISM) {
                    double r = random.nextDouble();
                    if (r < 0.015) a.belief = BeliefSystem.BUDDHISM;
                    else if (r < 0.025) a.belief = BeliefSystem.JAINISM;
                }
                if (a.belief == BeliefSystem.PAGAN_TRIBAL && random.nextDouble() < 0.01) {
                    a.belief = BeliefSystem.JUDAISM;
                }
            }
            // 4. Spread of Christianity (c. 1 CE - 600 CE)
            else if (simulationYears >= 1 && simulationYears < 600) {
                stageLabel.setText("Era: Spread of Christianity across empires");
                if ((a.belief == BeliefSystem.PAGAN_TRIBAL || a.belief == BeliefSystem.JUDAISM) && random.nextDouble() < 0.02) {
                    a.belief = BeliefSystem.CHRISTIANITY;
                }
            }
            // 5. Rise of Islam (c. 600 CE - 1500 CE)
            else if (simulationYears >= 600 && simulationYears < 1500) {
                stageLabel.setText("Era: Expansion of Islam");
                if ((a.belief == BeliefSystem.PAGAN_TRIBAL || a.belief == BeliefSystem.ZOROASTRIANISM) && random.nextDouble() < 0.02) {
                    a.belief = BeliefSystem.ISLAM;
                }
            }
            // 6. Birth of Sikhism (c. 1500 CE onwards)
            else if (simulationYears >= 1500) {
                stageLabel.setText("Era: Modern Era — Rise of Sikhism & Globalization of Faiths");
                if ((a.belief == BeliefSystem.HINDUISM || a.belief == BeliefSystem.ISLAM) && random.nextDouble() < 0.008) {
                    a.belief = BeliefSystem.SIKHISM;
                }
            }
        }
    }

    // --- Visualization Canvas Panel ---
    private class SimulationPanel extends JPanel {
        public SimulationPanel() {
            setBackground(new Color(25, 25, 30));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (Agent agent : agents) {
                g2d.setColor(agent.belief.getColor());
                g2d.fillOval((int) agent.x, (int) agent.y, 10, 10);
            }
        }
    }

    // --- Statistics & Legend Panel ---
    private class StatsPanel extends JPanel {
        public StatsPanel() {
            setBackground(new Color(245, 245, 245));
            setPreferredSize(new Dimension(320, 0));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));

            // Count distribution
            EnumMap<BeliefSystem, Integer> counts = new EnumMap<>(BeliefSystem.class);
            for (BeliefSystem b : BeliefSystem.values()) counts.put(b, 0);
            for (Agent a : agents) counts.put(a.belief, counts.get(a.belief) + 1);

            int y = 25;
            g2.setColor(Color.BLACK);
            g2.drawString("Belief Distribution (" + agents.size() + " total)", 15, y);
            y += 20;

            for (BeliefSystem b : BeliefSystem.values()) {
                int count = counts.get(b);
                int barWidth = (int) (((double) count / agents.size()) * 180);

                g2.setColor(b.getColor());
                g2.fillRect(15, y, 12, 12);

                g2.setColor(Color.DARK_GRAY);
                g2.drawString(b.getDisplayName(), 35, y + 11);

                g2.setColor(new Color(200, 200, 200));
                g2.fillRect(15, y + 16, 180, 6);
                g2.setColor(b.getColor());
                g2.fillRect(15, y + 16, barWidth, 6);

                g2.setColor(Color.BLACK);
                g2.drawString(String.valueOf(count), 205, y + 15);

                y += 38;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReligionEvolutionSimulation frame = new ReligionEvolutionSimulation();
            frame.setVisible(true);
        });
    }
}