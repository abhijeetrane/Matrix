package matrix.evolution;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class CarbonEnergyCycleSimulation extends JFrame {
    private SimulationPanel simPanel;
    private JLabel chronoLabel;
    private JButton pauseBtn, resumeBtn, resetBtn;
    private Timer chronoTimer;
    private long startTime, pausedTime;
    private boolean isPaused = false;

    public CarbonEnergyCycleSimulation() {
        setTitle("Carbon & Energy Cycle - Sun + CO2 -> Leaf + O2 -> Herbivore -> Carnivore -> Soil");
        setSize(1150, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(20,35,20));
        top.setBorder(BorderFactory.createEmptyBorder(8,15,8,15));

        chronoLabel = new JLabel("00:00:00.0");
        chronoLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        chronoLabel.setForeground(Color.WHITE);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
        left.setOpaque(false);
        left.add(new JLabel("⏱ CHRONOGRAPH:"){{setForeground(new Color(180,255,180)); setFont(new Font("SansSerif",Font.BOLD,12));}});
        left.add(chronoLabel);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        center.setOpaque(false);
        JLabel title = new JLabel("CARBON CYCLE: Surya Prakash + CO₂ → Patta (Chlorophyll) + O₂ → Shakahari → Mansahari → Mitti → Patta");
        title.setForeground(new Color(220,255,220));
        title.setFont(new Font("SansSerif", Font.BOLD, 12));
        center.add(title);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        right.setOpaque(false);
        pauseBtn = new JButton("⏸ Pause");
        resumeBtn = new JButton("▶ Resume");
        resetBtn = new JButton("↺ Reset");
        for(JButton b: new JButton[]{pauseBtn,resumeBtn,resetBtn}){ b.setFocusPainted(false); b.setBackground(new Color(45,80,45)); b.setForeground(Color.WHITE); b.setFont(new Font("SansSerif",Font.BOLD,12));}
        right.add(pauseBtn); right.add(resumeBtn); right.add(resetBtn);

        top.add(left, BorderLayout.WEST);
        top.add(center, BorderLayout.CENTER);
        top.add(right, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        simPanel = new SimulationPanel();
        add(simPanel, BorderLayout.CENTER);

        chronoTimer = new Timer(100, e -> chronoLabel.setText(format(System.currentTimeMillis()-startTime)));
        pauseBtn.addActionListener(e->pause());
        resumeBtn.addActionListener(e->resume());
        resetBtn.addActionListener(e->reset());
        reset();
    }
    private String format(long ms){ return String.format("%02d:%02d:%02d.%d", ms/3600000, (ms/60000)%60, (ms/1000)%60, (ms/100)%10); }
    private void pause(){ if(isPaused) return; isPaused=true; pausedTime=System.currentTimeMillis()-startTime; chronoTimer.stop(); simPanel.pause(); pauseBtn.setEnabled(false); resumeBtn.setEnabled(true);}
    private void resume(){ if(!isPaused) return; isPaused=false; startTime=System.currentTimeMillis()-pausedTime; chronoTimer.start(); simPanel.resume(); pauseBtn.setEnabled(true); resumeBtn.setEnabled(false);}
    private void reset(){ startTime=System.currentTimeMillis(); pausedTime=0; isPaused=false; chronoTimer.start(); simPanel.reset(); pauseBtn.setEnabled(true); resumeBtn.setEnabled(false);}
    public static void main(String[] args){ SwingUtilities.invokeLater(()-> new CarbonEnergyCycleSimulation().setVisible(true)); }

    class SimulationPanel extends JPanel implements ActionListener {
        Timer timer;
        Random rand = new Random();
        int tick=0;

        // Entities
        List<CO2> co2List = new ArrayList<>();
        List<O2> o2List = new ArrayList<>();
        List<LightRay> rays = new ArrayList<>();
        List<SoilNutrient> soil = new ArrayList<>();
        List<String> logs = new ArrayList<>();

        // Positions
        int sunX, sunY=60, plantX, plantBaseY;
        float plantHealth = 50; // 0-100
        int leafCount = 5;
        float herbivoreX, herbivoreY, herbEnergy=100;
        float carnivoreX, carnivoreY, carnEnergy=100;
        boolean herbAlive=true, carnAlive=true;
        int photosynthesisFlash=0;
        int herbEatingCooldown=0, carnHuntingCooldown=0;

        SimulationPanel(){
            setBackground(new Color(180,220,255));
            timer = new Timer(35, this);
            timer.start();
        }
        void pause(){timer.stop();}
        void resume(){timer.start();}
        void reset(){
            co2List.clear(); o2List.clear(); rays.clear(); soil.clear(); logs.clear();
            tick=0; plantHealth=60; leafCount=6; herbEnergy=90; carnEnergy=90; herbAlive=true; carnAlive=true;
            photosynthesisFlash=0;
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int W=getWidth(), H=getHeight();
            plantX=W/2; plantBaseY=(int)(H*0.78);
            sunX=W-130;

            // Sky gradient
            GradientPaint sky = new GradientPaint(0,0,new Color(135,206,250),0,H*0.6f,new Color(200,235,255));
            g2.setPaint(sky); g2.fillRect(0,0,W,H);
            // Ground
            g2.setColor(new Color(120,90,50)); g2.fillRect(0,plantBaseY,W,H-plantBaseY);
            g2.setColor(new Color(80,140,50)); g2.fillRect(0,plantBaseY-10,W,20);

            // Sun
            g2.setColor(new Color(255,235,80));
            g2.fillOval(sunX-40,sunY-40,90,90);
            g2.setColor(new Color(255,255,150,100));
            g2.setStroke(new BasicStroke(2));
            for(int a=0;a<360;a+=30){
                int x2 = sunX + (int)(Math.cos(Math.toRadians(a))*110);
                int y2 = sunY + (int)(Math.sin(Math.toRadians(a))*110);
                g2.drawLine(sunX+10, sunY+10, x2, y2);
            }

            // Light rays to plant (photosynthesis)
            g2.setColor(new Color(255,255,100,120));
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for(LightRay r: rays){
                g2.drawLine(sunX, sunY, (int)r.x, (int)r.y);
                g2.fillOval((int)r.x-3,(int)r.y-3,6,6);
            }

            // CO2
            g2.setFont(new Font("SansSerif",Font.BOLD,12));
            for(CO2 c: co2List){
                g2.setColor(new Color(100,100,100,180));
                g2.fillOval((int)c.x,(int)c.y,14,14);
                g2.setColor(Color.WHITE); g2.drawString("CO₂",(int)c.x-2,(int)c.y+10);
            }
            // O2 bubbles
            for(O2 o: o2List){
                g2.setColor(new Color(80,180,255,200));
                g2.fillOval((int)o.x,(int)o.y,16,16);
                g2.setColor(Color.WHITE); g2.drawString("O₂",(int)o.x+1,(int)o.y+11);
            }

            // Soil nutrients
            for(SoilNutrient s: soil){
                g2.setColor(new Color(200,180,100,180));
                g2.fillOval((int)s.x,(int)s.y,8,8);
            }

            // Plant - stem
            int stemH = 120;
            g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(60,120,40));
            g2.drawLine(plantX, plantBaseY, plantX, plantBaseY-stemH);
            // leaves
            for(int i=0;i<leafCount;i++){
                double ang = -60 + i*25;
                int lx = plantX + (int)(Math.cos(Math.toRadians(ang))*40);
                int ly = plantBaseY-stemH+30 + (int)(Math.sin(Math.toRadians(ang))*15) + i* -8;
                Color leafCol = (photosynthesisFlash>0) ? new Color(120,255,120) : new Color(40,180,60);
                if(plantHealth<30) leafCol = new Color(160,140,40);
                g2.setColor(leafCol);
                g2.fillOval(lx-18, ly-10, 36,20);
                g2.setColor(new Color(30,100,30));
                g2.drawLine(plantX, plantBaseY-stemH+30, lx, ly);
            }
            // chlorophyll label
            g2.setColor(new Color(0,0,0,90));
            g2.fillRoundRect(plantX-70, plantBaseY-stemH-45, 140,22,10,10);
            g2.setColor(Color.WHITE); g2.setFont(new Font("SansSerif",Font.BOLD,11));
            g2.drawString("Chlorophyll in Leaf", plantX-55, plantBaseY-stemH-30);

            // Herbivore - Deer
            herbivoreY = plantBaseY-25;
            g2.setColor(new Color(0,0,0,60));
            g2.fillRoundRect((int)herbivoreX-35, (int)herbivoreY-28, 110,26,10,10);
            g2.setColor(herbAlive? new Color(210,180,120) : Color.GRAY);
            // body
            g2.fillOval((int)herbivoreX-20,(int)herbivoreY-15,50,20);
            g2.fillOval((int)herbivoreX+15,(int)herbivoreY-20,22,22); // head
            g2.setColor(new Color(80,60,30));
            g2.fillOval((int)herbivoreX+28,(int)herbivoreY-16,6,6); // nose
            // legs
            g2.setColor(herbAlive? new Color(180,150,100): Color.DARK_GRAY);
            g2.fillRect((int)herbivoreX-15,(int)herbivoreY+2,4,12);
            g2.fillRect((int)herbivoreX+15,(int)herbivoreY+2,4,12);
            g2.setColor(Color.WHITE); g2.setFont(new Font("SansSerif",Font.BOLD,11));
            g2.drawString(herbAlive? "Herbivore (Deer) E="+ (int)herbEnergy : "Dead Herbivore", (int)herbivoreX-30, (int)herbivoreY-32);

            // Carnivore - Tiger/Lion
            carnivoreY = plantBaseY-25;
            g2.setColor(new Color(0,0,0,60));
            g2.fillRoundRect((int)carnivoreX-40, (int)carnivoreY-50, 130,26,10,10);
            g2.setColor(carnAlive? new Color(240,160,40) : Color.GRAY);
            g2.fillOval((int)carnivoreX-25,(int)carnivoreY-15,60,24);
            g2.fillOval((int)carnivoreX+25,(int)carnivoreY-22,26,26);
            g2.setColor(Color.BLACK); // stripes
            if(carnAlive){ g2.drawLine((int)carnivoreX-10,(int)carnivoreY-10,(int)carnivoreX-5,(int)carnivoreY); g2.drawLine((int)carnivoreX+5,(int)carnivoreY-8,(int)carnivoreX+10,(int)carnivoreY);}
            g2.setColor(Color.WHITE); g2.drawString(carnAlive? "Carnivore (Tiger) E="+ (int)carnEnergy : "Dead -> Soil", (int)carnivoreX-35, (int)carnivoreY-54);

            // Energy flow arrows
            drawArrow(g2, sunX-20, sunY+30, plantX+30, plantBaseY-stemH-20, "Sunlight", new Color(255,200,0));
            if(!co2List.isEmpty()) drawArrow(g2, 120, 120, plantX-30, plantBaseY-stemH, "CO₂", Color.DARK_GRAY);
            if(!o2List.isEmpty()) drawArrow(g2, plantX+40, plantBaseY-stemH+10, plantX+120, plantBaseY-stemH-40, "O₂ released", new Color(30,120,255));
            if(herbAlive && leafCount>0) drawArrow(g2, plantX+20, plantBaseY-stemH+30, (int)herbivoreX-10, (int)herbivoreY-5, "Leaf eaten", new Color(40,150,40));
            if(herbAlive && carnAlive) drawArrow(g2, (int)herbivoreX+40, (int)herbivoreY, (int)carnivoreX-10, (int)carnivoreY, "Hunted", Color.RED);
            if(!carnAlive || !herbAlive) drawArrow(g2, (int)carnivoreX, (int)carnivoreY+15, plantX+60, plantBaseY-5, "Becomes Soil → Nutrients", new Color(120,80,20));

            // Cycle box bottom
            g2.setColor(new Color(0,0,0,160));
            g2.fillRoundRect(10, H-62, W-20, 52,12,12);
            g2.setColor(Color.WHITE); g2.setFont(new Font("Monospaced",Font.PLAIN,11));
            String cycle = "CYCLE: [1 Sun + CO₂ → Photosynthesis] → [2 Leaf + Chlorophyll + O₂] → [3 Deer eats Leaf] → [4 Tiger eats Deer] → [5 Death → Decomposers → Soil nutrients → Plant]";
            g2.drawString(cycle, 20, H-35);
            if(!logs.isEmpty()){
                g2.setColor(new Color(255,255,150)); g2.drawString(logs.get(logs.size()-1), 20, H-18);
            }
        }

        void drawArrow(Graphics2D g2, int x1,int y1,int x2,int y2, String label, Color c){
            g2.setColor(c); g2.setStroke(new BasicStroke(1.8f));
            g2.drawLine(x1,y1,x2,y2);
            double ang = Math.atan2(y2-y1,x2-x1);
            int len=10;
            g2.drawLine(x2,y2,(int)(x2-len*Math.cos(ang-0.4)), (int)(y2-len*Math.sin(ang-0.4)));
            g2.drawLine(x2,y2,(int)(x2-len*Math.cos(ang+0.4)), (int)(y2-len*Math.sin(ang+0.4)));
            g2.setFont(new Font("SansSerif",Font.BOLD,10));
            g2.drawString(label,(x1+x2)/2+5,(y1+y2)/2-5);
        }

        @Override
        public void actionPerformed(ActionEvent e){
            tick++;
            int W=getWidth(), H=getHeight();
            if(W==0) return;
            plantX=W/2; plantBaseY=(int)(H*0.78);

            // Spawn CO2
            if(tick%25==0) co2List.add(new CO2(rand.nextInt(W-100)+20, rand.nextInt(150)+20));
            // Move CO2 to plant
            Iterator<CO2> itC = co2List.iterator();
            while(itC.hasNext()){
                CO2 c=itC.next();
                c.x += (plantX - c.x)*0.02f;
                c.y += (plantBaseY-80 - c.y)*0.02f;
                if(Math.hypot(c.x-plantX, c.y-(plantBaseY-80))<20){
                    // Photosynthesis
                    plantHealth = Math.min(100, plantHealth+8);
                    leafCount = Math.min(10, leafCount+ (rand.nextFloat()<0.3?1:0));
                    o2List.add(new O2(plantX+20, plantBaseY-90));
                    rays.add(new LightRay(plantX + rand.nextInt(30)-15, plantBaseY-90));
                    photosynthesisFlash=10;
                    addLog("Photosynthesis: CO₂ + Sunlight → Glucose (Chlorophyll) + O₂");
                    itC.remove();
                }
            }
            // O2 float up
            o2List.removeIf(o-> { o.y -=1.5f; o.x+= (rand.nextFloat()-0.5f); return o.y<10; });
            if(photosynthesisFlash>0) photosynthesisFlash--;
            rays.removeIf(r-> { r.life--; return r.life<=0; });

            // Herbivore behavior
            if(herbAlive){
                if(herbEatingCooldown>0) herbEatingCooldown--;
                float target = plantX+60;
                herbivoreX += (target - herbivoreX)*0.03f;
                if(Math.abs(herbivoreX-target)<15 && leafCount>0 && herbEatingCooldown==0){
                    leafCount--; plantHealth-=5; herbEnergy=Math.min(100, herbEnergy+25);
                    herbEatingCooldown=80;
                    addLog("Deer ate leaf → Energy transferred");
                    if(leafCount==0){ plantHealth=20; }
                }
                herbEnergy -=0.15f;
                if(herbEnergy<=0){ herbAlive=false; addLog("Herbivore died → Nutrients to soil"); soil.add(new SoilNutrient(herbivoreX, plantBaseY)); }
            }

            // Carnivore behavior
            if(carnAlive){
                if(carnHuntingCooldown>0) carnHuntingCooldown--;
                float target = herbAlive? herbivoreX+50 : W*0.75f;
                carnivoreX += (target - carnivoreX)*0.025f;
                if(herbAlive && Math.abs(carnivoreX - herbivoreX)<25 && carnHuntingCooldown==0){
                    // eat herbivore
                    herbAlive=false; carnEnergy=Math.min(100, carnEnergy+50);
                    carnHuntingCooldown=120;
                    addLog("Tiger hunted Deer → Energy up the chain");
                    soil.add(new SoilNutrient(herbivoreX, plantBaseY));
                }
                carnEnergy-=0.12f;
                if(carnEnergy<=0){ carnAlive=false; addLog("Carnivore died → Body becomes soil"); for(int i=0;i<6;i++) soil.add(new SoilNutrient(carnivoreX+rand.nextInt(40)-20, plantBaseY)); }
            } else {
                // Dead carnivore decomposes to soil, then soil feeds plant
                if(tick%20==0 && !soil.isEmpty()){
                    SoilNutrient s = soil.get(rand.nextInt(soil.size()));
                    s.x += (plantX - s.x)*0.05f;
                    if(Math.abs(s.x-plantX)<10){ soil.remove(s); plantHealth=Math.min(100, plantHealth+10); leafCount=Math.min(10, leafCount+1); addLog("Soil nutrients absorbed → New leaves"); if(!herbAlive && rand.nextFloat()<0.02){ herbAlive=true; herbEnergy=60; herbivoreX=W*0.15f; addLog("New Herbivore born (nutrients)"); } if(!carnAlive && rand.nextFloat()<0.01){ carnAlive=true; carnEnergy=70; carnivoreX=W*0.85f; addLog("New Carnivore born"); } }
                }
            }

            // Keep positions in bounds
            if(herbivoreX<20) herbivoreX=20;
            if(carnivoreX<20) carnivoreX=20;

            // Respawn if all dead
            if(!herbAlive && !carnAlive && plantHealth<10 && tick%200==0){ plantHealth=50; leafCount=4; }

            repaint();
        }
        void addLog(String s){ logs.add(s); if(logs.size()>6) logs.remove(0); }

        class CO2 { float x,y; CO2(float x,float y){this.x=x;this.y=y;} }
        class O2 { float x,y; O2(float x,float y){this.x=x;this.y=y;} }
        class LightRay { float x,y; int life=15; LightRay(float x,float y){this.x=x;this.y=y;} }
        class SoilNutrient { float x,y; SoilNutrient(float x,float y){this.x=x;this.y=y;} }
    }
}
