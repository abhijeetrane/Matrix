package matrix.uitility;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OlapCubeApp extends JFrame {

    // Database Connection Credentials (In-Memory H2 DB)
    private static final String DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "";

    // UI Components
    private JCheckBox chkRegion;
    private JCheckBox chkProduct;
    private JCheckBox chkYear;
    private JComboBox<String> cbMeasure;
    private JComboBox<String> cbAggregation;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public OlapCubeApp() {
        setTitle("Relational OLAP Cube Query Tool");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initDatabase();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Control Panel (Dimensions, Measures, Actions)
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createTitledBorder("OLAP Dimensions & Aggregations"));

        // Dimension Checkboxes
        JPanel dimPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dimPanel.add(new JLabel("Dimensions (Group By): "));
        chkRegion = new JCheckBox("Region");
        chkProduct = new JCheckBox("Product");
        chkYear = new JCheckBox("Year");
        
        // Default selection
        chkRegion.setSelected(true);
        chkProduct.setSelected(true);

        dimPanel.add(chkRegion);
        dimPanel.add(chkProduct);
        dimPanel.add(chkYear);

        // Measure & Aggregation Selectors
        JPanel measurePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        measurePanel.add(new JLabel("Measure: "));
        cbMeasure = new JComboBox<>(new String[]{"sales_amount", "quantity"});
        measurePanel.add(cbMeasure);

        measurePanel.add(new JLabel(" Aggregation: "));
        cbAggregation = new JComboBox<>(new String[]{"SUM", "AVG", "COUNT", "MAX", "MIN"});
        measurePanel.add(cbAggregation);

        JButton btnExecute = new JButton("Execute OLAP Query");
        btnExecute.addActionListener(e -> executeOlapQuery());
        measurePanel.add(btnExecute);

        controlPanel.add(dimPanel);
        controlPanel.add(measurePanel);

        add(controlPanel, BorderLayout.NORTH);

        // Results Table
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);

        // Status Bar
        statusLabel = new JLabel(" Ready. Select dimensions and click 'Execute OLAP Query'.");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement()) {

            // Create Fact/Dimension Relational Table
            stmt.execute("DROP TABLE IF EXISTS fact_sales");
            stmt.execute("CREATE TABLE fact_sales (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "region VARCHAR(50), " +
                    "product VARCHAR(50), " +
                    "year INT, " +
                    "sales_amount DECIMAL(10,2), " +
                    "quantity INT)");

            // Populate Sample Fact Data
            stmt.execute("INSERT INTO fact_sales (region, product, year, sales_amount, quantity) VALUES " +
                    "('North', 'Electronics', 2024, 1500.00, 10), " +
                    "('North', 'Electronics', 2025, 2000.00, 12), " +
                    "('North', 'Furniture', 2024, 800.00, 5), " +
                    "('South', 'Electronics', 2024, 1200.00, 8), " +
                    "('South', 'Furniture', 2025, 1100.00, 7), " +
                    "('East', 'Electronics', 2025, 2500.00, 15), " +
                    "('East', 'Furniture', 2024, 400.00, 3)");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Initialization Failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executeOlapQuery() {
        List<String> selectedDims = new ArrayList<>();
        if (chkRegion.isSelected()) selectedDims.add("region");
        if (chkProduct.isSelected()) selectedDims.add("product");
        if (chkYear.isSelected()) selectedDims.add("year");

        if (selectedDims.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one dimension.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String measure = (String) cbMeasure.getSelectedItem();
        String agg = (String) cbAggregation.getSelectedItem();

        // Construct Relational OLAP SQL Query using GROUP BY CUBE
        String dimensionsCsv = String.join(", ", selectedDims);
        String sql = String.format(
                "SELECT %s, %s(%s) AS aggregated_value " +
                "FROM fact_sales " +
                "GROUP BY CUBE(%s) " +
                "ORDER BY %s",
                dimensionsCsv, agg, measure, dimensionsCsv, dimensionsCsv
        );

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Build Dynamic Table Headers
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            for (String dim : selectedDims) {
                tableModel.addColumn(dim.toUpperCase());
            }
            tableModel.addColumn(agg + " (" + measure.toUpperCase() + ")");

            // Process Multi-Dimensional Data Rows
            int rowCount = 0;
            while (rs.next()) {
                Object[] row = new Object[selectedDims.size() + 1];
                for (int i = 0; i < selectedDims.size(); i++) {
                    Object val = rs.getObject(i + 1);
                    // ROLAP NULL represents the ALL/Subtotal level in CUBE aggregations
                    row[i] = (val == null) ? "[ ALL ]" : val.toString();
                }
                row[selectedDims.size()] = String.format("%.2f", rs.getDouble("aggregated_value"));
                tableModel.addRow(row);
                rowCount++;
            }

            statusLabel.setText(" Query Executed Successfully. Returned " + rowCount + " subtotal/total slice combinations.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "OLAP Query Execution Error:\n" + e.getMessage(),
                    "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OlapCubeApp().setVisible(true);
        });
    }
}