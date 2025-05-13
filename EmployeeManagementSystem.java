import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EmployeeManagementSystem extends Frame implements ActionListener {
    TextField idField, nameField, emailField, salaryField;
    Button addButton, updateButton, deleteButton, searchButton;
    Connection conn;

    EmployeeManagementSystem() {
        // UI Components
        setLayout(new GridLayout(6, 2));

        add(new Label("Employee ID:"));
        idField = new TextField();
        add(idField);

        add(new Label("Name:"));
        nameField = new TextField();
        add(nameField);

        add(new Label("Email:"));
        emailField = new TextField();
        add(emailField);

        add(new Label("Salary:"));
        salaryField = new TextField();
        add(salaryField);

        addButton = new Button("Add");
        updateButton = new Button("Update");
        deleteButton = new Button("Delete");
        searchButton = new Button("Search");

        add(addButton);
        add(updateButton);
        add(deleteButton);
        add(searchButton);

        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        searchButton.addActionListener(this);

        setTitle("Employee Management System");
        setSize(400, 300);
        setVisible(true);

        // Connect to database
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_db", "root", "your_password");
        } catch (Exception e) {
            e.printStackTrace();
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                try {
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        String id = idField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String salary = salaryField.getText();

        try {
            if (ae.getSource() == addButton) {
                PreparedStatement pst = conn.prepareStatement("INSERT INTO employees VALUES (?, ?, ?, ?)");
                pst.setInt(1, Integer.parseInt(id));
                pst.setString(2, name);
                pst.setString(3, email);
                pst.setDouble(4, Double.parseDouble(salary));
                pst.executeUpdate();
                showMessage("Employee added successfully!");

            } else if (ae.getSource() == updateButton) {
                PreparedStatement pst = conn.prepareStatement("UPDATE employees SET name=?, email=?, salary=? WHERE id=?");
                pst.setString(1, name);
                pst.setString(2, email);
                pst.setDouble(3, Double.parseDouble(salary));
                pst.setInt(4, Integer.parseInt(id));
                pst.executeUpdate();
                showMessage("Employee updated successfully!");

            } else if (ae.getSource() == deleteButton) {
                PreparedStatement pst = conn.prepareStatement("DELETE FROM employees WHERE id=?");
                pst.setInt(1, Integer.parseInt(id));
                pst.executeUpdate();
                showMessage("Employee deleted successfully!");

            } else if (ae.getSource() == searchButton) {
                PreparedStatement pst = conn.prepareStatement("SELECT * FROM employees WHERE id=?");
                pst.setInt(1, Integer.parseInt(id));
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    nameField.setText(rs.getString("name"));
                    emailField.setText(rs.getString("email"));
                    salaryField.setText(String.valueOf(rs.getDouble("salary")));
                } else {
                    showMessage("Employee not found!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error: " + e.getMessage());
        }
    }

    void showMessage(String message) {
        Dialog d = new Dialog(this, "Message", true);
        d.setLayout(new FlowLayout());
        d.add(new Label(message));
        Button b = new Button("OK");
        b.addActionListener(e -> d.setVisible(false));
        d.add(b);
        d.setSize(250, 100);
        d.setVisible(true);
    }

    public static void main(String[] args) {
        new EmployeeManagementSystem();
    }
}
