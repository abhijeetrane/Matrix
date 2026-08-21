package matrix.uitility;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class WindowsTaskScheduler extends JFrame {

    // Task Model
    static class Task {
        String name;
        String filePath;
        String args;
        LocalDateTime scheduledTime;
        String status; // "Scheduled", "Running", "Completed", "Failed"
        Process process;
        Integer exitCode;

        public Task(String name, String filePath, String args, LocalDateTime scheduledTime) {
            this.name = name;
            this.filePath = filePath;
            this.args = args;
            this.scheduledTime = scheduledTime;
            this.status = "Scheduled";
        }
    }

    private final List<Task> taskList = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    // UI Components
    private JTable futureTable, runningTable, historyTable;
    private DefaultTableModel futureModel, runningModel, historyModel;
    private JTextField nameField, pathField, argsField, timeField;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public WindowsTaskScheduler() {
        setTitle("Windows Task Scheduler Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(createSchedulingPanel());
        splitPane.setBottomComponent(createDashboardTabs());
        splitPane.setDividerLocation(180);

        add(splitPane);

        // Timer to refresh tables every second
        Timer refreshTimer = new Timer(1000, e -> updateDashboard());
        refreshTimer.start();
    }

    private JPanel createSchedulingPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Schedule New Task (Windows Executable / Batch File)"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels and Inputs
        nameField = new JTextField(15);
        pathField = new JTextField(20);
        argsField = new JTextField(15);
        timeField = new JTextField(LocalTime.now().plusMinutes(1).format(TIME_FORMATTER), 8);

        JButton browseBtn = new JButton("Browse...");
        browseBtn.addActionListener(e -> browseFile());

        JButton scheduleBtn = new JButton("Schedule Task");
        scheduleBtn.setBackground(new Color(46, 139, 87));
        scheduleBtn.setForeground(Color.WHITE);
        scheduleBtn.setFocusPainted(false);
        scheduleBtn.addActionListener(e -> scheduleTask());

        // Layout construction
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Task Name:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);

        gbc.gridx = 2; panel.add(new JLabel("Time (HH:mm:ss):"), gbc);
        gbc.gridx = 3; panel.add(timeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Executable / Bat Path:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; panel.add(pathField, gbc);
        gbc.gridx = 3; gbc.gridwidth = 1; panel.add(browseBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Arguments (Optional):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; panel.add(argsField, gbc);
        gbc.gridx = 3; gbc.gridwidth = 1; panel.add(scheduleBtn, gbc);

        return panel;
    }

    private JTabbedPane createDashboardTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Future Tasks Table
        futureModel = new DefaultTableModel(new String[]{"Name", "Path", "Scheduled Time", "Status"}, 0);
        futureTable = new JTable(futureModel);
        tabbedPane.addTab("Future Tasks (Today)", new JScrollPane(futureTable));

        // Running Tasks Table
        runningModel = new DefaultTableModel(new String[]{"Name", "Path", "Started Time", "Status"}, 0);
        runningTable = new JTable(runningModel);
        tabbedPane.addTab("Currently Running", new JScrollPane(runningTable));

        // History Table
        historyModel = new DefaultTableModel(new String[]{"Name", "Path", "Scheduled Time", "Exit Code", "Status"}, 0);
        historyTable = new JTable(historyModel);
        tabbedPane.addTab("Ran Today (History)", new JScrollPane(historyTable));

        return tabbedPane;
    }

    private void browseFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Windows Applications (*.exe, *.bat)", "exe", "bat");
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            pathField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void scheduleTask() {
        String name = nameField.getText().trim();
        String path = pathField.getText().trim();
        String args = argsField.getText().trim();
        String timeStr = timeField.getText().trim();

        if (name.isEmpty() || path.isEmpty() || timeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Task Name, Path, and Time.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "The specified file path does not exist.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalTime targetTime = LocalTime.parse(timeStr, TIME_FORMATTER);
            LocalDateTime targetDateTime = LocalDateTime.of(LocalDate.now(), targetTime);

            if (targetDateTime.isBefore(LocalDateTime.now())) {
                JOptionPane.showMessageDialog(this, "Scheduled time must be later than the current time.", "Time Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Task task = new Task(name, path, args, targetDateTime);
            taskList.add(task);

            long delaySeconds = java.time.Duration.between(LocalDateTime.now(), targetDateTime).getSeconds();
            scheduler.schedule(() -> executeTask(task), delaySeconds, TimeUnit.SECONDS);

            // Clear inputs
            nameField.setText("");
            pathField.setText("");
            argsField.setText("");
            JOptionPane.showMessageDialog(this, "Task scheduled successfully!");

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid time format. Use HH:mm:ss (24-hour).", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executeTask(Task task) {
        task.status = "Running";
        SwingUtilities.invokeLater(this::updateDashboard);

        List<String> command = new ArrayList<>();
        if (task.filePath.toLowerCase().endsWith(".bat")) {
            command.add("cmd.exe");
            command.add("/c");
            command.add(task.filePath);
        } else {
            command.add(task.filePath);
        }

        if (!task.args.isEmpty()) {
            for (String arg : task.args.split("\\s+")) {
                command.add(arg);
            }
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            task.process = pb.start();
            task.exitCode = task.process.waitFor();
            task.status = (task.exitCode == 0) ? "Completed" : "Failed (Code: " + task.exitCode + ")";
        } catch (IOException | InterruptedException e) {
            task.status = "Failed";
            task.exitCode = -1;
        }

        SwingUtilities.invokeLater(this::updateDashboard);
    }

    private synchronized void updateDashboard() {
        futureModel.setRowCount(0);
        runningModel.setRowCount(0);
        historyModel.setRowCount(0);

        LocalDate today = LocalDate.now();

        for (Task task : taskList) {
            if (!task.scheduledTime.toLocalDate().equals(today)) {
                continue; // Only process today's tasks
            }

            String timeFormatted = task.scheduledTime.format(TIME_FORMATTER);

            switch (task.status) {
                case "Scheduled":
                    futureModel.addRow(new Object[]{task.name, task.filePath, timeFormatted, task.status});
                    break;
                case "Running":
                    runningModel.addRow(new Object[]{task.name, task.filePath, timeFormatted, task.status});
                    break;
                default: // Completed or Failed
                    historyModel.addRow(new Object[]{task.name, task.filePath, timeFormatted, task.exitCode, task.status});
                    break;
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new WindowsTaskScheduler().setVisible(true);
        });
    }
}