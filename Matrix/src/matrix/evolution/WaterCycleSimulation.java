package matrix.evolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class WaterCycleSimulation extends JFrame {
    private SimulationPanel simulationPanel;
    private JLabel chronoLabel;
    private JButton pauseBtn, resumeBtn, resetBtn;
    private Timer chronoTimer;
    private long startTime = 0;
    private long pausedTime = 0;
    private boolean isPaused = false;

    public WaterCycleSimulation() {
        setTitle("Himalayan Water Cycle - Evaporation > Cloud > Himalayas > Rain/Snow > River > Sea");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top bar with chronograph
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(20, 30, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        chronoLabel = new JLabel("00:00:00.0");
        chronoLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        chronoLabel.setForeground(Color.WHITE);

        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftTop.setOpaque(false);
        leftTop.add(new JLabel("⏱ CHRONOGRAPH:"){{
            setForeground(new Color(150,200,255));
            setFont(new Font("SansSerif", Font.BOLD, 12));
        }});
        leftTop.add(chronoLabel);

        JPanel centerTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerTop.setOpaque(false);
        JLabel title = new JLabel("JAL CHAKRA: Samudra → Bhaap → Badal → Himalaya → Varsha/Himpaat → Nadi → Samudra");
        title.setForeground(new Color(220,230,255));
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        centerTop.add(title);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightTop.setOpaque(false);
        pauseBtn = new JButton("⏸ Pause");
        resumeBtn = new JButton("▶ Resume");
        resetBtn = new JButton("↺ Reset");
        styleBtn(pauseBtn); styleBtn(resumeBtn); styleBtn(resetBtn);
        resumeBtn.setEnabled(false);
        rightTop.add(pauseBtn); rightTop.add(resumeBtn); rightTop.add(resetBtn);

        topPanel.add(leftTop, BorderLayout.WEST);
        topPanel.add(centerTop, BorderLayout.CENTER);
        topPanel.add(rightTop, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        simulationPanel = new SimulationPanel();
        add(simulationPanel, BorderLayout.CENTER);

        // Chrono logic
        chronoTimer = new Timer(100, e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            chronoLabel.setText(formatTime(elapsed));
        });

        pauseBtn.addActionListener(e -> pause());
        resumeBtn.addActionListener(e -> resume());
        resetBtn.addActionListener(e -> reset());

        reset(); // start
    }

    private void styleBtn(JButton b){
        b.setFocusPainted(false);
        b.setBackground(new Color(40,60,100));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
    }
    private String formatTime(long ms){
        long tenth = (ms/100)%10;
        long sec = (ms/1000)%60;
        long min = (ms/60000)%60;
        long hr = ms/3600000;
        return String.format("%02d:%02d:%02d.%d", hr, min, sec, tenth);
    }
    private void pause(){
        if(isPaused) return;
        isPaused = true;
        pausedTime = System.currentTimeMillis() - startTime;
        chronoTimer.stop();
        simulationPanel.pauseSimulation();
        pauseBtn.setEnabled(false);
        resumeBtn.setEnabled(true);
    }
    private void resume(){
        if(!isPaused) return;
        isPaused = false;
        startTime = System.currentTimeMillis() - pausedTime;
        chronoTimer.start();
        simulationPanel.resumeSimulation();
        pauseBtn.setEnabled(true);
        resumeBtn.setEnabled(false);
    }
    private void reset(){
        startTime = System.currentTimeMillis();
        pausedTime = 0;
        isPaused = false;
        chronoTimer.start();
        simulationPanel.resetSimulation();
        pauseBtn.setEnabled(true);
        resumeBtn.setEnabled(false);
        chronoLabel.setText("00:00:00.0");
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new WaterCycleSimulation().setVisible(true));
    }

    // ---------------- Simulation Panel ----------------
    class SimulationPanel extends JPanel implements ActionListener {
        private Timer animTimer;
        private List<Particle> vapors = new ArrayList<>();
        private List<Cloud> clouds = new ArrayList<>();
        private List<Precip> precips = new ArrayList<>();
        private List<RiverDrop> river = new ArrayList<>();
        private Random rand = new Random();
        private int tick = 0;
        private float seaLevel = 0.72f;
        private Polygon himalayas;

        SimulationPanel(){
            setBackground(new Color(135,206,250));
            animTimer = new Timer(33, this); // ~30 FPS
            animTimer.start();
        }
        void pauseSimulation(){ animTimer.stop(); }
        void resumeSimulation(){ animTimer.start(); }
        void resetSimulation(){
            vapors.clear(); clouds.clear(); precips.clear(); river.clear(); tick=0;
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int W = getWidth(), H = getHeight();

            // Sky gradient
            GradientPaint sky = new GradientPaint(0,0,new Color(140,190,255),0,H*0.7f,new Color(200,225,255));
            g2.setPaint(sky);
            g2.fillRect(0,0,W,H);

            // Sun
            g2.setColor(new Color(255,235,100,200));
            g2.fillOval(W-140, 30, 80,80);

            // Define Himalayas polygon (right side)
            int[] mx = { (int)(W*0.55), (int)(W*0.65), (int)(W*0.72), (int)(W*0.82), (int)(W*0.90), (int)(W*0.98), W, W, (int)(W*0.55)};
            int[] my = { (int)(H*seaLevel), (int)(H*0.42), (int)(H*0.48), (int)(H*0.30), (int)(H*0.45), (int)(H*0.38), (int)(H*0.38), (int)(H*seaLevel), (int)(H*seaLevel)};
            himalayas = new Polygon(mx, my, mx.length);

            // Snow caps
            g2.setColor(new Color(80,80,80));
            g2.fillPolygon(himalayas);
            g2.setColor(new Color(245,245,255));
            int[] sx = { (int)(W*0.65), (int)(W*0.72), (int)(W*0.82), (int)(W*0.90), (int)(W*0.82), (int)(W*0.72)};
            int[] sy = { (int)(H*0.42), (int)(H*0.48), (int)(H*0.30), (int)(H*0.45), (int)(H*0.38), (int)(H*0.38)};
            g2.fillPolygon(sx, sy, sx.length);

            // Label Himalayas
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            g2.drawString("HIMALAYAS", (int)(W*0.68), (int)(H*0.68));

            // Sea - left to center
            g2.setColor(new Color(30,100,180));
            g2.fillRect(0, (int)(H*seaLevel), (int)(W*0.6), H);
            // waves
            g2.setColor(new Color(60,140,220));
            for(int x=0;x<W*0.6;x+=40){
                g2.drawArc(x, (int)(H*seaLevel)-2, 40, 10, 0, 180);
            }
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString("ARABIAN SEA / INDIAN OCEAN", 20, (int)(H*0.95));

            // Labels for cycle
            drawPhase(g2, "1. Evaporation", 30, (int)(H*0.55), new Color(255,220,100));
            drawPhase(g2, "2. Cloud Formation & Drift →", W/4, 80, Color.WHITE);
            drawPhase(g2, "3. Blocked by Himalayas", (int)(W*0.5), (int)(H*0.22), Color.WHITE);
            drawPhase(g2, "4. Rain / Snow", (int)(W*0.66), (int)(H*0.28), new Color(200,230,255));
            drawPhase(g2, "5. Glacier Melt → River → Sea", (int)(W*0.35), (int)(H*0.82), new Color(180,255,200));

            // Draw vapors (small rising dots)
            g2.setColor(new Color(255,255,255,120));
            for(Particle p: vapors){
                g2.fillOval((int)p.x, (int)p.y, 4,4);
            }

            // Draw clouds
            for(Cloud c: clouds){
                g2.setColor(new Color(255,255,255, c.alpha));
                for(int i=0;i<3;i++){
                    g2.fillOval((int)(c.x + i*18), (int)(c.y + (i%2)*8), c.size, c.size-5);
                }
                g2.fillOval((int)(c.x+10), (int)(c.y-8), c.size+10, c.size);
            }

            // Draw precipitation
            for(Precip p: precips){
                if(p.isSnow){
                    g2.setColor(Color.WHITE);
                    g2.fillOval((int)p.x, (int)p.y, 5,5);
                } else {
                    g2.setColor(new Color(50,120,255));
                    g2.drawLine((int)p.x, (int)p.y, (int)p.x-2, (int)p.y+8);
                }
            }

            // Draw river (flowing down mountain to sea)
            g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(90,180,255,180));
            // river path
            int rx1 = (int)(W*0.70), ry1 = (int)(H*0.55);
            int rx2 = (int)(W*0.55), ry2 = (int)(H*0.75);
            int rx3 = 0, ry3 = (int)(H*seaLevel+10);
            g2.drawPolyline(new int[]{rx1, rx2, rx3}, new int[]{ry1, ry2, ry3}, 3);
            for(RiverDrop rd: river){
                g2.fillOval((int)rd.x, (int)rd.y, 6,6);
            }

            // Arrow showing cycle
            g2.setColor(new Color(0,0,0,80));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.drawString("Evaporating... ↑", 40, (int)(H*0.65));
        }

        private void drawPhase(Graphics2D g2, String text, int x, int y, Color c){
            g2.setColor(new Color(0,0,0,100));
            g2.fillRoundRect(x-5, y-16, g2.getFontMetrics().stringWidth(text)+10, 20, 10,10);
            g2.setColor(c);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString(text, x, y);
        }

        @Override
        public void actionPerformed(ActionEvent e){
            tick++;
            int W = getWidth(), H = getHeight();
            if(W==0) return;

            // 1. Evaporation from sea
            if(tick % 4 == 0){
                vapors.add(new Particle(rand.nextInt((int)(W*0.5))+20, H*seaLevel, -0.5f - rand.nextFloat(), -1.2f - rand.nextFloat()*2));
            }
            // Convert vapors to clouds
            Iterator<Particle> itV = vapors.iterator();
            while(itV.hasNext()){
                Particle p = itV.next();
                p.x += p.vx; p.y += p.vy;
                p.vy -= 0.02f;
                if(p.y < H*0.18f + rand.nextInt(80)){
                    clouds.add(new Cloud(p.x, p.y, 22+rand.nextInt(12)));
                    itV.remove();
                }
                if(p.y<0) itV.remove();
            }

            // Move clouds to the right, wind
            for(Cloud c: clouds){
                c.x += 1.2f + c.speed;
                c.y += (rand.nextFloat()-0.5f)*0.3f;
                // If hitting Himalayas range (x > 0.55W)
                if(c.x > W*0.55 && c.x < W*0.95){
                    // Orographic lifting - trigger rain/snow
                    if(rand.nextFloat() < 0.08f){
                        boolean snow = c.y < H*0.45;
                        precips.add(new Precip(c.x+20, c.y+20, snow));
                    }
                    // Slow down clouds
                    c.x -= 0.6f;
                    if(rand.nextFloat()<0.02f) c.alpha = Math.max(80, c.alpha-10);
                }
            }
            clouds.removeIf(c -> c.x > W+50 || c.alpha < 50);

            // Precipitation falling
            Iterator<Precip> itP = precips.iterator();
            while(itP.hasNext()){
                Precip p = itP.next();
                p.y += p.isSnow ? 1.5f : 4.5f;
                p.x -= 0.5f;
                // Hit mountain -> become river
                if(p.y > H*0.55f && p.x > W*0.55f){
                    river.add(new RiverDrop(p.x, p.y));
                    itP.remove();
                } else if(p.y > H*seaLevel){
                    // Rain directly to sea (coastal)
                    itP.remove();
                }
            }

            // River flow to sea
            Iterator<RiverDrop> itR = river.iterator();
            while(itR.hasNext()){
                RiverDrop r = itR.next();
                // flow along slope to sea: interpolate to sea
                r.x -= 2.2f;
                r.y += 0.8f;
                if(r.x < 0 || r.y > H*seaLevel+5){
                    itR.remove(); // back to sea, cycle complete
                }
            }

            repaint();
        }

        class Particle { float x,y,vx,vy; Particle(float x,float y,float vx,float vy){this.x=x;this.y=y;this.vx=vx;this.vy=vy;} }
        class Cloud { float x,y,speed; int size, alpha=200; Cloud(float x,float y,int s){this.x=x;this.y=y;this.size=s;this.speed=new Random().nextFloat();} }
        class Precip { float x,y; boolean isSnow; Precip(float x,float y,boolean s){this.x=x;this.y=y;this.isSnow=s;} }
        class RiverDrop { float x,y; RiverDrop(float x,float y){this.x=x;this.y=y;} }
    }
}
