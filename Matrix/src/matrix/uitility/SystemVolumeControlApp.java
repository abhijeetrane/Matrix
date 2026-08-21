package matrix.uitility;

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class SystemVolumeControlApp extends JFrame {

    private JSlider volumeSlider;
    private JButton muteButton;
    private JLabel volumeLabel;
    private JLabel speakerLabel;
    private EqualizerPanel equalizerPanel;
    private TrayIcon trayIcon;

    private boolean isMuted = false;
    private int lastVolume = 50;
    private boolean hasNircmd = false;
    private String osName;

    // For fallback Java mixer
    private FloatControl volumeControl = null;
    private FloatControl gainControl = null;

    public SystemVolumeControlApp() {
        osName = System.getProperty("os.name").toLowerCase();
        checkNircmd();
        initAudioMixer();
        initUI();
        initSystemTray();
        setVolume(50);
    }

    private void checkNircmd() {
        // Check in current dir, system32, and PATH
        String[] paths = {"nircmd.exe", "./nircmd.exe", "C:\\Windows\\System32\\nircmd.exe", "C:\\Windows\\nircmd.exe","F:\\nircmd-x64\\nircmd.exe"};
        for (String p : paths) {
            if (new File(p).exists()) {
                hasNircmd = true;
                return;
            }
        }
        try {
            Process pr = Runtime.getRuntime().exec("F:\\nircmd-x64\\nircmd.exe");
            pr.destroy();
            hasNircmd = true;
        } catch (Exception e) {
            hasNircmd = false;
            System.out.println("nircmd not found - using Java AudioSystem fallback. Download from nirsoft.net for perfect control.");
        }
    }

    private void initAudioMixer() {
        try {
            for (Mixer.Info mi : AudioSystem.getMixerInfo()) {
                Mixer mixer = AudioSystem.getMixer(mi);
                for (Line.Info li : mixer.getTargetLineInfo()) {
                    try {
                        Line line = mixer.getLine(li);
                        line.open();
                        if (line.isControlSupported(FloatControl.Type.VOLUME))
                            volumeControl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                        if (line.isControlSupported(FloatControl.Type.MASTER_GAIN))
                            gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception ignored) {}
    }

    // THE CORE - Real system volume via Runtime.exec
    public void setSystemVolumeReal(int percent) {
        percent = Math.max(0, Math.min(100, percent));
        try {
            if (osName.contains("win")) {
                if (hasNircmd) {
                    // nircmd: 0 to 65535
                    int nircmdVol = (int) (percent * 655.35);
                    Runtime.getRuntime().exec("F:\\nircmd-x64\\nircmd.exe setsysvolume " + nircmdVol);
                } else {
                    // Fallback: PowerShell for Windows 10/11
                    // This uses WScript.Shell SendKeys workaround is unreliable, so we use Java mixer as backup
                    // You can also use: powershell -c "$wsh = New-Object -ComObject WScript.Shell; ..."
                }
            } else if (osName.contains("mac")) {
                // Mac: 0-100
                Runtime.getRuntime().exec(new String[]{"osascript", "-e", "set volume output volume " + percent});
            } else { // Linux
                Runtime.getRuntime().exec(new String[]{"amixer", "-D", "pulse", "sset", "Master", percent + "%"});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Also set Java mixer for UI feedback
        try {
            if (volumeControl != null) volumeControl.setValue(percent / 100f);
            if (gainControl != null) {
                if (percent == 0) gainControl.setValue(gainControl.getMinimum());
                else {
                    float min = gainControl.getMinimum();
                    float max = gainControl.getMaximum();
                    gainControl.setValue(min + (max - min) * (percent / 100f));
                }
            }
        } catch (Exception ignored) {}
    }

    public void setSystemMuteReal(boolean mute) {
        try {
            if (osName.contains("win") && hasNircmd) {
                Runtime.getRuntime().exec("F:\\nircmd-x64\\nircmd.exe mutesysvolume " + (mute ? 1 : 0));
            } else if (osName.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"osascript", "-e", "set volume output muted " + mute});
            } else if (osName.contains("nix") || osName.contains("nux")) {
                Runtime.getRuntime().exec(new String[]{"amixer", "-D", "pulse", "sset", "Master", mute ? "mute" : "unmute"});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        setTitle("Speaker Volume Control - System Tray Edition");
        setSize(500, 430);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(18, 18, 20));

        // Top
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(18, 18, 20));
        top.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));

        speakerLabel = new JLabel("🔊", SwingConstants.CENTER);
        speakerLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 68));
        volumeLabel = new JLabel("50%", SwingConstants.CENTER);
        volumeLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        volumeLabel.setForeground(Color.WHITE);

        top.add(speakerLabel, BorderLayout.CENTER);
        top.add(volumeLabel, BorderLayout.SOUTH);

        equalizerPanel = new EqualizerPanel();
        equalizerPanel.setBackground(new Color(18, 18, 20));

        // Bottom controls
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(new Color(30, 30, 32));
        bottom.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setBackground(new Color(30, 30, 32));
        volumeSlider.setPaintTicks(true);
        volumeSlider.setMajorTickSpacing(20);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintLabels(true);
        volumeSlider.addChangeListener(e -> {
            int v = volumeSlider.getValue();
            if (v > 0 && isMuted) isMuted = false;
            setVolume(v);
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnPanel.setBackground(new Color(30, 30, 32));

        JButton minus = styledBtn("−");
        muteButton = styledBtn("🔇 MUTE");
        muteButton.setPreferredSize(new Dimension(130, 40));
        muteButton.setBackground(new Color(220, 60, 60));
        muteButton.setForeground(Color.BLUE);
        JButton plus = styledBtn("+");
        plus.setForeground(Color.BLUE); 
        JButton trayBtn = styledBtn("⬇ To Tray");
        trayBtn.setForeground(Color.BLUE);

        minus.addActionListener(e -> volumeSlider.setValue(Math.max(0, volumeSlider.getValue() - 5)));
        minus.setForeground(Color.BLUE);
        plus.addActionListener(e -> volumeSlider.setValue(Math.min(100, volumeSlider.getValue() + 5)));
        muteButton.addActionListener(e -> toggleMute());
        trayBtn.addActionListener(e -> minimizeToTray());

        btnPanel.add(minus);
        btnPanel.add(muteButton);
        btnPanel.add(plus);
        btnPanel.add(trayBtn);

        bottom.add(volumeSlider);
        bottom.add(Box.createVerticalStrut(10));
        bottom.add(btnPanel);

        if (!hasNircmd && osName.contains("win")) {
            JLabel warn = new JLabel("nircmd.exe not found - using fallback. Download for perfect control.", SwingConstants.CENTER);
            warn.setForeground(Color.ORANGE);
            warn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            bottom.add(warn);
        }

        add(top, BorderLayout.NORTH);
        add(equalizerPanel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        new Timer(70, e -> equalizerPanel.repaint()).start();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                minimizeToTray();
            }
        });
    }

    private JButton styledBtn(String t) {
        JButton b = new JButton(t);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(new Color(60, 62, 65));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void setVolume(int percent) {
        volumeLabel.setText(percent + "%");
        setSystemVolumeReal(percent);

        if (!isMuted) lastVolume = percent == 0 ? lastVolume : percent;

        String icon = percent == 0 || isMuted ? "🔇" : percent < 35 ? "🔈" : percent < 70 ? "🔉" : "🔊";
        speakerLabel.setText(icon);
        equalizerPanel.setVolume(isMuted ? 0 : percent);

        if (isMuted || percent == 0) {
            muteButton.setText("🔊 UNMUTE");
            muteButton.setBackground(new Color(66, 133, 244));
        } else {
            muteButton.setText("🔇 MUTE");
            muteButton.setBackground(new Color(220, 60, 60));
        }

        if (trayIcon != null) {
            trayIcon.setToolTip("Volume: " + (isMuted ? "Muted" : percent + "%"));
        }
    }

    private void toggleMute() {
        isMuted = !isMuted;
        setSystemMuteReal(isMuted);
        if (isMuted) {
            lastVolume = volumeSlider.getValue();
            volumeSlider.setValue(0);
        } else {
            volumeSlider.setValue(lastVolume == 0 ? 50 : lastVolume);
        }
    }

    private void initSystemTray() {
        if (!SystemTray.isSupported()) return;

        try {
            SystemTray tray = SystemTray.getSystemTray();
            // Create tray image
            BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setColor(Color.BLACK);
            g2.fillOval(0, 0, 16, 16);
            g2.setColor(Color.BLUE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            g2.drawString("♪", 3, 12);
            g2.dispose();

            trayIcon = new TrayIcon(img, "Volume: 50%");
            trayIcon.setImageAutoSize(true);

            PopupMenu popup = new PopupMenu();
           
            MenuItem openItem = new MenuItem("Open Volume Control");
           
            
            
            MenuItem muteItem = new MenuItem("Mute/Unmute");
            MenuItem volUp = new MenuItem("Volume +10%");
            MenuItem volDown = new MenuItem("Volume -10%");
            MenuItem exitItem = new MenuItem("Exit");

            openItem.addActionListener(e -> {
                setVisible(true);
                setExtendedState(JFrame.NORMAL);
            });
            muteItem.addActionListener(e -> toggleMute());
            volUp.addActionListener(e -> volumeSlider.setValue(Math.min(100, volumeSlider.getValue() + 10)));
            volDown.addActionListener(e -> volumeSlider.setValue(Math.max(0, volumeSlider.getValue() - 10)));
            exitItem.addActionListener(e -> {
                tray.remove(trayIcon);
                System.exit(0);
            });

            popup.add(openItem);
            popup.add(muteItem);
            popup.addSeparator();
            popup.add(volUp);
            popup.add(volDown);
            popup.addSeparator();
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);
            trayIcon.addActionListener(e -> setVisible(true));

            tray.add(trayIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void minimizeToTray() {
        if (SystemTray.isSupported() && trayIcon != null) {
            setVisible(false);
            trayIcon.displayMessage("Volume Control", "App minimized to system tray. Click tray icon to open.", TrayIcon.MessageType.INFO);
        } else {
            setVisible(false);
        }
    }

    static class EqualizerPanel extends JPanel {
        private int volume = 50;
        private final java.util.Random rand = new java.util.Random();
        void setVolume(int v) { volume = v; }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight(), BARS = 6, barW = 30, gap = 14;
            int totalW = BARS*barW + (BARS-1)*gap;
            int sx = (w-totalW)/2;
            for (int i=0; i<BARS; i++) {
                int x = sx + i*(barW+gap);
                float base = volume/100f;
                float jit = volume==0?0.05f:0.3f+rand.nextFloat()*0.7f;
                int bh = (int)(12 + (h-35)*base*jit*(1.2f - Math.abs(i-2.5f)*0.12f));
                int y = h-bh-10;
                Color c = volume==0?new Color(60,60,60):new Color(66+i*18,133,244);
                g2.setColor(c);
                g2.fillRoundRect(x,y,barW,bh,10,10);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new SystemVolumeControlApp().setVisible(true);
        });
    }
}