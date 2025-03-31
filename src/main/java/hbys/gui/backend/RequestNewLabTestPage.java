package hbys.gui.backend;

import hbys.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class RequestNewLabTestPage extends javax.swing.JFrame {

    private int doctorID;

    public RequestNewLabTestPage(int doctorID) {
        this.doctorID = doctorID;
        initComponents();
        loadPatients();
        loadTechnicians();
    }

    private void loadPatients() {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT p.PatientID, CONCAT(u.FirstName, ' ', u.LastName) AS PatientName "
                    + "FROM Patients p JOIN Users u ON p.UserID = u.UserID";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jComboBox_Patient.addItem(rs.getString("PatientName") + " (ID: " + rs.getInt("PatientID") + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patients.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTechnicians() {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT TechnicianID, CONCAT(FirstName, ' ', LastName) AS TechnicianName FROM LabTechnicians";
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

    private void saveLabTest() {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int patientID = Integer.parseInt(jComboBox_Patient.getSelectedItem().toString().split("ID: ")[1].replace(")", "").trim());
            int technicianID = Integer.parseInt(jComboBox_Technician.getSelectedItem().toString().split("ID: ")[1].replace(")", "").trim());
            String testName = jTextField_TestName.getText().trim();
            String testResult = jTextArea_Notes.getText().trim();

            String query = "INSERT INTO LabTests (PatientID, TechnicianID, TestName, TestDate, TestResult) VALUES (?, ?, ?, GETDATE(), ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, patientID);
            ps.setInt(2, technicianID);
            ps.setString(3, testName);
            ps.setString(4, testResult);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Lab Test requested successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Close the window
            } else {
                JOptionPane.showMessageDialog(this, "Failed to request lab test.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error requesting lab test.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel_Patient = new javax.swing.JLabel();
        jComboBox_Patient = new javax.swing.JComboBox<>();
        jLabel_TestName = new javax.swing.JLabel();
        jTextField_TestName = new javax.swing.JTextField();
        jLabel_Technician = new javax.swing.JLabel();
        jComboBox_Technician = new javax.swing.JComboBox<>();
        jLabel_Notes = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_Notes = new javax.swing.JTextArea();
        jButton_Save = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Request New Lab Test");

        jLabel_Patient.setText("Patient:");
        jLabel_TestName.setText("Test Name:");
        jLabel_Technician.setText("Technician:");
        jLabel_Notes.setText("Notes:");

        jTextArea_Notes.setColumns(20);
        jTextArea_Notes.setRows(5);
        jScrollPane1.setViewportView(jTextArea_Notes);

        jButton_Save.setText("Save");
        jButton_Save.addActionListener(evt -> saveLabTest());

        jButton_Cancel.setText("Cancel");
        jButton_Cancel.addActionListener(evt -> dispose());

        // Layout settings
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel_Patient)
                                        .addComponent(jLabel_TestName)
                                        .addComponent(jLabel_Technician)
                                        .addComponent(jLabel_Notes))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addComponent(jTextField_TestName)
                                        .addComponent(jComboBox_Technician, 0, 300, Short.MAX_VALUE)
                                        .addComponent(jComboBox_Patient, 0, 300, Short.MAX_VALUE))
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
                                        .addComponent(jComboBox_Patient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_TestName)
                                        .addComponent(jTextField_TestName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_Technician)
                                        .addComponent(jComboBox_Technician, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel_Notes)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    private javax.swing.JComboBox<String> jComboBox_Patient;
    private javax.swing.JComboBox<String> jComboBox_Technician;
    private javax.swing.JLabel jLabel_Notes;
    private javax.swing.JLabel jLabel_Patient;
    private javax.swing.JLabel jLabel_Technician;
    private javax.swing.JLabel jLabel_TestName;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_Notes;
    private javax.swing.JTextField jTextField_TestName;
}
