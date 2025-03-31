package hbys.gui.backend;

import hbys.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class UpdateAdmissionPage extends javax.swing.JFrame {

    private int admissionID;

    public UpdateAdmissionPage(int admissionID) {
        this.admissionID = admissionID;
        initComponents();
        loadAvailableRooms();
        loadNurses();
        loadAdmissionDetails();
    }

    private void loadAdmissionDetails() {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // SQL query to load the admission details
            String query = "SELECT PatientID, RoomID, ResponsibleNurseID, Notes FROM Admissions WHERE AdmissionID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, admissionID); // Use the passed AdmissionID
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Set PatientID and Notes
                jTextField_PatientID.setText(String.valueOf(rs.getInt("PatientID")));
                jTextArea_Notes.setText(rs.getString("Notes"));

                // Get RoomID and ResponsibleNurseID
                int roomID = rs.getInt("RoomID");
                int nurseID = rs.getInt("ResponsibleNurseID");

                // Pre-select the correct Room
                for (int i = 0; i < jComboBox_Room.getItemCount(); i++) {
                    String roomItem = jComboBox_Room.getItemAt(i);
                    if (roomItem.contains("ID: " + roomID)) {
                        jComboBox_Room.setSelectedIndex(i);
                        break;
                    }
                }

                // Pre-select the correct Nurse
                for (int i = 0; i < jComboBox_Nurse.getItemCount(); i++) {
                    String nurseItem = jComboBox_Nurse.getItemAt(i);
                    if (nurseItem.contains("ID: " + nurseID)) {
                        jComboBox_Nurse.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Admission not found.", "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading admission details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getRoomName(int roomID) {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT RoomNumber FROM Rooms WHERE RoomID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, roomID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("RoomNumber") + " (ID: " + roomID + ")";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getNurseName(int nurseID) {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT CONCAT(FirstName, ' ', LastName) AS FullName FROM Staff WHERE StaffID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, nurseID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("FullName") + " (ID: " + nurseID + ")";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void loadAvailableRooms() {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT RoomID, RoomNumber FROM Rooms WHERE AvailabilityStatus = 'Available' OR RoomID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, admissionID); // Include the current room in the list
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jComboBox_Room.addItem(rs.getString("RoomNumber") + " (ID: " + rs.getInt("RoomID") + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading available rooms.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadNurses() {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT StaffID, CONCAT(FirstName, ' ', LastName) AS FullName FROM Staff WHERE Role = 'Nurse'";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jComboBox_Nurse.addItem(rs.getString("FullName") + " (ID: " + rs.getInt("StaffID") + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading nurses.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAdmission() {
        try ( Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int roomID = Integer.parseInt(jComboBox_Room.getSelectedItem().toString().split("ID: ")[1].replace(")", "").trim());
            int nurseID = Integer.parseInt(jComboBox_Nurse.getSelectedItem().toString().split("ID: ")[1].replace(")", "").trim());
            String notes = jTextArea_Notes.getText().trim();

            String query = "UPDATE Admissions SET RoomID = ?, ResponsibleNurseID = ?, Notes = ? WHERE AdmissionID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, roomID);
            ps.setInt(2, nurseID);
            ps.setString(3, notes);
            ps.setInt(4, admissionID);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Admission updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Close the window
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update admission.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating admission.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel_PatientID = new javax.swing.JLabel();
        jTextField_PatientID = new javax.swing.JTextField();
        jLabel_Room = new javax.swing.JLabel();
        jComboBox_Room = new javax.swing.JComboBox<>();
        jLabel_Nurse = new javax.swing.JLabel();
        jComboBox_Nurse = new javax.swing.JComboBox<>();
        jLabel_Notes = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_Notes = new javax.swing.JTextArea();
        jButton_Update = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update Admission");

        jLabel_PatientID.setText("Patient ID:");
        jLabel_Room.setText("Room:");
        jLabel_Nurse.setText("Responsible Nurse:");
        jLabel_Notes.setText("Notes:");

        jTextArea_Notes.setColumns(20);
        jTextArea_Notes.setRows(5);
        jScrollPane1.setViewportView(jTextArea_Notes);

        jButton_Update.setText("Update");
        jButton_Update.addActionListener(evt -> updateAdmission());

        jButton_Cancel.setText("Cancel");
        jButton_Cancel.addActionListener(evt -> dispose());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel_PatientID, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                                        .addComponent(jLabel_Room, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel_Nurse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel_Notes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jTextField_PatientID)
                                                        .addComponent(jComboBox_Room, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jComboBox_Nurse, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(200, Short.MAX_VALUE)
                                .addComponent(jButton_Cancel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_Update)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_PatientID)
                                        .addComponent(jTextField_PatientID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_Room)
                                        .addComponent(jComboBox_Room, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_Nurse)
                                        .addComponent(jComboBox_Nurse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel_Notes)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton_Update)
                                        .addComponent(jButton_Cancel))
                                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }

    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Update;
    private javax.swing.JComboBox<String> jComboBox_Nurse;
    private javax.swing.JComboBox<String> jComboBox_Room;
    private javax.swing.JLabel jLabel_Nurse;
    private javax.swing.JLabel jLabel_PatientID;
    private javax.swing.JLabel jLabel_Room;
    private javax.swing.JLabel jLabel_Notes;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_Notes;
    private javax.swing.JTextField jTextField_PatientID;
}
