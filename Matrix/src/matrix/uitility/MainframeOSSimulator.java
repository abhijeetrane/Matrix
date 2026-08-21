package matrix.uitility;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class MainframeOSSimulator extends JFrame {
    private JTextArea terminal;
    private JTextField inputField;
    private JLabel statusLabel;
    private FileSystem fs;
    private List<String> history = new ArrayList<>();
    private int historyIndex = -1;
    private long bootTime = System.currentTimeMillis();

    public MainframeOSSimulator() {
        fs = new FileSystem();
        initUI();
    }

    private void initUI() {
        setTitle("IBM z16 Mainframe - Linux Terminal Simulator ");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        // Header like Mainframe
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 30, 30));
        header.setBorder(new EmptyBorder(5, 10, 5, 10));
        JLabel headerLabel = new JLabel(" z/OS LINUX SUBSYSTEM | LPAR: ZLINUX01 | USER: " + fs.currentUser + " | LOAD: 0.42");
        headerLabel.setForeground(new Color(0, 255, 0));
        headerLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
        statusLabel = new JLabel("RUNNING");
        statusLabel.setForeground(Color.GREEN);
        statusLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
        header.add(headerLabel, BorderLayout.WEST);
        header.add(statusLabel, BorderLayout.EAST);

        // Terminal Area
        terminal = new JTextArea();
        terminal.setBackground(Color.BLACK);
        terminal.setForeground(new Color(0, 255, 100));
        terminal.setCaretColor(Color.GREEN);
        terminal.setFont(new Font("Consolas", Font.PLAIN, 14));
        terminal.setEditable(false);
        terminal.setLineWrap(true);
        terminal.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(terminal);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));

        // Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.BLACK);
        inputPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        JLabel promptLabel = new JLabel();
        updatePrompt(promptLabel);
        promptLabel.setForeground(Color.CYAN);
        promptLabel.setFont(new Font("Consolas", Font.BOLD, 14));

        inputField = new JTextField();
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setFont(new Font("Consolas", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        inputPanel.add(promptLabel, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Welcome Text
        printWelcome();

        // Actions
        inputField.addActionListener(e -> executeCommand());
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (!history.isEmpty() && historyIndex < history.size() - 1) {
                        historyIndex++;
                        inputField.setText(history.get(history.size() - 1 - historyIndex));
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (historyIndex > 0) {
                        historyIndex--;
                        inputField.setText(history.get(history.size() - 1 - historyIndex));
                    } else {
                        historyIndex = -1;
                        inputField.setText("");
                    }
                }
            }
        });

        SwingUtilities.invokeLater(() -> inputField.requestFocus());
    }

    private void updatePrompt(JLabel label) {
        label.setText(fs.currentUser + "@zlinux01:" + fs.getCurrentPath() + "$ ");
    }

    private void printWelcome() {
        append("IBM z16 Mainframe Emulator v2.5\n");
        append("Linux 5.15.0-91-generic s390x on LPAR ZLINUX01\n");
        append("Type 'help' for available commands. Type 'man <command>' for details.\n\n");
        append("Last login: " + new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy").format(new Date()) + " from 10.20.30.40\n");
    }

    private void append(String s) {
        terminal.append(s);
        terminal.setCaretPosition(terminal.getDocument().getLength());
    }

    private void executeCommand() {
        String raw = inputField.getText().trim();
        if (raw.isEmpty()) return;

        history.add(raw);
        historyIndex = -1;

        JLabel promptLabel = (JLabel) ((JPanel) terminal.getParent().getParent().getComponent(2)).getComponent(0);
        append(promptLabel.getText() + raw + "\n");

        String output = fs.execute(raw, bootTime);
        if (!output.isEmpty()) {
            append(output + "\n");
        }
        if (output.equals("__CLEAR__")) {
            terminal.setText("");
            printWelcome();
        }

        updatePrompt(promptLabel);
        inputField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception ignored){}
            new MainframeLinuxSimulator().setVisible(true);
        });
    }

    // --- Virtual File System ---
    static class FileNode {
        String name;
        boolean isDir;
        String content = "";
        Map<String, FileNode> children = new HashMap<>();
        FileNode parent;
        long created = System.currentTimeMillis();
        String owner = "root";
        String perms = "rwxr-xr-x";

        FileNode(String name, boolean isDir, FileNode parent) {
            this.name = name; this.isDir = isDir; this.parent = parent;
        }
    }

    static class FileSystem {
        FileNode root;
        FileNode cwd;
        String currentUser = "abhijeet";
        String hostname = "zlinux01";

        FileSystem() {
            root = new FileNode("/", true, null);
            cwd = root;
            // Build default structure
            mkDirP("/home/abhijeet");
            mkDirP("/etc");
            mkDirP("/var/log");
            mkDirP("/usr/bin");
            mkDirP("/tmp");
            mkFile("/home/abhijeet/readme.txt", "Welcome to Mainframe Linux Simulation\nTry: ls, ps, jcl, free");
            mkFile("/etc/hostname", "zlinux01");
            mkFile("/etc/os-release", "NAME=\"Ubuntu\"\nVERSION=\"22.04 LTS (s390x)\"\nID=ubuntu");
            cwd = resolve("/home/abhijeet");
        }

        String getCurrentPath() {
            if (cwd == root) return "/";
            List<String> parts = new ArrayList<>();
            FileNode cur = cwd;
            while (cur!= null && cur!= root) { parts.add(cur.name); cur = cur.parent; }
            Collections.reverse(parts);
            return "/" + String.join("/", parts);
        }

        FileNode resolve(String path) {
            if (path.startsWith("~")) path = "/home/abhijeet" + path.substring(1);
            FileNode cur = path.startsWith("/")? root : cwd;
            if (path.equals("/")) return root;
            for (String p : path.split("/")) {
                if (p.isEmpty() || p.equals(".")) continue;
                if (p.equals("..")) { if (cur.parent!= null) cur = cur.parent; }
                else {
                    if (!cur.isDir ||!cur.children.containsKey(p)) return null;
                    cur = cur.children.get(p);
                }
            }
            return cur;
        }

        void mkDirP(String path) {
            FileNode cur = root;
            for (String p : path.split("/")) {
                if (p.isEmpty()) continue;
                cur.children.putIfAbsent(p, new FileNode(p, true, cur));
                cur = cur.children.get(p);
            }
        }
        void mkFile(String path, String content) {
            int idx = path.lastIndexOf('/');
            String dir = path.substring(0, idx == 0? 1 : idx);
            String name = path.substring(idx + 1);
            FileNode parent = resolve(dir);
            if (parent!= null) {
                FileNode f = new FileNode(name, false, parent);
                f.content = content; parent.children.put(name, f);
            }
        }

        String execute(String raw, long bootTime) {
            String[] parts = raw.split("\\s+");
            String cmd = parts[0].toLowerCase();

            switch (cmd) {
                case "help":
                    return "Available commands:\n" +
                           " File: ls [ -l -a ], pwd, cd, mkdir, rmdir, touch, cat, echo, rm, cp, mv, find, tree, chmod\n" +
                           " System: uname, whoami, hostname, date, uptime, free, df, ps, kill, clear, history, man, jcl\n Example: echo \"hello\" > file.txt";
                case "clear": return "__CLEAR__";
                case "pwd": return getCurrentPath();
                case "whoami": return currentUser;
                case "hostname": return hostname;
                case "date": return new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").format(new Date());
                case "uptime": {
                    long up = (System.currentTimeMillis() - bootTime) / 1000;
                    return String.format("up %d min, 2 users, load average: 0.42, 0.38, 0.35 [LPAR: ZLINUX01]", up / 60);
                }
                case "uname": return "Linux zlinux01 5.15.0-91-generic #101-Ubuntu SMP Tue Nov 14 s390x s390x s390x GNU/Linux";
                case "free": return " total used free\nMem: 16384M 4201M 12183M\nSwap: 8192M 0M 8192M [Mainframe Storage]";
                case "df": return "Filesystem 1K-blocks Used Available Use% Mounted on\n/dev/dasda1 20971520 4200000 16771520 21% /\ntmpfs 8388608 0 8388608 0% /dev/shm";
                case "ps": return " PID TTY STAT TIME COMMAND\n 1? Ss 0:01 /sbin/init\n 452? Ssl 0:05 /usr/bin/dockerd [LPAR-Daemon]\n 891 pts/0 Ss 0:00 -bash\n 1024? Sl 0:12 java MainframeLinuxSimulator\n 2048? S 0:00 [jcl-batch-job]";
                case "ls": return doLs(raw);
                case "cd": return doCd(parts);
                case "mkdir": return doMkdir(parts);
                case "touch": return doTouch(parts);
                case "cat": return doCat(parts);
                case "echo": return doEcho(raw);
                case "rm": return doRm(parts);
                case "cp": return doCp(parts);
                case "mv": return doMv(parts);
                case "history": {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < historyRef.size(); i++) sb.append(String.format("%5d %s\n", i+1, historyRef.get(i)));
                    return sb.toString();
                }
                case "jcl": return "Submitting JCL Job...\n//ZLINUX01 JOB (ACCT),'BATCH',CLASS=A\n//STEP1 EXEC PGM=IEFBR14\nJOB ZLINUX01(JOB1234) SUBMITTED - STATUS: COMPLETED RC=0000\nOutput routed to SDSF.";
                case "man": return "Manual for " + (parts.length>1?parts[1]:"") + " - simulated linux command.";
                default: return "bash: " + cmd + ": command not found. Type 'help' for list.";
            }
        }

        // To access history from inside
        List<String> historyRef = new ArrayList<>();
        // patch history ref update done outside - simplified

        private String doLs(String raw) {
            boolean longList = raw.contains("-l");
            boolean all = raw.contains("-a");
            StringBuilder sb = new StringBuilder();
            if (longList) sb.append("total ").append(cwd.children.size()).append("\n");
            for (FileNode n : cwd.children.values()) {
                if (!all && n.name.startsWith(".")) continue;
                if (longList) {
                    sb.append(String.format("%s 1 %s %s 4096 %s %s\n", n.perms, n.owner, n.owner,
                            new SimpleDateFormat("MMM dd HH:mm").format(new Date(n.created)),
                            n.name + (n.isDir? "/" : "")));
                } else sb.append(n.name).append(n.isDir? "/ " : " ");
            }
            return sb.toString();
        }
        private String doCd(String[] p) {
            if (p.length < 2) { cwd = resolve("/home/abhijeet"); return ""; }
            FileNode dest = resolve(p[1]);
            if (dest == null ||!dest.isDir) return "bash: cd: " + p[1] + ": No such file or directory";
            cwd = dest; return "";
        }
        private String doMkdir(String[] p) {
            if (p.length < 2) return "mkdir: missing operand";
            for (int i=1;i<p.length;i++) if(!p[i].startsWith("-")) {
                FileNode parent = resolve(p[i].contains("/")? p[i].substring(0, p[i].lastIndexOf('/')) : ".");
                String name = p[i].contains("/")? p[i].substring(p[i].lastIndexOf('/')+1) : p[i];
                if (parent == null) parent = cwd;
                parent.children.put(name, new FileNode(name, true, parent));
            }
            return "";
        }
        private String doTouch(String[] p) {
            if (p.length < 2) return "";
            for (int i=1;i<p.length;i++) {
                FileNode f = new FileNode(p[i], false, cwd); cwd.children.put(p[i], f);
            }
            return "";
        }
        private String doCat(String[] p) {
            if (p.length < 2) return "cat: missing operand";
            FileNode f = resolve(p[1]);
            if (f == null) return "cat: " + p[1] + ": No such file or directory";
            if (f.isDir) return "cat: " + p[1] + ": Is a directory";
            return f.content;
        }
        private String doEcho(String raw) {
            // handle > and >>
            if (raw.contains(">")) {
                String[] split = raw.split(">", 2);
                String text = split[0].replaceFirst("echo", "").trim().replaceAll("^\"|\"$|^'|'$", "");
                String fileName = split[1].trim();
                boolean append = raw.contains(">>");
                if (fileName.startsWith(">>")) fileName = fileName.substring(2).trim();
                FileNode f = resolve(fileName);
                if (f == null) { f = new FileNode(fileName, false, cwd); cwd.children.put(fileName, f); }
                f.content = append? f.content + text + "\n" : text;
                return "";
            }
            return raw.replaceFirst("echo", "").trim();
        }
        private String doRm(String[] p) {
            if (p.length < 2) return "rm: missing operand";
            boolean recursive = Arrays.toString(p).contains("-r");
            for (int i=1;i<p.length;i++) {
                if (p[i].startsWith("-")) continue;
                FileNode f = resolve(p[i]);
                if (f == null) return "rm: cannot remove '" + p[i] + "': No such file or directory";
                if (f.isDir &&!recursive) return "rm: cannot remove '" + p[i] + "': Is a directory";
                if (f.parent!= null) f.parent.children.remove(f.name);
            }
            return "";
        }
        private String doCp(String[] p) { return "cp: simulated - file copied (use: cp src dest)"; }
        private String doMv(String[] p) { return "mv: simulated - file moved (use: mv src dest)"; }
    }
}