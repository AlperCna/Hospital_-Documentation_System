package hbys.gui.backend;

import hbys.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import javax.swing.JOptionPane;

public class RequestLabTestPage extends javax.swing.JFrame {

    private static int patientID;
    private static int doctorID;

    public RequestLabTestPage(int patientID, int doctorID) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        System.out.println("Initializing RequestLabTestPage...");
        System.out.println("Patient ID: " + patientID);
        System.out.println("Doctor ID: " + doctorID);
        initComponents();
        loadPatientDetails(); // Hasta bilgilerini yükle
    }

    private void loadPatientDetails() {
        System.out.println("Loading patient details...");
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT CONCAT(u.FirstName, ' ', u.LastName) AS PatientName, u.Email " +
                           "FROM Users u " +
                           "JOIN Patients p ON u.UserID = p.UserID " +
                           "WHERE p.PatientID = ?";
            System.out.println("SQL Query: " + query);

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, patientID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                jLabel_PatientName.setText("Name: " + rs.getString("PatientName"));
                jLabel_Email.setText("Email: " + rs.getString("Email"));
                System.out.println("Patient details loaded successfully.");
            } else {
                System.out.println("No details found for Patient ID: " + patientID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading patient details: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading patient details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getRandomTechnicianID() {
        System.out.println("Selecting random lab technician...");
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return -1;
            }

            String query = "SELECT TechnicianID FROM LabTechnicians";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            int[] technicianIDs = new int[100];
            int count = 0;

            while (rs.next()) {
                technicianIDs[count++] = rs.getInt("TechnicianID");
            }

            if (count == 0) {
                System.out.println("No lab technicians available.");
                JOptionPane.showMessageDialog(this, "No lab technicians available.", "Error", JOptionPane.ERROR_MESSAGE);
                return -1;
            }

            Random random = new Random();
            int randomIndex = random.nextInt(count);
            int selectedTechnicianID = technicianIDs[randomIndex];
            System.out.println("Selected Technician ID: " + selectedTechnicianID);
            return selectedTechnicianID;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error selecting lab technician: " + e.getMessage());
            return -1;
        }
    }

    private void requestLabTest() {
        System.out.println("Requesting lab test...");
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int technicianID = getRandomTechnicianID();
            if (technicianID == -1) {
                return;
            }

            String testName = jTextField_TestName.getText().trim();
            String testResult = jTextArea_TestResult.getText().trim();

            if (testName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Test name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "INSERT INTO LabTests (PatientID, TechnicianID, TestName, TestResult) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, patientID);
            ps.setInt(2, technicianID);
            ps.setString(3, testName);
            ps.setString(4, testResult);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Lab test requested successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Pencereyi kapat
            } else {
                JOptionPane.showMessageDialog(this, "Failed to request lab test.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error requesting lab test: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "An error occurred while requesting the lab test.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel_PatientName = new javax.swing.JLabel();
        jLabel_Email = new javax.swing.JLabel();
        jLabel_TestName = new javax.swing.JLabel();
        jTextField_TestName = new javax.swing.JTextField();
        jLabel_TestResult = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_TestResult = new javax.swing.JTextArea();
        jButton_RequestTest = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Request Lab Test");

        jLabel_TestName.setText("Test Name:");

        jLabel_TestResult.setText("Test Result (Optional):");

        jTextArea_TestResult.setColumns(20);
        jTextArea_TestResult.setRows(5);
        jScrollPane1.setViewportView(jTextArea_TestResult);

        jButton_RequestTest.setText("Request Test");
        jButton_RequestTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requestLabTest();
            }
        });

        jButton_Cancel.setText("Cancel");
        jButton_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_TestName)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField_TestName))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_TestResult)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 200, Short.MAX_VALUE)
                        .addComponent(jButton_Cancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_RequestTest)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_PatientName, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_PatientName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_Email)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_TestName)
                    .addComponent(jTextField_TestName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_TestResult)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_RequestTest)
                    .addComponent(jButton_Cancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_RequestTest;
    private javax.swing.JLabel jLabel_Email;
    private javax.swing.JLabel jLabel_PatientName;
    private javax.swing.JLabel jLabel_TestName;
    private javax.swing.JLabel jLabel_TestResult;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_TestResult;
    private javax.swing.JTextField jTextField_TestName;
}
