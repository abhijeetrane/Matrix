package matrix.uitility;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class MainframeLinuxSimulator extends JFrame {

    // Terminal / Console Components
    private JTextArea terminalOutput;
    private JTextField terminalInput;
    private final List<String> commandHistory = new ArrayList<>();
    private int historyIndex = -1;

    // Mainframe System Monitor Components
    private DefaultTableModel lparTableModel;
    private DefaultTableModel processTableModel;
    private DefaultTableModel jobTableModel;
    private JProgressBar cpuUsageBar;
    private JProgressBar memoryUsageBar;
    private JLabel uptimeLabel;

    // Simulated OS State
    private String currentDirectory = "/root";
    private final Map<String, List<String>> virtualFS = new HashMap<>();
    private final Map<Integer, String[]> processes = new LinkedHashMap<>();
    private int nextPid = 1000;
    private int nextJobId = 5001;
    private final long startTime = System.currentTimeMillis();

    public MainframeLinuxSimulator() {
        initVirtualFileSystem();
        initDefaultProcesses();

        setTitle("IBM zSystems - Linux on Mainframe Simulator (s390x)");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Dark Theme Colors
        Color bgDark = new Color(18, 18, 18);
        Color panelBg = new Color(28, 28, 28);
        Color terminalGreen = new Color(50, 205, 50);

        getContentPane().setBackground(bgDark);
        setLayout(new BorderLayout(5, 5));

        // --- TOP: Mainframe Hardware & LPAR Header ---
        JPanel topPanel = createHeaderPanel(panelBg);
        add(topPanel, BorderLayout.NORTH);

        // --- CENTER: Split Pane (Left: Terminal Console, Right: System Dashboard) ---
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(620);
        mainSplitPane.setBackground(bgDark);

        // Left Component: Linux Terminal
        JPanel terminalPanel = createTerminalPanel(bgDark, terminalGreen);
        mainSplitPane.setLeftComponent(terminalPanel);

        // Right Component: Mainframe Control Dashboard
        JPanel dashboardPanel = createDashboardPanel(panelBg);
        mainSplitPane.setRightComponent(dashboardPanel);

        add(mainSplitPane, BorderLayout.CENTER);

        // Dynamic System Monitoring Background Thread
        startSystemMonitors();
    }

    private void initVirtualFileSystem() {
        virtualFS.put("/", Arrays.asList("bin", "boot", "dev", "etc", "home", "lib64", "proc", "root", "sys", "usr", "var"));
        virtualFS.put("/root", Arrays.asList("batch_job.jcl", "sys_config.conf", "logs"));
        virtualFS.put("/root/logs", Arrays.asList("jes2.log", "syslog"));
        virtualFS.put("/etc", Arrays.asList("os-release", "hosts", "fstab", "zvm.conf"));
        virtualFS.put("/var", Arrays.asList("log", "spool", "run"));
        virtualFS.put("/home", Arrays.asList("ibmadmin", "db2inst1"));
    }

    private void initDefaultProcesses() {
        processes.put(1, new String[]{"1", "root", "systemd", "0.0", "ACTIVE"});
        processes.put(2, new String[]{"2", "root", "kthreadd", "0.0", "ACTIVE"});
        processes.put(101, new String[]{"101", "root", "zvm_hypervisor", "1.2", "ACTIVE"});
        processes.put(245, new String[]{"245", "db2inst1", "db2sysc", "4.5", "ACTIVE"});
        processes.put(412, new String[]{"412", "root", "sshd", "0.1", "ACTIVE"});
    }

    private JPanel createHeaderPanel(Color panelBg) {
        JPanel header = new JPanel(new GridLayout(1, 4, 10, 10));
        header.setBackground(panelBg);
        header.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        cpuUsageBar = new JProgressBar(0, 100);
        cpuUsageBar.setValue(38);
        cpuUsageBar.setStringPainted(true);
        cpuUsageBar.setForeground(new Color(255, 140, 0));

        memoryUsageBar = new JProgressBar(0, 100);
        memoryUsageBar.setValue(62);
        memoryUsageBar.setStringPainted(true);
        memoryUsageBar.setForeground(new Color(30, 144, 255));

        uptimeLabel = new JLabel("Uptime: 00:00:00");
        uptimeLabel.setForeground(Color.LIGHT_GRAY);
        uptimeLabel.setFont(new Font("Monospaced", Font.BOLD, 12));

        JLabel archLabel = new JLabel("Arch: s390x (IBM Z CEC)");
        archLabel.setForeground(Color.CYAN);
        archLabel.setFont(new Font("Monospaced", Font.BOLD, 12));

        header.add(createGaugePanel("CEC CPU Utilization", cpuUsageBar, panelBg));
        header.add(createGaugePanel("Mainframe Central Storage", memoryUsageBar, panelBg));
        header.add(archLabel);
        header.add(uptimeLabel);

        return header;
    }

    private JPanel createGaugePanel(String title, JProgressBar bar, Color bg) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(bg);
        JLabel lbl = new JLabel(title);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        p.add(lbl, BorderLayout.NORTH);
        p.add(bar, BorderLayout.CENTER);
        return p;
    }

    private JPanel createTerminalPanel(Color bgDark, Color termGreen) {
        JPanel terminalPanel = new JPanel(new BorderLayout());
        terminalPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(termGreen), " Linux Guest Console (z/VM LPAR1 - Linux s390x) ",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Monospaced", Font.BOLD, 12), termGreen));
        terminalPanel.setBackground(bgDark);

        terminalOutput = new JTextArea();
        terminalOutput.setBackground(Color.BLACK);
        terminalOutput.setForeground(termGreen);
        terminalOutput.setCaretColor(termGreen);
        terminalOutput.setFont(new Font("Monospaced", Font.PLAIN, 13));
        terminalOutput.setEditable(false);
        terminalOutput.setLineWrap(true);

        terminalOutput.setText("IBM zSystems Enterprise Mainframe [Architecture: s390x]\n");
        terminalOutput.append("Linux on z/VM Version 7 Release 2.0\n");
        terminalOutput.append("Type 'help' to list supported Linux and Mainframe commands.\n\n");
        printPrompt();

        JScrollPane scrollPane = new JScrollPane(terminalOutput);
        scrollPane.setBorder(null);
        terminalPanel.add(scrollPane, BorderLayout.NORTH);
        
        terminalInput = new JTextField();
        terminalInput.setBackground(Color.BLACK);
        terminalInput.setForeground(termGreen);
        terminalInput.setCaretColor(termGreen);
        terminalInput.setFont(new Font("Monospaced", Font.BOLD, 13));
        terminalInput.setEditable(true);
        terminalOutput.setLineWrap(true);
                
        terminalInput.setBackground(Color.BLACK);

        JScrollPane scrollPaneInput = new JScrollPane(terminalInput);
        scrollPane.setBorder(null);
        terminalPanel.add(scrollPaneInput, BorderLayout.SOUTH);
               
        
        // Command Listener & Arrow Key History Navigation
        terminalInput.addActionListener(e -> {
            String command = terminalInput.getText().trim();
            if (!command.isEmpty()) {
                commandHistory.add(command);
                historyIndex = commandHistory.size();
                executeCommand(command);
                terminalInput.setText("");
            }
        });

        terminalInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (historyIndex > 0) {
                        historyIndex--;
                        terminalInput.setText(commandHistory.get(historyIndex));
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (historyIndex < commandHistory.size() - 1) {
                        historyIndex++;
                        terminalInput.setText(commandHistory.get(historyIndex));
                    } else {
                        historyIndex = commandHistory.size();
                        terminalInput.setText("");
                    }
                }
            }
        });

        
        
        //terminalPanel.add(terminalInput);

        return terminalPanel;
    }

    private JPanel createDashboardPanel(Color panelBg) {
        JPanel dashboard = new JPanel(new GridLayout(3, 1, 5, 5));
        dashboard.setBackground(panelBg);

        // 1. LPAR Allocation Table
        JPanel lparPanel = new JPanel(new BorderLayout());
        lparPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.CYAN), " PR/SM Logical Partitions (LPARs) ",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 11), Color.CYAN));
        lparPanel.setBackground(panelBg);

        String[] lparCols = {"LPAR", "OS", "Type", "vCPUs", "RAM", "Status"};
        Object[][] lparData = {
                {"LPAR01", "RHEL 9.2", "Linux/z", "8 IFL", "64 GB", "ACTIVE"},
                {"LPAR02", "SLES 15", "Linux/z", "4 IFL", "32 GB", "ACTIVE"},
                {"LPAR03", "z/OS V2.5", "z/OS", "16 CP", "128 GB", "ACTIVE"},
                {"LPAR04", "z/TPF", "TPF", "2 CP", "16 GB", "STANDBY"}
        };
        lparTableModel = new DefaultTableModel(lparData, lparCols);
        JTable lparTable = createStyledTable(lparTableModel);
        lparPanel.add(new JScrollPane(lparTable), BorderLayout.CENTER);

        // 2. Linux Active Processes (ps)
        JPanel processPanel = new JPanel(new BorderLayout());
        processPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GREEN), " Linux Process Monitor (ps) ",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 11), Color.GREEN));
        processPanel.setBackground(panelBg);

        String[] procCols = {"PID", "USER", "COMMAND", "%CPU", "STATE"};
        processTableModel = new DefaultTableModel(null, procCols);
        refreshProcessTable();
        JTable processTable = createStyledTable(processTableModel);
        processPanel.add(new JScrollPane(processTable), BorderLayout.CENTER);

        // 3. JES2 Mainframe Batch Job Queue
        JPanel jobPanel = new JPanel(new BorderLayout());
        jobPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.YELLOW), " Mainframe Subsystem (JES2 Queue) ",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 11), Color.YELLOW));
        jobPanel.setBackground(panelBg);

        String[] jobCols = {"Job ID", "Job Name", "Owner", "Class", "Status"};
        jobTableModel = new DefaultTableModel(null, jobCols);
        JTable jobTable = createStyledTable(jobTableModel);
        jobPanel.add(new JScrollPane(jobTable), BorderLayout.CENTER);

        dashboard.add(lparPanel);
        dashboard.add(processPanel);
        dashboard.add(jobPanel);

        return dashboard;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(new Color(20, 20, 20));
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.GRAY);
        table.setFont(new Font("Monospaced", Font.PLAIN, 11));
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(Color.CYAN);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        return table;
    }

    private void printPrompt() {
        terminalOutput.append("root@zlinux-lpar1:" + currentDirectory + "# ");
    }

    // --- Linux Command Interpreter ---
    private void executeCommand(String input) {
        terminalOutput.append(input + "\n");
        String[] parts = input.split("\\s+");
        String cmd = parts[0].toLowerCase();

        switch (cmd) {
            case "help":
                showHelp();
                break;
            case "uname":
                if (parts.length > 1 && parts[1].equals("-a")) {
                    terminalOutput.append("Linux zlinux-lpar1 5.14.0-388.el9.s390x #1 SMP IBM zSystems s390x GNU/Linux\n");
                } else {
                    terminalOutput.append("Linux\n");
                }
                break;
            case "pwd":
                terminalOutput.append(currentDirectory + "\n");
                break;
            case "ls":
                listDirectory();
                break;
            case "cd":
                changeDirectory(parts);
                break;
            case "mkdir":
                makeDirectory(parts);
                break;
            case "cat":
                readFile(parts);
                break;
            case "ps":
                listProcesses();
                break;
            case "top":
                terminalOutput.append("Tasks: " + processes.size() + " total, 1 running, " + (processes.size() - 1) + " sleeping\n");
                terminalOutput.append("%Cpu(s): " + cpuUsageBar.getValue() + "% us, 2.1% sy, 0.0% ni\n");
                terminalOutput.append("KiB Mem : 67108864 total, " + (67108864 * (100 - memoryUsageBar.getValue()) / 100) + " free\n");
                listProcesses();
                break;
            case "free":
            case "free -m":
                terminalOutput.append("              total        used        free      shared  buff/cache   available\n");
                terminalOutput.append("Mem:          65536       40632       24904         512        8192       24384\n");
                terminalOutput.append("Swap:         16384           0       16384\n");
                break;
            case "lscpu":
                terminalOutput.append("Architecture:            s390x\n");
                terminalOutput.append("CPU op-mode(s):          32-bit, 64-bit\n");
                terminalOutput.append("Address sizes:           System Controller managed\n");
                terminalOutput.append("Byte Order:              Big-Endian\n");
                terminalOutput.append("CPU(s):                  8 (Dedicated IFLs)\n");
                terminalOutput.append("Vendor ID:               IBM/S390\n");
                terminalOutput.append("Machine Type:            3931 (IBM z16)\n");
                break;
            case "run":
                runBackgroundProcess(parts);
                break;
            case "kill":
                killProcess(parts);
                break;
            case "submit_jcl":
            case "sub":
                submitJclJob(parts);
                break;
            case "clear":
                terminalOutput.setText("");
                break;
            case "whoami":
                terminalOutput.append("root\n");
                break;
            case "date":
                terminalOutput.append(new Date().toString() + "\n");
                break;

            default:
                if (!cmd.isEmpty()) {
                    terminalOutput.append("bash: " + cmd + ": command not found. Type 'help' for command list.\n");
                }
                break;
        }

        printPrompt();
    }

    private void showHelp() {
        terminalOutput.append("=== Linux Standard Commands ===\n");
        terminalOutput.append("  uname -a     : Display Linux kernel and System Architecture (s390x)\n");
        terminalOutput.append("  lscpu        : Display IBM zSystems Processor Details\n");
        terminalOutput.append("  ls, cd, pwd  : File system navigation\n");
        terminalOutput.append("  mkdir [dir]  : Create a directory\n");
        terminalOutput.append("  cat [file]   : Read file contents\n");
        terminalOutput.append("  ps / top     : View active Linux guest processes\n");
        terminalOutput.append("  free -m      : View memory statistics\n");
        terminalOutput.append("  run [name]   : Spawn a simulated Linux background process\n");
        terminalOutput.append("  kill [pid]   : Terminate a running process\n");
        terminalOutput.append("  clear, date  : Clear console / view current timestamp\n\n");
        terminalOutput.append("=== Mainframe/zVM Integrated Commands ===\n");
        terminalOutput.append("  submit_jcl [name] : Submit JCL workload to JES2 Subsystem Queue\n");
    }

    private void listDirectory() {
        List<String> contents = virtualFS.get(currentDirectory);
        if (contents != null && !contents.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String item : contents) {
                sb.append(item).append("  ");
            }
            terminalOutput.append(sb.toString().trim() + "\n");
        }
    }

    private void changeDirectory(String[] parts) {
        if (parts.length < 2) {
            currentDirectory = "/root";
            return;
        }
        String target = parts[1];
        if (target.equals("..")) {
            if (!currentDirectory.equals("/")) {
                int lastSlash = currentDirectory.lastIndexOf("/");
                currentDirectory = (lastSlash == 0) ? "/" : currentDirectory.substring(0, lastSlash);
            }
        } else {
            String newPath = currentDirectory.equals("/") ? "/" + target : currentDirectory + "/" + target;
            if (virtualFS.containsKey(newPath)) {
                currentDirectory = newPath;
            } else {
                terminalOutput.append("bash: cd: " + target + ": No such file or directory\n");
            }
        }
    }

    private void makeDirectory(String[] parts) {
        if (parts.length < 2) {
            terminalOutput.append("mkdir: missing operand\n");
            return;
        }
        String dirName = parts[1];
        String newPath = currentDirectory.equals("/") ? "/" + dirName : currentDirectory + "/" + dirName;

        List<String> currentList = new ArrayList<>(virtualFS.getOrDefault(currentDirectory, new ArrayList<>()));
        currentList.add(dirName + "/");
        virtualFS.put(currentDirectory, currentList);
        virtualFS.put(newPath, new ArrayList<>());
        terminalOutput.append("Directory created: " + newPath + "\n");
    }

    private void readFile(String[] parts) {
        if (parts.length < 2) {
            terminalOutput.append("cat: missing filename\n");
            return;
        }
        String fileName = parts[1];
        if (fileName.equals("batch_job.jcl")) {
            terminalOutput.append("//PAYJOB  JOB (ACCOUNT),'MAINFRAME BATCH',CLASS=A\n");
            terminalOutput.append("//STEP1    EXEC PGM=SORT\n");
            terminalOutput.append("//SORTIN   DD DSN=SYS1.DATA.INPUT,DISP=SHR\n");
            terminalOutput.append("//SORTOUT  DD DSN=SYS1.DATA.OUTPUT,DISP=(NEW,CATLG)\n");
        } else if (fileName.equals("os-release")) {
            terminalOutput.append("NAME=\"Red Hat Enterprise Linux\"\nVERSION=\"9.2 (Plow)\"\nID=\"rhel\"\nARCH=\"s390x\"\n");
        } else if (fileName.equals("sys_config.conf")) {
            terminalOutput.append("LPAR_ID=1\nMAX_VCPUS=8\nHYPERVISOR=z/VM v7.2\nNET_IF=enc1000\n");
        } else {
            terminalOutput.append("cat: " + fileName + ": No such file or simulated text binary\n");
        }
    }

    private void listProcesses() {
        terminalOutput.append(String.format("%-6s %-10s %-15s %-6s %-8s\n", "PID", "USER", "COMMAND", "%CPU", "STATE"));
        for (String[] proc : processes.values()) {
            terminalOutput.append(String.format("%-6s %-10s %-15s %-6s %-8s\n", proc[0], proc[1], proc[2], proc[3], proc[4]));
        }
    }

    private void runBackgroundProcess(String[] parts) {
        String procName = (parts.length > 1) ? parts[1] : "worker_daemon";
        int pid = nextPid++;
        String[] procData = new String[]{String.valueOf(pid), "root", procName, "2.4", "RUNNING"};
        processes.put(pid, procData);
        refreshProcessTable();
        terminalOutput.append("[+] Started background process '" + procName + "' with PID " + pid + "\n");
    }

    private void killProcess(String[] parts) {
        if (parts.length < 2) {
            terminalOutput.append("kill: usage: kill <pid>\n");
            return;
        }
        try {
            int pid = Integer.parseInt(parts[1]);
            if (processes.containsKey(pid)) {
                processes.remove(pid);
                refreshProcessTable();
                terminalOutput.append("[-] Process " + pid + " terminated.\n");
            } else {
                terminalOutput.append("kill: (" + pid + ") - No such process\n");
            }
        } catch (NumberFormatException e) {
            terminalOutput.append("kill: invalid PID format\n");
        }
    }

    private void submitJclJob(String[] parts) {
        String jobName = (parts.length > 1) ? parts[1].toUpperCase() : "BATCH_SORT";
        String jobId = "JOB" + nextJobId++;
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());

        jobTableModel.addRow(new Object[]{jobId, jobName, "IBMADMIN", "CLASS-A", "EXECUTING"});
        terminalOutput.append("[JES2] Job " + jobId + " (" + jobName + ") submitted to queue at " + timestamp + "\n");

        // Simulate asynchronous job completion
        Timer jobTimer = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < jobTableModel.getRowCount(); i++) {
                    if (jobTableModel.getValueAt(i, 0).equals(jobId)) {
                        jobTableModel.setValueAt("MAXCC=0000", i, 4);
                        terminalOutput.append("\n[JES2 NOTIFY] " + jobId + " " + jobName + " ENDED - HIGHEST RETURN CODE 0000\n");
                        printPrompt();
                        break;
                    }
                }
            }
        });
        jobTimer.setRepeats(false);
        jobTimer.start();
    }

    private void refreshProcessTable() {
        processTableModel.setRowCount(0);
        for (String[] proc : processes.values()) {
            processTableModel.addRow(proc);
        }
    }

    private void startSystemMonitors() {
        // Fluctuate CPU & RAM gauges, update Uptime
        Timer systemTimer = new Timer(1000, e -> {
            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
            long hrs = elapsed / 3600;
            long mins = (elapsed % 3600) / 60;
            long secs = elapsed % 60;
            uptimeLabel.setText(String.format("Uptime: %02d:%02d:%02d", hrs, mins, secs));

            // Dynamic load simulation
            int randomCpu = Math.max(15, Math.min(95, cpuUsageBar.getValue() + (int)(Math.random() * 11 - 5)));
            int randomRam = Math.max(40, Math.min(85, memoryUsageBar.getValue() + (int)(Math.random() * 5 - 2)));
            cpuUsageBar.setValue(randomCpu);
            memoryUsageBar.setValue(randomRam);
        });
        systemTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainframeLinuxSimulator simulator = new MainframeLinuxSimulator();
            simulator.setVisible(true);
        });
    }
}