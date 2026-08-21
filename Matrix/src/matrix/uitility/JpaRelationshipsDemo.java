package matrix.uitility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JpaRelationshipsDemo extends JFrame {

    private JTextArea codeArea;
    private JLabel statusLabel;

    public JpaRelationshipsDemo() {

        setTitle("Hibernate / Spring Boot JPA Relationships");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---------------------------------------------------------
        // Top panel
        // ---------------------------------------------------------

        JPanel topPanel = new JPanel();

        JLabel title = new JLabel(
                "Hibernate / Spring Boot JPA - Entity Relationships"
        );

        title.setFont(new Font("Arial", Font.BOLD, 22));

        topPanel.add(title);

        // ---------------------------------------------------------
        // Buttons
        // ---------------------------------------------------------

        JPanel buttonPanel = new JPanel(
                new GridLayout(1, 4, 10, 10)
        );

        JButton oneToOneButton =
                new JButton("One-to-One");

        JButton oneToManyButton =
                new JButton("One-to-Many");

        JButton manyToOneButton =
                new JButton("Many-to-One");

        JButton manyToManyButton =
                new JButton("Many-to-Many");

        buttonPanel.add(oneToOneButton);
        buttonPanel.add(oneToManyButton);
        buttonPanel.add(manyToOneButton);
        buttonPanel.add(manyToManyButton);

        // ---------------------------------------------------------
        // Code display area
        // ---------------------------------------------------------

        codeArea = new JTextArea();

        codeArea.setFont(
                new Font("Monospaced", Font.PLAIN, 15)
        );

        codeArea.setEditable(false);
        codeArea.setLineWrap(false);

        JScrollPane scrollPane =
                new JScrollPane(codeArea);

        // ---------------------------------------------------------
        // Status
        // ---------------------------------------------------------

        statusLabel = new JLabel(
                "Select a JPA relationship above."
        );

        statusLabel.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        // ---------------------------------------------------------
        // Layout
        // ---------------------------------------------------------

        JPanel northPanel = new JPanel(new BorderLayout());

        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // ---------------------------------------------------------
        // Button actions
        // ---------------------------------------------------------

        oneToOneButton.addActionListener(
                e -> showOneToOne()
        );

        oneToManyButton.addActionListener(
                e -> showOneToMany()
        );

        manyToOneButton.addActionListener(
                e -> showManyToOne()
        );

        manyToManyButton.addActionListener(
                e -> showManyToMany()
        );

        // Show first relationship by default
        showOneToOne();
    }

    // =============================================================
    // ONE TO ONE
    // =============================================================

    private void showOneToOne() {

        statusLabel.setText(
                "ONE-TO-ONE: One Person has one Passport"
        );

        codeArea.setText(
                "====================================================\n" +
                "                 ONE-TO-ONE\n" +
                "====================================================\n\n" +

                "Relationship:\n\n" +

                "        Person\n" +
                "          |\n" +
                "          | 1\n" +
                "          |\n" +
                "          | 1\n" +
                "          v\n" +
                "       Passport\n\n" +

                "Example:\n" +
                "One Person has exactly one Passport.\n\n" +

                "JPA CODE\n" +
                "----------------------------------------------------\n\n" +

                "@Entity\n" +
                "public class Person {\n\n" +

                "    @Id\n" +
                "    @GeneratedValue\n" +
                "    private Long id;\n\n" +

                "    private String name;\n\n" +

                "    @OneToOne\n" +
                "    private Passport passport;\n" +
                "}\n\n" +

                "@Entity\n" +
                "public class Passport {\n\n" +

                "    @Id\n" +
                "    @GeneratedValue\n" +
                "    private Long id;\n\n" +

                "    private String passportNumber;\n" +
                "}\n\n" +

                "----------------------------------------------------\n" +
                "Database concept:\n\n" +

                "PERSON\n" +
                "--------------------------------\n" +
                "id | name | passport_id\n" +
                "--------------------------------\n" +
                "1  | John | 101\n\n" +

                "PASSPORT\n" +
                "--------------------------------\n" +
                "id  | passport_number\n" +
                "--------------------------------\n" +
                "101 | A1234567\n\n" +

                "The foreign key passport_id connects Person\n" +
                "to Passport."
        );
    }

    // =============================================================
    // ONE TO MANY
    // =============================================================

    private void showOneToMany() {

        statusLabel.setText(
                "ONE-TO-MANY: One Department has many Employees"
        );

        codeArea.setText(
                "====================================================\n" +
                "                 ONE-TO-MANY\n" +
                "====================================================\n\n" +

                "Relationship:\n\n" +

                "             Department\n" +
                "                 |\n" +
                "                 | 1\n" +
                "                 |\n" +
                "          +------+------+\n" +
                "          |      |      |\n" +
                "          v      v      v\n" +
                "       Employee Employee Employee\n" +
                "           *       *       *\n\n" +

                "Example:\n" +
                "One Department has many Employees.\n\n" +

                "JPA CODE\n" +
                "----------------------------------------------------\n\n" +

                "@Entity\n" +
                "public class Department {\n\n" +

                "    @Id\n" +
                "    @GeneratedValue\n" +
                "    private Long id;\n\n" +

                "    private String name;\n\n" +

                "    @OneToMany(mappedBy = \"department\")\n" +
                "    private List<Employee> employees;\n" +
                "}\n\n" +

                "@Entity\n" +
                "public class Employee {\n\n" +

                "    @Id\n" +
                "    @GeneratedValue\n" +
                "    private Long id;\n\n" +

                "    private String name;\n\n" +

                "    @ManyToOne\n" +
                "    @JoinColumn(name = \"department_id\")\n" +
                "    private Department department;\n" +
                "}\n\n" +

                "----------------------------------------------------\n" +
                "Database concept:\n\n" +

                "DEPARTMENT\n" +
                "--------------------------------\n" +
                "id | name\n" +
                "--------------------------------\n" +
                "1  | IT\n\n" +

                "EMPLOYEE\n" +
                "--------------------------------\n" +
                "id | name  | department_id\n" +
                "--------------------------------\n" +
                "10 | John  | 1\n" +
                "11 | Alice | 1\n" +
                "12 | Bob   | 1\n\n" +

                "One Department is associated with many Employees.\n" +
                "The foreign key is normally stored in Employee."
        );
    }

    // =============================================================
    // MANY TO ONE
    // =============================================================

    private void showManyToOne() {

        statusLabel.setText(
                "MANY-TO-ONE: Many Employees belong to one Department"
        );

        codeArea.setText(
                "====================================================\n" +
                "                 MANY-TO-ONE\n" +
                "====================================================\n\n" +

                "Relationship:\n\n" +

                "Employee  -----+\n" +
                "               |\n" +
                "Employee  -----+-----> Department\n" +
                "               |\n" +
                "Employee  -----+\n\n" +

                "Many Employees belong to one Department.\n\n" +

                "JPA CODE\n" +
                "----------------------------------------------------\n\n" +

                "@Entity\n" +
                "public class Employee {\n\n" +

                "    @Id\n" +
                "    @GeneratedValue\n" +
                "    private Long id;\n\n" +

                "    private String name;\n\n" +

                "    @ManyToOne\n" +
                "    @JoinColumn(name = \"department_id\")\n" +
                "    private Department department;\n" +
                "}\n\n" +

                "@Entity\n" +
                "public class Department {\n\n" +

                "    @Id\n" +
                "    @GeneratedValue\n" +
                "    private Long id;\n\n" +

                "    private String name;\n" +
                "}\n\n" +

                "----------------------------------------------------\n" +
                "Database concept:\n\n" +

                "EMPLOYEE\n" +
                "--------------------------------\n" +
                "id | name  | department_id\n" +
                "--------------------------------\n" +
                "10 | John  | 1\n" +
                "11 | Alice | 1\n" +
                "12 | Bob   | 1\n\n" +

                "DEPARTMENT\n" +
                "--------------------------------\n" +
                "id | name\n" +
                "--------------------------------\n" +
                "1  | IT\n\n" +

                "Three Employees reference the same Department.\n\n" +

                "IMPORTANT:\n" +
                "One-to-Many and Many-to-One are the two sides\n" +
                "of the same bidirectional relationship when both\n" +
                "entities contain references to each other."
        );
    }

    // =============================================================
    // MANY TO MANY
    // =============================================================

    private void showManyToMany() {

        statusLabel.setText(
                "MANY-TO-MANY: Students can take many Courses"
        );

        codeArea.setText(
                "====================================================\n" +
                "                 MANY-TO-MANY\n" +
                "====================================================\n\n" +

                "Relationship:\n\n" +

                "Student 1  --------+------ Course 1\n" +
                "                    |\n" +
                "Student 2  ---------+------ Course 2\n" +
                "                    |\n" +
                "Student 3  ---------+------ Course 3\n\n" +

                "A Student can take many Courses.\n" +
                "A Course can have many Students.\n\n" +

                "JPA CODE\n" +
                "----------------------------------------------------\n\n" +

                "@Entity\n" +
                "public class Student {\n\n" +

                "    @Id\n" +
                "    @GeneratedValue\n" +
                "    private Long id;\n\n" +

                "    private String name;\n\n" +

                "    @ManyToMany\n" +
                "    @JoinTable(\n" +
                "        name = \"student_course\",\n" +
                "        joinColumns = @JoinColumn(name = \"student_id\"),\n" +
                "        inverseJoinColumns =\n" +
                "            @JoinColumn(name = \"course_id\")\n" +
                "    )\n" +
                "    private List<Course> courses;\n" +
                "}\n\n" +

                "@Entity\n" +
                "public class Course {\n\n" +

                "    @Id\n" +
                "    @GeneratedValue\n" +
                "    private Long id;\n\n" +

                "    private String name;\n" +
                "}\n\n" +

                "----------------------------------------------------\n" +
                "Database concept:\n\n" +

                "STUDENT\n" +
                "--------------------------------\n" +
                "id | name\n" +
                "--------------------------------\n" +
                "1  | John\n" +
                "2  | Alice\n\n" +

                "COURSE\n" +
                "--------------------------------\n" +
                "id | name\n" +
                "--------------------------------\n" +
                "101 | Java\n" +
                "102 | Spring Boot\n" +
                "103 | Hibernate\n\n" +

                "STUDENT_COURSE\n" +
                "--------------------------------\n" +
                "student_id | course_id\n" +
                "--------------------------------\n" +
                "1          | 101\n" +
                "1          | 102\n" +
                "2          | 101\n" +
                "2          | 103\n\n" +

                "The intermediate table STUDENT_COURSE implements\n" +
                "the many-to-many relationship."
        );
    }

    // =============================================================
    // MAIN
    // =============================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JpaRelationshipsDemo application =
                    new JpaRelationshipsDemo();

            application.setVisible(true);
        });
    }
}