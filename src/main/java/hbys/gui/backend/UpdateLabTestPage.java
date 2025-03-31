package hbys.gui;

import hbys.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class UpdateLabTestPage extends javax.swing.JFrame {

    private int testID;

    public UpdateLabTestPage(int testID) {
        this.testID = testID;
        initComponents();
        loadLabTestDetails();
        loadTechnicians();
    }

    private void loadLabTestDetails() {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT lt.TestName, lt.TestResult, lt.PatientID, lt.TechnicianID, "
                    + "CONCAT(u.FirstName, ' ', u.LastName) AS PatientName "
                    + "FROM LabTests lt "
                    + "JOIN Patients p ON lt.PatientID = p.PatientID "
                    + "JOIN Users u ON p.UserID = u.UserID "
                    + "WHERE lt.TestID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, testID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                jTextField_TestName.setText(rs.getString("TestName"));
                jTextArea_TestResult.setText(rs.getString("TestResult"));
                jLabel_PatientName.setText(rs.getString("PatientName"));
            } else {
                JOptionPane.showMessageDialog(this, "Lab test not found.", "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading lab test details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTechnicians() {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT TechnicianID, CONCAT(FirstName, ' ', LastName) AS TechnicianName "
                    + "FROM LabTechnicians";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                jComboBox_Technician.addItem(rs.getString("TechnicianName") + " (ID: " + rs.getInt("TechnicianID") + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading technicians.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateLabTest() {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String testName = jTextField_TestName.getText().trim();
            String testResult = jTextArea_TestResult.getText().trim();
            int technicianID = Integer.parseInt(jComboBox_Technician.getSelectedItem().toString().split("ID: ")[1].replace(")", "").trim());

            String query = "UPDATE LabTests SET TestName = ?, TestResult = ?, TechnicianID = ? WHERE TestID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, testName);
            ps.setString(2, testResult);
            ps.setInt(3, technicianID);
            ps.setInt(4, testID);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Lab test updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close the window
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update lab test.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating lab test.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel_Patient = new javax.swing.JLabel();
        jLabel_PatientName = new javax.swing.JLabel();
        jLabel_TestName = new javax.swing.JLabel();
        jTextField_TestName = new javax.swing.JTextField();
        jLabel_TestResult = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_TestResult = new javax.swing.JTextArea();
        jLabel_Technician = new javax.swing.JLabel();
        jComboBox_Technician = new javax.swing.JComboBox<>();
        jButton_Save = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update Lab Test");

        jLabel_Patient.setText("Patient:");

        jLabel_TestName.setText("Test Name:");

        jLabel_TestResult.setText("Test Result:");

        jTextArea_TestResult.setColumns(20);
        jTextArea_TestResult.setRows(5);
        jScrollPane1.setViewportView(jTextArea_TestResult);

        jLabel_Technician.setText("Technician:");

        jButton_Save.setText("Save");
        jButton_Save.addActionListener(evt -> updateLabTest());

        jButton_Cancel.setText("Cancel");
        jButton_Cancel.addActionListener(evt -> dispose());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel_Patient)
                                        .addComponent(jLabel_TestName)
                                        .addComponent(jLabel_TestResult)
                                        .addComponent(jLabel_Technician))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addComponent(jComboBox_Technician, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField_TestName)
                                        .addComponent(jLabel_PatientName, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_Patient)
                                        .addComponent(jLabel_PatientName))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_TestName)
                                        .addComponent(jTextField_TestName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel_TestResult)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_Technician)
                                        .addComponent(jComboBox_Technician, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton_Save)
                                        .addComponent(jButton_Cancel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Save;
    private javax.swing.JComboBox<String> jComboBox_Technician;
    private javax.swing.JLabel jLabel_Patient;
    private javax.swing.JLabel jLabel_PatientName;
    private javax.swing.JLabel jLabel_TestName;
    private javax.swing.JLabel jLabel_TestResult;
    private javax.swing.JLabel jLabel_Technician;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_TestResult;
    private javax.swing.JTextField jTextField_TestName;
}
