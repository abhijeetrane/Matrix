package matrix.uitility;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class TaskSchedulerDashboard extends JFrame {
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter TIME_ONLY_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private static final File STORE_FILE = new File(System.getProperty("user.home"), ".win_task_dashboard.dat");

    private final List<ScheduledTask> tasks = new ArrayList<>();
    private final List<TaskRun> history = new ArrayList<>();
    private final List<RunningTask> running = new ArrayList<>();

    private final ScheduledTaskTableModel futureModel = new ScheduledTaskTableModel();
    private final HistoryTableModel historyModel = new HistoryTableModel();
    private final RunningTableModel runningModel = new RunningTableModel();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final Set<String> ranToday = new HashSet<>();

    public TaskSchedulerDashboard() {
        super("Windows Task Runner - Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        loadData();

        // Top Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Schedule New Task"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(15);
        JTextField pathField = new JTextField(25);
        pathField.setEditable(false);
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor de = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(de);
        timeSpinner.setValue(new Date());
        JCheckBox dailyCheck = new JCheckBox("Repeat Daily", true);
        JButton browseBtn = new JButton("Browse.exe /.bat");
        JButton addBtn = new JButton("Schedule");

        gbc.gridx=0; gbc.gridy=0; form.add(new JLabel("Task Name:"), gbc);
        gbc.gridx=1; form.add(nameField, gbc);
        gbc.gridx=2; form.add(new JLabel("Time (Today):"), gbc);
        gbc.gridx=3; form.add(timeSpinner, gbc);

        gbc.gridx=0; gbc.gridy=1; form.add(new JLabel("Application:"), gbc);
        gbc.gridx=1; gbc.gridwidth=2; form.add(pathField, gbc);
        gbc.gridwidth=1; gbc.gridx=3; form.add(browseBtn, gbc);

        gbc.gridx=0; gbc.gridy=2; form.add(dailyCheck, gbc);
        gbc.gridx=1; form.add(addBtn, gbc);

        browseBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("Apps (.exe,.bat,.cmd)", "exe", "bat", "cmd"));
            if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
                pathField.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });

        addBtn.addActionListener(e -> {
            if(nameField.getText().trim().isEmpty() || pathField.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Enter name and select file");
                return;
            }
            Date d = (Date) timeSpinner.getValue();
            LocalTime lt = d.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0);
            ScheduledTask t = new ScheduledTask(UUID.randomUUID().toString(), nameField.getText().trim(), pathField.getText(), lt, dailyCheck.isSelected());
            tasks.add(t);
            saveData();
            refreshTables();
            nameField.setText(""); pathField.setText("");
        });

        add(form, BorderLayout.NORTH);

        // Center Tabs
        JTabbedPane tabs = new JTabbedPane();

        // Running Panel
        JTable runningTable = new JTable(runningModel);
        JPanel runningPanel = new JPanel(new BorderLayout());
        runningPanel.add(new JScrollPane(runningTable), BorderLayout.CENTER);
        JButton killBtn = new JButton("Kill Selected");
        runningPanel.add(killBtn, BorderLayout.SOUTH);
        killBtn.addActionListener(ev -> {
            int r = runningTable.getSelectedRow();
            if(r>=0) running.get(r).process.destroyForcibly();
        });

        // History Panel
        JTable histTable = new JTable(historyModel);
        JPanel histPanel = new JPanel(new BorderLayout());
        histPanel.add(new JScrollPane(histTable), BorderLayout.CENTER);

        // Future Panel
        JTable futureTable = new JTable(futureModel);
        JPanel futurePanel = new JPanel(new BorderLayout());
        futurePanel.add(new JScrollPane(futureTable), BorderLayout.CENTER);
        JPanel futureBtnPanel = new JPanel();
        JButton runNowBtn = new JButton("Run Now");
        JButton deleteBtn = new JButton("Delete Selected");
        futureBtnPanel.add(runNowBtn); futureBtnPanel.add(deleteBtn);
        futurePanel.add(futureBtnPanel, BorderLayout.SOUTH);

        runNowBtn.addActionListener(ev -> {
            int r = futureTable.getSelectedRow();
            if(r>=0) executeTask(tasks.get(r));
        });
        deleteBtn.addActionListener(ev -> {
            int r = futureTable.getSelectedRow();
            if(r>=0){ tasks.remove(r); saveData(); refreshTables(); }
        });

        tabs.addTab("Currently Running ("+running.size()+")", runningPanel);
        tabs.addTab("Future Today", futurePanel);
        tabs.addTab("History - Today", histPanel);

        add(tabs, BorderLayout.CENTER);

        // Status Bar
        JLabel status = new JLabel("Ready | Storage: " + STORE_FILE.getAbsolutePath());
        add(status, BorderLayout.SOUTH);

        // Core scheduler loop - every 15 sec
        scheduler.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(this::checkAndRunDueTasks);
        }, 0, 15, TimeUnit.SECONDS);

        // UI Refresh loop - every 1 sec
        scheduler.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(() -> {
                cleanupRunning();
                refreshTables();
                tabs.setTitleAt(0, "Currently Running ("+running.size()+")");
            });
        }, 0, 1, TimeUnit.SECONDS);

        refreshTables();
    }

    private void checkAndRunDueTasks(){
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        LocalDate today = LocalDate.now();
        for(ScheduledTask t : tasks){
            String key = t.id + "_" + today.toString();
            if(ranToday.contains(key) &&!t.repeatDaily) continue;
            // If task time == now (minute match) and not already ran this minute
            if(t.time.getHour()==now.getHour() && t.time.getMinute()==now.getMinute()){
                if(!ranToday.contains(key + "_" + now.toString())){
                    executeTask(t);
                    ranToday.add(key + "_" + now.toString());
                    ranToday.add(key); // prevent double run for non-daily if needed
                }
            }
        }
    }

    private void executeTask(ScheduledTask task){
        try {
            ProcessBuilder pb;
            File f = new File(task.exePath);
            if(task.exePath.toLowerCase().endsWith(".bat") || task.exePath.toLowerCase().endsWith(".cmd")){
                pb = new ProcessBuilder("cmd.exe", "/c", "\"" + task.exePath + "\"");
                pb.directory(f.getParentFile());
            } else {
                pb = new ProcessBuilder(task.exePath);
                pb.directory(f.getParentFile());
            }
            pb.redirectErrorStream(true);
            Process p = pb.start();
            RunningTask rt = new RunningTask(task, p, LocalDateTime.now());
            running.add(rt);
            history.add(0, new TaskRun(task.name, task.exePath, LocalDateTime.now(), "RUNNING", (int)p.pid()));
            saveData();

            // Watcher thread for completion
            CompletableFuture.runAsync(() -> {
                try {
                    int exit = p.waitFor();
                    SwingUtilities.invokeLater(() -> {
                        history.stream()
                           .filter(h -> h.pid == p.pid() && h.status.equals("RUNNING"))
                           .findFirst()
                           .ifPresent(h -> h.status = exit==0? "SUCCESS" : "FAILED ("+exit+")");
                        saveData();
                        refreshTables();
                    });
                } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
            });

        } catch (Exception ex){
            history.add(0, new TaskRun(task.name, task.exePath, LocalDateTime.now(), "FAILED: "+ex.getMessage(), -1));
            JOptionPane.showMessageDialog(this, "Failed to start: "+ex.getMessage());
            saveData();
        }
    }

    private void cleanupRunning(){
        running.removeIf(rt ->!rt.process.isAlive());
    }

    private void refreshTables(){
        LocalTime now = LocalTime.now();
        List<ScheduledTask> future = tasks.stream()
               .filter(t -> t.time.isAfter(now) || t.repeatDaily)
               .sorted(Comparator.comparing(t -> t.time))
               .toList();
        // For simplicity, show all tasks in future model but highlight past
        futureModel.setData(tasks);
        historyModel.setData(history.stream().filter(h -> h.startTime.toLocalDate().equals(LocalDate.now())).toList());
        runningModel.setData(running);
    }

    private void loadData(){
        if(!STORE_FILE.exists()) return;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STORE_FILE))){
            Map<String,Object> map = (Map<String,Object>) ois.readObject();
            tasks.addAll((List<ScheduledTask>) map.getOrDefault("tasks", new ArrayList<>()));
            history.addAll((List<TaskRun>) map.getOrDefault("history", new ArrayList<>()));
        } catch(Exception e){ e.printStackTrace(); }
    }
    private void saveData(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STORE_FILE))){
            Map<String,Object> map = new HashMap<>();
            map.put("tasks", tasks);
            map.put("history", history);
            oos.writeObject(map);
        } catch(Exception e){ e.printStackTrace(); }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new TaskSchedulerDashboard().setVisible(true));
    }

    // --- Models ---
    static class ScheduledTask implements Serializable {
        String id, name, exePath; LocalTime time; boolean repeatDaily;
        ScheduledTask(String id, String name, String exePath, LocalTime time, boolean repeat){ this.id=id; this.name=name; this.exePath=exePath; this.time=time; this.repeatDaily=repeat; }
    }
    static class TaskRun implements Serializable {
        String taskName, path; LocalDateTime startTime; String status; int pid;
        TaskRun(String n, String p, LocalDateTime s, String st, int pid){ taskName=n; path=p; startTime=s; status=st; this.pid=pid; }
    }
    static class RunningTask {
        ScheduledTask task; Process process; LocalDateTime started;
        RunningTask(ScheduledTask t, Process p, LocalDateTime s){ task=t; process=p; started=s; }
    }
    static class ScheduledTaskTableModel extends AbstractTableModel {
        List<ScheduledTask> data = new ArrayList<>();
        String[] cols = {"Name","Application","Time","Daily","Status Today"};
        void setData(List<ScheduledTask> d){ data = new ArrayList<>(d); data.sort(Comparator.comparing(x->x.time)); fireTableDataChanged(); }
        public int getRowCount(){ return data.size(); }
        public int getColumnCount(){ return cols.length; }
        public String getColumnName(int c){ return cols[c]; }
        public Object getValueAt(int r, int c){
            ScheduledTask t = data.get(r);
            return switch(c){
                case 0 -> t.name;
                case 1 -> t.exePath;
                case 2 -> t.time.format(TIME_ONLY_FMT);
                case 3 -> t.repeatDaily? "Yes" : "No";
                case 4 -> t.time.isAfter(LocalTime.now())? "UPCOMING in " + Duration.between(LocalTime.now(), t.time).toMinutes() + "m" : "Done / Overdue";
                default -> "";
            };
        }
    }
    static class HistoryTableModel extends AbstractTableModel {
        List<TaskRun> data = new ArrayList<>();
        String[] cols = {"Time","Task","Path","PID","Status"};
        void setData(List<TaskRun> d){ data=d; fireTableDataChanged(); }
        public int getRowCount(){ return data.size(); }
        public int getColumnCount(){ return cols.length; }
        public String getColumnName(int c){ return cols[c]; }
        public Object getValueAt(int r, int c){
            TaskRun h = data.get(r);
            return switch(c){
                case 0 -> h.startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                case 1 -> h.taskName;
                case 2 -> h.path;
                case 3 -> h.pid==-1?"-":h.pid;
                case 4 -> h.status;
                default -> "";
            };
        }
    }
    static class RunningTableModel extends AbstractTableModel {
        List<RunningTask> data = new ArrayList<>();
        String[] cols = {"Task","PID","Started","Application"};
        void setData(List<RunningTask> d){ data=d; fireTableDataChanged(); }
        public int getRowCount(){ return data.size(); }
        public int getColumnCount(){ return cols.length; }
        public String getColumnName(int c){ return cols[c]; }
        public Object getValueAt(int r, int c){
            RunningTask rt = data.get(r);
            return switch(c){
                case 0 -> rt.task.name;
                case 1 -> rt.process.pid();
                case 2 -> rt.started.format(TIME_FMT);
                case 3 -> rt.task.exePath;
                default -> "";
            };
        }
    }
}