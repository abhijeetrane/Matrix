package matrix.justice;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.List;

public class GoddessOfJusticeSimulation extends JPanel {

    // Animation variables
    private double angle = 0;
    private final Timer timer;

    // Countries / groups
    private final List<String> leftSide = Arrays.asList(
            "Russia",
            "China",
            "North Korea",
            "Iran",
            "Belarus",
            "Tajiskisthan",
            "Kazhakisthan",
            "Uzbekisthan",
            "Kirgistan",
            "East Ukraine"
    );

    private final List<String> rightSide = Arrays.asList(
            "America",
            "Britain",
            "European Union",
            "Canada",
            "Australia",
            "New Zealand",
            "South Korea",
            "Saudi Arabia",
            "Israel",
            "Japan",
            "Taiwan",
            "West Ukraine"
    );

    public GoddessOfJusticeSimulation() {

        setPreferredSize(new Dimension(1400, 900));
        setBackground(new Color(245, 242, 232));

        // Approximately 60 FPS
        timer = new Timer(16, e -> {
            angle += 0.045;
            repaint();
        });

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        // Anti-aliasing
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        int width = getWidth();
        int height = getHeight();

        // ---------------------------------------------------------
        // TITLE
        // ---------------------------------------------------------

        drawCenteredText(
                g2,
                "INDIA + PAKISTAN",
                width / 2,
                55,
                new Font("Serif", Font.BOLD, 34),
                new Color(80, 40, 20)
        );

        drawCenteredText(
                g2,
                "GODDESS OF JUSTICE",
                width / 2,
                92,
                new Font("Serif", Font.BOLD, 22),
                Color.DARK_GRAY
        );

        // ---------------------------------------------------------
        // LEFT AND RIGHT COUNTRY GROUPS
        // ---------------------------------------------------------

        drawCountryPanel(
                g2,
                leftSide,
                40,
                150,
                260,
                360,
                "SIDE A"
        );

        drawCountryPanel(
                g2,
                rightSide,
                width - 300,
                150,
                260,
                500,
                "SIDE B"
        );

        // ---------------------------------------------------------
        // GODDESS
        // ---------------------------------------------------------

        int centerX = width / 2;
        int goddessTop = 145;

        drawGoddess(
                g2,
                centerX,
                goddessTop
        );

        // ---------------------------------------------------------
        // WEIGHING SCALE
        // ---------------------------------------------------------

        drawWeighingScale(
                g2,
                centerX,
                400
        );

        // ---------------------------------------------------------
        // NEUTRAL COUNTRIES / REGIONS
        // ---------------------------------------------------------

        drawNeutralArea(
                g2,
                centerX,
                height - 90
        );

        g2.dispose();
    }

    // =============================================================
    // GODDESS
    // =============================================================

    private void drawGoddess(
            Graphics2D g2,
            int cx,
            int top
    ) {

        // Halo
        g2.setColor(new Color(218, 177, 74));
        g2.setStroke(new BasicStroke(5));

        g2.drawOval(
                cx - 105,
                top,
                210,
                210
        );

        // Head
        g2.setColor(new Color(224, 184, 150));

        g2.fillOval(
                cx - 72,
                top + 45,
                144,
                150
        );

        // Hair
        g2.setColor(new Color(55, 35, 25));

        g2.fillArc(
                cx - 78,
                top + 25,
                156,
                150,
                0,
                180
        );

        // Crown
        Polygon crown = new Polygon();

        crown.addPoint(cx - 60, top + 50);
        crown.addPoint(cx - 35, top + 10);
        crown.addPoint(cx - 10, top + 42);
        crown.addPoint(cx + 10, top + 5);
        crown.addPoint(cx + 35, top + 42);
        crown.addPoint(cx + 62, top + 15);
        crown.addPoint(cx + 70, top + 65);

        g2.setColor(new Color(210, 170, 55));
        g2.fillPolygon(crown);

        g2.setColor(new Color(120, 85, 20));
        g2.drawPolygon(crown);

        // Nose
        g2.setColor(new Color(190, 140, 110));

        Path2D nose = new Path2D.Double();

        nose.moveTo(cx, top + 105);
        nose.lineTo(cx - 8, top + 135);
        nose.lineTo(cx + 8, top + 135);

        g2.draw(nose);

        // Mouth
        g2.setColor(new Color(130, 50, 50));

        g2.drawArc(
                cx - 20,
                top + 140,
                40,
                18,
                200,
                140
        );

        // ---------------------------------------------------------
        // BLACK BLINDFOLD
        // ---------------------------------------------------------

        g2.setColor(Color.BLACK);

        g2.fillRoundRect(
                cx - 78,
                top + 91,
                156,
                48,
                18,
                18
        );

        // Blindfold knot
        g2.fillOval(
                cx - 90,
                top + 102,
                25,
                25
        );

        g2.fillOval(
                cx + 65,
                top + 102,
                25,
                25
        );

        // Cloth folds
        g2.setColor(new Color(80, 80, 80));
        g2.drawLine(
                cx - 55,
                top + 100,
                cx - 40,
                top + 132
        );

        g2.drawLine(
                cx + 55,
                top + 100,
                cx + 40,
                top + 132
        );

        // ---------------------------------------------------------
        // BODY / ROBE
        // ---------------------------------------------------------

        Polygon robe = new Polygon();

        robe.addPoint(cx - 72, top + 175);
        robe.addPoint(cx + 72, top + 175);
        robe.addPoint(cx + 145, top + 430);
        robe.addPoint(cx - 145, top + 430);

        g2.setColor(new Color(190, 185, 170));
        g2.fillPolygon(robe);

        g2.setColor(new Color(100, 100, 100));
        g2.setStroke(new BasicStroke(3));
        g2.drawPolygon(robe);

        // Neck
        g2.setColor(new Color(224, 184, 150));

        g2.fillRect(
                cx - 25,
                top + 165,
                50,
                40
        );

        // Necklace
        g2.setColor(new Color(218, 177, 74));
        g2.setStroke(new BasicStroke(4));

        g2.drawArc(
                cx - 55,
                top + 155,
                110,
                70,
                0,
                -180
        );

        // Left arm
        g2.setColor(new Color(224, 184, 150));

        g2.setStroke(new BasicStroke(25));

        g2.drawLine(
                cx - 60,
                top + 210,
                cx - 165,
                top + 340
        );

        // Right arm
        g2.drawLine(
                cx + 60,
                top + 210,
                cx + 165,
                top + 340
        );

        // Hands
        g2.fillOval(
                cx - 178,
                top + 330,
                30,
                30
        );

        g2.fillOval(
                cx + 148,
                top + 330,
                30,
                30
        );
    }

    // =============================================================
    // WEIGHING SCALE
    // =============================================================

    private void drawWeighingScale(
            Graphics2D g2,
            int cx,
            int cy
    ) {

        // ---------------------------------------------------------
        // Scale pillar
        // ---------------------------------------------------------

        g2.setColor(new Color(90, 70, 40));

        g2.setStroke(new BasicStroke(12));

        g2.drawLine(
                cx,
                cy + 40,
                cx,
                cy + 285
        );

        // Base
        g2.drawLine(
                cx - 100,
                cy + 285,
                cx + 100,
                cy + 285
        );

        g2.setStroke(new BasicStroke(8));

        // ---------------------------------------------------------
        // Beam
        // ---------------------------------------------------------

        double beamSwing = Math.sin(angle) * 0.08;

        int beamLength = 300;

        int leftX = (int)
                (cx - beamLength * Math.cos(beamSwing));

        int leftY = (int)
                (cy + beamLength * Math.sin(beamSwing));

        int rightX = (int)
                (cx + beamLength * Math.cos(beamSwing));

        int rightY = (int)
                (cy - beamLength * Math.sin(beamSwing));

        g2.setColor(new Color(110, 80, 40));

        g2.drawLine(
                leftX,
                leftY,
                rightX,
                rightY
        );

        // ---------------------------------------------------------
        // Animation of plates
        // ---------------------------------------------------------

        double leftMovement =
                Math.sin(angle) * 45;

        double rightMovement =
                Math.sin(angle + Math.PI) * 45;

        int leftPlateY =
                (int) (cy + 100 + leftMovement);

        int rightPlateY =
                (int) (cy + 100 + rightMovement);

        // Suspension lines
        g2.setStroke(new BasicStroke(3));

        g2.drawLine(
                cx - 300,
                cy + 5,
                cx - 300,
                leftPlateY
        );

        g2.drawLine(
                cx - 300,
                cy + 5,
                cx - 220,
                leftPlateY
        );

        g2.drawLine(
                cx + 300,
                cy + 5,
                cx + 300,
                rightPlateY
        );

        g2.drawLine(
                cx + 300,
                cy + 5,
                cx + 220,
                rightPlateY
        );

        // Left plate
        drawPlate(
                g2,
                cx - 300,
                leftPlateY
        );

        // Right plate
        drawPlate(
                g2,
                cx + 300,
                rightPlateY
        );

        // Central pivot
        g2.setColor(new Color(150, 110, 45));

        g2.fillOval(
                cx - 22,
                cy - 17,
                44,
                44
        );

        // Labels
        drawCenteredText(
                g2,
                "SIDE A",
                cx - 300,
                leftPlateY + 55,
                new Font("SansSerif", Font.BOLD, 15),
                Color.DARK_GRAY
        );

        drawCenteredText(
                g2,
                "SIDE B",
                cx + 300,
                rightPlateY + 55,
                new Font("SansSerif", Font.BOLD, 15),
                Color.DARK_GRAY
        );
    }

    // =============================================================
    // PLATE
    // =============================================================

    private void drawPlate(
            Graphics2D g2,
            int x,
            int y
    ) {

        g2.setColor(new Color(170, 135, 70));

        g2.setStroke(new BasicStroke(5));

        Arc2D.Double plate =
                new Arc2D.Double(
                        x - 90,
                        y - 20,
                        180,
                        70,
                        0,
                        -180,
                        Arc2D.OPEN
                );

        g2.draw(plate);

        g2.drawLine(
                x - 90,
                y + 15,
                x + 90,
                y + 15
        );
    }

    // =============================================================
    // COUNTRY PANELS
    // =============================================================

    private void drawCountryPanel(
            Graphics2D g2,
            List<String> countries,
            int x,
            int y,
            int width,
            int height,
            String title
    ) {

        // Panel
        g2.setColor(new Color(255, 255, 255, 210));

        g2.fillRoundRect(
                x,
                y,
                width,
                height,
                20,
                20
        );

        g2.setColor(new Color(120, 120, 120));

        g2.drawRoundRect(
                x,
                y,
                width,
                height,
                20,
                20
        );

        // Title
        drawCenteredText(
                g2,
                title,
                x + width / 2,
                y + 30,
                new Font("SansSerif", Font.BOLD, 18),
                new Color(70, 70, 70)
        );

        int textY = y + 70;

        for (String country : countries) {

            drawCenteredText(
                    g2,
                    country,
                    x + width / 2,
                    textY,
                    new Font("SansSerif", Font.PLAIN, 16),
                    Color.BLACK
            );

            textY += 30;

            // Stop before leaving panel
            if (textY > y + height - 15) {
                break;
            }
        }
    }

    // =============================================================
    // NEUTRAL AREA
    // =============================================================

    private void drawNeutralArea(
            Graphics2D g2,
            int cx,
            int y
    ) {

        int width = getWidth();

        g2.setColor(new Color(235, 235, 235));

        g2.fillRoundRect(
                120,
                y - 35,
                width - 240,
                70,
                20,
                20
        );

        g2.setColor(new Color(110, 110, 110));

        g2.drawRoundRect(
                120,
                y - 35,
                width - 240,
                70,
                20,
                20
        );

        drawCenteredText(
                g2,
                "NEUTRAL",
                cx,
                y - 10,
                new Font("SansSerif", Font.BOLD, 18),
                new Color(70, 70, 70)
        );

        drawCenteredText(
                g2,
                "Africa  •  Latin America  •  Other Gulf Countries • Mongolia • Turkmenistan • Singapore • Swizterland • South East Asian countries ",
                cx,
                y + 18,
                new Font("SansSerif", Font.PLAIN, 16),
                Color.DARK_GRAY
        );
    }

    // =============================================================
    // CENTERED TEXT HELPER
    // =============================================================

    private void drawCenteredText(
            Graphics2D g2,
            String text,
            int x,
            int y,
            Font font,
            Color color
    ) {

        g2.setFont(font);
        g2.setColor(color);

        FontMetrics fm = g2.getFontMetrics();

        int textWidth = fm.stringWidth(text);

        g2.drawString(
                text,
                x - textWidth / 2,
                y
        );
    }

    // =============================================================
    // MAIN
    // =============================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame =
                    new JFrame("Goddess of Justice - Geopolitical Simulation");

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            GoddessOfJusticeSimulation panel =
                    new GoddessOfJusticeSimulation();

            frame.add(panel);

            frame.pack();

            frame.setLocationRelativeTo(null);

            frame.setVisible(true);
        });
    }
}